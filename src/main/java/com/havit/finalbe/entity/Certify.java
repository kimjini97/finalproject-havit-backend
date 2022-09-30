package com.havit.finalbe.entity;

import com.havit.finalbe.dto.CertifyDto;
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

    @Column
    private Long imageId;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Groups groups;

    @OneToMany(mappedBy = "certify", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;


    public void update(CertifyDto.Request certifyRequestDto) {
        String title = certifyRequestDto.getTitle();
        Long newImageId = certifyRequestDto.getImageId();
        String latitude = certifyRequestDto.getLatitude();
        String longitude = certifyRequestDto.getLongitude();

        if (null != title) {
            this.title = title;
        }
        if (null != newImageId) {
            this.imageId = newImageId;
        }
        if (null != latitude) {
            this.latitude = latitude;
        }
        if (null != longitude) {
            this.longitude = longitude;
        }
    }
}
