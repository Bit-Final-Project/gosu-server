package comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequest {

    private Long memberNo;
    private Long articleNo;
    private String content;
    private Long parentCommentNo;

}
