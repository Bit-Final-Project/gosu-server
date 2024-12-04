package com.ncp.moeego.comment.test;

import com.ncp.moeego.comment.bean.CommentStatus;
import com.ncp.moeego.comment.dto.MemberCommentResponse;
import com.ncp.moeego.comment.repository.CommentRepository;
import com.ncp.moeego.comment.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("회원 ID로 댓글 조회 - 실패(댓글 없음)")
    public void testFindCommentsByMember_noComments() {

        // given
        Long memberNo = 1L;
        Mockito.when(commentRepository.findByMember_MemberNo(memberNo)).thenReturn(Collections.emptyList());

        // when
        List<MemberCommentResponse> responses = commentService.findCommentsByMember(memberNo);

        // then
        Assertions.assertNotNull(responses, "결과는 null이 아니어야 합니다.");
        Assertions.assertTrue(responses.isEmpty(), "결과 리스트는 비어 있어야 합니다.");
    }

    @Test
    @DisplayName("회원 ID로 댓글 조회 - 성공")
    public void testFindCommentsByMember_Success() {

        //given
        Long memberNo = 1L;

        MemberCommentResponse response1 = new MemberCommentResponse(
                1L, 101L, "첫 번째 게시글", "첫 번째 댓글", CommentStatus.DEFAULT, LocalDateTime.now());

        MemberCommentResponse response2 = new MemberCommentResponse(
                2L, 102L, "두 번째 게시글", "두 번째 댓글", CommentStatus.DEFAULT, LocalDateTime.now());

        List<MemberCommentResponse> mockResponses = List.of(response1, response2);

        Mockito.when(commentRepository.findByMember_MemberNo(memberNo)).thenReturn(mockResponses);

        //when
        List<MemberCommentResponse> responses = commentService.findCommentsByMember(memberNo);

        //then
        Assertions.assertNotNull(responses);
        Assertions.assertEquals(2, responses.size());
        Assertions.assertEquals("첫 번째 댓글", responses.get(0).getContent());
        Assertions.assertEquals("두 번째 댓글", responses.get(1).getContent());

    }

}

