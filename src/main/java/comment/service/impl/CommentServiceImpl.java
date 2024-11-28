package comment.service.impl;

import comment.bean.Comment;
import comment.bean.CommentStatus;
import comment.dto.CommentRequest;
import comment.dto.CommentResponse;
import comment.dto.MemberCommentResponse;
import comment.mapper.CommentMapper;
import comment.repository.CommentRepository;
import comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

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

}
