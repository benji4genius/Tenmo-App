package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {
    private final String SELECT = "SELECT account_id, user_id, balance" +
            "FROM account";

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BigDecimal getAccountBalanceByAccountID(int accountID) {
        BigDecimal accountBalance = null;
        String sql = SELECT + "WHERE account_id =?";


        try {
            accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountID);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return accountBalance;
    }


    @Override
    public BigDecimal getAccountBalanceByUserID(int userID) {
        BigDecimal accountBalanceById = null;
        String sql = SELECT + "WHERE user_id =?";


        try {
            accountBalanceById = jdbcTemplate.queryForObject(sql, BigDecimal.class, userID);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return accountBalanceById;

    }

    @Override
    public BigDecimal getAccountBalanceByUsername(String username) {

        BigDecimal accountBalanceByUsername = null;
        String sql = SELECT +  "JOIN tenmo_user ON account.user_id = tenmo_user.user_id" +
                "WHERE username ILIKE ?";
        try {
            accountBalanceByUsername = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return accountBalanceByUsername;

    }

    @Override
    public BigDecimal addFunds(BigDecimal amount, Account account) {
        BigDecimal addFunds = account.getBalance().add(amount);

        String sql = "UPDATE account SET balance =? WHERE account_id =?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, addFunds, account.getAccountID());
            if (rowsAffected == 0) {
                throw new DaoException("Zero Rows Affected. Expected at least one.");
            } else {
                addFunds = BigDecimal.valueOf(0); // placeholder
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return addFunds;

    }

    @Override
    public BigDecimal subtractFunds(BigDecimal amount,Account account) {
        BigDecimal subtractFunds = account.getBalance().subtract(amount);

        String sql = "UPDATE account SET balance =? WHERE account_id =?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, subtractFunds, account.getAccountID());
            if (rowsAffected == 0) {
                throw new DaoException("Zero Rows Affected. Expected at least one.");
            } else {
                subtractFunds = BigDecimal.valueOf(0); // placeholder
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return subtractFunds;

    }




}
