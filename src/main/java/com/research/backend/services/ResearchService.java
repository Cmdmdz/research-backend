package com.research.backend.services;

import com.research.backend.dto.ReseachDto;
import com.research.backend.dto.WeightDto;
import com.research.backend.model.Research;
import com.research.backend.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;

public interface ResearchService {
    public ResponseEntity<?> addResearch(Research research, UserDetailsImpl current);
    public ResponseEntity<?> updateResearch(Long id, ReseachDto research);
    public void deleteResearch(Long id);
    public ResponseEntity<?> findAllResearch(UserDetailsImpl current);
    public ResponseEntity<?> findAllById(Long id);
    public ResponseEntity<?> findAllByAccountId(Long id);
    public ResponseEntity<?> getAllResearch( String startDate);
    public ResponseEntity<?> updateWeight(Long id, WeightDto weightDto);

}
