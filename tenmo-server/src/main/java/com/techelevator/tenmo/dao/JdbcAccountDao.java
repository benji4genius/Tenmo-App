package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(){
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public BigDecimal getAccountBalanceByAccountID(int accountID){

    }

    @Override
    public BigDecimal getAccountBalanceByUserID(int userID){

    }

    @Override
    public BigDecimal getAccountBalanceByUsername(String username){

    }

    @Override
    public BigDecimal addFunds(BigDecimal amount){

    }

    @Override
    public BigDecimal subtractFunds(BigDecimal amount){

    }
}
