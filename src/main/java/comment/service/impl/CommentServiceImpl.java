package comment.service.impl;

import article.bean.Article;
import comment.bean.Comment;
import comment.bean.CommentStatus;
import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import member.bean.Member;
import member.dao.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public CommentServiceImpl(CommentRepository commentRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public CommentResponse writeComment(CommentRequest writeRequest) {

        Comment newComment = toEntity(writeRequest);

        if (newComment.getParent() != null) {
            commentRepository.findById(newComment.getParent().getCommentNo())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        commentRepository.save(newComment);

        return toDTO(newComment);

    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentNo, String newContent) {
        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Deleted comment");
        }

        existingComment.setContent(newContent);
        existingComment.setCommentStatus(CommentStatus.EDITED);

        log.debug("수정된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return toDTO(existingComment);

    }


    @Override
    @Transactional
    public CommentResponse deleteComment(Long commentNo) {

        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Already deleted");
        }

        existingComment.setCommentStatus(CommentStatus.DELETED);

        log.debug("삭제된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return toDTO(existingComment);
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

        response.setMemberName(getMemberName(comment.getMember().getMemberNo()));
        response.setMemberProfileImage(getMemberProfileImage(comment.getMember().getMemberNo()));

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


    public String getMemberName(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberNo));
        return member.getName();
    }

    public String getMemberProfileImage(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberNo));
        return member.getProfileImage();
    }

}
