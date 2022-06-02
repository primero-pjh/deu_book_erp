package com.example.bap.controller;

import com.example.bap.dao.BookMapper;
import com.example.bap.dao.RecordMapper;
import com.example.bap.dao.SettingMapper;
import com.example.bap.dto.BookDto;
import com.example.bap.dto.RecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RecordController {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private RecordMapper recordMapper;

    public class ReturnData {
        public int success;
        public List<Map<String, Object>> recordList;
        public int state;
        public String message;

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        public List<Map<String, Object>> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<Map<String, Object>> recordList) {
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

    @RequestMapping(value = "api/myRecord/{accountId}", method = RequestMethod.GET)
    public @ResponseBody ReturnData getMyRecordList(@PathVariable int accountId) {
        var obj = new ReturnData();
        List<Map<String, Object>> my_record_list = recordMapper.MyRecordList(accountId);

        obj.setSuccess(1);
        obj.setRecordList(my_record_list);
        return obj;
    }

    @RequestMapping(value = "api/filterMyRecord/{accountId}", method = RequestMethod.GET)
    public @ResponseBody ReturnData getFilterMyRecordList(@PathVariable int accountId,
                                                     @RequestParam(value="startDate") String startDate,
                                                     @RequestParam(value="endDate") String endDate,
                                                     @RequestParam(value="type") String type) {
        var obj = new ReturnData();
        List<Map<String, Object>> filter_my_record_list = recordMapper.MyFilterRecordList(accountId, startDate, endDate, type);

        if(filter_my_record_list.size() == 0) {
            obj.setSuccess(0);
            return obj;
        }
        obj.setSuccess(1);
        obj.setRecordList(filter_my_record_list);
        return obj;
    }
}