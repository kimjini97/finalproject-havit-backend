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
    private Long imageId;

    @Column
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GroupTag> groupTagList;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Certify> certifyList;

    public void update(GroupRequestDto groupRequestDto) {
        String title = groupRequestDto.getTitle();
        String startDate = groupRequestDto.getStartDate();
        Long newImageId = groupRequestDto.getImageId();
        String content = groupRequestDto.getContent();
        String leaderName = groupRequestDto.getLeaderName();
        String crewName = groupRequestDto.getCrewName();

        if (null != title) {
            this.title = title;
        }
        if (null != startDate) {
            this.startDate = startDate;
        }
        if (null != newImageId) {
            this.imageId = newImageId;
        }
        if (null != content) {
            this.content = content;
        }
        if (null != leaderName) {
            this.leaderName = leaderName;
        }
        if (null != crewName) {
            this.crewName = crewName;
        }
    }

}
