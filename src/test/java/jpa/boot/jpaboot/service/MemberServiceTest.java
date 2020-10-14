package jpa.boot.jpaboot.service;

import jpa.boot.jpaboot.domain.Member;
import jpa.boot.jpaboot.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Rollback(value = false)
    // false를 안 하면 flush조차 하지 않아서 insert문 실행 안 됨
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("Kim");
        
        // when
        Long saveId = memberService.join(member);

        // then
        assertThat(memberRepository.findOne(saveId)).isEqualTo(member);
    }

    @DisplayName("중복 회원 예외")
    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);

        // then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}