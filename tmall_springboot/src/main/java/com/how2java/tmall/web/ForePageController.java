package com.how2java.tmall.web;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
 

//和 AdminPageController 类似的， 这里专门做前台页面的跳转。

@Controller
public class ForePageController {
    @GetMapping(value="/")
    public String index(){
        return "redirect:home";
    }
    @GetMapping(value="/home")
    public String home(){
        return "fore/home";
    }
}