package com.example.bap.controller;

import com.example.bap.dao.AccountMapper;
import com.example.bap.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IndexController {
    @Autowired

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String renderMainPage() { return "MainUI"; }
    @RequestMapping(value = "/rental", method = RequestMethod.GET)
    public String renderRentalPage() { return "rental/RentalUI";}
    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public String renderReturnPage() { return "return/ReturnUI";}
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String renderLoginPage() { return "login/LoginUI";}
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String renderSignUpPage() { return "signup/signup";}
    @RequestMapping(value = "/myPage", method = RequestMethod.GET)
    public String rendermyPage() { return "myPage/myPageUI";}
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String renderadminPage() { return "setting/AdminPageUI"; }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String rendersearchPage() { return "search/BookSearchUI"; }
}