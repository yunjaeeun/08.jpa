package com.ohgiraffers.section03.persistencecontext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class A_EntityLifeCycleTests {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    /* 필기.
     *  영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공가능로 엔티티 객체를 보관하고 관리한다.
     *  엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 만들어진다.
     *  [엔티티의 생명 주기]
     *  비영속, 영속, 준영속, 삭제상태
    * */

    @Test
    public void 비영속성_테스트() {
        Menu foundMenu = entityManager.find(Menu.class, 11);
//        System.out.println("foundMenu = " + foundMenu);

        /* 설명. 영속 상태의 객체에서 값을 추출해 담더라도 새로 생성되어 영속성 컨텍스트와 관련 없는 객체는 비영속 상태이다. */
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        boolean isTrue = (foundMenu == newMenu);

        assertFalse(isTrue);


    }

    @Test
    public void 영속성_연속_조회_테스트() {
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        boolean isTrue = (foundMenu1 == foundMenu2);

        assertTrue(isTrue);
    }

    @Test
    public void 영속성_객체_추가_테스트() {

        /* 설명.
         *  이 예제에서는 menuCode 필드 값에 직접 값을 주고자 한다.(auto_increment 개념 안 쓸 예정)
         *  @GeneratedValue 부분을 주석하고 테스트를 작성한다
        * */
        Menu menuToResist = new Menu();

        menuToResist.setMenuCode(500);
        menuToResist.setMenuName("수박죽");
        menuToResist.setMenuPrice(4030033);
        menuToResist.setCategoryCode(10);
        menuToResist.setOrderableStatus("Y");

        entityManager.persist(menuToResist);
        Menu foundMenu = entityManager.find(Menu.class, 500);
        boolean isTrue = (menuToResist == foundMenu);

        assertTrue(isTrue);

    }

    @Test
    public void 영속성_객체_추가_값_변경_테스트() {
        Menu menuToResist = new Menu();

        menuToResist.setMenuCode(500);
        menuToResist.setMenuName("수박죽");
        menuToResist.setMenuPrice(4030033);
        menuToResist.setCategoryCode(10);
        menuToResist.setOrderableStatus("Y");

        entityManager.persist(menuToResist);
        menuToResist.setMenuName("메론죽");

        Menu foundMenu = entityManager.find(Menu.class,500);

        assertEquals("메론죽", foundMenu.getMenuName());
    }

}
