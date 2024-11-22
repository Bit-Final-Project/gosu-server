package category.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "main_category")
@Data
public class MainCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "main_cate_no")
	private Long mainCateNo;
	
	@Column(name = "main_cate_name", length = 200)
	private String mainCateName;
	
}
