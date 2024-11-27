package comment.service.impl;

import comment.bean.Comment;
import comment.bean.CommentStatus;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment writeComment(Comment comment) {

        if (comment.getParent() != null) {
            commentRepository.findById(comment.getParent().getCommentNo())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentNo, String newContent) {
        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Deleted comment");
        }

        existingComment.setContent(newContent);
        existingComment.setCommentStatus(CommentStatus.EDITED);

        log.debug("수정된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return existingComment;

    }


    @Override
    @Transactional
    public Comment deleteComment(Long commentNo) {

        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Already deleted");
        }

        existingComment.setCommentStatus(CommentStatus.DELETED);

        log.debug("삭제된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());
        return existingComment;
    }
}
