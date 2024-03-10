package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int accountID, userID;

    private BigDecimal balance;

    public Account(){}
    public Account(int accountID, int userID, BigDecimal balance){
        this.accountID = accountID;
        this.userID = userID;
        this.balance = balance;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean equals(Account account) {
        if(userID == account.getUserID()){
            return true;
        }else{
            return false;
        }
    }
}
