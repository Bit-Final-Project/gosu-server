package com.ncp.moeego.article.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncp.moeego.article.bean.ArticleDTO;
import com.ncp.moeego.article.entity.Article;
import com.ncp.moeego.article.repository.ArticleRepository;
import com.ncp.moeego.comment.repository.CommentRepository;
import com.ncp.moeego.common.ConvertDate;
import com.ncp.moeego.image.entity.Image;
import com.ncp.moeego.image.repository.ImageRepository;
import com.ncp.moeego.member.entity.Member;
import com.ncp.moeego.member.repository.MemberRepository;
import com.ncp.moeego.ncp.service.ObjectStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;


    private final ImageRepository imageRepository;

    // 네이버 클라우드
    private final ObjectStorageService objectStorageService;

    private String bucketName = "moeego";


    // memberNo맞춰서 이름 가져오는 로직
    public String getMemberNameByMemberNo(Long memberNo) {
        Optional<Member> member = memberRepository.findById(memberNo); // memberNo로 Member 조회

        return member.map(Member::getName).orElse("Unknown"); // Member가 없으면 "Unknown" 반환
    }

    @Override
    public List<Article> getArticleList(int type) {
        return articleRepository.findAllByType(type);
    }

    @Override
    public void write(Article article) {
        articleRepository.save(article);

    }

    @Override
    public Article getEventList(int num) {
        return articleRepository.findByArticleNo(num);
    }

    @Override
    public void update(Article article) {
        articleRepository.save(article);

    }

    @Override
    public void deleteByArticleNo(int num) {
        articleRepository.deleteById((long) num);
    }

    @Override
    public List<Article> searchArticles(String subject, String content) {
        return articleRepository.searchArticles(subject, content);
    }

    @Override
    public List<Article> searchSubjectArticles(String subject) {
        return articleRepository.searchSubjectArticles(subject);
    }

    @Override
    public List<Article> searchContentArticles(String content) {
        return articleRepository.searchContentArticles(content);
    }

    // 좋아요 순으로 조회(인기 게시글)
    @Override
    public Page<ArticleDTO> getHotArticleByPage(int pg, int pageSize) {

        // Pageable 객체 생성: likes 기준 내림차순 정렬
        Pageable pageable = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("likes")));

        // 단일 쿼리로 게시글과 댓글 수를 함께 조회
        Page<Object[]> articlePage = articleRepository.findHotArticlesWithCommentCount(pageable);

        // Article 객체를 ArticleDTO로 변환하여 반환
        return articlePage.map(result -> {
            Article article = (Article) result[0];
            Long commentCount = (Long) result[1];

            String elapsedTime = ConvertDate.calculateDate(article.getWriteDate()); // 경과 시간 계산
            String memberName = getMemberNameByMemberNo(article.getMember().getMemberNo()); // 회원 이름 가져오기

            return new ArticleDTO(
                    article.getArticleNo(),
                    article.getSubject(),
                    article.getContent(),
                    article.getView(),
                    article.getType(),
                    article.getWriteDate(),
                    article.getMember().getMemberNo(),
                    article.getLikes(),
                    elapsedTime,
                    memberName,
                    article.getService(),
                    article.getArea(),
                    commentCount.intValue()
            );
        });
    }


    // 전체 게시글 조회 페이징 성능 개선 후
    @Override
    public Page<ArticleDTO> getArticleListByPage(int pg, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

        // 성능 개선을 위한 단일 쿼리로 댓글 수 가져오기
        Page<Object[]> articlePage = articleRepository.findArticlesWithCommentCount(pageRequest);

        return articlePage.map(result -> {
            Article article = (Article) result[0];
            Long commentCount = (Long) result[1];

            String elapsedTime = ConvertDate.calculateDate(article.getWriteDate());
            String memberName = getMemberNameByMemberNo(article.getMember().getMemberNo());

            return new ArticleDTO(
                    article.getArticleNo(),
                    article.getSubject(),
                    article.getContent(),
                    article.getView(),
                    article.getType(),
                    article.getWriteDate(),
                    article.getMember().getMemberNo(),
                    article.getLikes(),
                    elapsedTime,
                    memberName,
                    article.getService(),
                    article.getArea(),
                    commentCount.intValue());
        });
    }


