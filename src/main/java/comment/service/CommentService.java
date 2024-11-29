package comment.service;

import comment.bean.Comment;
import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import comment.dto.MemberCommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    CommentResponse writeComment(CommentRequest writeRequest);

    CommentResponse updateComment(Long commentNo, String newContent);

    CommentResponse deleteComment(Long commentNo);

    List<MemberCommentResponse> findCommentsByMember(Long memberNo);

    List<CommentResponse> findCommentsByArticle(Long articleNo);

    Page<CommentResponse> findPagedCommentsByArticle(Long articleNo, int page, int pageSize);
}
