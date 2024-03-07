package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getAccountBalanceByAccountID(int accountID);

    BigDecimal getAccountBalanceByUserID(int userID);

    BigDecimal getAccountBalanceByUsername(String username);

    BigDecimal addFunds(BigDecimal amount,Account account);

    BigDecimal subtractFunds(BigDecimal amount, Account account);


}
