package com.ncp.moeego.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.image.bean.ImageDTO;

public interface ImageService {

	public List<ImageDTO> getAllImages();

	public List<ImageDTO> getImageListByArticleNo(Long articleNo);

	public String profileUpload(MultipartFile file, Long memberNo);

	public boolean profileDelete(Long memberNo);

	public String getUploadedUuid(Long memberNo);



}
