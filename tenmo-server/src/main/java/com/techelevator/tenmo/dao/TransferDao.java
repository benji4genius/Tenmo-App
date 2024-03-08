package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers(int accountID);

    Transfer getTransferByID(int id);

    List<Transfer> getTransfersByAccountFrom(int accountFromID);

    List<Transfer> getTransfersByAccountTo(int accountToID);

    List<Transfer> getTransfersByStatus(int statusTypeID); //This is the subquery using status_id > status_type_descrip

    Transfer createTransfer(Transfer transfer);
}
