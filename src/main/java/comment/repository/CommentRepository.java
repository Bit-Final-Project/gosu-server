package comment.repository;

import comment.bean.Comment;
import comment.dto.MemberCommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT new comment.dto.MemberCommentResponse(
                c.commentNo,
                c.article.articleNo,
                c.article.subject,
                c.content,
                c.commentStatus,
                c.writeDate)
            FROM Comment c
            WHERE c.member.memberNo = :memberNo
            """)
    List<MemberCommentResponse> findByMember_MemberNo(Long memberNo);

    List<Comment> findByArticle_ArticleNo(Long articleNo);
}
