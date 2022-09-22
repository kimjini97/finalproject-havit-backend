package com.havit.finalbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Group extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String leaderName;

    @Column(nullable = false)
    private String crewName;

    @Column
    private String startDate;

    @Column(nullable = false)
    private String imgUrl;

    @Column
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GroupTag> groupTagList;
}
