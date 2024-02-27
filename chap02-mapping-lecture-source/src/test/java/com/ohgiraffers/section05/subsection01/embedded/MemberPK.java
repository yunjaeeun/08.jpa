package com.ohgiraffers.section05.subsection01.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/* 설명.
 *  Embeddable 타입은 하나의 값의 묶음이자 불변 객체로 다루는 타입이다.
 *  (불변객체 -> setter가 없고 필드값에 변화를 주고 싶으면 새로운 필드를 가지는 새로운 객체가 생성되어야 함.)
 *  그리고 equals, hashCode를 반드시 오버라이딩 해야한다. -> 객체의 값이 같으면 같은걸로 취급하기 위해서.
 *  VO와 유사하며 @EmbeddedId로 복합키를 표현하고자 할 때 쓰이기도 한다
* */
@Embeddable
public class MemberPK implements Serializable {       // 복합키를 사용할 때 Embeddable 타입은 Serializable을 상속받아야 한다.

    @Column(name="member_no")
    private int memberNo;

    @Column(name="member_id")
    private String memberId;

    public MemberPK() {
    }

    public MemberPK(int memberNo, String memberId) {
        this.memberNo = memberNo;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberPK memberPK = (MemberPK) o;
        return memberNo == memberPK.memberNo && Objects.equals(memberId, memberPK.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNo, memberId);
    }
}
