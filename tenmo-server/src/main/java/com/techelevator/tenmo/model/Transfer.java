package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferID, transferTypeID, transferStatusID;
    
    private int accountFromID, accountToID;
    
    private BigDecimal amount;
    
    public Transfer(int transferID, int transferTypeID, int transferStatusID, int accountFromID, int accountToID, BigDecimal amount){
        this.transferID = transferID;
        this.transferTypeID = transferTypeID;
        this.transferStatusID = transferStatusID;
        this.accountFromID = accountFromID;
        this.accountToID = accountToID;
    }

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
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

    public int getaccountFromIDID() {
        return accountFromID;
    }

    public void setaccountFromIDID(int accountFromIDID) {
        this.accountFromID = accountFromIDID;
    }

    public int getaccountToID() {
        return accountToID;
    }

    public void setaccountToID(int accountToID) {
        this.accountToID = accountToID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
