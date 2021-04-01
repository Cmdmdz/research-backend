package com.research.backend.controller;


import com.research.backend.config.cross.CurrentUser;
import com.research.backend.dto.AccountDto;
import com.research.backend.model.Account;
import com.research.backend.payload.request.LoginRequest;
import com.research.backend.payload.request.SignupRequest;
import com.research.backend.security.UserDetailsImpl;
import com.research.backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControl {


    private final AuthService authService;

    @PostMapping("/signing")
    public ResponseEntity<?> authenticateUser1(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        return authService.registerUser(signUpRequest);
    }

    @PutMapping("/account")
    public Account updateAccount(@CurrentUser UserDetailsImpl current , @RequestBody AccountDto accountDto){
        return authService.updateAccount(current, accountDto);
    }

    @GetMapping("/account")
    public ResponseEntity<?> findAllAccount(){
        return authService.findAllAccount();
    }

    @GetMapping("/accountBybranch")
    public ResponseEntity<?> findAllAccountBybranch(String branch){
        return authService.findAccountByBranch(branch);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<?> findAllById(@PathVariable("id") Long id){
        return authService.findAllById(id);
    }


    @GetMapping("/accounts")
    public ResponseEntity<?> findAllByCurren(@CurrentUser UserDetailsImpl current){
        return authService.findAllById(current.getId());
    }


}
