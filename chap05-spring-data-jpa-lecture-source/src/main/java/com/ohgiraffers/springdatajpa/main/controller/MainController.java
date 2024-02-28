package com.ohgiraffers.springdatajpa.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/main"})     // view resolver 에게 / 또는 /main 요청이 오면 main으로 보내줌
    public String main() {
        return "main/main";
    }
}
