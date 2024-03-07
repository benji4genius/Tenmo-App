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

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BigDecimal getAccountBalanceByAccountID(int accountID) {
        BigDecimal accountBalance = null;
        String sql = "SELECT balance FROM account WHERE account_id=?";


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

    }

    @Override
    public BigDecimal getAccountBalanceByUsername(String username) {

    }

    @Override
    public BigDecimal addFunds(BigDecimal amount) {

    }

    @Override
    public BigDecimal subtractFunds(BigDecimal amount) {

    }




}
