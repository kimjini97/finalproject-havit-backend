package com.havit.finalbe.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RandomProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long randomId;

    @Column(nullable = false)
    private String savePath;

    @Column(nullable = false)
    private String fileName;

    @Column(length = 20, nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;
}
