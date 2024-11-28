package comment.controller;

import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import comment.dto.MemberCommentResponse;
import comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/write")
    public ResponseEntity<CommentResponse> writeComment(@RequestBody CommentRequest writeRequest) {
        CommentResponse writtenComment = commentService.writeComment(writeRequest);
        return ResponseEntity.ok(writtenComment);

/*
        댓글 작성시 db에 입력되야하는 값:
        id, 게시글id, 작성자id, 부모댓글id, 내용, 작성시간, 댓글상태
        클라이언트에서 받아야하는값:
        게시글id(?), 작성자id(?), 부모댓글id, 내용
*/

    }

    @PatchMapping("/update")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest updateRequest) {

        //수정때 받아야할 데이터 : 수정할 댓글 id, 수정할 댓글 content
        //로직에서 자동으로 수정해야할 내용 : 댓글 상태 (DEFAULT -> EDITED)
        //수정된 시간 << 필요? 필요하면 작성시간에 덮어씌우기 vs 새로운 컬럼 생성
        log.debug("UPDATE REQUEST = commentNo: {} , content: {}", updateRequest.getCommentNo(), updateRequest.getContent());
        CommentResponse updatedComment = commentService.updateComment(updateRequest.getCommentNo(), updateRequest.getContent());
        return ResponseEntity.ok(updatedComment);
    }

    @PatchMapping("/delete")
    public ResponseEntity<CommentResponse> deleteComment(@RequestBody CommentRequest deleteRequest) {

        //삭제한 댓글도 실무에선 DB에 남긴다고하는데 edited 말고 차라리 comment_status로 DB에는 남기지만 출력만 안시키는 방식이 괜찮을듯?
        //0=일반댓글 1=수정된댓글 99=삭제된댓글 - enum 처리

        log.debug("DELETE REQUEST = commentNo: {}", deleteRequest.getCommentNo());
        CommentResponse deletedComment = commentService.deleteComment(deleteRequest.getCommentNo());


        return ResponseEntity.ok(deletedComment);
    }

    @GetMapping("/myPage")
    public List<MemberCommentResponse> myPage(@RequestParam Long memberNo) {
        return commentService.findCommentsByMember(memberNo);

    }

    @GetMapping("/article")
    public List<CommentResponse> article(@RequestParam Long articleNo) {
        return commentService.findCommentsByArticle(articleNo);
    }

}
