package com.research.backend.services;

import com.research.backend.model.Files;
import com.research.backend.model.Research;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public ResponseEntity<?> uploadChapter1(Long id, int chapter,MultipartFile multipartFile, boolean enablePublicReadAccess) throws IOException;
    public String getResourceUrl(String fileName);
    public byte[] downloadFile(final String keyName);
    public ResponseEntity<?> findFilesAllByResearch(Long id);
    public ResponseEntity<?> findFileByResearch(Long id);
    public ResponseEntity<?> doGetFileByResearch(Long id);
    public ResponseEntity<?> findById(Long research , int id);
    public void deleteFile(Long id, String fileName);
    public ResponseEntity<?> deletedPoint(Long id);
}
