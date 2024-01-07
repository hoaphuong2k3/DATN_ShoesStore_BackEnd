package com.example.shoestore.infrastructure.utils;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;

@Component

public class QRCodeUtils {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    public String uploadQRCodeToS3(String productId) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        QRCode.from("qr_code_"+productId).withSize(300, 300)
                .to(ImageType.PNG).writeTo(outputStream);

        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        String s3Key = "qr_code_" + productId + ".png";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType("image/png")
                .build();

        s3.putObject(putObjectRequest, RequestBody.fromBytes(outputStream.toByteArray()));

        s3.putObjectAcl(PutObjectAclRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build());

        return s3Key;
    }

}
