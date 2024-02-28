package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

/* 설명.
 *  Service 계층: 비즈니스 로직, 트랜잭션처리(@Transactional), DTO<->Entity(modelmapper lib 활용)
 * */
@Service
public class MenuService {

    private final ModelMapper mapper;               // 의존성 주입
    private final MenuRepository menuRepository;    // repository는 DB에 쿼리를 날려주는 역할
    private final CategoryRepository categoryRepository;

    @Autowired
    public MenuService(ModelMapper mapper, MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    /* 설명. 1. findById 예제 */
    public MenuDTO findMenuByCode(int menuCode) {

        /* 필기. findById로 조회를 하면 optional 타입으로 넘어오고 orElstThrow를 사용해서 예외처리 후 만들었던 Mapper 타입으로 변환 */
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);      // findById는 JpaRepository가 물려준다.

        return mapper.map(menu, MenuDTO.class);     // ModelMapper를 활용해서 Menu Entity를 MenuDTO로 변환
    }


    /* 설명. 2. findAll(페이징 처리 전) */
    public List<MenuDTO> findMenuList() {

        /* 필기. 조회할 때 menuCode를 내림차순으로 정렬할 수 있다. */
        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());

        /* 필기. menuList에 담긴 값을 하나씩 꺼내서 menu를 MenuDTO 타입으로 바꿔 통채로 List에 담아준다. */
        return menuList.stream().map(menu -> mapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }
    
    
}
