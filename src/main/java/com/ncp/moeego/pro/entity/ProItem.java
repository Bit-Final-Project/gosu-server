package com.ncp.moeego.pro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncp.moeego.category.entity.SubCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "pro_item")
@Data
public class ProItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_item_no")
    private Long proItemNo;

    @ManyToOne
    @JoinColumn(name = "pro_no")
    @JsonIgnore
    @ToString.Exclude
    private Pro pro;

    @ManyToOne
    @JoinColumn(name = "sub_cate_no")
    private SubCategory subCategory;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "price")
    private Long price;

    @Column(name = "star")
    private float star;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;
}
