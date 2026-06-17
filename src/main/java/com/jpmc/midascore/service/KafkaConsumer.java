
package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KafkaConsumer {
    
     private final UserRepository userRepository;
     private final TransactionRecordRepository transactionRepository;
     RestTemplate restTemplate = new RestTemplate();

    public KafkaConsumer(UserRepository userRepository, TransactionRecordRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
 
     
        @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {

        
        System.out.println("Received transaction: " + transaction);
        
          UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);
        if (sender == null || recipient == null) return;

        
        if (sender.getBalance() < transaction.getAmount()) return;

   
//        sender.setBalance(sender.getBalance() - transaction.getAmount());
//        recipient.setBalance(recipient.getBalance() + transaction.getAmount());
//
//        userRepository.save(sender);
//        userRepository.save(recipient);
//
//        


          Map response = restTemplate.postForObject(
        "http://localhost:8080/incentive",
        transaction,
        Map.class
);

double incentive = response == null ? 0.0 : (double) response.get("amount");

    // update balances
    sender.setBalance(sender.getBalance() - transaction.getAmount());

    recipient.setBalance((float) (recipient.getBalance()
            + transaction.getAmount()
            + incentive));

    userRepository.save(sender);
    userRepository.save(recipient);

        TransactionRecord record =
                new TransactionRecord(sender, recipient, transaction.getAmount() , (float) incentive);

        transactionRepository.save(record);
        
        Iterable<UserRecord> users = userRepository.findAll();

        for (UserRecord user : users) {
            System.out.println(user.toString());
        }
    
    }
    
    
}
