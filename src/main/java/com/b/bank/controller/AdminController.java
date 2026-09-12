package com.b.bank.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.b.bank.dto.AdminLogiRequest;
import com.b.bank.dto.LoginRequest;
import com.b.bank.entity.AdminEntity;
import com.b.bank.entity.TransectionEntity;
import com.b.bank.entity.UserEntity;
import com.b.bank.repo.AdminRepo;
import com.b.bank.repo.TransectionRepo;
import com.b.bank.repo.UserRepo;
import com.b.bank.service.JwtService;
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
   @Autowired
   AdminRepo adminRepo;
   @Autowired
   UserRepo userRepo1;
    @Autowired
  JwtService jwtService1 ;
  @Autowired 
  TransectionRepo transectionRepo;
   @PostMapping("/add")
   public boolean addAdmin(@RequestBody AdminEntity admin)
   {
      AdminEntity a=adminRepo.findByMobileNumber(admin.getMobileNumber());
      if(a==null)
      {
         adminRepo.save(admin);
         return true;
      }
      return false;
   }
  @GetMapping("/getByMobile")
 public AdminEntity getAdmin(@RequestParam String mobileNumber)
 {
   AdminEntity admin=adminRepo.findByMobileNumber(mobileNumber);
   if(admin==null)
   {
      return null;
   }
   return admin;
 }

 @PatchMapping("/update")
  public boolean updateDetail(@RequestParam String accountNumber , @RequestParam int choice , @RequestParam String newDetail)
  {
    AdminEntity admin=adminRepo.findByMobileNumber(accountNumber);
     switch(choice)
     {
      case 1 :admin.setAdminName(newDetail);
              break;
      case 2 :admin.setPassword(newDetail);
              break; 
      case 3 :admin.setMobileNumber(newDetail);
              break;
     }
     adminRepo.save(admin);
     return true;
  } 
@PatchMapping("/userStatusUpdate")
public boolean userStatusUpdate(@RequestParam String accountNumber)
{
   UserEntity user=userRepo1.findByAccountNumber(accountNumber);
   if(user.getAccountStatus())
   user.setAccountStatus(false);
else
   user.setAccountStatus(true);
   userRepo1.save(user);
   return true;
}

@GetMapping("/userStatusCheck")
public boolean userStatusCheck(@RequestParam String accountNumber)
{
   UserEntity user=userRepo1.findByAccountNumber(accountNumber);
   if(user==null)return false;
   return user.getAccountStatus();
}

   @PostMapping("/loginByMobile")
public ResponseEntity<?> userLogin(@RequestBody AdminLogiRequest request) {

    AdminEntity admin =
            adminRepo.findByMobileNumber(request.getMobileNumber());

    if (admin == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid Mobile number or password");
    }

    if (!admin.getPassword().equals(request.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid mobile number or password");
    }

    String token = jwtService1.generateToken(admin);

    return ResponseEntity.ok(
            java.util.Map.of(
                    "success", true,
                    "token", token,
                    "mobileNumber", admin.getMobileNumber(),
                    "adminName", admin.getAdminName()
            )
    );
}
     @GetMapping("/getByAccount")
   public ResponseEntity<UserEntity> getByAccountNumber(@RequestParam String accountNumber)
   { 
     UserEntity user=userRepo1.findByAccountNumber(accountNumber);
    if(user!=null){user.setPin(0);
     user.setPassword(null); 
     return ResponseEntity.ok(user); }
     return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
   }
    @PatchMapping("/deposit")
  public boolean depositBalance(@RequestParam String accountNumber , @RequestParam double ammount  )
  {
    UserEntity user=userRepo1.findByAccountNumber(accountNumber);
     if(user==null)return false;
     TransectionEntity newTransection=new TransectionEntity();
     newTransection.setUser(user);
     newTransection.setAmount(ammount);
     newTransection.setBalanceAfter(user.getBalance()+ammount);
     newTransection.setTransectionTime(LocalDateTime.now());
     newTransection.setStatus("Success");
     newTransection.setDiscription("depositUsingPin");
     newTransection.setType("Credit");
     transectionRepo.save(newTransection);
     user.setBalance(user.getBalance()+ammount);
     userRepo1.save(user);
     return true;
  }  
    @PatchMapping("/withdraw")
  public int withdrawBalance(@RequestParam String accountNumber , @RequestParam double ammount , @RequestParam int pin)
  {
    UserEntity user=userRepo1.findByAccountNumber(accountNumber);
    if(pin!=user.getPin())
    {
      return 0;
    }
    if(user.getAccountStatus()==false)return 1;
    if(ammount>user.getBalance())
    {
      return 2;
    }
    TransectionEntity newTransection=new TransectionEntity();
     newTransection.setUser(user);
     newTransection.setAmount(ammount);
     newTransection.setBalanceAfter(user.getBalance()-ammount);
     newTransection.setTransectionTime(LocalDateTime.now());
     newTransection.setStatus("Success");
     newTransection.setDiscription("WithDrawl..");
     newTransection.setType("Debit");
     transectionRepo.save(newTransection);
     user.setBalance(user.getBalance()-ammount);
     userRepo1.save(user);
     return 3;
  } 
}
