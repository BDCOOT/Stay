package stay.app.app.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    private String dir = "/image";
    private String defaultUrl = "https://ybbucket14";


    public void deleteFile(String fileName) throws IOException {
        try {
            int startIndex = fileName.indexOf("image/");
            String fileKey = fileName.substring(startIndex);
            amazonS3.deleteObject(bucketName, fileKey);
        } catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String bucketDir = bucketName + dir;
        String dirUrl = defaultUrl + dir + "/";
        String fileName = generateFileName(file);

        amazonS3.putObject(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file));
        return dirUrl + fileName;

    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }
}