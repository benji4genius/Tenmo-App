package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
/*
    private final String SELECT = "SELECT user_id, balance" +
            "FROM account";
*/

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public User getUserByAccountID(int accountID){
        User user = null;
        String sql =
                "SELECT user_id, username " +
                        "FROM tenmo_user " +
                        "WHERE user_id = ( " +
                            "SELECT user_id " +
                            "FROM account " +
                            "WHERE account_id = ?);";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,accountID);
            if(result.next()){
                user = mapRowToUser(result);
                return user;
            }else{
                throw new DaoException("Account was not Found in Database");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public Account getAccountByID(int accountID){
        Account account = null;
        String sql =
                "SELECT account_id, user_id, balance " +
                        "FROM account " +
                        "WHERE account_id = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,accountID);
            if(result.next()){
                account = mapRowToAccount(result);
                return account;
            }else{
                throw new DaoException("Account was not Found in Database");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Account getAccountByUserID(int userID){
        Account myAccount = null;
        String sql =
                "SELECT account_id, user_id, balance " +
                        "FROM account " +
                        "WHERE user_id = ?;";
        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userID);
            if(result.next()){
                myAccount = mapRowToAccount(result);
                return myAccount;
            }else{
                throw new DaoException("Account was not Found in Database");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public BigDecimal getAccountBalanceByUserID(int userID) {
        BigDecimal accountBalance= null;
        String sql = "SELECT balance " +
                     "FROM account "  +
                     "WHERE user_id = ?";

        try {
            Account account ;
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userID);
            if(result.next()){
                account = mapRowToAccount(result);
                accountBalance = account.getBalance();
                return accountBalance;
            }else{
                throw new DaoException("No Account Found");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public void updateBalance(int userID, Account account) {
        BigDecimal newBalance = account.getBalance();
        String sql = "UPDATE account SET balance =? WHERE account_id =?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, newBalance, userID);
            if (rowsAffected == 0) {
                throw new DaoException("Zero Rows Affected. Expected at least one.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountID(results.getInt("account_id"));
        account.setUserID(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));

        return account;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}

