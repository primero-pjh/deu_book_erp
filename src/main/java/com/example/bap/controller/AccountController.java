package com.example.bap.controller;

import com.example.bap.dao.AccountMapper;
import com.example.bap.dao.BookMapper;
import com.example.bap.dao.RecordMapper;
import com.example.bap.dto.AccountDto;
import com.example.bap.dto.BookDto;
import com.example.bap.dto.RecordDto;
import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
public class AccountController {
    @Autowired
    private AccountMapper accountMapper;

    final String key = "!@#$%^&*09128340913890123890";

    public class ReturnData {
        public int success;
        public int state;
        public boolean isLogged;
        public HashMap<String, String> error;
        public String token;
        public Map<String, Object> user_map;

        public Map<String, Object> getUser_map() { return user_map; }
        public void setUser_map(Map<String, Object> user_map) { this.user_map = user_map; }

        public String message;
        public List<AccountDto> user_list;
        public AccountDto user;

        public boolean isLogged() { return isLogged; }
        public void setLogged(boolean logged) { isLogged = logged; }

        public HashMap<String, String> getError() { return error;}
        public void setError(HashMap<String, String> error) { this.error = error; }

        public List<AccountDto> getUser_list() {
            return user_list;
        }
        public void setUser_list(List<AccountDto> user_list) {
            this.user_list = user_list;
        }

        public AccountDto getUser() {
            return user;
        }
        public void setUser(AccountDto user) {
            this.user = user;
        }

        public int getSuccess() {
            return success;
        }
        public void setSuccess(int success) {
            this.success = success;
        }

        public int getState() {
            return state;
        }
        public void setState(int state) {
            this.state = state;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    @RequestMapping(value = "/api/user/login", method = RequestMethod.GET)
    public @ResponseBody ReturnData Login(@RequestParam(value="userId") String userId, @RequestParam(value="password") String password,
                                          @RequestParam(value="isRemember") boolean isRemember) {
        ReturnData obj = new ReturnData();
//        String userId = req.getParameter("userId");
//        String password = req.getParameter("password");
//        boolean isRemember = Boolean.parseBoolean(req.getParameter("isRemember"));
        var error = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userId)) { error.put("userId", "???????????? ???????????????."); }
        if(StringUtils.isNullOrEmpty(password)) { error.put("password", "???????????? ???????????????."); }
        if(error.size() > 0) {
            obj.setSuccess(0);
            obj.setError(error);
            return obj;
        }

        AccountDto user = new AccountDto();
        user = accountMapper.get_user(userId, password);
        if(user == null) {
            obj.setSuccess(0);
            obj.setMessage("????????? ?????? ??????????????? ?????????????????????.");
            return obj;
        }

        if(isRemember == true) {
            String token = createToken(userId);
            obj.setToken(token);
        }

        obj.setSuccess(1);
        obj.setUser(user);
        return obj;
    }

    @RequestMapping(value = "/api/user/check/password", method = RequestMethod.GET)
    public @ResponseBody ReturnData checkPassword(HttpServletRequest req) {
        ReturnData obj = new ReturnData();
        String token = req.getParameter("token");
        try {
            var result = verifyJWT(token);
        } catch (ExpiredJwtException e) { // ????????? ??????????????? ??????
            System.out.println("????????? ??????????????? ??????");
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        } catch (Exception e) { // ?????? ???????????? ??????
            System.out.println("?????? ???????????? ??????");
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        }

        String password = req.getParameter("password");
        String userId = req.getParameter("userId");
        var error = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(password)) { error.put("password", "???????????? ???????????????."); }
        if(error.size() > 0) {
            obj.setSuccess(0);
            obj.setError(error);
            return obj;
        }

        AccountDto user = new AccountDto();
        user = accountMapper.get_user_userId(userId);

        if(user == null) {
            obj.setSuccess(0);
            obj.setMessage("????????? ?????? ?????????????????????. ?????? ????????? ????????????.");
            return obj;
        }

