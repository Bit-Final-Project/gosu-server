package com.ncp.moeego.member.bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.article.entity.Article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ArticleImageDTO {
//
//    private Long articleNo;   // 게시글 번호
//    private String subject;   // 제목
//    private String content;   // 내용
//    private Long memberNo;    // 작성자 번호
//    private int type;         // 게시글 타입 (예: 공지, 이벤트 등)
//    private int view;         // 조회수
//    private int likes;        // 좋아요 수
//    private LocalDateTime writeDate;  // 작성 일자
//    
//    // 이미지 관련 필드
//    private List<String> imageUuidNames;     // 스토리지에 저장된 이미지 UUID 리스트 [uuid1,uuid2]
//    private List<String> removeImageUuidNames;	 // 삭제할 이미지의 UUID 리스트
//    private List<MultipartFile> imageFiles; 
//
//    // 생성자 추가 (JPQL에서 사용하는 결과 매핑에 맞게)
//    public ArticleImageDTO(Long articleNo, String subject, String content, Long memberNo, 
//                           int type, LocalDateTime writeDate, String imageUuidName) {
//        this.articleNo = articleNo;
//        this.subject = subject;
//        this.content = content;
//        this.memberNo = memberNo;
//        this.type = type;
//        this.writeDate = writeDate;
//        this.imageUuidNames = List.of(imageUuidName);  // 단일 이미지만 반환되므로 List로 변환
//    }
//    
//    public ArticleImageDTO(Long articleNo, String subject, String content, Long memberNo, 
//            int type, LocalDateTime writeDate, List<String> imageUuidNames) {
//		this.articleNo = articleNo;
//		this.subject = subject;
//		this.content = content;
//		this.memberNo = memberNo;
//		this.type = type;
//		this.writeDate = writeDate;
//		this.imageUuidNames = imageUuidNames;  // 여러 이미지 UUID 리스트를 받아 List로 처리
//	}
//    
//    public ArticleImageDTO(Article article, List<String> imageUuidNames) {
//		this.articleNo = article.getArticleNo();
//		this.subject = article.getSubject();
//		this.content = article.getContent();
//		this.memberNo = article.getMember().getMemberNo();
//		this.type = article.getType();
//		this.writeDate = article.getWriteDate();
//		this.imageUuidNames = imageUuidNames;  // 여러 이미지 UUID 리스트를 받아 List로 처리
//	}
//    
//}


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleImageDTO {

    private Long articleNo;   // 게시글 번호
    private String subject;   // 제목
    private String content;   // 내용
    private Long memberNo;    // 작성자 번호
    private int type;         // 게시글 타입 (예: 공지, 이벤트 등)
    private int view;         // 조회수
    private int likes;        // 좋아요 수
    private LocalDateTime writeDate;  // 작성 일자
    
    // 이미지 관련 필드
    private List<String> imageUuidNames = new ArrayList<>();     // 스토리지에 저장된 이미지 UUID 리스트 [uuid1,uuid2]
    private List<String> removeImageUuidNames = new ArrayList<>(); // 삭제할 이미지의 UUID 리스트
    private List<MultipartFile> imageFiles = new ArrayList<>(); // 추가된 이미지 파일들

    // 생성자 추가 (JPQL에서 사용하는 결과 매핑에 맞게)
    public ArticleImageDTO(Long articleNo, String subject, String content, Long memberNo, 
                           int type, LocalDateTime writeDate, String imageUuidName) {
        this.articleNo = articleNo;
        this.subject = subject;
        this.content = content;
        this.memberNo = memberNo;
        this.type = type;
        this.writeDate = writeDate;
        this.imageUuidNames = List.of(imageUuidName);  // 단일 이미지만 반환되므로 List로 변환
    }
    
    public ArticleImageDTO(Long articleNo, String subject, String content, Long memberNo, 
            int type, LocalDateTime writeDate, List<String> imageUuidNames) {
        this.articleNo = articleNo;
        this.subject = subject;
        this.content = content;
        this.memberNo = memberNo;
        this.type = type;
        this.writeDate = writeDate;
        this.imageUuidNames = imageUuidNames;  // 여러 이미지 UUID 리스트를 받아 List로 처리
    }

    public ArticleImageDTO(Article article, List<String> imageUuidNames) {
        this.articleNo = article.getArticleNo();
        this.subject = article.getSubject();
        this.content = article.getContent();
        this.memberNo = article.getMember().getMemberNo();
        this.type = article.getType();
        this.writeDate = article.getWriteDate();
        this.imageUuidNames = imageUuidNames;  // 여러 이미지 UUID 리스트를 받아 List로 처리
    }
}


