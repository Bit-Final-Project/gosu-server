package member.dao;

import member.bean.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    //email(id)로 회원 정보 출력
    @Query("select t.email from Member t where t.email like concat('%', :email, '%')")
    String findByEmailLike(@Param("email") String email);
    //이름으로 전체 회원 검색
    List<Member> findAllByNameContaining(String name);
    //로그인
    @Query("SELECT t FROM Member t WHERE t.email = :email AND t.pwd = :pwd")
    Member login(@Param("email") String email, @Param("pwd") String pwd);
}
