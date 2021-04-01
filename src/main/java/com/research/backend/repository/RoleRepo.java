package com.research.backend.repository;


import com.research.backend.model.Account;
import com.research.backend.model.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends CommonRepository<Role, Long> {
    Optional<Role> findByName(Account.ERole name);
}
