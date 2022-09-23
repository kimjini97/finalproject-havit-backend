package com.havit.finalbe.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springdoc.core.SpringDocConfigProperties;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class GroupTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupTagId;

    @JoinColumn(name = "group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Groups groups;

    @JoinColumn(name = "tags_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Tags tags;

    public void updateTags(Tags tags) {
        this.tags = tags;
    }

}
