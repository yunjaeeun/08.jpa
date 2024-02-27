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

    @Test
    public void 준영속성_detach_테스트(){

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        /* 설명
         *  영속성 컨텍스트가 관리하던 엔티티 객체를 관리하지 않는 상태가 되게 한 것을 준영속 상태라고 한다.
         *  detach가 준영속 상태를 만들기 위한 메소드이다.
        * */
        entityManager.detach(foundMenu2);

        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);

        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());      // 오류발생

    }

    @Test
    public void  준영속성_clear_close_테스트() {
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        /* 설명. 영속성 컨텍스트로 관리되던 엔티티 객체들을 모두 비영속 상태로 바꿈. */
//        entityManager.clear();

        /* 설명. 영속성 컨텍스트 및 엔티티 매니저까지 종료해 버린다.(사용불가) */
        entityManager.close();


        foundMenu1.setMenuPrice(5000);      // 비영속 상태인 객체를 바꿈.
        foundMenu2.setMenuPrice(5000);

        /* 설명. DB에서 새로 조회 해온 객체를 영속 상태로 두기 때문에 전혀 다른 결과가 나온다. */
        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 병합_merge_수정_테스트(){
        Menu menuToDetach = entityManager.find(Menu.class, 2);
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuName("수박죽");
        Menu refoundMenu = entityManager.find(Menu.class, 2);       // 기존의 메뉴 이름이 있음.

        System.out.println(menuToDetach.hashCode());
        System.out.println(refoundMenu.hashCode());

        entityManager.merge(menuToDetach);

        Menu mergedMenu = entityManager.find(Menu.class, 2);
        assertEquals("수박죽", mergedMenu.getMenuName());
    }

    @Test
    public void 병합_merge_삽입_테스트(){
        Menu menuToDetach = entityManager.find(Menu.class, 2);
        entityManager.detach(menuToDetach);             // 가져온 메뉴를 잠깐 분리함.

        menuToDetach.setMenuCode(999);                  // 분리한 메뉴를 수정
        menuToDetach.setMenuName("수박죽");

        entityManager.merge(menuToDetach);              // merge하면 병합되기 때문에 원본의 데이터가 수정되어버림.

        Menu newMenu = entityManager.find(Menu.class, 2);       // 2번 메뉴를 조회하면 db에 없기 때문에 새로 생성됨.
        Menu mergedMenu = entityManager.find(Menu.class, 999);
        assertNotEquals(mergedMenu.getMenuCode(), newMenu.getMenuCode());


    }

}
