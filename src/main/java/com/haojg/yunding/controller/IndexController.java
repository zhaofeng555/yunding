package com.haojg.yunding.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpSession session){
        return "login";
    }

    @GetMapping("/goto/{path}")
    public String goPath(@PathVariable String path){
        return path;
    }
    
   
}
