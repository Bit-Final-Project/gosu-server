package comment.service.impl;

import comment.bean.Comment;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment writeComment(Comment comment) {

        if (comment.getParent() != null) {
            Comment parentComment = commentRepository.findById(comment.getParent().getCommentNo())
                    .orElse(null);

            if (parentComment == null) {
                throw new IllegalArgumentException("Parent comment not found");

            }
        }

        return commentRepository.save(comment);
    }
}
