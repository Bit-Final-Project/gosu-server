package admin.repository;

import member.bean.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminMemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select name from member where email = :email and pwd = :pwd and memberStatus = 'ADMIN'", nativeQuery = true)
    String adminLogin(@Param("email") String email, @Param("pwd") String pwd);
}
