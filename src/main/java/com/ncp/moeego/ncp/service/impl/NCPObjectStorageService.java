package com.ncp.moeego.ncp.service.impl;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ncp.moeego.ncp.config.NCPUtil;
import com.ncp.moeego.ncp.service.ObjectStorageService;

@Service
public class NCPObjectStorageService implements ObjectStorageService{
	 final AmazonS3 s3;

	 public NCPObjectStorageService(NCPUtil ncpUtil) {
	      s3 = AmazonS3ClientBuilder
	            .standard()
	            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
	            		ncpUtil.getEndPoint(), 
	            		ncpUtil.getRegionName()))
	            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
	            		ncpUtil.getAccessKey(),
	            		ncpUtil.getSecretKey()))
	            )
	            .build();  
	   }

		@Override
		public String uploadFile(String bucketName, String directoryPath, MultipartFile img) {
			try(InputStream inputStream = img.getInputStream()){			
				String imageFileName = UUID.randomUUID().toString();
				
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(img.getContentType());
				
				PutObjectRequest putObjectRequest =
						new PutObjectRequest(bucketName,
											 directoryPath + imageFileName,
											 inputStream,
											 objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
				
				// 파일 업로드
				s3.putObject(putObjectRequest);
				
				return imageFileName;
			}catch (Exception e) {
				throw new RuntimeException("파일 업로드 에러" + e);
			}
		}

		
		// 프로필 수정시 이미 프로필 있으면 삭제 후 재 업로드
		@Override
		public void deleteFile(String profileImage, String bucketName, String directoryPath) {

			s3.deleteObject(bucketName, directoryPath + profileImage);
			
		}

}
