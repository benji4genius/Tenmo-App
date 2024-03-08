package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferDao transferDao;

    public TransferController(TransferDao transferDao){
        this.transferDao = transferDao;
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

    @RequestMapping(path = "/transfersFrom/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountFrom(@PathVariable int accountFromID){
        List<Transfer> transfers = transferDao.getTransfersByAccountFrom(accountFromID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path = "/transfersTo/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountTo(@PathVariable int accountToID){
        List<Transfer> transfers = transferDao.getTransfersByAccountFrom(accountToID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

    @RequestMapping(path ="/searchByStatus")
    public List<Transfer> getTransfersByStatus(@RequestParam int statusTypeID){
        List<Transfer> transfers = transferDao.getTransfersByStatus(statusTypeID);
        if(transfers == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Transfers were found");
        }else{
            return transfers;
        }
    }

}

