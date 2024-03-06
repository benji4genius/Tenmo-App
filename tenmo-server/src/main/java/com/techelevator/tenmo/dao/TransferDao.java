package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers();

    Transfer getTransferByID(int id);

    List<Transfer> getTransfersByAccountFrom(int accountFrom);

    List<Transfer> getTransfersByAccountTo(int accountTo);

    List<Transfer> getTransfersByStatus(String statusType); //This is the subquery using status_id > status_type_descrip

    Transfer createTransfer(Transfer transfer);
}
