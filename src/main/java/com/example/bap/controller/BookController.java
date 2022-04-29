package com.example.bap.controller;

import com.example.bap.dao.BookMapper;
import com.example.bap.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {
    @Autowired
    private BookMapper bookMapper;

//    @RequestMapping(value = "api/setting", method = RequestMethod.GET)
//    public @ResponseBody List<SettingDto> SettingReadAll() {
//        List<SettingDto> setting_list = settingMapper.SettingReadAll();
//        return setting_list;
//    }

    public class BookReadOneData {
        public int success;
        public BookDto bookDto;

        public int getSuccess() {
            return success;
        }
        public void setSuccess(int success) {
            this.success = success;
        }

        public BookDto getBookDto() {
            return bookDto;
        }
        public void setBookDto(BookDto bookDto) {
            this.bookDto = bookDto;
        }
    }

    public class BookReadAllData {
        public int success;
        public List<BookDto> bookList;
        public String message;

        public int getSuccess() {
            return success;
        }

        public void setSuccess(int success) {
            this.success = success;
        }

        public List<BookDto> getBookList() {
            return bookList;
        }

        public void setBookList(List<BookDto> bookList) {
            this.bookList = bookList;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @RequestMapping(value = "api/book/{bookId}", method = RequestMethod.GET)
    public @ResponseBody BookReadOneData BookReadOne(@PathVariable int bookId) {
        BookDto book = bookMapper.BookReadOne(bookId);
        var obj = new BookReadOneData();

        if(book == null) {
            obj.setSuccess(0);
            return obj;
        }
        obj.setSuccess(1);
        obj.setBookDto(book);
        return obj;
    }

    @RequestMapping(value = "api/book", method = RequestMethod.GET)
    public @ResponseBody BookReadAllData BookReadAll() {
        List<BookDto> bookList = bookMapper.BookReadAll();
        var obj = new BookReadAllData();

        if(bookList == null) {
            obj.setSuccess(0);
            return obj;
        }
        obj.setSuccess(1);
        obj.setBookList(bookList);
        return obj;
    }


}