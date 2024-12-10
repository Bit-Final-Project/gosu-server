package com.ncp.moeego.comment.service;

import com.ncp.moeego.comment.entity.Comment;
import com.ncp.moeego.comment.entity.CommentStatus;
import com.ncp.moeego.comment.dto.CommentRequest;
import com.ncp.moeego.comment.dto.CommentResponse;
import com.ncp.moeego.comment.dto.MemberCommentResponse;
import com.ncp.moeego.comment.mapper.CommentMapper;
import com.ncp.moeego.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

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
    public Page<MemberCommentResponse> findCommentsByMember(Long memberNo, int pg, int pageSize) {
        Pageable pageable = PageRequest.of(pg-1, pageSize, Sort.by("writeDate").ascending());
        return commentRepository.findByMember_MemberNo(memberNo, pageable);
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

    //게시물 ID로 페이징된 댓글 조회
    @Override
    public Page<CommentResponse> findPagedCommentsByArticle(Long articleNo, int pg, int pagesize) {

        Pageable pageable = PageRequest.of(pg-1, pagesize, Sort.by("writeDate").ascending());

        Page<Comment> commentList = commentRepository.findParentCommentsByArticle(articleNo, pageable);

        // Comment -> CommentResponse로 변환
        return commentList.map(commentMapper::toDTO);
    }

}
