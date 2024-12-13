package com.ncp.moeego.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ncp.moeego.image.bean.ImageDTO;
import com.ncp.moeego.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ImageController {

	
	private final ImageService imageService;
	
	
	@GetMapping("/image/list")
	public ResponseEntity<List<ImageDTO>> getAllImages() {
        // 서비스에서 이미지 목록 가져오기
        List<ImageDTO> images = imageService.getAllImages();

        // 이미지 목록을 바로 응답으로 반환
        return ResponseEntity.ok(images);
    }
}
