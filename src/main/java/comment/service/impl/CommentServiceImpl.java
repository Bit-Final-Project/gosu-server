package comment.service.impl;

import article.bean.Article;
import comment.bean.Comment;
import comment.bean.CommentStatus;
import comment.dto.CommentDTO;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import member.bean.Member;
import member.dao.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
    public CommentDTO writeComment(CommentDTO commentDTO) {

        Comment newComment = toEntity(commentDTO);

        if (newComment.getParent() != null) {
            commentRepository.findById(newComment.getParent().getCommentNo())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        commentRepository.save(newComment);

        return toDTO(newComment);

    }

    @Override
    @Transactional
    public CommentDTO updateComment(Long commentNo, String newContent) {
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
    public CommentDTO deleteComment(Long commentNo) {

        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("Comment not found : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("Already deleted");
        }

        existingComment.setCommentStatus(CommentStatus.DELETED);

        log.debug("삭제된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return toDTO(existingComment);
    }

    public Comment toEntity(CommentDTO dto) {

        Comment comment = new Comment();

        comment.setContent(dto.getContent());

        Article article = new Article();
        article.setArticleNo(dto.getArticleNo());
        comment.setArticle(article);

        Member member = new Member();
        member.setMemberNo(dto.getMemberNo());
        comment.setMember(member);

        if (dto.getParentCommentNo() != 0) {
            Comment parent = new Comment();
            parent.setCommentNo(dto.getParentCommentNo());
            comment.setParent(parent);
        }

        comment.setChildren(new ArrayList<>());
        for (CommentDTO child : dto.getChildren()) {
            comment.getChildren().add(toEntity(child));
        }

        return comment;
    }


    public CommentDTO toDTO(Comment comment) {

        CommentDTO dto = new CommentDTO();

        dto.setCommentNo(comment.getCommentNo());
        dto.setArticleNo(comment.getArticle().getArticleNo());
        dto.setMemberNo(comment.getMember().getMemberNo());
        dto.setContent(comment.getContent());
        dto.setCommentStatus(comment.getCommentStatus());
        dto.setWriteDate(comment.getWriteDate());

        dto.setParentCommentNo(
                comment.getParent() != null ? comment.getParent().getCommentNo() : 0
        );

        //재귀
        for (Comment child : comment.getChildren()) {
            dto.getChildren().add(toDTO(child));
        }

        dto.setMemberName(getMemberName(comment.getMember().getMemberNo()));
        dto.setMemberProfileImage(getMemberProfileImage(comment.getMember().getMemberNo()));

        return dto;

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
