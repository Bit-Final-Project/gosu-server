package com.ncp.moeego.article.repository;

import com.ncp.moeego.article.bean.ArticleDTO;
import com.ncp.moeego.article.entity.Article;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 1번 (이벤트 게시글) 만 가져오기
    @Query("select t from Article t where t.type = :type")
    List<Article> findAllByType(@Param("type") int type);
    
    // 0, 1(공지, 이벤트) 가져오기
    @Query("select a from Article a where a.type in (0, 1)")
    List<Article> findAllEventArticle();
    
    // 관리자 수정 부분 해당 게시글 조회
    @Query("select a from Article a where a.articleNo = :articleNo")
    Article findByArticleNo(@Param("articleNo") Long articleNo);
    
    // 아티클 넘버가 10번인 게시글 가져오기
    @Query("select t from Article t where t.articleNo = :num")
    Article findByArticleNo(@Param("num") int num);

    // 제목 + 내용 검색한 게시글 조회
    @Query("SELECT a FROM Article a WHERE a.subject LIKE CONCAT('%', :subject, '%') AND a.content LIKE CONCAT('%', :content, '%')")
    List<Article> searchArticles(@Param("subject") String subject, @Param("content") String content);

    // 제목만 조회
    @Query("SELECT a FROM Article a WHERE a.subject LIKE CONCAT('%', :subject,'%')")
    List<Article> searchSubjectArticles(@Param("subject") String subject);

    // 내용만 조회
    @Query("SELECT a FROM Article a WHERE a.content LIKE CONCAT('%', :content,'%')")
    List<Article> searchContentArticles(@Param("content") String content);

    // 페이징
    @Query("SELECT a FROM Article a ORDER BY a.articleNo DESC")
    Page<Article> findAllWithPaging(Pageable pageable);

    // 인기 게시글 조회
    Page<Article> findAllByOrderByLikesDesc(Pageable pageable);

    // 타입별 게시판 조회
    Page<Article> findByType(int type, PageRequest pageRequest);

    // 내가 작성한 게시글 목록
    @Query("SELECT a FROM Article a WHERE a.member.memberNo = :memberNo")
    Page<Article> findByMemberNo(@Param("memberNo") Long memberNo, Pageable pageable);

    // 전체 게시글 댓글 수 가져오는 쿼리
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN Comment c ON a.articleNo = c.article.articleNo " +
            "AND c.commentStatus <> 'DELETED' " +  // 여기서 'DELETED' 댓글만 제외 현식이 요청사항 ^^...
            "WHERE a.type NOT IN (0, 1) " +      // 게시글 type이 0, 1인 것 제외 현식이 요청사항 ^^...
            "GROUP BY a.articleNo")
    Page<Object[]> findArticlesWithCommentCount(Pageable pageable);

    // 타입별 댓글 수 가져오는 쿼리
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN Comment c ON a.articleNo = c.article.articleNo AND c.commentStatus <> 'DELETED' WHERE a.type = :type GROUP BY a")
    Page<Object[]> findTypeArticlesWithCommentCount(@Param("type") int type, Pageable pageable);

    // 검색 값 가져오는 쿼리
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN Comment c ON a.articleNo = c.article.articleNo AND c.commentStatus <> 'DELETED' WHERE (a.content like %:value% or a.subject like %:value% or :value is null) and a.type in (2, 3, 4) GROUP BY a")
    Page<Object[]> findSearchArticles(@Param("value") String value, Pageable pageable);

    // 내가 작성한 글 댓글 수 가져오는 쿼리
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN Comment c ON a.articleNo = c.article.articleNo AND c.commentStatus <> 'DELETED' WHERE a.member.memberNo = :memberNo GROUP BY a")
    Page<Object[]> findMyArticlesWithCommentCount(@Param("memberNo") Long memberNo, Pageable pageable);

    // 상세 페이지 댓글 수 가져오는 쿼리
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN Comment c ON a.articleNo = c.article.articleNo AND c.commentStatus <> 'DELETED' WHERE a.articleNo = :articleNo GROUP BY a")
    Optional<Object[]> findArticleWithCommentCount(@Param("articleNo") Long articleNo);

    // 좋아요 순 댓글 수 가져오는 쿼리
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN Comment c ON a.articleNo = c.article.articleNo AND c.commentStatus <> 'DELETED' WHERE a.type NOT IN (0,1) GROUP BY a ORDER BY a.likes DESC")
    Page<Object[]> findHotArticlesWithCommentCount(Pageable pageable);

    // 공지 및 게시글 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Article a WHERE a.articleNo = :articleNo AND a.member.memberNo = :memberNo")
    int deleteArticleByArticleNoAndMemberNo(@Param("articleNo") Long articleNo, @Param("memberNo") Long memberNo);

//    @Query("SELECT new com.ncp.moeego.article.bean.ArticleDTO(" +
//	       "a.articleNo, a.subject, a.content, a.view, a.type, a.writeDate, " +
//	       "a.memberNo, a.likes, a.elapsedTime, m.name, a.service, a.area, " +
//	       "a.commentCount, m.profileImage) " +
//	       "FROM Article a " +
//	       "JOIN Member m ON a.memberNo = m.memberNo " +
//	       "WHERE a.articleNo = :articleNo")
//	ArticleDTO findArticleWithProfileImageByArticleNo(@Param("articleNo") Long articleNo);
}
