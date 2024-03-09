package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TransferService {

    private final String baseUrl = "http://localhost:8080/transfers";
    private final RestTemplate restTemplate = new RestTemplate();

    private final Scanner sc = new Scanner(System.in);
    private  Transfer transfer;
    private String authToken;

    public void setTransfer(int accountFromID, int accountToID, BigDecimal amount, int transferStatusID, int transferTypeID){
        transfer.setAccountFrom(accountFromID);
        transfer.setAccountTo(accountToID);
        transfer.setAmount(amount);
        transfer.setTransferStatusID(transferStatusID);
        transfer.setTransferTypeID(transferTypeID);
    }

    /***************************************************************************************
    * STEP 4 BELOW (addSendTransfer) -GL
     * A USER SENDS MONEY, CREATING A TRANSFER ENTRY AND UPDATING ACCOUNT BALANCES
     *      - I should be able to choose from a list of users to send TE Bucks to.
     *      - I must not be allowed to send money to myself.
     *      - A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
     *      - The receiver's account balance is increased by the amount of the transfer.
     *      - The sender's account balance is decreased by the amount of the transfer.
     *      - I can't send more TE Bucks than I have in my account.
     *      - I can't send a zero or negative amount.
     *      - A Sending Transfer has an initial status of Approved.
     *
     *      -Needs defensive programming (conditionals, while loops, and try-catch) -GL
     *      -Needs testing - GL
    * **************************************************************************************/

    public void addSendTransfer(int userID){
        int accountToID, transferStatusID, transferTypeID, receiversUserID;
        BigDecimal amount;
        transferStatusID = 2; //Approved
        transferTypeID = 2; //Send

        Account sendersAccount = getAccountByUserID(userID);
        Account receiversAccount;
        User[] users = null;

        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "/listUsers", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        System.out.println("-----------------------------------");
        System.out.println("               USERS               ");
        System.out.println("-----------------------------------");
        System.out.println("        ID       |     Username    ");

        for(User entry: users ){
            System.out.print("      "+ entry.getId() +  "      |    " + entry.getUsername());
        }

        //NEEDS DEFENSIVE PROGRAMMING HERE (loops, try-catch, etc) TO STOP USER FROM DOING INVALID INPUTS -GL
        System.out.println("Enter ID of user you are sending to (0 to cancel):");
        receiversUserID = Integer.parseInt(sc.nextLine());
        receiversAccount = getAccountByUserID(receiversUserID);
        accountToID = receiversAccount.getAccountID();
        System.out.println("Enter amount:");
        amount = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));

        //                   Senders AcctID   |  Rcpt AcctID  |  $  |  Approve, Deny, Pending  |   Receive(1) or Send(2)
        setTransfer(sendersAccount.getAccountID(), accountToID, amount, transferStatusID, transferTypeID);

        //Should create and add a new Transfer entry in the Database
        addTransferToDatabase();

    }
    public void addTransferToDatabase() {
        try {
            restTemplate.exchange(baseUrl, HttpMethod.POST, makeTransferEntity(transfer) , Void.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    /*************************************************************
    *   I JUST COPIED AND PASTED MY ABOVE METHOD (same conditions basically) -GL
     *   THIS SHOULD BE:
     *   STEP 7 REQUEST MONEY FROM A USER INSTEAD OF SEND
     *      - I should be able to choose from a list of users to request TE Bucks from.
     *      - I must not be allowed to request money from myself.
     *      - I can't request a zero or negative amount.
     *      - A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
     *      - A Request Transfer has an initial status of Pending.
     *      - No account balance changes until the request is approved.
     *      - The transfer request should appear in both users' list of transfers (use case #5).
    **************************************************************/
    public void addRequestTransfer(int userID){
        int accountToID, transferStatusID, transferTypeID, receiversUserID;
        BigDecimal amount;
        transferStatusID = 1; //Pending
        transferTypeID = 1; //Request

        Account sendersAccount = getAccountByUserID(userID);
        Account receiversAccount;

        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "/listUsers", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        System.out.println("-----------------------------------");
        System.out.println("               USERS               ");
        System.out.println("-----------------------------------");
        System.out.println("        ID       |     Username    ");
        for(User entry: users ){
            System.out.print("      "+ entry.getId() +  "      |    " + entry.getUsername());
        }

        System.out.println("Enter ID of user you are requesting from (0 to cancel):");
        receiversUserID = Integer.parseInt(sc.nextLine());
        receiversAccount = getAccountByUserID(receiversUserID);
        accountToID = receiversAccount.getAccountID();
        System.out.println("Enter amount:");
        amount = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));

        //                   Senders AcctID   |  Rcpt AcctID  |  $  |  Approve, Deny, Pending  |   Receive(1) or Send(2)
        setTransfer(sendersAccount.getAccountID(), accountToID, amount, transferStatusID, transferTypeID);

        //Should create and add a new Transfer entry in the Database
        try{
            restTemplate.exchange(baseUrl + "/addTransfer", HttpMethod.POST, makeAuthEntity(), Void.class );

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

    }

    /******************************************************************
    *  getAccountByUserID was originally in the AccountService class
     * but I think we can probably just combine the 2 Services into 1
     * to make things easier on us with having all the info in one spot.
     * But this approach is also tighter coupling. -GL
    * *****************************************************************/
    public Account getAccountByUserID(int userID){
        Account account = null;
        try{
            ResponseEntity<Account> accountResponse = restTemplate.exchange(baseUrl + userID, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = accountResponse.getBody();
        } catch (RestClientResponseException e) {
            System.err.println("Error getting account balance: " + e.getMessage());
            e.printStackTrace();
        }
        return account;
    }

    /****************************************************************
    * STEP 5 View Only the Logged In User's Transfer History
     *  -Needs to be tested -GL
     *  -Needs the loop to print it out in the right format provided -GL
     *      in the README
     *      - I should've made the SQL (server-side transfer JDBC)
     *      only SELECT entries that had the Logged In User's accountID -GL
    * ***************************************************************/
    public void viewMyTransfers(int userID){
        Account myAccount = getAccountByUserID(userID);
        Transfer[] transfers = null;

        try{
            ResponseEntity<Transfer[]> myTransfers =
                    restTemplate.exchange(baseUrl + "/viewMyTransfers", HttpMethod.GET,
                                            makeAccountEntity(myAccount), Transfer[].class);
            transfers = myTransfers.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        for (Transfer transfer : transfers){

        }
    }

    /************************************************************************
    * STEP 8 The Logged-in User Views THEIR PENDING Transfers
    *   -Needs the loop to print out the transfer list -GL
     *  -Needs to be tested -GL
    * **********************************************************************/
    public void viewMyPendingTransfers(int userID){
        Account myAccount = getAccountByUserID(userID);
        Transfer[] transfers = null;

        try{
            ResponseEntity<Transfer[]> myTransfers =
                    restTemplate.exchange(baseUrl + "/viewMyTransfersByStatus=" + 1, HttpMethod.GET,
                            makeAccountEntity(myAccount),Transfer[].class);
            transfers = myTransfers.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        for (Transfer transfer : transfers){

        }
    }

    /*********************************************************************
    *   STEP 6  VIEW ANY TRANSFER BY SEARCH OF ITS ID -GL
     *   - Needs testing.
     *   - I commented out a blank toString @Override of the Transfer model
     *      it could be a fast way to just print things out. -GL
    * **********************************************************************/
    public Transfer getTransferByID(int id) {
        Transfer transfer = null;
        try{
            ResponseEntity<Transfer> transferResponse = restTemplate.exchange(baseUrl + "/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = transferResponse.getBody();
        } catch (RestClientResponseException e) {
            System.err.println("Error getting account balance: " + e.getMessage());
            e.printStackTrace();
        }
        return transfer;
    }

   // @RequestMapping(path = "/transfersFrom={accountFromID}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountFrom( int accountFromID, Account myAccount){

    }

   // @RequestMapping(path = "/transfersTo={accountToID}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountTo( Account myAccount, int accountToID){

    }

    //@RequestMapping(path ="/searchByStatus=", method = RequestMethod.GET)
    public List<Transfer> getTransfersByStatus( int statusTypeID){

    }

    //@RequestMapping(path="/listUsers", method = RequestMethod.GET)
    public List<User> getUsers(User user){

    }

    //@RequestMapping(path = "/listStatuses", method = RequestMethod.GET)
    public HashMap<Integer, String> getStatusesMap(){

    }

    /************************************************************************
    *    METHODS TO MAKE HTTP ENTITIES
     *    the non-void ones are for passing the object
     *    to the controller (server-side)
     *    Whatever methods that have @RequestBody in the controllers -GL
    ************************************************************************/

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    public void setAuthToken (String token){
        this.authToken = token;
    }

}