//    // 전체 게시글 조회 페이징 성능 개선 전
//    @Override
//    public Page<ArticleDTO> getArticleListByPage(int pg, int pageSize) {
//        PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));
//
//        Page<Article> articlePage = articleRepository.findAll(pageRequest);
//        
//        // Article 객체를 ArticleDTO로 변환하여 Page<ArticleDTO>로 반환
//        return articlePage.map(article -> {
//            String elapsedTime = Date.calculateDate(article.getWriteDate());
//            String memberName = getMemberNameByMemberNo(article.getMemberNo().getMemberNo());
//            int commentCount = commentRepository.countByArticleNo(article.getArticleNo());
//            return new ArticleDTO(
//            		article.getArticleNo(), 
//            		article.getSubject(), 
//            		article.getContent(), 
//            		article.getView(),
//                    article.getType(), 
//                    article.getWriteDate(), 
//                    article.getMemberNo().getMemberNo(), 
//                    article.getLikes(),
//                    elapsedTime, 
//                    memberName,
//                    article.getService(),
//                    article.getArea(),
//                    commentCount);
//        });
//    }


    // Type 별 게시판 조회
    @Override
    public Page<ArticleDTO> getTypeArticles(int pg, int pageSize, int type) {
        PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

        // 성능 개선을 위한 단일 쿼리로 댓글 수 가져오기
        Page<Object[]> articlePage = articleRepository.findTypeArticlesWithCommentCount(type, pageRequest);

        return articlePage.map(result -> {
            Article article = (Article) result[0];
            Long commentCount = (Long) result[1];

            String elapsedTime = ConvertDate.calculateDate(article.getWriteDate());
            String memberName = getMemberNameByMemberNo(article.getMember().getMemberNo());

            return new ArticleDTO(
                    article.getArticleNo(),
                    article.getSubject(),
                    article.getContent(),
                    article.getView(),
                    article.getType(),

                    article.getWriteDate(),
                    article.getMember().getMemberNo(),
                    article.getLikes(),
                    elapsedTime,
                    memberName,
                    article.getService(),
                    article.getArea(),
                    commentCount.intValue()
            );
        });
    }

    // 게시글 상세 조회
    @Override
    public ArticleDTO getArticleViewById(Long articleNo) {
        // 게시글을 조회할 repository 호출 (예시: ArticleRepository)
        Article article = articleRepository.findById(articleNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 번호입니다."));

        // 조회된 Article 엔티티를 ArticleDTO로 변환하여 반환
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleNo(article.getArticleNo());
        articleDTO.setMemberNo(article.getMember().getMemberNo());
        articleDTO.setSubject(article.getSubject());
        articleDTO.setContent(article.getContent());
        articleDTO.setView(article.getView());
        articleDTO.setType(article.getType());
        articleDTO.setWriteDate(article.getWriteDate());
        articleDTO.setLikes(article.getLikes());

        String elapsedTime = ConvertDate.calculateDate(article.getWriteDate());
        String memberName = getMemberNameByMemberNo(article.getMember().getMemberNo());

        // 시간을 포맷하여 변환
        articleDTO.setElapsedTime(elapsedTime);

        // 작성자 이름, 서비스, 지역 등의 추가 정보를 설정
        articleDTO.setMemberName(memberName);
        articleDTO.setService(article.getService());
        articleDTO.setArea(article.getArea());

        // 댓글 수 설정 (쿼리에서 가져온 값 사용)
        Long commentCount = commentRepository.countNonDeletedCommentsByArticleNo(articleNo);
        articleDTO.setCommentCount(commentCount.intValue());

        return articleDTO;

    }


    // 마이페이지 작성한 게시글 조회
    @Override
    public Page<ArticleDTO> getMyArticles(Long member_no, int pg, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pg - 1, pageSize, Sort.by(Sort.Order.desc("writeDate")));

        // 성능 개선을 위한 단일 쿼리로 댓글 수 가져오기
        Page<Object[]> articlePage = articleRepository.findMyArticlesWithCommentCount(member_no, pageRequest);

        return articlePage.map(result -> {
            Article article = (Article) result[0];
            Long commentCount = (Long) result[1];

            String elapsedTime = ConvertDate.calculateDate(article.getWriteDate());
            String memberName = getMemberNameByMemberNo(article.getMember().getMemberNo());

            return new ArticleDTO(
                    article.getArticleNo(),
                    article.getSubject(),
                    article.getContent(),
                    article.getView(),
                    article.getType(),
                    article.getWriteDate(),
                    article.getMember().getMemberNo(),
                    article.getLikes(),
                    elapsedTime,
                    memberName,
                    article.getService(),
                    article.getArea(),
                    commentCount.intValue()
            );
        });
    }

    // NCP 이미지 추가 해서 게시글 등록
    @Override
    public boolean writeArticle(ArticleDTO articleDTO) {
        try {
            // ArticleDTO → Article 변환
            Article article = new Article();
            article.setSubject(articleDTO.getSubject());
            article.setContent(articleDTO.getContent());
            article.setView(0); // 초기 조회수는 0
            article.setLikes(0); // 초기 좋아요는 0
            article.setWriteDate(LocalDateTime.now());
            article.setType(articleDTO.getType());
            article.setService(articleDTO.getService());
            article.setArea(articleDTO.getArea());

            // Member 조회 및 설정
            Optional<Member> member = memberRepository.findById(articleDTO.getMemberNo());
            if (member.isPresent()) {
                article.setMember(member.get());
            } else {
                return false; // Member가 없으면 실패 처리
            }

            // Article 저장
            Article savedArticle = articleRepository.save(article);

            // 이미지 업로드 및 저장 (이미지가 있을 경우에만 처리)
            if (articleDTO.getImageFiles() != null && !articleDTO.getImageFiles().isEmpty()) {
                for (MultipartFile imageFile : articleDTO.getImageFiles()) {
                    // 1. 오브젝트 스토리지에 업로드
                    String cloudKey = objectStorageService.uploadFile(bucketName, "storage/", imageFile);

                    // 2. 업로드된 이미지 정보를 DB에 저장
                    Image image = new Image();
                    image.setArticle(savedArticle); // 게시글과 연결
                    image.setMember(member.get()); // 작성자와 연결
                    image.setImageName(imageFile.getOriginalFilename());
                    image.setImageUuidName(cloudKey); // 스토리지의 키 저장
                    imageRepository.save(image);
                }
            }

            return true; // 성공
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 실패 시
        }
    }


    // 게시글 수정
    @Override
    public boolean updateArticle(Long articleNo, ArticleDTO articleDTO) {
        try {
            Optional<Article> optionalArticle = articleRepository.findById(articleNo);
            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();

                // 게시글 데이터 업데이트
                article.setSubject(articleDTO.getSubject());
                article.setContent(articleDTO.getContent());
                article.setType(articleDTO.getType());
                article.setService(articleDTO.getService());
                article.setArea(articleDTO.getArea());

                // 삭제할 이미지 처리
                if (articleDTO.getRemovedImageIds() != null && !articleDTO.getRemovedImageIds().isEmpty()) {
                    List<Image> imagesToDelete = imageRepository.findByImageUuidNameIn(articleDTO.getRemovedImageIds());
                    for (Image image : imagesToDelete) {
                        objectStorageService.deleteFile(image.getImageUuidName(), bucketName, "storage/");
                        imageRepository.delete(image);
                    }
                }

                // 새 이미지 업로드 및 저장
                if (articleDTO.getImageFiles() != null && !articleDTO.getImageFiles().isEmpty()) {
                    for (MultipartFile imageFile : articleDTO.getImageFiles()) {
                        String cloudKey = objectStorageService.uploadFile(bucketName, "storage/", imageFile);

                        Image newImage = new Image();
                        newImage.setArticle(article);
                        newImage.setMember(article.getMember());
                        newImage.setImageName(imageFile.getOriginalFilename());
                        newImage.setImageUuidName(cloudKey);
                        imageRepository.save(newImage);
                    }
                }

                // 게시글 저장
                articleRepository.save(article);
                return true;
            } else {
                return false; // 게시글이 존재하지 않음
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 게시글 삭제
    @Override
    public boolean deleteArticle(Long articleNo) {
        try {
            // 게시글 가져오기
            Optional<Article> optionalArticle = articleRepository.findById(articleNo);

            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();

                // 게시글과 연결된 이미지 리스트 가져오기
                List<Image> images = imageRepository.findByArticle(article);

                // 이미지 삭제
                if (images != null && !images.isEmpty()) {
                    for (Image image : images) {
                        // NCP 오브젝트 스토리지에서 파일 삭제
                        objectStorageService.deleteFile(image.getImageUuidName(), bucketName, "storage/");
                        
                        // 이미지 엔티티 삭제
                        imageRepository.delete(image);
                    }
                }

                // 게시글 삭제
                articleRepository.deleteById(articleNo);
                return true;
            } else {
                // 게시글이 없는 경우
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
