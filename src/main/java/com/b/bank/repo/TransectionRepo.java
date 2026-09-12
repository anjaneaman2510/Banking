package com.b.bank.repo;
import com.b.bank.entity.TransectionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransectionRepo extends JpaRepository<TransectionEntity,Long> {
    List<TransectionEntity> findByUser_AccountNumberOrderByTransectionTimeDesc(String acountNumber);
     List<TransectionEntity> findTop3ByUser_AccountNumberOrderByTransectionTimeDesc(String acountNumber);
}
