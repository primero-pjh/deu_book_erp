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
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String renderSettingPage() { return "/setting/index"; }

    @RequestMapping(value = "/rental", method = RequestMethod.GET)
    public String renderRentalPage() {
        return "/rental/index";
    }

    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public String renderReturnPage() {
        return "/return/index";
    }

    @RequestMapping(value = "/qr_maker", method = RequestMethod.GET)
    public String renderQRMakerPage() {
        return "/qr_maker/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String renderLoginPage() {
        return "/login/login";
    }
}