        if(user.getPassword().toString().equals(password)) {
            obj.setSuccess(1);
        } else {
            obj.setSuccess(0);
            obj.setMessage("??????????????? ?????? ?????????????????????.");
        }
        return obj;
    }

    @RequestMapping(value = "/api/user/change/password", method = RequestMethod.GET)
    public @ResponseBody ReturnData changePassword(HttpServletRequest req) {
        ReturnData obj = new ReturnData();
        String token = req.getParameter("token");
        try {
            var result = verifyJWT(token);
        } catch (ExpiredJwtException e) { // ????????? ??????????????? ??????
            System.out.println("????????? ??????????????? ??????");
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        } catch (Exception e) { // ?????? ???????????? ??????
            System.out.println("?????? ???????????? ??????");
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        }

        String new_password = req.getParameter("new_password");
        String new_password_confirm = req.getParameter("new_password_confirm");
        String userId = req.getParameter("userId");
        var error = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(new_password)) { error.put("new_password", "???????????? ???????????????."); }
        if(StringUtils.isNullOrEmpty(new_password_confirm)) { error.put("new_password_confirm", "???????????? ???????????????."); }
        if(error.size() > 0) {
            obj.setSuccess(0);
            obj.setError(error);
            return obj;
        }

        AccountDto user = new AccountDto();
        user = accountMapper.get_user_userId(userId);
        if(user == null) {
            obj.setSuccess(0);
            obj.setMessage("????????? ?????? ?????????????????????. ?????? ????????? ????????????.");
            return obj;
        }

        System.out.println(new_password);
        if(new_password.equals(new_password_confirm)) {
            accountMapper.updateAccount(user.getUserId(), new_password);
            obj.setSuccess(1);
        } else {
            obj.setSuccess(0);
            obj.setMessage("??????????????? ?????? ?????????????????????.");
        }
        return obj;
    }

    @RequestMapping(value = "/api/user/verityJWT", method = RequestMethod.GET)
    public @ResponseBody ReturnData verityJWT(HttpServletRequest req) {
        ReturnData obj = new ReturnData();
        String token = req.getParameter("token");

        try {
            var result = verifyJWT(token);
        } catch (ExpiredJwtException e) { // ????????? ??????????????? ??????
            System.out.println(e);
            obj.setSuccess(0);
            return obj;
        } catch (Exception e) { // ?????? ???????????? ??????
            System.out.println(e);
            obj.setSuccess(0);
            return obj;
        }

        obj.setSuccess(1);
        return obj;
    }

    /*
       ?????????: /template/shared/top_menu.html --> getUser();
       ??????????????? ????????????.
    */
    @RequestMapping(value = "/api/user/own/info", method = RequestMethod.GET)
    public @ResponseBody ReturnData getUser(HttpServletRequest req) {
        ReturnData obj = new ReturnData();
        var list = req.getCookies();
        String token = "";
        for(Cookie cookie:list) {
            if(cookie.getName().equals("jwt")) {
                token = cookie.getValue();
            }
        }
        if(StringUtils.isNullOrEmpty(token) == true) {
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        }
        Map<String, Object> user = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes("UTF-8")) // Set Key
                    .parseClaimsJws(token) // ?????? ??? ??????, ?????? ??? ??????
                    .getBody();
            user = claims;
        } catch (ExpiredJwtException e) { // ????????? ??????????????? ??????
            System.out.println(e);
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        } catch (Exception e) { // ?????? ???????????? ??????
            System.out.println(e);
            obj.setSuccess(0);
            obj.setLogged(false);
            return obj;
        }

        var userId = user.get("userId").toString();
        var userDTO = accountMapper.get_user_userId(userId);

        obj.setSuccess(1);
        obj.setLogged(true);
        obj.setUser(userDTO);
        return obj;
    }

    /*
        ?????????: /template/signup/index --> handleSignUp();
        ??????????????? ??????.
     */
    @RequestMapping(value = "/api/user/signup", method = RequestMethod.GET)
    public @ResponseBody ReturnData SignUp(HttpServletRequest req) {
        ReturnData obj = new ReturnData();
        HashMap<String, String> error = new HashMap<String, String>();
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        String phoneNumber = req.getParameter("phoneNumber");

        if(StringUtils.isNullOrEmpty(userId) == true) {
            error.put("userId", "???????????? ???????????????.");
        }
        if(StringUtils.isNullOrEmpty(password) == true) {
            error.put("password", "???????????? ???????????????.");
        }
        if(StringUtils.isNullOrEmpty(phoneNumber) == true) {
            error.put("phoneNumber", "???????????? ???????????????.");
        }

        if(error.size() > 0) {
            obj.setSuccess(0);
            obj.setError(error);
            return obj;
        }

        var user = accountMapper.check_user(userId);
        if(user != null) {
            obj.setSuccess(0);
            error.put("userId", "?????? ???????????? Id?????????.");
            obj.setError(error);
            return obj;
        }

        accountMapper.sign_up(userId, password, phoneNumber);

        obj.setSuccess(1);
        obj.setMessage("??????????????? ?????????????????????.");
        return obj;
    }

    public String createToken(String userId) {
        //Header ?????? ??????
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload ?????? ??????
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("userId", userId);

        Long expiredTime = 1000 * 60L * 60L * 2L; // ?????? ?????? ?????? (2??????)

        Date ext = new Date(); // ?????? ?????? ??????
        ext.setTime(ext.getTime() + expiredTime);

        // ?????? Builder
        String jwt = Jwts.builder()
                .setHeader(headers) // Headers ??????
                .setClaims(payloads) // Claims ??????
                .setSubject("user") // ?????? ??????
                .setExpiration(ext) // ?????? ?????? ?????? ??????
                .signWith(SignatureAlgorithm.HS256, key.getBytes()) // HS256??? Key??? Sign
                .compact(); // ?????? ??????

        return jwt;
    }

    //?????? ??????
    public boolean verifyJWT(String jwt) throws UnsupportedEncodingException {
        Map<String, Object> claimMap = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes("UTF-8")) // Set Key
                    .parseClaimsJws(jwt) // ?????? ??? ??????, ?????? ??? ??????
                    .getBody();

            claimMap = claims;
            //Date expiration = claims.get("exp", Date.class);
            //String data = claims.get("data", String.class);

        } catch (ExpiredJwtException e) { // ????????? ??????????????? ??????
            System.out.println(e);
            return false;
        } catch (Exception e) { // ?????? ???????????? ??????
            System.out.println(e);
            return false;
        }
        return true;
    }
}