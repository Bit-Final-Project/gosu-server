package com.ncp.moeego.ncp.config;

import org.springframework.stereotype.Component;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;

@Component
@Getter
public class NCPUtil {
	@Value("${ncp.accessKey}")
	private String accessKey;
	
	@Value("${ncp.secretKey}")
	private String secretKey;
	
	@Value("${ncp.regionName}")
	private String regionName;
	
	@Value("${ncp.endPoint}")
	private String endPoint;
	
	public NCPUtil() {
		
	}
	
}
