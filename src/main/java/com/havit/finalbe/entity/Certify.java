package com.havit.finalbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Certify extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certifyId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imgUrl;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;
}
