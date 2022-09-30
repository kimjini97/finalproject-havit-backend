package com.havit.finalbe.repository;

import com.havit.finalbe.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageByImageId(Long imageId);
}
