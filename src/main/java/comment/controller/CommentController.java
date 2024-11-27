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

    @PatchMapping("/update/{commentNo}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentNo, @RequestBody String newContent) {

        //수정때 받아야할 데이터 : 수정할 댓글 Id, 수정할 댓글 content
        //로직에서 자동으로 수정해야할 내용 : 댓글 상태 (DEFAULT -> EDITED)
        //수정된 시간 << 필요? 필요하면 작성시간에 덮어씌우기 vs 새로운 컬럼 생성

        Comment updatedComment = commentService.updateComment(commentNo, newContent);
        return ResponseEntity.ok(updatedComment);
    }

    @PatchMapping("/delete/{commentNo}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long commentNo) {

        //삭제한 댓글도 실무에선 DB에 남긴다고하는데 edited 말고 차라리 comment_status로 DB에는 남기지만 출력만 안시키는 방식이 괜찮을듯?
        //0=일반댓글 1=수정된댓글 99=삭제된댓글

        Comment deletedComment = commentService.deleteComment(commentNo);
        return ResponseEntity.ok(deletedComment);
    }
}
