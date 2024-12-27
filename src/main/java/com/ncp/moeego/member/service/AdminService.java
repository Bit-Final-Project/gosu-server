package com.ncp.moeego.member.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.article.bean.ArticleDTO;
import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.member.bean.ArticleImageDTO;
import com.ncp.moeego.member.bean.CancelDTO;
import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.ProDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.entity.MemberStatus;
import com.ncp.moeego.pro.entity.Pro;


public interface AdminService {

	int getRoleUserCount();

	int getRoleCancelCount();

	int getRoleProCount();

	List<MemberSummaryDTO> getPendingProMembers(Pageable pageable, MemberStatus rolePendPro);

	boolean approveMember(Long id, boolean check);

	boolean cancelMember(Long id, boolean check);

	List<Map<String, Object>> getWeekMemberData();

	List<Map<String, Object>> getProMemberJoinData(LocalDateTime startDateTime, LocalDateTime endDateTime);

	List<Map<String, Object>> getCancelledMemberData(LocalDateTime startDateTime, LocalDateTime endDateTime);

	Page<Member> getUserMembers(Pageable pageable);
	Page<ProDTO> getProMembersWithDetails(Pageable pageable);
	Page<CancelDTO> getCancelMembersWithDetails(Pageable pageable);
	
	
	void revokeMember(Long memberNo);

	boolean writeArticle(ArticleImageDTO articleImageDTO);

	List<ArticleImageDTO> getArticles();
	
	ArticleImageDTO getArticle(Long articleNo);

	boolean updateArticle(ArticleImageDTO articleImageDTO);

	boolean deleteArticle(Long articleNo, Long memberNo);




	
}
