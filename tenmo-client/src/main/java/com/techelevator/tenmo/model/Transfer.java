package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferTypeID, transferStatusID;

    private int accountFrom, accountTo;

    private BigDecimal amount;

    public Transfer(int transferTypeID, int transferStatusID, int accountFrom, int accountTo, BigDecimal amount){
        this.transferTypeID = transferTypeID;
        this.transferStatusID = transferStatusID;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
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

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
//    @Override
//    public String toString(){
//
//    }
}
