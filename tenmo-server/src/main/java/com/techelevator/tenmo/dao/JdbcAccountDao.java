package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
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
    public BigDecimal getAccountBalanceByUserID(int userID) {
        BigDecimal accountBalance= null;
        String sql = "SELECT balance " +
                     "FROM account "  +
                     "WHERE user_id =?;";

        try {
            accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userID);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return accountBalance;

    }

    @Override
    public void updateBalance(Account account) {
        BigDecimal newBalance = account.getBalance();

        String sql = "UPDATE account SET balance =? WHERE account_id =?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, newBalance, account.getAccountID());
            if (rowsAffected == 0) {
                throw new DaoException("Zero Rows Affected. Expected at least one.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }
}

