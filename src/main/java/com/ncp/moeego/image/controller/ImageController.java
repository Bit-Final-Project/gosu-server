package com.ncp.moeego.image.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.common.ApiResponse;
import com.ncp.moeego.image.bean.ImageDTO;
import com.ncp.moeego.image.service.ImageService;
import com.ncp.moeego.member.bean.MemberDetails;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;
	private final MemberService memberService;

    @GetMapping("/image/list")
    public ResponseEntity<List<ImageDTO>> getAllImages() {

        // 서비스에서 이미지 목록 가져오기
        List<ImageDTO> images = imageService.getAllImages();

        // 이미지 목록을 바로 응답으로 반환
        return ResponseEntity.ok(images);
    }
	
	@PutMapping("/image/profileUpload")
	public ResponseEntity<ApiResponse> uploadProfileImage(@RequestPart("image") MultipartFile image, Authentication authentication) {
	    try {
	        // 업로드 파일이 비어 있는지 확인
	        if (image.isEmpty()) {
	            throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
	        }

	        // Long 타입 memberNo 가져오기
	        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
	        String email = memberDetails.getUsername(); 
	        Member member = memberService.getMemberByEmail(email);
	        
	        log.info("사용자 보고싶어 : {}", member);
	        
	        Long memberNo = member.getMemberNo();
	        	        
	        String uploadedUuid = imageService.profileUpload(image, memberNo);

	        if (uploadedUuid != null) {
	        	return ResponseEntity.ok(ApiResponse.success("반환 성공", uploadedUuid));
	        } else {
	        	return ResponseEntity.badRequest().body(ApiResponse.error("반환 실패", HttpStatus.BAD_REQUEST.name()));
	        }
	        // 모든 예외 GlobalExceptionHandler로
	    } catch (Exception e) {
	        throw e;
	    }
	}
	
	@DeleteMapping("/image/profileDelete")
	public ResponseEntity<String> deleteProfileImage(Authentication authentication) {
	    try {
	    	MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
	        String email = memberDetails.getName(); // Long 타입 memberNo 가져오기
	        Member member = memberService.getMemberByEmail(email);
	        
	        Long memberNo = member.getMemberNo();
	    	
	        boolean result = imageService.profileDelete(memberNo);
	        if (result) {
	            return ResponseEntity.ok("게시글이 성공적으로 작성되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("게시글 작성 중 오류가 발생했습니다.");
	        }
	        // 모든 예외 GlobalExceptionHandler로
	    } catch (Exception e) {
	        throw e;
	    }
	}
}
