package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public Account register(Account account){

    }

    public BigDecimal getAccountBalanceByAccountID(int accountID){

    }

    public BigDecimal getAccountBalanceByUserID(int userID){

    }

    public BigDecimal getAccountBalanceByUsername(String username){

    }


    public BigDecimal addFunds(BigDecimal amount){

    }

    public BigDecimal subtractFunds(BigDecimal amount){

    }
}
