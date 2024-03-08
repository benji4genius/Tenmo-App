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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer createTransfer(@Valid@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferByID(int id) {
        return transferDao.getTransferByID(id);
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

    @RequestMapping(path ="/searchByStatus=", method = RequestMethod.GET)
    public List<Transfer> getTransfersByStatus(@RequestParam int statusTypeID){
        List<Transfer> transfers = transferDao.getTransfersByStatus(statusTypeID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path="/listUsers", method = RequestMethod.GET)
    public List<User> getUsers(@RequestBody User user){
        List<User> users = userDao.getUsers(user);
        if(users == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Users were found");
        }else{
            return users;
        }
    }

    @RequestMapping(path = "/listStatuses", method = RequestMethod.GET)
    public HashMap<Integer, String> getStatusesMap(){
        HashMap<Integer, String> statuses = transferDao.getStatuses();
        if(statuses == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Statuses were found");
        }else{
            return statuses;
        }
    }

}

