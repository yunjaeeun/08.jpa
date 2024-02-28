package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagination;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
//    @GetMapping("/list")
//    public String findMenuList(Model model) {
//
//        /* 필기. DB의 한 행들이 newMenu(); 엔티티 한개씩 나오기 때문에 List에 담아준다. */
//        List<MenuDTO> menuList = menuService.findMenuList();
//        model.addAttribute("menuList", menuList);   // menuList를 menuList라는 키 값에 담아서 전달해준다.
//
//        return "menu/list";
//    }

    /* 설명. 페이징 처리 후 */
    @GetMapping("/list")
    public String findMenuList(@PageableDefault Pageable pageable, Model model) {
        log.info("pageable: {}", pageable);
//        List<MenuDTO> menuList = menuService.findMenuList();
//        model.addAttribute("menuList", menuList);

        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        log.info("조회한 내용 목록: {}", menuList.getContent());
        log.info("총 페이지 수: {}", menuList.getTotalPages());
        log.info("총 메뉴 수: {}", menuList.getTotalElements());
        log.info("해당 페이지에 표시 될 요소: {}", menuList.getSize());
        log.info("해당 페이지에 실제 요소 수: {}", menuList.getNumberOfElements());
        log.info("첫 페이지 여부: {}", menuList.isFirst());
        log.info("마지막 페이지 여부: {}", menuList.isLast());
        log.info("정렬 방식: {}", menuList.getSort());
        log.info("여러 페이지 중 현재 인덱스: {}", menuList.getNumber());

        /* 설명. 화면에서 버튼을 그리기 위해 필요한 재료 준비(클래스(모듈화) 두개 추가) */
        PagingButtonInfo paging = Pagination.getPagingButtonInfo(menuList);

        model.addAttribute("paging", paging);
        model.addAttribute("menuList", menuList);

        return "menu/list";
    }

    @GetMapping("/querymethod")     // /menu/querymethod
    public void queryMethodPage() {}

    @GetMapping("/search")
    public String fingMenuPrice(@RequestParam int menuPrice, Model model) {

        List<MenuDTO> menuList = menuService.findMenuPrice(menuPrice);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuPrice", menuPrice);

        return "menu/searchResult";
    }

    @GetMapping("/regist")
    public void registPage(){}

    /* 설명. /menu/regist.html이 열리자마자 js 코드를 통해 /menu/category 비동기 요청이 오게 된다. */
    /* 필기. 동기요청 -> 요청이 순차적으로, 비동기 요청 -> 요청이 진행중일 떄 따로 따로 요청을 발생시키는 것. */
    @GetMapping(value = "/category", produces = "application/json; charset=UTF-8")

    /* 설명.
     *  메소드에 @ResponseBody가 붙은 메소드의 반환형은 ViewResolver가 해석하지 않는다.
     *  @ResponseBody가 붙었을 때 기존과 다른 핸들러 메소드의 차이점
     *  1. 핸들러 메소드의 반환형이 어떤 것이라도 상관 없다(-> 모두 json 문자열 형태로 요청이 들어온 곳으로 반환된다.
     *  2. 한글이 포함된 데이터는 produces속성에 'application/json'라는 MIME 타입과 'charset=UTF-8' 인코딩 타입을 붙여준다.
    * */
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {
        return menuService.findAllCategory();
    }

    /* 설명. Spring Data JPA로 DML 작업하기(Insert, Update, Delete */
    @PostMapping("/regist")
    public String registMenu(MenuDTO newMenu){
        menuService.registMenu(newMenu);

        return "redirect:/menu/list";
    }

    @GetMapping("/modify")
    public void modifyPage(){}

    @PostMapping("/modify")
    public String modifyMenu(MenuDTO modifyMenu) {
        menuService.modifyMenu(modifyMenu);

        return "redirect:/menu/" + modifyMenu.getMenuCode();
    }

    @GetMapping("/delete")
    public void deletePage(){}

    @PostMapping("/delete")
    public String deleteMenu(@RequestParam int menuCode) {
        menuService.deleteMenu(menuCode);

        return "redirect:/menu/list";
    }
}
