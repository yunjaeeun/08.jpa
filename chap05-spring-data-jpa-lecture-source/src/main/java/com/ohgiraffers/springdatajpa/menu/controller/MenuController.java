package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/menu")
@Slf4j                  // Lombok 형식의 Log
public class MenuController {

    /* 설명. 로그 생성해 보기 */
//    Logger logger = LoggerFactory.getLogger(MenuController.class);
//    Logger logger =LoggerFactory.getLogger(getClass());

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {         // MenuService가 Bean으로 관리되어야 의존성 주입 가능.
        this.menuService = menuService;
    }

    @GetMapping("/{menuCode}")                            // @PathVariable을 이용해 경로 뒤에 변수로 오는것을 받음
    public String findMenuByCode(@PathVariable int menuCode, Model model) {
//        logger.info("menuCode: {}", menuCode);               // 실행결과 -> menuCode: 7 (깃에 push할 때 log는 지우고 해야함)
        log.info("menuCode: {}", menuCode);

        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);


        return "menu/detail";
    }

    /* 설명. 페이징 처리 전 */
    @GetMapping("/list")
    public String findMenuList(Model model) {

        /* 필기. DB의 한 행들이 newMenu(); 엔티티 한개씩 나오기 때문에 List에 담아준다. */
        List<MenuDTO> menuList = menuService.findMenuList();
        model.addAttribute("menuList", menuList);   // menuList를 menuList라는 키 값에 담아서 전달해준다.

        return "menu/list";
    }
}
