package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountByUserID(int userID);
    BigDecimal getAccountBalanceByUserID(int userID);

    void updateBalance(int userID, Account account);

}
