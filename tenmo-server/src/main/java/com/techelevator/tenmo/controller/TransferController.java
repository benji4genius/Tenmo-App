package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfers")
public class TransferController {

    private final TransferDao transferDao;

    private final UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao){
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferByID(int id) {
        return transferDao.getTransferByID(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/addTransfer", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransferEntry(transfer);
    }

    @RequestMapping(path = "/{accountID}/viewMyTransfers", method = RequestMethod.GET)
    public List<Transfer> getMyTransfers(@PathVariable int accountID){
        List<Transfer> transfers = transferDao.getTransfers(accountID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Users were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path = "/transfersFrom={accountFromID}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountFrom(@PathVariable int accountFromID, @Valid@RequestBody Account myAccount){
        List<Transfer> transfers = transferDao.getTransfersByAccountFrom(accountFromID, myAccount);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path = "/transfersTo={accountToID}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountTo(@Valid@RequestBody Account myAccount, @PathVariable int accountToID){
        List<Transfer> transfers = transferDao.getTransfersByAccountTo(myAccount, accountToID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path ="/viewMyTransfersByStatus={statusTypeID}/{accountID}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByStatus(@PathVariable("statusTypeID") int statusTypeID, @PathVariable("accountID") int accountID){
        List<Transfer> transfers = transferDao.getTransfersByStatus(statusTypeID, accountID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path = "/{id}/updateTransfer", method = RequestMethod.PUT)
    public void updateTransfer(@PathVariable int id, @RequestBody Transfer transfer){
        try {
            transferDao.updateTransfer(id, transfer);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        } catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer Entity was not Valid (contains null values)");
        }
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path="/listUsers", method = RequestMethod.GET)
    public List<User> getUsers(){
        List<User> users = userDao.getUsers();
        if(users == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Users were found");
        }else{
            return users;
        }
    }
/*
    @RequestMapping(path = "/listStatuses", method = RequestMethod.GET)
    public HashMap<Integer, String> getStatusesMap(){
        HashMap<Integer, String> statuses = transferDao.getStatuses();
        if(statuses == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Statuses were found");
        }else{
            return statuses;
        }
    }

    @RequestMapping(path = "/listTypes", method = RequestMethod.GET)
    public HashMap<Integer, String> getTypeMap(){
        HashMap<Integer, String> types = transferDao.getTypes();
        if(types == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Statuses were found");
        }else{
            return types;
        }
    }*/

}

