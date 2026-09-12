package com.b.bank.entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity 
@Table(name="user_transections")
public class TransectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
   
    @jakarta.persistence.ManyToOne
    @JoinColumn(name="accountNumber",referencedColumnName = "accountNumber",nullable=false)
    @JsonIgnore 
    public UserEntity user;

    public String type;

    double amount;

    double balanceAfter;

    public LocalDateTime transectionTime;

    public String Discription;

    public String status;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(double balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public LocalDateTime getTransectionTime() {
		return transectionTime;
	}

	public void setTransectionTime(LocalDateTime transectionTime) {
		this.transectionTime = transectionTime;
	}

	public String getDiscription() {
		return Discription;
	}

	public void setDiscription(String discription) {
		Discription = discription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
   
}
