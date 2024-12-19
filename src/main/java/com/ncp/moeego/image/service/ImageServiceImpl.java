package com.ncp.moeego.image.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.image.bean.ImageDTO;
import com.ncp.moeego.image.repository.ImageRepository;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.ncp.service.ObjectStorageService;

import lombok.RequiredArgsConstructor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
	
	private final ImageRepository imageRepository;
	private final MemberRepository memberRepository;
	private final ObjectStorageService objectStorageService;
	
	private String bucketName = "moeego";
	
	@Override
	public List<ImageDTO> getAllImages() {
	    
	    List<Image> images = imageRepository.findAll();
	    
	    // 엔티티를 DTO로 변환
	    return images.stream()
	        .map(image -> new ImageDTO(
	            image.getImageNo(),
	            image.getReview() != null ? image.getReview().getReviewNo() : null,
	            image.getProItem() != null ? image.getProItem().getProItemNo() : null,
	            image.getArticle() != null ? image.getArticle().getArticleNo() : null,
	            image.getMember().getMemberNo(),
	            image.getImageName(),
	            image.getImageUuidName()
	        ))
	        .collect(Collectors.toList());
	}
	
	@Override
	public List<ImageDTO> getImageListByArticleNo(Long articleNo) {
		List<Image> images = imageRepository.findByArticle_ArticleNo(articleNo);
		return images.stream()
				.map(image -> new ImageDTO(
		            image.getImageNo(),
		            image.getReview() != null ? image.getReview().getReviewNo() : null,
		            image.getProItem() != null ? image.getProItem().getProItemNo() : null,
		            image.getArticle() != null ? image.getArticle().getArticleNo() : null,
		            image.getMember().getMemberNo(),
		            image.getImageName(),
		            image.getImageUuidName()
		        ))
		        .collect(Collectors.toList());
	}

	@Override
	public String profileUpload(MultipartFile file, Long memberNo) {
	    try {
	        // 파일이 비어있는지 확인
	        if (file == null || file.isEmpty()) {
	            throw new IllegalArgumentException("업로드된 파일이 비어 있습니다.");
	        }
	        Optional<Member> member = memberRepository.findByIdWithEmptyImage(memberNo);

	        // 회원이 존재하지 않으면 예외 처리
	        if (member.isEmpty()) {
	            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
	        }

	        // 1. 기존 프로필 이미지 있으면 삭제
	        String profileImage = member.get().getProfileImage();
	        System.out.println(profileImage);
	        
	        if(profileImage != null) {
	        	// 이미지 테이블에서 삭제
	            imageRepository.deleteByImageUuidName(profileImage);

	            // 오브젝트 스토리지에서 삭제
	            objectStorageService.deleteFile(profileImage, bucketName, "profile/");
	        }
	        
	        // 2. 오브젝트 스토리지에 업로드
	        String cloudKey = objectStorageService.uploadFile(bucketName, "profile/", file);
	        
	        System.out.println("cloudKey : " + cloudKey);

	        // 3. 업로드된 이미지 정보를 DB에 저장
	        Image image = new Image();
	        image.setImageName(file.getOriginalFilename());
	        image.setMember(member.get()); // 사용자 연결
	        image.setImageUuidName(cloudKey); // 스토리지의 키 저장
	        image.setArticle(null);
	        image.setProItem(null);
	        image.setReview(null);
	        imageRepository.save(image);

	        // 3. profile_image 컬럼만 업데이트
	        memberRepository.updateProfileImage(member.get(), cloudKey);

	        return cloudKey;  // 업로드 성공 시 true 반환
	    } catch (IllegalArgumentException e) {
	        // IllegalArgumentException이 발생하면 GlobalExceptionHandler로 처리
	        throw e;
	    } catch (Exception e) {
	        // 기타 예외가 발생하면 RuntimeException으로 감싸서 던집니다.
	        throw new RuntimeException("프로필 이미지 업로드 중 오류 발생", e);
	    }
	}

	@Override
	public boolean profileDelete(Long memberNo) {
	    try {

	        Optional<Member> memberOptional = memberRepository.findByIdWithEmptyImage(memberNo);

	        if (memberOptional.isEmpty()) {
	            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
	        }

	        Member member = memberOptional.get();
	        // 이미지 테이블에서 해당 회원의 프로필 이미지 UUID 가져오기
	        String profileImageUuid = member.getProfileImage();

	        if (profileImageUuid == null) {
	            throw new IllegalArgumentException("프로필 이미지가 존재하지 않습니다.");
	        }

	        // NCP 오브젝트 스토리지에서 프로필 이미지 삭제
	        objectStorageService.deleteFile(profileImageUuid, bucketName, "profile/");

	        // image 테이블에서 해당 프로필 이미지 삭제
	        imageRepository.deleteByImageUuidName(profileImageUuid);

	        // member 테이블에서 profile_image를 null로 업데이트
	        memberRepository.updateProfileImageToNull(memberNo);

	        return true;  // 삭제 성공 시 true 반환
	    } catch (IllegalArgumentException e) {
	        throw e;  // 잘못된 입력 처리
	    } catch (Exception e) {
	        throw new RuntimeException("프로필 삭제 중 오류 발생", e);
	    }
		
	}

	// UUID 반환
	@Override
	public String getUploadedUuid(Long memberNo) {
		Optional<Member> members = memberRepository.findByIdWithEmptyImage(memberNo);
		
		if (members.isPresent()) {
			Member member = members.get();
		    return member.getProfileImage();
		}
		
		return null;  
	}



	
	
	
}
