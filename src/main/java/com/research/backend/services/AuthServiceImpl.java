package com.research.backend.services;


import com.research.backend.dto.AccountDto;
import com.research.backend.model.Account;
import com.research.backend.model.Role;
import com.research.backend.payload.request.LoginRequest;
import com.research.backend.payload.request.SignupRequest;
import com.research.backend.payload.response.JwtResponse;
import com.research.backend.payload.response.MessageResponse;
import com.research.backend.repository.AccountRepo;
import com.research.backend.repository.RoleRepo;
import com.research.backend.security.UserDetailsImpl;
import com.research.backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepo accountRepo;
    private final RoleRepo roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        log.info(roles);
        log.info(userDetails.getUsername());
        log.info(userDetails.getFirstname());
        log.info(userDetails.getBranch());
        log.info(userDetails.getFaculty());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstname(),
                userDetails.getLastname(),
                userDetails.getAddress(),
                userDetails.getBranch(),
                userDetails.getFaculty(),
                roles));
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (accountRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (accountRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        Account user = new Account(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getBranch(),
                signUpRequest.getFaculty(),
                signUpRequest.getAddress(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getPermission();
        Set<Role> roles = new HashSet<>();


        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Account.ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(Account.ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(Account.ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        accountRepo.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseEntity<?> findAccountByBranch(String branch) {
        List<Account> accounts = new ArrayList<>();
        accountRepo.findAccountByBranch(branch).forEach(accounts::add);
        return ResponseEntity.ok(accounts);
    }

    @Override
    public ResponseEntity<?> findAllAccount() {
        List<Account> accounts = new ArrayList<>();
        accountRepo.findAll().forEach(accounts::add);
        return  ResponseEntity.ok(accounts);

    }

    @Override
    public Account updateAccount( UserDetailsImpl current,AccountDto account) {
        Optional<Account> _account = accountRepo.findById(current.getId());
        Account account1 = _account.get();
        account1.setFirstname(account.getFirstname());
        account1.setLastname(account.getLastname());
        account1.setBranch(account.getBranch());
        account1.setFaculty(account.getFaculty());
        account1.setAddress((account.getAddress()));

        return accountRepo.save(account1);

    }

    @Override
    public ResponseEntity<?> findAllById(Long id) {
        Optional<Account> account = accountRepo.findById(id);
        return ResponseEntity.ok(account.get());
    }

    @Override
    public ResponseEntity<?> findAllByCurrent(UserDetailsImpl current) {
        Optional<Account> _account = accountRepo.findById(current.getId());

        return ResponseEntity.ok(_account);
    }
}
