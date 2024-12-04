package comment.mapper;

import article.bean.Article;
import comment.bean.Comment;
import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import member.entity.Member;
import member.service.MemberServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final MemberServiceImpl memberService;

    public CommentMapper(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }


    public CommentResponse toDTO(Comment comment) {
        CommentResponse response = new CommentResponse();

        response.setCommentNo(comment.getCommentNo());
        response.setArticleNo(comment.getArticle().getArticleNo());
        response.setMemberNo(comment.getMember().getMemberNo());
        response.setContent(comment.getContent());
        response.setCommentStatus(comment.getCommentStatus());
        response.setWriteDate(comment.getWriteDate());

        response.setParentCommentNo(
                comment.getParent() != null ? comment.getParent().getCommentNo() : 0
        );

        //재귀
        for (Comment child : comment.getChildren()) {
            response.getChildren().add(toDTO(child));
        }

        response.setMemberName(memberService.getMemberName(comment.getMember().getMemberNo()));
        response.setMemberProfileImage(memberService.getMemberProfileImage(comment.getMember().getMemberNo()));

        return response;
    }

    public Comment toEntity(CommentRequest request) {

        Comment comment = new Comment();

        comment.setContent(request.getContent());

        Article article = new Article();
        article.setArticleNo(request.getArticleNo());
        comment.setArticle(article);

        Member member = new Member();
        member.setMemberNo(request.getMemberNo());
        comment.setMember(member);

        if (request.getParentCommentNo() != 0) {
            Comment parent = new Comment();
            parent.setCommentNo(request.getParentCommentNo());
            comment.setParent(parent);
        }

        /*
        dto->entity 전환시에는 이 요소가 필요없는거 같아서 일단 주석처리

        comment.setChildren(new ArrayList<>());
        for (CommentDTO child : request.getChildren()) {
            comment.getChildren().add(toEntity(child));
        }

        */

        return comment;
    }
}
