package com.b.bank.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.b.bank.dto.LoginRequest;
import com.b.bank.entity.TransectionEntity;
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
import com.b.bank.entity.UserEntity;
import com.b.bank.repo.TransectionRepo;
import com.b.bank.repo.UserRepo;
import com.b.bank.service.JwtService;
 @CrossOrigin 
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired 
    TransectionRepo transectionRepo;
    @Autowired
  JwtService jwtService;
    @PostMapping("/add")
    public boolean addUser(@RequestBody UserEntity user)
    { 
        UserEntity u=userRepo.findByMobileNumber(user.getMobileNumber());
        if(u==null)
        {
            userRepo.save(user);
            user.setAccountNumber(user.getId());
            user.setAccountStatus(true);
            userRepo.save(user);
            return true;
        }
       return false;
    }

    
   @GetMapping("/getByMobile")
   public UserEntity getByMobileNumber(@RequestParam String number)
   { 
     UserEntity user=userRepo.findByMobileNumber(number);
     user.setPin(0);
     user.setPassword(null); 
     return user;  
   } 
     @GetMapping("/getByAccount")
   public ResponseEntity<UserEntity> getByAccountNumber(@RequestParam String accountNumber)
   { 
     UserEntity user=userRepo.findByAccountNumber(accountNumber);
    if(user!=null){user.setPin(0);
     user.setPassword(null); 
     return ResponseEntity.ok(user); }
     return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
   }
   @PostMapping("/loginByAccount")
public ResponseEntity<?> userLogin(@RequestBody LoginRequest request) {

    UserEntity user =
            userRepo.findByAccountNumber(request.getAccountNumber());

    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid account number or password");
    }

    if (!user.getPassword().equals(request.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid account number or password");
    }

    String token = jwtService.generateToken(user);

    return ResponseEntity.ok(
            java.util.Map.of(
                    "success", true,
                    "token", token,
                    "accountNumber", user.getAccountNumber(),
                    "userName", user.getUserName()
            )
    );
}
  @PostMapping("/loginByMobile")
