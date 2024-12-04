package com.ncp.moeego.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Refresh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    @Column(length = 3000)
    private String refresh;
    private String expiration;

    @Builder
    public Refresh(String email, String name, String refresh, String expiration) {
        this.email = email;
        this.name = name;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
