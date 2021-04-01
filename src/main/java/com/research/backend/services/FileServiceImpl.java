package com.research.backend.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.research.backend.model.Files;
import com.research.backend.model.Research;
import com.research.backend.repository.FilesRepo;
import com.research.backend.repository.ResearchRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.research.backend.config.ConfigTypeFile.isPdf;

@Service
@Log4j2
public class FileServiceImpl implements FileService {

    @Autowired
    private FilesRepo filesRepo;
    @Autowired
    private ResearchRepo researchRepo;
    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;

    @Autowired
    public FileServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket) {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    @Override
    public String getResourceUrl(String fileName) {
        return amazonS3.getUrl(awsS3AudioBucket, "pdf/" + fileName).toString();
    }


    @Override
    public ResponseEntity<?> uploadChapter1(Long id,int chapter, MultipartFile multipartFile, boolean enablePublicReadAccess) throws IOException {


        String fileName = multipartFile.getOriginalFilename();
        File file = new File(String.valueOf(fileName));
        log.info("" + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();

        if (isPdf(fileName)) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket + "/pdf", fileName, file);
            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            this.amazonS3.putObject(putObjectRequest);
        }

        file.delete();
        Optional<Research> research1 = researchRepo.findById(id);

        Optional<Research> research = researchRepo.findById(id);
        Files files = new Files();
        Research research2 = research.get();

        research2.setPoint(20);
        files.setResearch(research.get());
        files.setFiles(fileName);
        files.setChapter(chapter);
        filesRepo.save(files);
        String getUrl =  amazonS3.getUrl(awsS3AudioBucket, "pdf/" + fileName).toString();
        log.info("M: ",amazonS3.getUrl(awsS3AudioBucket, "pdf/" + fileName) );

        return ResponseEntity.ok(getUrl);

    }


    @Override
    public byte[] downloadFile(String keyName) {
        byte[] content = null;
        log.info("Downloading an object with key= " + keyName);

        final S3Object s3Object = amazonS3.getObject(this.awsS3AudioBucket + "/pdf", keyName);
        log.info(s3Object);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            log.info("File downloaded successfully.");
            s3Object.close();
        } catch (final IOException ex) {
            log.info("IO Error Message= " + ex.getMessage());
        }

        return content;
    }

    @Override
    public ResponseEntity<?> findFilesAllByResearch(Long id) {
        Optional<Research> research = researchRepo.findById(id);
        List<Files> files1 = new ArrayList<>();
        filesRepo.findAllByResearch(research.get()).forEach(files1::add);
        return ResponseEntity.ok(files1);
    }

    @Override
    public ResponseEntity<?> findFileByResearch(Long id) {
        Optional<Research> research = researchRepo.findById(id);
        List<Files> files1 = new ArrayList<>();
        filesRepo.findAllByResearch(research.get()).forEach(files1::add);
        int progressbar = 0;
        Research research2 = research.get();
        research2.setPoint(0);
        for (int i =0; i < files1.toArray().length; i++){
            if(i == 0){
                research2.setPoint(10);
            }
            if(i == 1){
                research2.setPoint(20);
            }
            if(i ==2){
                research2.setPoint(30);
            }
            if(i ==3){
                research2.setPoint(40);
            }
            if(i ==4){
                research2.setPoint(50);
            }
            if(i == 5){
                research2.setPoint(75);
            }
            if(i == 6){
                research2.setPoint(100);
            }
        }
        return ResponseEntity.ok(researchRepo.save(research2));
    }

    @Override
    public ResponseEntity<?> deletedPoint(Long id) {
        Optional<Research> research = researchRepo.findById(id);
        List<Files> files1 = new ArrayList<>();
        filesRepo.findAllByResearch(research.get()).forEach(files1::add);
        int progressbar = 0;
        Research research2 = research.get();

        for (int i =0; i < files1.toArray().length; i++){
            if(i == 0){
                research2.setPoint(0);

            }
            if(i == 1){
                research2.setPoint(10);

            }
            if(i ==2){

                research2.setPoint(20);

            }
            if(i ==3){

                research2.setPoint(30);

            }
            if(i ==4){

                research2.setPoint(40);

            }
            if(i ==5){

                research2.setPoint(50);

            }
            if(i ==6){

                research2.setPoint(75);

            }
        }
        return ResponseEntity.ok(researchRepo.save(research2));
    }

    @Override
    public ResponseEntity<?> doGetFileByResearch(Long id) {
        Optional<Research> research = researchRepo.findById(id);
        List<Files> files1 = new ArrayList<>();
        filesRepo.findAllByResearch(research.get()).forEach(files1::add);

        return ResponseEntity.ok(files1);
    }

    @Override
    public ResponseEntity<?> findById(Long id, int chapter) {
        Optional<Research> research = researchRepo.findById(id);
        List<Files> files = new ArrayList<Files>();
//        filesRepo.fetchData(research.get(),chapter);
        filesRepo.fetchData(research.get(),chapter).forEach(files::add);
        return ResponseEntity.ok(files);
    }

    @Override
    public void deleteFile(Long id, String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket+"/pdf", fileName));
        filesRepo.deleteById(id);
    }


}
