package admin.repository;

import member.bean.Member;
import member.bean.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminMemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select name from member where email = :email and pwd = :pwd and member_status = 'ADMIN'", nativeQuery = true)
    String adminLogin(@Param("email") String email, @Param("pwd") String pwd);

    long countByMemberStatus(MemberStatus memberStatus);
}
