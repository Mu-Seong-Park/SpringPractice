package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final MemberRepositoryV2 memberRepository;
    private final DataSource dataSource;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false);//트랜잭션 시작, 수동 커밋 모드

            //비즈니스 로직 수행
            bizLogic(con, fromId, toId, money);

            //성공 -> 커밋 명령어 실행
            con.commit();
        } catch (Exception e) {
            //실패 -> 롤백
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            if(con != null) {
                try{
                    //AutoCommit의 기본값인 true로 돌려서, 다시 자동 커밋 모드로 만들어준다.
                    con.setAutoCommit(true);
                    //커넥션 풀에 반환한다.
                    con.close();
                    
                } catch (Exception e) {
                    log.info("error", e);
                }
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);

        validation(toMember);

        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

}
