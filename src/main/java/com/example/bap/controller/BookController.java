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

    public class ReturnData {
        public int success;
        public List<BookDto> bookList;
        public BookDto bookDTO;
        public String message;

        public int getSuccess() { return success; }
        public void setSuccess(int success) { this.success = success; }

        public List<BookDto> getBookList() { return bookList; }
        public void setBookList(List<BookDto> bookList) { this.bookList = bookList; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public BookDto getBookDTO() {return bookDTO;}
        public void setBookDTO(BookDto bookDTO) {this.bookDTO = bookDTO;}
    }

    @RequestMapping(value = "api/book/{bookId}", method = RequestMethod.GET)
    public @ResponseBody ReturnData BookReadOne(@PathVariable int bookId) {
        BookDto book = bookMapper.BookReadOne(bookId);
        var obj = new ReturnData();

        if(book == null) {
            obj.setSuccess(0);
            return obj;
        }
        obj.setSuccess(1);
        obj.setBookDTO(book);
        return obj;
    }

    @RequestMapping(value = "api/book", method = RequestMethod.GET)
    public @ResponseBody ReturnData BookReadAll() {
        List<BookDto> bookList = bookMapper.BookReadAll();
        var obj = new ReturnData();

        if(bookList == null) {
            obj.setSuccess(0);
            return obj;
        }
        obj.setSuccess(1);
        obj.setBookList(bookList);
        return obj;
    }


}