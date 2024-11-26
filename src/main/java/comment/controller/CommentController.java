package comment.controller;

import comment.bean.Comment;
import comment.service.CommentService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/write")
    public ResponseEntity<Comment> writeComment(RequestEntity<Comment> requestEntity) {
        Comment wrtitenComment = commentService.writeComment(requestEntity.getBody());
        return ResponseEntity.ok(wrtitenComment);

    }

}
