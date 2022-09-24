package com.havit.finalbe.entity;

import com.havit.finalbe.dto.request.GroupRequestDto;
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
@Table(name="groups")
public class Groups extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String leaderName;

//    @JoinColumn(name = "leader_member_id", nullable = false)
//    @OneToMany
//    private List<Member> leaderMember;

    @Column(nullable = false)
    private String crewName;

//    @JoinColumn(name = "crew_member_id", nullable = false)
//    @OneToMany
//    private List<Member> crewMember;

    @Column
    private String startDate;

    @Column(nullable = false)
    private String imgUrl;

    @Column
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GroupTag> groupTagList;

    // 참여자 리스트 추가

    public boolean isValidateMember(Member member) {
        return !this.member.equals(member);
    }

    public void update(GroupRequestDto groupRequestDto, String imgUrl) {
        this.title = groupRequestDto.getTitle();
        this.startDate = groupRequestDto.getStartDate();
        this.imgUrl = imgUrl;
        this.content = groupRequestDto.getContent();
        this.leaderName = groupRequestDto.getLeaderName();
        this.crewName = groupRequestDto.getCrewName();
    }
}
