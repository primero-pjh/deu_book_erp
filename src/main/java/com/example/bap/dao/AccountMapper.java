package com.example.bap.dao;

import com.example.bap.dto.BookDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.example.bap.dto.AccountDto;

@Mapper
public interface AccountMapper {
    public List<AccountDto> account_list();
    public AccountDto get_user(String id, String pw);

    public void bookDelete(int bookId);
    public void bookUpdate();
    public void bookInsert();

}
