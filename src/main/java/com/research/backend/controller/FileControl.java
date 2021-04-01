package com.research.backend.controller;


import com.research.backend.services.FileService;
import com.research.backend.services.UrlDemo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/path")
public class FileControl {

    private final FileService fileService;
    private final UrlDemo urlDemo;


    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("id") Long id ,@RequestParam int chapter,@RequestParam(value = "file") MultipartFile file, boolean enablePublicReadAccess, RedirectAttributes redirectAttributes) throws IOException {
        return ResponseEntity.ok(fileService.uploadChapter1(id, chapter,file, false));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping(value= "/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(value= "fileName") final String keyName) {
        final byte[] data = fileService.downloadFile(keyName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + keyName + "\"")
                .body(resource);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/file/{id}")
    public ResponseEntity<?> findAllByResearch(@PathVariable("id") Long id){
        return fileService.findFilesAllByResearch(id);
    }

    @GetMapping("/url/{filename}")
    public String getUrlPresice(@PathVariable("filename") String fileName) {
        return fileService.getResourceUrl(fileName);
    }


    @GetMapping("/file/pr/{id}")
    public ResponseEntity<?> findFileByResearch(@PathVariable("id") Long id){
        return fileService.findFileByResearch(id);
    }

    @GetMapping("/file/pdf/{id}")
    public ResponseEntity<?> doGetFileByResearch(@PathVariable("id") Long id){
        return fileService.doGetFileByResearch(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/file/id/{id}/{chapter}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id, @PathVariable("chapter") int chapter){
        return fileService.findById(id, chapter);
    }

    @DeleteMapping("/file/delete/{id}/{fileName}")
    public void deleteFile(@PathVariable("id")Long id, @PathVariable("fileName") String fileName){
        fileService.deleteFile(id, fileName);
    }

}