public ResponseEntity<?> userLogin1(@RequestBody LoginRequest request) {

    UserEntity user =
            userRepo.findByMobileNumber(request.getMobileNumber());

    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid account number or password");
    }

    if (!user.getPassword().equals(request.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid account number or password");
    }

    String token = jwtService.generateToken(user);

    return ResponseEntity.ok(
            java.util.Map.of(
                    "success", true,
                    "token", token,
                    "mobileNumber", user.getMobileNumber(),
                    "userName", user.getUserName()
            )
    );
}
   @GetMapping("/balance")
   public double getBalance(@RequestParam String accountNumber)
   { 
     UserEntity user=userRepo.findByAccountNumber(accountNumber);
     return user.getBalance();
   } 
  @PatchMapping("/withdraw")
  public int withdrawBalance(@RequestParam String accountNumber , @RequestParam double ammount , @RequestParam int pin)
  {
    UserEntity user=userRepo.findByAccountNumber(accountNumber);
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
     userRepo.save(user);
     return 3;
  } 

  @PatchMapping("/deposit")
  public boolean depositBalance(@RequestParam String accountNumber , @RequestParam double ammount  )
  {
    UserEntity user=userRepo.findByAccountNumber(accountNumber);
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
     userRepo.save(user);
     return true;
  }  
    @PatchMapping("/depositUsingPin")
  public boolean depositBalance1(@RequestParam String accountNumber , @RequestParam double ammount ,@RequestParam int pin )
  {
    UserEntity user=userRepo.findByAccountNumber(accountNumber);
     if(user==null)return false;
     if(pin!=user.getPin())return false;
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
     userRepo.save(user);
     return true;
  } 

  @PatchMapping("/update")
  public boolean updateDetail(@RequestParam String accountNumber , @RequestParam int choice , @RequestParam String newDetail)
  {
    UserEntity user=userRepo.findByAccountNumber(accountNumber);
     switch(choice)
     {
      case 1 :user.setUserName(newDetail);
              break;
      case 2 :user.setPassword(newDetail);
              break; 
      case 3 :user.setMobileNumber(newDetail);
              break;
     }
     userRepo.save(user);
     return true;
  } 

  @PatchMapping("/updatePIN")
  public boolean updatePIN(@RequestParam String accountNumber , @RequestParam int pin ,@RequestParam int newPin )
   {
    UserEntity user=userRepo.findByAccountNumber(accountNumber);
    if(pin!=user.getPin())
    {
      return false;
    }
    user.setPin(newPin);
    userRepo.save(user);
    return true;
   }
  
   @GetMapping("/transection")
   public List<TransectionEntity> getTransectionByAccountNumber(@RequestParam String number)
   { 
     UserEntity user=userRepo.findByAccountNumber(number);
      if(user==null)return null;
      List<TransectionEntity> transections=transectionRepo.findByUser_AccountNumberOrderByTransectionTimeDesc(number);
     return transections ;  
   } 


   
   @GetMapping("/transection3")
   public List<TransectionEntity> get3TransectionByAccountNumber(@RequestParam String number)
   { 
     UserEntity user=userRepo.findByAccountNumber(number);
      if(user==null)return null;
      List<TransectionEntity> transections=transectionRepo.findTop3ByUser_AccountNumberOrderByTransectionTimeDesc(number);
     return transections ;  
   } 

     @PatchMapping("/transfer")
  public int transfer(@RequestParam String accountNumber ,@RequestParam double amount , @RequestParam int pin 
                            , @RequestParam String receverAccount ,@RequestParam String description )
   {
    UserEntity user=userRepo.findByAccountNumber(accountNumber);
    UserEntity recever=userRepo.findByAccountNumber(receverAccount);
    if(recever==null)return 0;
    if(pin!=user.getPin())
    {
      return 1;
    }
    user.setBalance(user.getBalance()-amount);
    recever.setBalance(recever.getBalance()+amount);
     TransectionEntity newTransectionUser=new TransectionEntity();
     newTransectionUser.setUser(user);
     newTransectionUser.setAmount(amount);
     newTransectionUser.setBalanceAfter(user.getBalance()-amount);
     newTransectionUser.setTransectionTime(LocalDateTime.now());
     newTransectionUser.setStatus("Success");
     newTransectionUser.setDiscription(description);
     newTransectionUser.setType("Debit");
     transectionRepo.save(newTransectionUser);
    
          TransectionEntity newTransectionRecever=new TransectionEntity();
      newTransectionRecever.setUser(recever);
      newTransectionRecever.setAmount(amount);
      newTransectionRecever.setBalanceAfter(recever.getBalance()+amount);
      newTransectionRecever.setTransectionTime(LocalDateTime.now());
      newTransectionRecever.setStatus("Success");
      newTransectionRecever.setDiscription(description);
      newTransectionRecever.setType("Credit");
     transectionRepo.save( newTransectionRecever);

    userRepo.save(user);
    userRepo.save(recever);
    return 2;
   }
 @GetMapping("/checkLoginStatus")
 public boolean checkLoginStatus(@RequestParam String accountNumber)
 {
   UserEntity user=userRepo.findByAccountNumber(accountNumber);
   return user.getLoginStatus();
 }
 @GetMapping("/checkLoginStatusByMobile")
 public boolean checkLoginStatus1(@RequestParam String mobileNumber)
 {
   UserEntity user=userRepo.findByAccountNumber(mobileNumber);
   return user.getLoginStatus();
 }
 @PatchMapping("/setLoginStatus")
 public void setLoginStatus(@RequestParam  String accountNumber)
 {
   UserEntity user=userRepo.findByAccountNumber(accountNumber);
   System.out.println("setLoginStatus");
   if(user.getLoginStatus()==true)user.setLoginStatus(false);
   else
    {
       user.setLoginStatus(true);
    }
    userRepo.save(user);
 }
}
