package com.jpmc.midascore.entity;

import jakarta.persistence.*;


@Entity
public class TransactionRecord {
    
        @Id
    @GeneratedValue()
    private long id;

    private float amount;
    
    private float incentive ; 

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord recipient;

    public TransactionRecord() {
    }

    public TransactionRecord( UserRecord sender, UserRecord recipient, float amount ) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public TransactionRecord( UserRecord sender, UserRecord recipient , float amount, float incentive) {
        this.amount = amount;
        this.incentive = incentive;
        this.sender = sender;
        this.recipient = recipient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }
    
    
        
        
}
