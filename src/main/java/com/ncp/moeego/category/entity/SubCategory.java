package com.ncp.moeego.category.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sub_category")
@Data
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_cate_no")
    private Long subCateNo;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "main_cate_no", nullable = false)
    private MainCategory mainCategory;

    @Column(name = "sub_cate_name", nullable = false, length = 100)
    private String subCateName;

}