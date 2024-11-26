package comment.controller;

import comment.bean.Comment;
import comment.service.CommentService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
/*
    @PatchMapping("/update")
    public ResponseEntity<Comment> updateComment(RequestEntity<Comment> requestEntity) {

        //TODO
        //사용자 검증 로직 추가 필요
        //본인이 작성한 댓글만 수정 할 수 있게
        //추후에 ss랑 jwt 완성되면 추가하는게 일 두번안할듯
        //엔티티 수정 필요할듯? edited = 0 , 1

        Comment updatedComment = commentService.updateComment(requestEntity.getBody());
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Comment> deleteComment(RequestEntity<Comment> requestEntity) {

        //TODO
        //마찬가지로 검증 로직 추가 필요
        //삭제한 댓글도 실무에선 DB에 남긴다고하는데 edited 말고 차라리 commentStatus로 DB에는 남기지만 출력만 안시키는 방식이 괜찮을듯?
        // 0=일반댓글 1=수정된댓글 99=삭제된댓글

        Comment deletedComment = commentService.deleteComment(requestEntity.getBody());
        return ResponseEntity.ok(deletedComment);
    }*/
}
