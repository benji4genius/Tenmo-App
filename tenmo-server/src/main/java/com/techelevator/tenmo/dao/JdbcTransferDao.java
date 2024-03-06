package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.JDBCType;
import java.util.List;

public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(){
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public List<Transfer> getTransfers(){

    }

    @Override
    public Transfer getTransferByID(int id){

    }

    @Override
    public List<Transfer> getTransfersByAccountFrom(int accountFrom){

    }

    @Override
    public List<Transfer> getTransfersByAccountTo(int accountTo){

    }

    @Override
    public List<Transfer> getTransfersByStatus(String statusType){

    }

    @Override
    public Transfer createTransfer(Transfer transfer){

    }
}
