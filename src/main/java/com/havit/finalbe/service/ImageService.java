package com.havit.finalbe.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.havit.finalbe.entity.Image;
import com.havit.finalbe.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String havitbucket;
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;


    public Long getImageId(MultipartFile multipartFile) throws IOException {

        LocalDateTime now = LocalDateTime.now();

        String imgUrl = null;
        if (!multipartFile.isEmpty()) {
            imgUrl = uploadFile(multipartFile, "havit");
        }

        Image image = Image.builder()
                .imageUrl(imgUrl)
                .fileName(multipartFile.getOriginalFilename())
                .extension(multipartFile.getContentType())
                .savePath(now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .size(multipartFile.getSize())
                .build();
        imageRepository.save(image);

        return image.getImageId();
    }

    public String getImageUrl(Long imageId) {

        Image image = imageRepository.findImageByImageId(imageId);
        if (null == image) {
            return "이미지가 존재하지 않습니다.";
        }
        return image.getImageUrl();
    }

    public String deleteImage(Long imageId) {

        Image originFile = imageRepository.findImageByImageId(imageId);
        if (null != originFile) {
            String key = originFile.getImageUrl().substring(52);
            amazonS3Client.deleteObject(havitbucket, key);

            imageRepository.delete(originFile);
            return "이미지 삭제가 완료되었습니다.";
        }
        return "삭제할 이미지가 없습니다.";
    }


    // 이미지 업로드 및 URL 변환
    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {

//        String fileName = dirName + "/" + UUID.randomUUID() + multipartFile.getName();
        List<Image> imageList = imageRepository.findAll();
        List<Long> imageLength = new ArrayList<>();

        for (Image image : imageList) {
            imageLength.add(image.getImageId());
        }

        Long cnt = (long) imageLength.size();
        String fileName = dirName + "/" + (cnt + 1);

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(multipartFile.getSize());

        // S3 에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(havitbucket, fileName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        // URL 변환
        return amazonS3Client.getUrl(havitbucket, fileName).toString();
    }
}
