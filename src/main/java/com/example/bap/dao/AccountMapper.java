package com.example.bap.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.example.bap.dto.AccountDto;

@Mapper
public interface AccountMapper {
    public List<AccountDto> account_list();
}
