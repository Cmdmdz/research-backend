package com.research.backend.services;

import com.research.backend.dto.AccountDto;
import com.research.backend.model.Account;
import com.research.backend.payload.request.LoginRequest;
import com.research.backend.payload.request.SignupRequest;
import com.research.backend.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<?> authenticateUser(LoginRequest LoginRequest);
    public ResponseEntity<?> registerUser(SignupRequest signupRequest);
    public Account updateAccount(UserDetailsImpl current ,AccountDto account);
    public ResponseEntity<?> findAllAccount();
    public ResponseEntity<?> findAccountByBranch(String branch);
    public ResponseEntity<?> findAllById(Long id);
    public ResponseEntity<?> findAllByCurrent(UserDetailsImpl current);

}
