package com.example.bap.dao;

import com.example.bap.dto.AccountDto;
import com.example.bap.dto.BookDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    public BookDto BookReadOne(int bookId);
    public List<BookDto> BookReadAll();
}
