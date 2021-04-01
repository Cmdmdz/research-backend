package com.research.backend.controller;

import com.research.backend.config.cross.CurrentUser;
import com.research.backend.dto.ReseachDto;
import com.research.backend.dto.WeightDto;
import com.research.backend.model.Research;
import com.research.backend.security.UserDetailsImpl;
import com.research.backend.services.ResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/path")
public class ResearchControl {

    private final ResearchService researchService;


    @PostMapping("/research")
    public ResponseEntity<?> addResearch(@RequestBody Research research, @CurrentUser UserDetailsImpl current) {
        return researchService.addResearch(research, current);
    }

    @PutMapping("/research/{id}")
    public ResponseEntity<?> updateResearch(@PathVariable("id") Long id, @RequestBody ReseachDto research) {
        return researchService.updateResearch(id,research);
    }
    @PutMapping(path = "/research/weight/{id}")
    public ResponseEntity<?> updateWeight(@PathVariable("id") Long id, @RequestBody WeightDto weightDto) {
        return researchService.updateWeight(id,weightDto);
    }

    @DeleteMapping("/research/{id}")
    public void  deleteResearchById(@PathVariable("id") Long id){
        researchService.deleteResearch(id);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("research")
    public ResponseEntity<?> findAllResearch(@CurrentUser UserDetailsImpl current){
        return researchService.findAllResearch(current);
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("research/{id}")
    public ResponseEntity<?> findAllById(@PathVariable("id")Long id){
        return researchService.findAllById(id);
    }


    @GetMapping("/research/all")
    public ResponseEntity<?> getAllResearch(@RequestParam(required = false) String startDate){
        return researchService.getAllResearch(startDate);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/research/all/{id}")
    public ResponseEntity<?> findAllByAccountId(@PathVariable("id") Long id){
        return researchService.findAllByAccountId(id);
    }



}
