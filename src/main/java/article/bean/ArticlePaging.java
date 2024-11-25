package article.bean;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ArticlePaging {
	private int currentPage; //현재페이지
	private int pageBlock; //[이전][1][2][3][4][5][다음]
	private int pageSize; //1페이지당 10개씩
	private int totalA; //총글수
	private StringBuffer pagingHTML;
	
	public void makePagingHTML() {
	    pagingHTML = new StringBuffer();

	    int totalP = (totalA + pageSize - 1) / pageSize; // 총 페이지 수

	    // currentPage 검증
	    if (currentPage < 1) currentPage = 1;
	    if (currentPage > totalP) currentPage = totalP;

	    int startPage = (currentPage - 1) / pageBlock * pageBlock + 1;
	    int endPage = startPage + pageBlock - 1;

	    if (endPage > totalP) endPage = totalP;

	    // 이전 페이지 링크
	    if (startPage != 1)
	        pagingHTML.append("<span id='paging' onclick='articlePaging(" + (startPage - 1) + ")'>이전</span>");

	    // 페이지 번호들
	    for (int i = startPage; i <= endPage; i++) {
	        if (i == currentPage)
	            pagingHTML.append("<span id='currentPaging' onclick='articlePaging(" + i + ")'>" + i + "</span>");
	        else
	            pagingHTML.append("<span id='paging' onclick='articlePaging(" + i + ")'>" + i + "</span>");
	    }

	    // 다음 페이지 링크
	    if (endPage < totalP)
	        pagingHTML.append("<span id='paging' onclick='articlePaging(" + (endPage + 1) + ")'>다음</span>");

	    System.out.println(pagingHTML.toString());
	}
	
	
}
