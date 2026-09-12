package com.b.bank.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.b.bank.entity.UserEntity;
public interface UserRepo extends JpaRepository<UserEntity,Long> {
    UserEntity findByMobileNumber(String mobileNumber);
    UserEntity findByUserName(String userName);
    UserEntity findByAccountNumber(String accountNumber); 
} 
