package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.HashMap;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers(Account account);

    Transfer getTransferByID(int id);

/*    HashMap<Integer, String> getStatuses();

    HashMap<Integer, String> getTypes();*/

    List<Transfer> getTransfersByAccountFrom(int accountFromID, Account myAccount);

    List<Transfer> getTransfersByAccountTo (Account myAccount, int accountToID);

    List<Transfer> getTransfersByStatus(int statusTypeID, Account myAccount); //This is the subquery using status_id > status_type_descrip

    Transfer createTransfer(Transfer transfer);

    void updateTransfer(int transferID, Transfer transfer);

}
