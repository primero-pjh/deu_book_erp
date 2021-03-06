package com.example.bap.controller;

import com.example.bap.dao.BookMapper;
import com.example.bap.dao.RecordMapper;
import com.example.bap.dao.SettingMapper;
import com.example.bap.dto.BookDto;
import com.example.bap.dto.RecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RentalController {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private SettingMapper settingMapper;

//    @RequestMapping(value = "api/setting", method = RequestMethod.GET)
//    public @ResponseBody List<SettingDto> SettingReadAll() {
//        List<SettingDto> setting_list = settingMapper.SettingReadAll();
//        return setting_list;
//    }

    public class ReturnData {
        public int success;
        public BookDto bookDto;
        public RecordDto recordDto;
        public int state;
        public String message;

        public int getSuccess() {
            return success;
        }
        public void setSuccess(int success) {
            this.success = success;
        }

        public BookDto getBookDto() {
            return bookDto;
        }
        public void setBookDto(BookDto bookDto) {this.bookDto = bookDto;}

        public String getMessage() {return message;}
        public void setMessage(String message) {this.message = message;}

        public int getState() { return state; }
        public void setState(int state) { this.state = state; }

        public RecordDto getRecordDto() {return recordDto;}
        public void setRecordDto(RecordDto recordDto) {this.recordDto = recordDto;}
    }



    @RequestMapping(value = "api/rental/{bookId}", method = RequestMethod.POST)
    public @ResponseBody ReturnData Rental(@PathVariable int bookId,
                                                     HttpServletRequest httpServletRequest) {
        BookDto book = bookMapper.getSpBook(bookId);
        var obj = new ReturnData();
        int accountId = Integer.parseInt(httpServletRequest.getParameter("accountId"));
        if(accountId == 0) {
            obj.setSuccess(0);
            obj.setMessage("????????? ??? ?????? ??????????????????.");
            return obj;
        }

        Calendar time = Calendar.getInstance();
        String today = new SimpleDateFormat ( "yyyy-MM-dd").format(time.getTime());
        // ???????????????
        int OverdueDate = Integer.parseInt(settingMapper.getSpKeyword("OverdueDate").getValue());
        time.add(Calendar.DATE, OverdueDate);
        // ???????????????
        String returnDueDate = new SimpleDateFormat ( "yyyy-MM-dd").format(time.getTime());

        if(book == null) {
            obj.setSuccess(0);
            obj.setMessage("?????? ?????? ???????????? ????????????. ?????? ??? ?????? ??????????????????.");
            return obj;
        }

        List<RecordDto> my_record_list = recordMapper.MyRentalList(accountId);  // ?????? ???????????? ??????, ???????????? ?????? ??? ?????????
        int MaxRentalCount = Integer.parseInt(settingMapper.getSpKeyword("MaxRentalCount").getValue());

        // ?????? ??????????????? ????????? ????????? ?????????.
        List<RecordDto> my_over_due_list = recordMapper.MyOverDueList(accountId);
        if(my_over_due_list.size() > 0) {
            my_over_due_list = my_over_due_list.stream()
                .sorted(Comparator.comparing(RecordDto::getReturnDate).reversed())
                .collect(Collectors.toList());
            var record = my_over_due_list.get(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                Date date = sdf.parse(record.getReturnDate());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, OverdueDate);
            String rentalOkDate = new SimpleDateFormat("yyyy-MM-dd")
                .format(calendar.getTime());
            if( today.compareTo(rentalOkDate) < 0) {
                obj.setSuccess(0);
                obj.setState(200);
                obj.setMessage("?????????????????? ?????? " + OverdueDate +
                                "?????? ???????????? ????????? ???????????? ??? ????????????.");
                return obj;
            }
        }

        // ?????? (??????X, ??????X) ????????? ?????? ????????? MaxRentalCount ?????????, ?????? ????????? ?????????.
        if(my_record_list.size() >= MaxRentalCount) {
            obj.setSuccess(0);
            obj.setState(300);
            obj.setMessage("???????????? ?????? " + MaxRentalCount + 
                            "??? ???????????????. ???????????? ?????? ?????? ??? ??????????????????.");
            return obj;
        }

        // 3????????? ????????? ?????? ?????????????????? ????????? ????????????.
        var record = new RecordDto();
        record.setAccountId(accountId);
        record.setBookId(bookId);
        record.setRentalDate(today);
        record.setReturnDueDate(returnDueDate);
        record.setOverDue(0);
        recordMapper.AddRentalRecord(record);

        // book??? status??? ???????????? (1=>0);
        BookDto b = bookMapper.getSpBook(bookId);
        b.setStatus(0);
        bookMapper.UpdateBook(b);

        obj.setSuccess(1);
        obj.setBookDto(book);
        obj.setMessage("??????????????? ?????????????????????.");
        return obj;
    }
}