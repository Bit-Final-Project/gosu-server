package comment.service;

import comment.bean.Comment;

public interface CommentService {

    Comment writeComment(Comment comment);

    Comment updateComment(Long commentNo, String newContent);

    Comment deleteComment(Long commentNo);

}
