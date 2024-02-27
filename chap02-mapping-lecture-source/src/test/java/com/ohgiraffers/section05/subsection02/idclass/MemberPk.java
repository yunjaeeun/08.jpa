package com.ohgiraffers.section05.subsection02.idclass;

import java.io.Serializable;
import java.util.Objects;

/* 설명. IdClass 타입으로 쓰인 클래스도 1차 캐시에서 식별자로 쓰이는 객체 이므로 equals hashcode 오버라이딩 */
public class MemberPk implements Serializable {

    private int memberNo;
    private String memberId;

    public MemberPk() {
    }

    public MemberPk(int memberNo, String memberId) {
        this.memberNo = memberNo;
        this.memberId = memberId;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberPk memberPk = (MemberPk) o;
        return memberNo == memberPk.memberNo && Objects.equals(memberId, memberPk.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberNo, memberId);
    }
}
