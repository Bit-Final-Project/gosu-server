package com.ncp.moeego.member.service;

import java.util.List;

import com.ncp.moeego.member.bean.MemberSummaryDTO;
import com.ncp.moeego.member.bean.oauth2.MemberDTO;


public interface AdminService {

	int getRoleUserCount();

	int getRoleCancelCount();

	int getRoleProCount();

	List<MemberSummaryDTO> getPendingProMembers();
	
	
}
