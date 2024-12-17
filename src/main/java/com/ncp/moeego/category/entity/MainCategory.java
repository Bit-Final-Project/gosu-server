package com.ncp.moeego.category.entity;

import jakarta.persistence.*;
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
