package com.ncp.moeego.member.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;
import com.ncp.moeego.pro.entity.Pro;


public interface AdminService {

	int getRoleUserCount();

	int getRoleCancelCount();

	int getRoleProCount();

	List<MemberSummaryDTO> getPendingProMembers();

	boolean approveMember(Long id);

	boolean cancelMember(Long id);

	List<Map<String, Object>> getWeekMemberData();


	List<Map<String, Object>> getProMemberJoinData(LocalDateTime startDateTime, LocalDateTime endDateTime);

	List<Map<String, Object>> getCancelledMemberData(LocalDateTime startDateTime, LocalDateTime endDateTime);
	
}
