package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class JdbcTransferDao implements TransferDao {

    private final String TRANSFER_SELECT = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
            "FROM transfer ";
    private final String INSERT_SELECT = "INSERT INTO transfer( " +
            "transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
            "VALUES (?, ?, ?, ?, ?) ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransferEntry(Transfer transfer) {
        Transfer updatedTransfer;
        String sql =
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            int transferID = jdbcTemplate.update(sql, transfer.getTransferTypeID(), transfer.getTransferStatusID(),
                    transfer.getaccountFromID(), transfer.getaccountToID(), transfer.getAmount());
            updatedTransfer = getTransferByID(transferID);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTransfer;
    }

    @Override
    public List<Transfer> getTransfers(int accountID) {

        List<Transfer> transfers = new ArrayList<>();
        String sql =
                TRANSFER_SELECT +
                        "WHERE account_from = ? OR account_to = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID, accountID);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;

    }

    /*    @Override
        public HashMap<Integer, String> getTypes() {
            HashMap<Integer, String> types = new HashMap<>();
            String sql =
                    "SELECT transfer_type_id, transfer_type_desc " +
                            "FROM transfer_type " +
                            "ORDER BY transfer_type_id;";
            try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
                while (results.next()) {
                    types.put(results.getInt("transfer_status_id"), results.getString("transfer_status_desc"));
                }
                if (types.size() != 2) {
                    throw new DaoException("Error, not enough rows found!");
                }
            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Unable to connect to server or database", e);
            } catch (DataIntegrityViolationException e) {
                throw new DaoException("Data integrity violation", e);
            }
            return types;
        }

        @Override
        public HashMap<Integer, String> getStatuses(){
            HashMap<Integer,String> statuses = new HashMap<>();
            String sql =
                    "SELECT transfer_status_id, transfer_status_desc " +
                            "FROM transfer_status " +
                            "ORDER BY transfer_status_id;";
            try{
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
                while(results.next()){
                    statuses.put(results.getInt("transfer_status_id"), results.getString("transfer_status_desc"));
                }
                if(statuses.size() != 3){
                    throw new DaoException("Error, not enough rows found!");
                }
            }catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Unable to connect to server or database", e);
            } catch (DataIntegrityViolationException e) {
                throw new DaoException("Data integrity violation", e);
            }
            return statuses;
        }*/
    @Override
    public Transfer getTransferByID(int id) {
        Transfer transfer = null;
        String sql =
                TRANSFER_SELECT +
                        "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return transfer;
    }

    @Override
    public List<Transfer> getTransfersByAccountFrom(int accountFromID, Account myAccount) {
        List<Transfer> transfersByAccountFrom = new ArrayList<>();
        String sql = TRANSFER_SELECT + "WHERE account_from = ? AND account_to = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFromID, myAccount.getAccountID());
            while (results.next()) {
                transfersByAccountFrom.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfersByAccountFrom;
    }

    @Override
    public List<Transfer> getTransfersByAccountTo(Account myAccount, int accountToID) {
        List<Transfer> transfersByAccountTo = new ArrayList<>();
        String sql = TRANSFER_SELECT + "WHERE account_from = ? AND account_to = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, myAccount.getAccountID(), accountToID);
            while (results.next()) {
                transfersByAccountTo.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfersByAccountTo;

    }

    @Override
    public List<Transfer> getTransfersByStatus(int statusTypeID,  int accountID) {
        List<Transfer> transfers = new ArrayList<>();
        String sql =
                "SELECT * FROM transfer " +
                        "WHERE transfer_status_id = ? AND account_to = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, statusTypeID, accountID);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;

    }

    @Override
    public void updateTransfer(int transferID, Transfer transfer) {
        String sql =
                "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, transfer.getTransferStatusID(), transferID);
            if (rowsAffected == 0) {
                throw new DaoException("Zero Rows Affected. Expected at least one.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferID(results.getInt("transfer_id"));
        transfer.setTransferTypeID(results.getInt("transfer_type_id"));
        transfer.setTransferStatusID(results.getInt("transfer_status_id"));
        transfer.setaccountFromID(results.getInt("account_from"));
        transfer.setaccountToID(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

}
