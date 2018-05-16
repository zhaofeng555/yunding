package com.haojg.shouji.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.haojg.shouji.bean.User;
import com.haojg.shouji.dao.UserDao;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpSession session){
        return "login";
    }

    @RequestMapping("/register")
    public String register(){

        return "register";
    }

    @GetMapping("/{path}")
    public String goPath(@PathVariable String path){
        return path;
    }
    
   
}
