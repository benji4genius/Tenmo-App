package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public AccountService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public Account register(Account account) {
        return null;
    }

    public BigDecimal getAccountBalanceByAccountID(int userID){
        // TODO
        return null;
    }

    public BigDecimal getAccountBalanceByUserID(int userID){
        BigDecimal balance = BigDecimal.ZERO; // Default to zero in case of errors

        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("userID", userID);

        try {
            ResponseEntity<Account> account = restTemplate.exchange("http://localhost:8080/accounts/{userID}", HttpMethod.GET, makeAuthEntity(), Account.class, uriVariables);

            if (account.getStatusCode().is2xxSuccessful()) {
                if (account.getBody() != null) {
                    balance = account.getBody().getBalance();
                }
            } else {
                System.err.println("Error getting account balance. HTTP Status: " + account.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            System.err.println("Error getting account balance: " + e.getMessage());
            e.printStackTrace();
        }

        return balance;
    }

    public BigDecimal getAccountBalanceByUsername(String username){
      //  BigDecimal balance = null;

       // try {
      //      ResponseEntity<Account> accountByName = restTemplate.postForEntity(baseUrl + "/transfers/{id}")
     //  } catch (Exception e) {
//
     //   }


      return null;
    }


    public BigDecimal addFunds(BigDecimal amount){
        return null;
    }

    public BigDecimal subtractFunds(BigDecimal amount){
        return null;
    }
    
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
