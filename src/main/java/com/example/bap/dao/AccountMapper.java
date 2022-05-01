package com.example.bap.dao;

import com.example.bap.dto.BookDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.example.bap.dto.AccountDto;

@Mapper
public interface AccountMapper {
    public List<AccountDto> account_list();
    public AccountDto get_user(String id, String pw);
    public AccountDto get_user_userId(String userId);
    public AccountDto check_user(String userId);
    public void sign_up(String userId, String password, String phoneNumber);
}
