package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.http.HttpRequest;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountController {

    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/{userID}", method = RequestMethod.GET)
    public BigDecimal getAccountBalanceByUserID(@PathVariable int userID){
        BigDecimal accountBalance = null;
        try {
            accountBalance = accountDao.getAccountBalanceByUserID(userID);
            return accountBalance;
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }

    @RequestMapping(path ="/balanceUpdate", method = RequestMethod.PUT)
    public void updateBalance(@Valid@RequestBody Account account) {
        try {
            accountDao.updateBalance(account);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account object was not Valid (contains null values)");
        }

    }
}
