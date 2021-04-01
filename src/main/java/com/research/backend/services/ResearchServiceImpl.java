package com.research.backend.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.research.backend.dto.ReseachDto;
import com.research.backend.dto.WeightDto;
import com.research.backend.model.Account;
import com.research.backend.model.Files;
import com.research.backend.model.Research;
import com.research.backend.repository.AccountRepo;
import com.research.backend.repository.FilesRepo;
import com.research.backend.repository.ResearchRepo;
import com.research.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ResearchServiceImpl implements ResearchService{

    @Autowired
    private  ResearchRepo researchRepo;
    @Autowired
    private  AccountRepo accountRepo;
    @Autowired
    private  FilesRepo filesRepo;
    private AmazonS3 amazonS3;
    private String awsS3AudioBucket;

    @Autowired
    public ResearchServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket) {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    @Override
    public ResponseEntity<?> addResearch(Research research, UserDetailsImpl current) {
        Optional<Account> account = accountRepo.findById(current.getId());

        research.setAccount(account.get());
        return ResponseEntity.ok(researchRepo.save(research));
    }

    @Override
    public ResponseEntity<?> updateResearch(Long id, ReseachDto research) {
        Optional<Research> research1 = researchRepo.findById(id);
        if(research1.isPresent()) {
            Research research2 = research1.get();
            research2.setResearch(research.getResearch());
            research2.setDetail(research.getDetail());
            research2.setEng(research.getEng());
            research2.setPoint(research.getPoint());
            return new  ResponseEntity<>(researchRepo.save(research2), HttpStatus.OK);

        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> updateWeight(Long id, WeightDto sharepoint) {
        Optional<Research> research1 = researchRepo.findById(id);
        if(research1.isPresent()){
            Research research2 = research1.get();
            research2.setSharePoint(sharepoint.getSharePoint());
            return new  ResponseEntity<>(researchRepo.save(research2), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteResearch(Long id) {
        Optional<Research> research = researchRepo.findById(id);
        List<Files> files1 = filesRepo.findAllByResearch(research.get());
        for (Files files: files1) {
            System.out.println();
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket+"/pdf",files.getFiles() ));
        }
        filesRepo.deleteByResearch(research.get());
        researchRepo.deleteById(id);
        ResponseEntity.ok(id);

    }

    @Override
    public ResponseEntity<?> findAllResearch(UserDetailsImpl current) {
        List<Research> research = new ArrayList<>();
        Optional<Account> account = accountRepo.findById(current.getId());
        researchRepo.findAllByAccount(account.get()).forEach(research::add);

        return ResponseEntity.ok(research);

    }

    @Override
    public ResponseEntity<?> findAllById(Long id) {
        Optional<Research> research = researchRepo.findById(id);
        return ResponseEntity.ok(research.get());
    }

    @Override
    public ResponseEntity<List<Research>> getAllResearch(String startDate) {
        try {
            List<Research> research = new ArrayList<>();
            if (startDate == null) {
                researchRepo.findAll().forEach(research::add);
            } else {
                researchRepo.findAllByStartDate(startDate).forEach(research::add);
            }
            if (research.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(research, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> findAllByAccountId(Long id) {
        List<Research> research = new ArrayList<>();
        Optional<Account> account = accountRepo.findById(id);
        researchRepo.findAllByAccount(account.get()).forEach(research::add);
        return ResponseEntity.ok(research);
    }
}
