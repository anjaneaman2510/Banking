package com.b.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.b.bank.entity.AdminEntity;

public interface AdminRepo extends JpaRepository<AdminEntity,Long> {
    AdminEntity findByMobileNumber(String mobileNumber);
    AdminEntity findByAdminName(String userName); 
}
