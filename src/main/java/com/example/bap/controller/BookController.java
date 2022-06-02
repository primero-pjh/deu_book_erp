package com.example.bap.controller;

import com.example.bap.dao.BookMapper;
import com.example.bap.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "api/search", method = RequestMethod.GET)
    public @ResponseBody ReturnData getFilterBookList(@RequestParam(value="type") String type,
                                                      @RequestParam(value="value") String value) {
        ReturnData obj = new ReturnData();
        System.out.println(type);
        System.out.println(value);
        List<BookDto> book_list = null;
        if(type.equals("none")) {
            obj.setSuccess(0);
            return obj;
        }
        if(type.equals("name")){
            System.out.println("?");
            book_list = bookMapper.BookSearch_n(value);

        } else if(type=="category"){
            book_list = bookMapper.BookSearch_c(value);

        } else if(type=="writer"){
            book_list = bookMapper.BookSearch_w(value);

        } else if(type=="description"){
            book_list = bookMapper.BookSearch_d(value);
        }
        obj.setSuccess(1);
        obj.setBookList(book_list);
        return obj;
    }
}