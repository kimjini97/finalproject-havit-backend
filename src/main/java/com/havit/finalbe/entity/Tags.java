package com.havit.finalbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagsId;

    @Column(nullable = false)
    private String tagName;
}
