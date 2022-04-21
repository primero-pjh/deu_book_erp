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
public class RecordController {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private RecordMapper recordMapper;

    public class RecordReadAllData {
        public int success;
        public List<RecordDto> recordList;
        public int state;
        public String message;

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        public List<RecordDto> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<RecordDto> recordList) {
            this.recordList = recordList;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @RequestMapping(value = "api/record/{accountId}", method = RequestMethod.GET)
    public @ResponseBody
    RecordReadAllData BookReadOne(@PathVariable int accountId) {
        var obj = new RecordReadAllData();
        if (accountId == 0) {
            obj.setSuccess(0);
            obj.setMessage("로그인 후 다시 이용해주세요.");
            return obj;
        }

        var record_list = recordMapper.MyRecordList(accountId);
        obj.setSuccess(1);
        obj.setRecordList(record_list);
        obj.setMessage("성공적으로 대여하였습니다.");
        return obj;
    }
}