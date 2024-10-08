package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferID, transferTypeID, transferStatusID;

    private int accountFromID, accountToID;

    private BigDecimal amount;

    public Transfer(int accountFromID, int accountToID,  int transferStatusID, int transferTypeID,  BigDecimal amount){
        this.accountFromID = accountFromID;
        this.accountToID = accountToID;
        this.transferStatusID = transferStatusID;
        this.transferTypeID = transferTypeID;
        this.amount = amount;
    }

    public Transfer(){

    }

    public int getTransferTypeID() {
        return transferTypeID;
    }

    public void setTransferTypeID(int transferTypeID) {
        this.transferTypeID = transferTypeID;
    }

    public int getTransferStatusID() {
        return transferStatusID;
    }

    public void setTransferStatusID(int transferStatusID) {
        this.transferStatusID = transferStatusID;
    }

    public int getAccountFromID() {
        return accountFromID;
    }

    public void setAccountFromID(int accountFromID) {
        this.accountFromID = accountFromID;
    }

    public int getAccountToID() {
        return accountToID;
    }

    public void setAccountToID(int accountToID) {
        this.accountToID = accountToID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferID(){
        return transferID;
    }
}
