package com.ncp.moeego.article.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.article.repository.ArticleRepository;

@SpringBootTest
public class ArticleRepoTest {
	@Autowired
	private ArticleRepository articleRepository;
	
	@Test
	public void eventTest() {
		List<Article> list = articleRepository.findAllEventArticle();
		for (Article article : list) {
			System.out.println(article);
		}
	}
}
