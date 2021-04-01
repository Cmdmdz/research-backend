package com.research.backend.repository;

import com.research.backend.model.Files;
import com.research.backend.model.Research;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;


@Repository
public interface FilesRepo extends CommonRepository<Files, Long>{
    List<Files> findAllByResearch(Research research);
//    List<Files> findAllByResearchDelete(Research research);


    @Transactional
    @Modifying
    @Query("delete from Files f where f.research = ?1")
    void deleteByResearch(Research research);

    @Transactional
    @Query("select f from Files f where f.research = ?1 and f.chapter = ?2")
    List<Files> fetchData(Research research , int chapter);


}
