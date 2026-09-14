package com.b.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin 
@Controller 
public class HealthController {
        @GetMapping("/health")
      public ResponseEntity<String> healthCHeck()
      {
        return ResponseEntity.ok("ok");
      }
}
