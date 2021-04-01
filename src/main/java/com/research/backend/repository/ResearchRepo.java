package com.research.backend.repository;

import com.research.backend.model.Account;
import com.research.backend.model.Research;

import java.util.List;

public interface ResearchRepo extends CommonRepository<Research, Long> {
    List<Research> findAllByAccount(Account account);
    List<Research> findAllByStartDate(String startDate);


}
