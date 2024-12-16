package com.ncp.moeego.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.image.bean.ImageDTO;

public interface ImageService {

	public List<ImageDTO> getAllImages();

	public List<ImageDTO> getImageListByArticleNo(Long articleNo);

	public boolean profileUpload(MultipartFile file, Long memberNo);

}
