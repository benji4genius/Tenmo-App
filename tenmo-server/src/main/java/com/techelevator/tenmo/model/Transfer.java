package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {

    private int transferID;

    private int transferTypeID, transferStatusID;

    private int accountFromID, accountToID;

    private BigDecimal amount;

    public Transfer(int transferTypeID, int transferStatusID, int accountFromID, int accountToID, BigDecimal amount) {
        this.transferTypeID = transferTypeID;
        this.transferStatusID = transferStatusID;
        this.accountFromID = accountFromID;
        this.accountToID = accountToID;
        this.amount = amount;
    }

    public Transfer() {

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

    public int getaccountFromID() {
        return accountFromID;
    }

    public void setaccountFromID(int accountFromId) {
        this.accountFromID = accountFromId;
    }

    public int getaccountToID() {
        return accountToID;
    }

    public void setaccountToID(int accountToId) {
        this.accountToID = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
