package com.example.bap.controller;

import com.example.bap.dao.AccountMapper;
import com.example.bap.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private AccountMapper accountMapper;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String hello(Model model) {
//        model.addAttribute("name", "dsunni");
        List<AccountDto> account_list = accountMapper.account_list();
        System.out.print(account_list);
        for(AccountDto data: account_list) {
            System.out.print(data.getUserId());
        }
//        System.out.print(AccountDto);
        return "/home/home";
    }
}