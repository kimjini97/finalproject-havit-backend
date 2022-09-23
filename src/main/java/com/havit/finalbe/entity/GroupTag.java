package com.havit.finalbe.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springdoc.core.SpringDocConfigProperties;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GroupTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupTagId;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Groups groups;

}
