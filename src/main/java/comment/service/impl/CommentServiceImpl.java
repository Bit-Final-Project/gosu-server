package comment.service.impl;

import comment.bean.Comment;
import comment.bean.CommentStatus;
import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import comment.dto.MemberCommentResponse;
import comment.mapper.CommentMapper;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    //댓글 작성
    @Override
    public CommentResponse writeComment(CommentRequest writeRequest) {

        Comment newComment = commentMapper.toEntity(writeRequest);

        if (newComment.getParent() != null) {
            commentRepository.findById(newComment.getParent().getCommentNo())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        commentRepository.save(newComment);

        return commentMapper.toDTO(newComment);

    }

    //댓글 수정
    @Override
    @Transactional
    public CommentResponse updateComment(Long commentNo, String newContent) {
        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Deleted comment");
        }

        existingComment.setContent(newContent);
        existingComment.setCommentStatus(CommentStatus.EDITED);

        log.debug("수정된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return commentMapper.toDTO(existingComment);

    }

    //댓글 삭제
    @Override
    @Transactional
    public CommentResponse deleteComment(Long commentNo) {

        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Already deleted");
        }

        existingComment.setCommentStatus(CommentStatus.DELETED);

        log.debug("삭제된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return commentMapper.toDTO(existingComment);
    }

    //회원 ID로 댓글 조회
    @Override
    public List<MemberCommentResponse> findCommentsByMember(Long memberNo) {
        return commentRepository.findByMember_MemberNo(memberNo);
    }

    //게시물 ID로 댓글 조회
    @Override
    public List<CommentResponse> findCommentsByArticle(Long articleNo) {
        List<Comment> commentList = commentRepository.findByArticle_ArticleNo(articleNo);

        return commentList.stream()
                .filter(comment -> comment.getParent() == null)
                .map(commentMapper::toDTO)
                .toList();
    }

}
