package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {

    private final String TRANSFER_SELECT = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount" +
            "FROM transfer";
    private final String INSERT_SELECT = "INSERT INTO transfer(" +
            "transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
            "VALUES (?, ?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transfer> getTransfers(){

        List<Transfer> transfers = new ArrayList<>();
        String sql = TRANSFER_SELECT;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;

    }

    @Override
    public Transfer getTransferByID(int id){
        Transfer transfer = null;
        String sql = TRANSFER_SELECT +
                " WHERE transfer_id=?";
        try {

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return transfer;

    }

    @Override
    public List<Transfer> getTransfersByAccountFrom(int accountFrom){
        List<Transfer> transfersByAccountFrom = new ArrayList<>();
        String sql = TRANSFER_SELECT + "WHERE account_from =?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfersByAccountFrom.add(mapRowToTransfer(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfersByAccountFrom;

    }

    @Override
    public List<Transfer> getTransfersByAccountTo(int accountTo){
        List<Transfer> transfersByAccountTo = new ArrayList<>();
        String sql = TRANSFER_SELECT + "WHERE account_to =?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfersByAccountTo.add(mapRowToTransfer(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfersByAccountTo;

    }

    @Override
    public List<Transfer> getTransfersByStatus(String statusType){
        List<Transfer> transfers = new ArrayList<>();
        String sql = TRANSFER_SELECT + "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id";


        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;

    }

    @Override
    public Transfer createTransfer(Transfer transfer){
        Transfer newTransfer = null;
        String sql = INSERT_SELECT + "RETURNING transfer_id;";
        try {
            int newTransferID = jdbcTemplate.queryForObject(sql,int.class, transfer.getTransferTypeID(),transfer.getTransferStatusID(),
                    transfer.getaccountFromId(), transfer.getaccountToId(), transfer.getAmount());
            newTransfer = getTransferByID(newTransferID);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newTransfer;

    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferID(results.getInt("transfer_id"));
        transfer.setTransferTypeID(results.getInt("transfer_type_id"));
        transfer.setTransferStatusID(results.getInt("transfer_status_id"));
        transfer.setaccountFromId(results.getInt("account_from"));
        transfer.setaccountToId(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

}
