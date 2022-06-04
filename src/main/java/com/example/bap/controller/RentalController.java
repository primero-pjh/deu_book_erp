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
            obj.setMessage("로그인 후 다시 이용해주세요.");
            return obj;
        }

        Calendar time = Calendar.getInstance();
        String today = new SimpleDateFormat ( "yyyy-MM-dd").format(time.getTime());
        // 연체기준일
        int OverdueDate = Integer.parseInt(settingMapper.getSpKeyword("OverdueDate").getValue());
        time.add(Calendar.DATE, OverdueDate);
        // 반납예정일
        String returnDueDate = new SimpleDateFormat ( "yyyy-MM-dd").format(time.getTime());

        if(book == null) {
            obj.setSuccess(0);
            obj.setMessage("해당 책은 존재하지 않습니다. 확인 후 다시 시도해주세요.");
            return obj;
        }

        List<RecordDto> my_record_list = recordMapper.MyRentalList(accountId);  // 아직 반납하지 않고, 연체되지 않은 책 리스트
        int MaxRentalCount = Integer.parseInt(settingMapper.getSpKeyword("MaxRentalCount").getValue());

        // 1. 만약 이미 대여한 책이라면 대여가 불가능하다.
        // => 22.06.02 수정. 이미 대여한 책이면 대여 버튼을 누르지 못한다.
//        var recent_record = recordMapper.getRecentRecord(accountId, bookId);
//        if(recent_record == null) {
//            obj.setSuccess(0);
//            obj.setRecordDto(recent_record);
//            obj.setState(100);
//            obj.setMessage("이미 대여하신 책입니다.");
//            return obj;
//        }

        // 만약 연체기록이 있다면 빌리지 못한다.
        List<RecordDto> my_over_due_list = recordMapper.MyOverDueList(accountId);
        if(my_over_due_list.size() > 0) {
            my_over_due_list = my_over_due_list.stream().sorted(Comparator.comparing(RecordDto::getReturnDate).reversed()).collect(Collectors.toList());
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
            String rentalOkDate = new SimpleDateFormat ( "yyyy-MM-dd").format(calendar.getTime());
            if( today.compareTo(rentalOkDate) < 0) {
                obj.setSuccess(0);
                obj.setState(200);
                obj.setMessage("최근연체기록 이후 " + OverdueDate + "일이 경과되지 않아서 대여하실 수 없습니다.");
                return obj;
            }
        }

        // 현재 (연체X, 반납X) 대여한 책의 권수가 MaxRentalCount 같거나, 크면 빌리지 못한다.
        if(my_record_list.size() >= MaxRentalCount) {
            obj.setSuccess(0);
            obj.setState(300);
            obj.setMessage("대여하신 책이 " + MaxRentalCount + "권 이상입니다. 대여하신 책을 반납 후 대여해주세요.");
            return obj;
        }

        // 3가지의 조건을 모두 통과했으므로 대여를 진행한다.
        var record = new RecordDto();
        record.setAccountId(accountId);
        record.setBookId(bookId);
        record.setRentalDate(today);
        record.setReturnDueDate(returnDueDate);
        record.setOverDue(0);
        recordMapper.AddRentalRecord(record);

        // book의 status를 변경한다 (1=>0);
        BookDto b = bookMapper.getSpBook(bookId);
        b.setStatus(0);
        bookMapper.UpdateBook(b);

        obj.setSuccess(1);
        obj.setBookDto(book);
        obj.setMessage("성공적으로 대여하였습니다.");
        return obj;
    }
}