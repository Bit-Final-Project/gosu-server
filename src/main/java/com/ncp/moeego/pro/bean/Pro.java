package com.ncp.moeego.pro.bean;

import java.util.Date;

import com.ncp.moeego.category.bean.MainCategory;
import com.ncp.moeego.category.bean.SubCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.ncp.moeego.reserve.bean.Reserve;

@Entity
@Table(name = "pro")
@Data
public class Pro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_no")
	private Long proNo;
	
	@OneToOne // 고수는 하나의 메인 카테고리를 선택할수있음
    @JoinColumn(name = "main_cate_no", nullable = false)
    private MainCategory mainCateNo;

	@OneToOne // 고수는 하나의 메인 카테고리를 선택할수있음
	@JoinColumn(name = "sub_cate_no", nullable = false)
	private SubCategory subCateNo;

	@ManyToOne // 고수는 여러개의 예약신청을 받을수도 있고 예약을 못 받을수도있음
	@JoinColumn(name = "reserve_no", nullable = false)
    private Reserve reserveNo;

	@Column(name = "deprive_date")
	private Date depriveDate;

	@Column(name = "access_date")
	private Date accessDate;

	private float star;
	
	@Column(name = "one_intro", length = 1000)
	private String oneIntro; // 한줄소개
	
	@Column(length = 3000)
	private String intro; // 서비스 소개
}
