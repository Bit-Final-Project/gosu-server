package comment.service;

import comment.dto.CommentRequest;
import comment.dto.CommentResponse;

public interface CommentService {

    CommentResponse writeComment(CommentRequest writeRequest);

    CommentResponse updateComment(Long commentNo, String newContent);

    CommentResponse deleteComment(Long commentNo);

}
