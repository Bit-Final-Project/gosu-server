package comment.service.impl;

import article.bean.Article;
import comment.bean.Comment;
import comment.bean.CommentStatus;
import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import comment.dto.MemberCommentResponse;
import comment.mapper.CommentMapper;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import member.bean.MemberEntity;
import member.dao.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MemberRepository memberRepository;

    //댓글 작성
    @Override
    public CommentResponse writeComment(CommentRequest writeRequest) {

        Comment newComment = commentMapper.toEntity(writeRequest);

        if (newComment.getParent() != null) {
            Comment parentComment = commentRepository.findById(newComment.getParent().getCommentNo())
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

            if (parentComment.getCommentStatus() == CommentStatus.DELETED) {
                throw new IllegalArgumentException("삭제된 댓글에 답글을 달수 없습니다.");
            }
        }
        commentRepository.save(newComment);

        return commentMapper.toDTO(newComment);

    }

    //댓글 수정
    @Override
    @Transactional
    public CommentResponse updateComment(Long commentNo, String newContent) {
        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("수정하려는 댓글을 찾을 수 없습니다. : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("이미 삭제된 댓글입니다.");
        }

        existingComment.setContent(newContent);
        existingComment.setCommentStatus(CommentStatus.EDITED);

        log.debug("수정된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return commentMapper.toDTO(existingComment);

    }

    //댓글 삭제
    @Override
    @Transactional
    public CommentResponse deleteComment(Long commentNo) {

        Comment existingComment = commentRepository.findById(commentNo).orElseThrow(() -> new IllegalArgumentException("삭제하려는 댓글을 찾을 수 없습니다. : " + commentNo));

        if (existingComment.getCommentStatus() == CommentStatus.DELETED) {
            throw new IllegalArgumentException("이미 삭제된 댓글입니다.");
        }

        existingComment.setCommentStatus(CommentStatus.DELETED);

        log.debug("삭제된 댓글: ID={}, Content={}", existingComment.getCommentNo(), existingComment.getContent());

        return commentMapper.toDTO(existingComment);
    }

    //회원 ID로 댓글 조회
    @Override
    public List<MemberCommentResponse> findCommentsByMember(Long memberNo) {
        return commentRepository.findByMember_MemberNo(memberNo);
    }

    //게시물 ID로 댓글 조회
    @Override
    public List<CommentResponse> findCommentsByArticle(Long articleNo) {
        List<Comment> commentList = commentRepository.findByArticle_ArticleNo(articleNo);

        return commentList.stream()
                .filter(comment -> comment.getParent() == null)
                .map(commentMapper::toDTO)
                .toList();
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

        MemberEntity member = new MemberEntity();
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
        MemberEntity member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberNo));
        return member.getName();
    }

    public String getMemberProfileImage(Long memberNo) {
        MemberEntity member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberNo));
        return member.getProfileImage();
    }

}
