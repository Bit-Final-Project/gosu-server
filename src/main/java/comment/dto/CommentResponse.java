package comment.dto;

import comment.bean.CommentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CommentResponse {

    private long commentNo;
    private long articleNo;
    private long memberNo;
    private String memberName; // 작성자 이름
    private String memberProfileImage; // 작성자 프로필 사진
    private String content;
    private CommentStatus commentStatus;
    private LocalDateTime writeDate;
    private long parentCommentNo;
    private List<CommentResponse> children = new ArrayList<>(); // 대댓글 리스트

}
