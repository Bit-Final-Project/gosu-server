package com.ncp.moeego.article.service;

import java.util.List;

import com.ncp.moeego.article.bean.Article;
import com.ncp.moeego.article.bean.ArticleDTO;
import org.springframework.data.domain.Page;

public interface ArticleService {

	public List<Article> getArticleList(int type);

	
	public void write(Article article);

	public Article getEventList(int num);

	
	public void update(Article article);
	public void deleteByArticleNo(int num);

	
	public List<Article> searchArticles(String subject, String Content);
	public List<Article> searchSubjectArticles(String keyword);
	public List<Article> searchContentArticles(String keyword);
		
	// 인기 게시글
	public Page<ArticleDTO> getHotArticleByPage(int pg, int pageSize);
	// 전체 게시글 조회 페이징
	public Page<ArticleDTO> getArticleListByPage(int pg, int pageSize);

	// 게시글 상세 조회
	public ArticleDTO getArticleViewById(Long articleNo);

	// Type 별 게시판 조회
	public Page<ArticleDTO> getTypeArticles(int pg, int pageSize, int type);


	public Page<ArticleDTO> getMyArticles(Long member_no, int pg, int pageSize);

	// 게시글 작성
	public boolean writeArticle(ArticleDTO articleDTO);

	// 게시글 수정
	public boolean updateArticle(Long articleNo, ArticleDTO articleDTO);

	// 게시글 삭제
	public boolean deleteArticle(Long articleNo);





}