package com.research.backend.repository;

import com.research.backend.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends CommonRepository<Account,Long>{
    Optional<Account> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<Account> findAccountByBranch(String branch);
}
