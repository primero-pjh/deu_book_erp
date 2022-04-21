package com.example.bap.controller;

import com.example.bap.dao.SettingMapper;
import com.example.bap.dto.SettingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import java.util.List;

@Controller
public class SettingController {
    @Autowired
    private SettingMapper settingMapper;

    @RequestMapping(value = "api/setting", method = RequestMethod.GET)
    public @ResponseBody List<SettingDto> SettingReadAll() {
        List<SettingDto> setting_list = settingMapper.SettingReadAll();
        return setting_list;
    }

    @RequestMapping(value = "api/setting/{settingId}", method = RequestMethod.GET)
    public @ResponseBody SettingDto SettingReadOne(@PathVariable int settingId) {
        SettingDto setting = settingMapper.SettingReadOne(settingId);
        return setting;
    }

    @RequestMapping(value = "api/setting/{settingId}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> SettingSave(@PathVariable int settingId,
                                           HttpServletRequest httpServletRequest) {


        String value = httpServletRequest.getParameter("value");
        String keyword = httpServletRequest.getParameter("keyword");
        HashMap<String, String> obj = new HashMap<>();
        if(settingId == 0) {
            obj.put("success", "0");
            obj.put("message", "값을 설정 후 다시 시도하세요");
            return obj;
        }
        if(value == null || value.isBlank() == true) {
            obj.put("success", "0");
            obj.put("message", "값을 설정 후 다시 시도하세요");
            return obj;
        }

        SettingDto setting = new SettingDto();
        setting.setKeyword(keyword);
        setting.setSettingId(settingId);
        setting.setValue(value);
        settingMapper.SettingSave(setting);
        obj.put("success", "1");
        obj.put("message", "성공적으로 저장하였습니다.");
        return obj;
    }
}