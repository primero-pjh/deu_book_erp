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
public class ReturnController {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private SettingMapper settingMapper;

    public class ReturnData {
        public int success;
        public BookDto bookDto;
        public int state;
        public String message;
        public String returnDueDate;

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

        public int getState() {
            return state;
        }
        public void setState(int state) {
            this.state = state;
        }

        public String getReturnDueDate() {return returnDueDate; }
        public void setReturnDueDate(String returnDueDate) { this.returnDueDate = returnDueDate; }
    }

    @RequestMapping(value = "api/return/{bookId}", method = RequestMethod.POST)
    public @ResponseBody ReturnData Return(@PathVariable int bookId,
                                                     HttpServletRequest httpServletRequest) {
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

        BookDto book = bookMapper.getSpBook(bookId);
        if(book == null) {
            obj.setSuccess(0);
            obj.setMessage("?????? ?????? ???????????? ????????????. ?????? ??? ?????? ??????????????????.");
            return obj;
        }

        // 1. ?????? ???????????? ?????? ????????? ????????? ??? ??????.
        var return_record = recordMapper.MyRecordWithBookId(accountId, bookId);
        if(return_record == null) {
            obj.setSuccess(0);
            obj.setMessage("?????? ?????? ??????????????????, ????????? ?????? ????????????. ?????? ??? ?????? ???????????????.");
            return obj;
        }
        // 2. ????????? ??? ?????????????????? ????????? ???
        var record = recordMapper.MyRentalDTO(accountId, bookId);
        var return_due_date = record.getReturnDueDate();
        if( today.compareTo(return_due_date) > 0 )
        {
            // 1. overDue -> 1??? ??????, returnDate ??????
            record.setOverDue(1);
            record.setReturnDate(today);
            recordMapper.Return(record);
        }
        // 3. ????????? ??? ?????????????????? ????????? ????????? ???
        else
        {
            record.setOverDue(0);
            record.setReturnDate(today);
            recordMapper.Return(record);
        }

        // book??? status??? ???????????? (0=>1);
        BookDto b = bookMapper.getSpBook(bookId);
        b.setStatus(1);
        bookMapper.UpdateBook(b);

        obj.setSuccess(1);
        obj.setReturnDueDate(return_due_date);
        obj.setMessage("??????????????? ?????????????????????.");
        return obj;
    }
}