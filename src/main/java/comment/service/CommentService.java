package comment.service;

import comment.dto.CommentDTO;

public interface CommentService {

    CommentDTO writeComment(CommentDTO commentDTO);

    CommentDTO updateComment(Long commentNo, String newContent);

    CommentDTO deleteComment(Long commentNo);

}
