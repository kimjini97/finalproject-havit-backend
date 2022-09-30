package com.havit.finalbe.entity;

import lombok.*;


import javax.persistence.*;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String savePath;

    @Column(nullable = false)
    private String fileName;

    @Column(length = 20, nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;
}
