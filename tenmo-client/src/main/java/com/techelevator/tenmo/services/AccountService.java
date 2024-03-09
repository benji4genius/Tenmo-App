package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountService {

    private final String baseUrl = "http://localhost:8080/accounts/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    private Account myAccount;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setMyAccount(int userID){

        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + userID , HttpMethod.GET, makeAuthEntity(), Account.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() != null) {
                    myAccount = response.getBody();
                }
            } else {
                System.err.println("Error getting account balance. HTTP Status: " + response.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            System.err.println("Error getting account balance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Account getMyAccount(){
        return myAccount;
    }

    public BigDecimal getAccountBalanceByUserID(int userID){
        BigDecimal balance = BigDecimal.ZERO; // Default to zero in case of errors

        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("userID", userID);

        try {
            ResponseEntity<BigDecimal> balanceResponse = restTemplate.exchange(baseUrl + userID + "/getBalance" , HttpMethod.GET, makeAuthEntity(), BigDecimal.class, uriVariables);
            if (balanceResponse.getStatusCode().is2xxSuccessful()) {
                if (balanceResponse.getBody() != null) {
                    balance = balanceResponse.getBody();
                }
            } else {
                System.err.println("Error getting account balance. HTTP Status: " + balanceResponse.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            System.err.println("Error getting account balance: " + e.getMessage());
            e.printStackTrace();
        }

        return balance;
    }

    public void updateBalance(int userID){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Account> request = new HttpEntity<>(myAccount, headers);
            restTemplate.put(baseUrl + "/" + userID + "/updateBalance", request);
        }catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
