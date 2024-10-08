package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
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

    @RequestMapping(path = "/getUserByAcctID/{id}", method = RequestMethod.GET)
    public User getUserByAccountID(@PathVariable int id){
        User user = null;
        try{
            user = accountDao.getUserByAccountID(id);
            return user;
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Integer object expected, but not found.)");
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Account getAccountByAccountID(@PathVariable int id){
        Account account = null;
        try{
            account = accountDao.getAccountByID(id);
            return account;
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Integer object expected, but not found.)");
        }
    }

    @RequestMapping(path = "/user/{userID}",method = RequestMethod.GET)
    public Account getAccountByUserID(@PathVariable int userID){
        Account myAccount = null;
        try{
            myAccount = accountDao.getAccountByUserID(userID);
            return myAccount;
        }catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Integer object expected, but not found.)");
        }
    }

    @RequestMapping(path = "/{userID}/getBalance",method = RequestMethod.GET)
    public BigDecimal getAccountBalanceByUserID(@PathVariable int userID){
        BigDecimal accountBalance = null;
        try {
            accountBalance = accountDao.getAccountBalanceByUserID(userID);
            return accountBalance;
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Integer object expected, but not found.)");
        }
    }

    @RequestMapping(path ="/{accountID}/updateBalance", method = RequestMethod.PUT)
    public void updateBalance(@PathVariable int accountID, @RequestBody Account account) {
        try {
            accountDao.updateBalance(accountID, account);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } catch (RequestRejectedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account object was not Valid (contains null values)");
        }

    }
}
