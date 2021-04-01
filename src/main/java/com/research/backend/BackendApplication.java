package com.research.backend;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class BackendApplication {

    private AmazonS3 amazonS3;



    public static void main(String[] args) {
         String awsS3AudioBucket = "exl-research";

        Object url = awsS3AudioBucket + "s3://exl-research/pdf/01.PHPOOP.pdf";
        log.info(url);
//        s3Client.utilities().getUrl(builder -> builder.bucket(AWS_BUCKET).key(s3RelativeFilePath)).toExternalForm();


        SpringApplication.run(BackendApplication.class, args);
    }

}
