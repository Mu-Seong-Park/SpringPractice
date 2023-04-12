package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void save() {
        //given, 주어졌을 때
        Member member = new Member("hello", 20);

        //when, 실행했을 때
        Member saveMember = memberRepository.save(member);

        //then, 이거해야 돼
        Member findMember = memberRepository.findById(saveMember.getId());
        assertThat(findMember).isEqualTo(saveMember);
    }
    
    @Test
    void findAll() {
        //given
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member2", 19);

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> result = memberRepository.findAll();

        
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1,member2);


    }
}
