package com.ncp.moeego.image.service;

import java.util.List;

import com.ncp.moeego.image.bean.ImageDTO;

public interface ImageService {

	public List<ImageDTO> getAllImages();

	public List<ImageDTO> getImageListByArticleNo(Long articleNo);

}
