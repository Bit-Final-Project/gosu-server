package com.ncp.moeego.image.bean;

import lombok.Data;

@Data
public class ImageUuidResponse {
	
	private String imageUuid;
	
	public ImageUuidResponse() {
		
	}
	
	public ImageUuidResponse(String imageUuid) {
		this.imageUuid = imageUuid;
	}
}
