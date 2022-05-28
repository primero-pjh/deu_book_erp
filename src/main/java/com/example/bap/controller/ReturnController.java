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

//    @RequestMapping(value = "api/setting", method = RequestMethod.GET)
//    public @ResponseBody List<SettingDto> SettingReadAll() {
//        List<SettingDto> setting_list = settingMapper.SettingReadAll();
//        return setting_list;
//    }

    public class JsonResult {
        public int success;
        public BookDto bookDto;
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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    @RequestMapping(value = "api/return/{bookId}", method = RequestMethod.POST)
    public @ResponseBody JsonResult Return(@PathVariable int bookId,
                                                     HttpServletRequest httpServletRequest) {
        var obj = new JsonResult();
        int accountId = Integer.parseInt(httpServletRequest.getParameter("accountId"));
        if(accountId == 0) {
            obj.setSuccess(0);
            obj.setMessage("로그인 후 다시 이용해주세요.");
            return obj;
        }

        Calendar time = Calendar.getInstance();
        String today = new SimpleDateFormat ( "yyyy-MM-dd").format(time.getTime());
        // 연체기준일
        int OverdueDate = Integer.parseInt(settingMapper.SettingReadOneWithKeyword("OverdueDate").getValue());
        time.add(Calendar.DATE, OverdueDate);
        // 반납예정일
        String returnDueDate = new SimpleDateFormat ( "yyyy-MM-dd").format(time.getTime());

        BookDto book = bookMapper.BookReadOne(bookId);
        if(book == null) {
            obj.setSuccess(0);
            obj.setMessage("해당 책은 존재하지 않습니다. 확인 후 다시 시도해주세요.");
            return obj;
        }

        // 1. 만약 대여하지 않은 책이면 반납할 수 없다.
        var count = recordMapper.MyRecordWithBookIdCount(accountId, bookId);
        if(count == 0) {
            obj.setSuccess(0);
            obj.setMessage("해당 책은 반납되었거나, 대여한 책이 아닙니다. 확인 후 다시 시도하세요.");
            return obj;
        }
        // 2. 반납할 때 반납예정일이 지났을 때
        var record = recordMapper.MyRentalDTO(accountId, bookId);
        var return_due_date = record.getReturnDueDate();
        if( today.compareTo(return_due_date) > 0 )
        {
            // 1. overDue -> 1로 수정, returnDate 삽입
            record.setOverDue(1);
            record.setReturnDate(today);
            recordMapper.Return(record);
        }
        // 3. 반납할 때 반납예정일이 지나지 않았을 때
        else
        {
            record.setOverDue(0);
            record.setReturnDate(today);
            recordMapper.Return(record);
        }

        // book의 status를 변경한다 (0=>1);
        BookDto b = bookMapper.BookReadOne(bookId);
        b.setStatus(1);
        bookMapper.UpdateBook(b);

        obj.setSuccess(1);
        obj.setMessage("반납완료");
        return obj;
    }
}