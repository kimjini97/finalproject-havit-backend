package com.havit.finalbe.entity;

import com.havit.finalbe.dto.request.CertifyRequestDto;
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
    private Groups groups;

    @OneToMany(mappedBy = "certify", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;

    public boolean isValidateMember(Member member) {
        return this.member.equals(member);
    }

    public void update(CertifyRequestDto certifyRequestDto, String imgUrl) {
        this.title = certifyRequestDto.getTitle();
        this.imgUrl = imgUrl;
        this.latitude = certifyRequestDto.getLatitude();
        this.longitude = certifyRequestDto.getLongitude();
    }
}
