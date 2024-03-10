package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {

    User getUserByAccountID(int accountID);
    Account getAccountByID(int accountID);
    Account getAccountByUserID(int userID);
    BigDecimal getAccountBalanceByUserID(int userID);
    void updateBalance(int userID, Account account);

}
