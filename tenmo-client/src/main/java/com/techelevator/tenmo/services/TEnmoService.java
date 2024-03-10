package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TEnmoService {

    private final String transfersUrl = "http://localhost:8080/transfers/";
    private final String accountsUrl = "http://localhost:8080/accounts/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner sc = new Scanner(System.in);
    private Transfer newTransferEntry;
    private Account myAccount, othersAccount;
    private User[] usersList;
    private Transfer[] transfersList;
    private String authToken;

    public void setUsersList(User user) {
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(
                            transfersUrl + "listUsers", HttpMethod.GET, makeUserEntity(user), User[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() != null) {
                    usersList = response.getBody();
                }
            } else {
                System.err.println("Error getting content! Http Status Code: " + response.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            System.err.println("Error getting content! Http Status Code: " + e.getStatusText());
            e.printStackTrace();
        }
    }

    public void showUsers() {
        System.out.println("-------------------------------------------");
        System.out.println("               Users List                 ");
        System.out.println("-------------------------------------------");
        for (User user : usersList) {
            System.out.println("ID:   " + user.getId() + "   |    " + user.getUsername());
        }
        System.out.println("-------------------------------------------");
    }

    public void setTransfersList(){
        try {
            ResponseEntity<Transfer[]> myTransfers =
                    restTemplate.exchange(transfersUrl + "viewMyTransfers", HttpMethod.GET,
                            makeAccountEntity(myAccount), Transfer[].class);
            transfersList = myTransfers.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    /*************************************************************************************************************
     *   EVERYTHING TO DO WITH THE CURRENT USER'S ACCOUNT/TRANSFERS
     *       GOALS:
     *       -----------------------------------------------------------
     *           - Acquire the Current User's Account
     *          - Update the Current User's Balance (USE CASE 4)
     *          - Ability acquire the Current User's Account Balance (USE CASE 3)
     *       -----------------------------------------------------------
     *  OTHER THINGS THE CURRENT USER SHOULD BE ABLE TO DO
     *      GOALS:
     *       -----------------------------------------------------------
     *          - View a List of all the Users
     *              - Used in Sending Money, and Requesting Money
     *
     *          - Select a User From the the List to:
     *              - SEND MONEY (USE CASE 4)
     *                  - UPDATES the CURRRENT USER'S ACCOUNT BALANCE (Subtracts)
     *                  - UPDATES the RECIPIENT'S ACCOUNT BALANCE (Adds)
     *                  - CREATES a TRANSFER object with its properties:
     *                      transfer_status_id = 2 (transfer is APPROVED)
     *                      transfer_type_id = 2 (the transfer is a SEND type)
     *                      - Transfer Object is POSTed to the DAO Controller via RestTemplate
     *
     *              - REQUEST MONEY (USE CASE 7)
     *                  - CREATES a TRANSFER object with its properties:
     *                       transfer_status_id = 1 (transfer is PENDING approval)
     *                       transfer_type_id = 1 (the transfer is a REQUEST type)
     *                       - Transfer Object is POSTed to the DAO Controller via RestTemplate
     *
     *          - View a List of ALL the CURRENT USER'S Transfers (USE CASE 5)
     *              - Select a Transfer from the Transfer List using the Transfer ID and view its contents
     *
     *          - View a List of the CURRENT USER's PENDING TRANSFER Requests (USE CASE 8)
     *              - The List is of user's who REQUESTED MONEY FROM THE CURRENT USER
     *              - SELECT a PENDING TRANSFER REQUEST to APPROVE or REJECT or NEITHER
     *
     *      USER SHOULD NOTS:
     *          - SELECT THEMSELVES WHEN REQUESTING AND SENDING MONEY
     *          - SEND MONEY GREATER THAN THEIR BALANCE
     *          - CAN'T SEND OR REQUEST a ZERO OR NEGATIVE AMOUNT
     **************************************************************************************************************/

    // ACQUIRING THE CURRENT USER'S ACCOUNT
    public void setMyAccount(int currentUserID) {
        try {
            ResponseEntity<Account> response = restTemplate.exchange(accountsUrl + "user/" + currentUserID, HttpMethod.GET, makeAuthEntity(), Account.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() != null) {
                    myAccount = response.getBody();
                }
            } else {
                System.err.println("Error getting content. HTTP Status: " + response.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            System.err.println("Error getting content balance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UPDATING THE CURRENT USER'S ACCOUNT BALANCE
    public void updateMyAccountBalance(BigDecimal newBalance) {
        myAccount.setBalance(newBalance);
        try {
            restTemplate.put(
                    accountsUrl + myAccount.getUserID() + "/updateBalance", makeAccountEntity(myAccount));
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }
    }

    public Account getMyAccount() {
        return myAccount;
    }

    // GETTING THE CURRENT USER'S ACCOUNT BALANCE
    public BigDecimal getMyAccountBalance() {
        return myAccount.getBalance();
    }

    /**********************************************
     *  CURRENT USER ACCOUNT IS DONE
     *  FINDING OTHER USERS' ACCOUNTS BELOW
     **********************************************/
    // SEARCHES FOR AN ACCOUNT BY USER ID | Used when Sending Money to find the Recipient's Account
    public void searchAndSetForAccountByUserID(int userID) {
            try {
                ResponseEntity<Account> accountResponse = restTemplate.exchange(accountsUrl + "user/" + userID, HttpMethod.GET, makeAuthEntity(), Account.class);
                this.othersAccount = accountResponse.getBody();
            } catch (RestClientResponseException e) {
                System.err.println("Error getting content! Http Status Code: " + e.getStatusText());
                e.printStackTrace();
            }
    }

    // SEARCHES FOR AN ACCOUNT BY THE ACCOUNT ID
    public void searchAndSetForAccountByAcctID(int accountID) {
        try {
            ResponseEntity<Account> accountResponse = restTemplate.exchange(accountsUrl + accountID, HttpMethod.GET, makeAuthEntity(), Account.class);
            this.othersAccount = accountResponse.getBody();
        } catch (RestClientResponseException e) {
            System.err.println("Error getting content! Http Status Code: " + e.getStatusText());
            e.printStackTrace();
        }
    }

    // UPDATES NON-CURRENT USER ACCOUNT BALANCE | Used when Sending Money to a Recipient or Money is sent to Current User
    public void updateOtherUsersAccountBalance(BigDecimal newBalance) {
        othersAccount.setBalance(newBalance);
        try {
            restTemplate.put(
                    accountsUrl + othersAccount.getUserID() + "/updateBalance", makeAccountEntity(othersAccount));
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }
    }

    /*public BigDecimal getMyAccountBalance(){
        BigDecimal balance = BigDecimal.ZERO; // Default to zero in case of errors

        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("userID", myAccount.getUserID());

        try {
            ResponseEntity<BigDecimal> balanceResponse = restTemplate.exchange(accountsUrl + myAccount.getUserID() + "/getBalance" , HttpMethod.GET, makeAuthEntity(), BigDecimal.class, uriVariables);
            if (balanceResponse.getStatusCode().is2xxSuccessful()) {
                if (balanceResponse.getBody() != null) {
                    balance = balanceResponse.getBody();
                }
            } else {
                System.err.println("Error getting account balance. HTTP Status: " + balanceResponse.getStatusCode());
            }
        } catch (RestClientResponseException e) {
            System.err.println("Error getting account balance: " + e.getMessage());
            e.printStackTrace();
        }

        return balance;
    }*/

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

    public void createNewTransfer(int accountFromID, int accountToID, BigDecimal amount, int transferStatusID, int transferTypeID) {
        newTransferEntry.setAccountFromID(accountFromID);
        newTransferEntry.setAccountToID(accountToID);
        newTransferEntry.setAmount(amount);
        newTransferEntry.setTransferStatusID(transferStatusID);
        newTransferEntry.setTransferTypeID(transferTypeID);
    }

    public void sendMoney() {
        int transferStatusID, transferTypeID;
        transferStatusID = 2; //Approved
        transferTypeID = 2; //Send

        BigDecimal amount;

        showUsers();

        //NEEDS DEFENSIVE PROGRAMMING HERE (loops, try-catch, etc) TO STOP USER FROM DOING INVALID INPUTS -GL
        System.out.println("Enter ID of user you are sending to (0 to cancel):");
        searchAndSetForAccountByUserID(Integer.parseInt(sc.nextLine()));
        while (othersAccount.equals(myAccount)) {
            System.out.println("Error! You CANNOT send money to yourself!");
            System.out.println("Enter ID of user you are sending to (0 to cancel):");
            searchAndSetForAccountByUserID(Integer.parseInt(sc.nextLine()));
        }
        System.out.println("Enter amount:");
        amount = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));

        int chkGreaterLessThanZero = amount.compareTo(BigDecimal.valueOf(0));
        int chkAmountGreaterThanMyAcctBalance = amount.compareTo(myAccount.getBalance());

        while (chkGreaterLessThanZero <= 0 || chkAmountGreaterThanMyAcctBalance > 0) {
            System.out.println("*****************************************************************");
            System.out.println("|                  Error! Invalid amount!                       |");
            System.out.println("|    No Zeros! No Negatives! Only Send What You Can Afford!     |");
            System.out.println("*****************************************************************\n");

            System.out.println("Enter ID of user you are sending to (0 to cancel):");
            System.out.println("Enter amount:");
            amount = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));
            chkAmountGreaterThanMyAcctBalance = amount.compareTo(myAccount.getBalance());
        }
        //                   Senders AcctID   |  Rcpt AcctID  |  $  |  Approve, Deny, Pending  |   Receive(1) or Send(2)
        createNewTransfer(myAccount.getAccountID(), othersAccount.getAccountID(), amount, transferStatusID, transferTypeID);

        //Should create and add a new Transfer entry in the Database
        addTransferToDatabase();
        BigDecimal myNewBalance = myAccount.getBalance().subtract(amount);
        BigDecimal recipientNewBalance = myAccount.getBalance().add(amount);

        updateMyAccountBalance(myNewBalance);
        updateOtherUsersAccountBalance(recipientNewBalance);
    }

    public void addTransferToDatabase() {
        try {
            restTemplate.exchange(transfersUrl, HttpMethod.POST, makeTransferEntity(newTransferEntry), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void requestMoney() {
        int accountToID, transferStatusID, transferTypeID, receiversUserID;
        BigDecimal amount;
        transferStatusID = 1; //Pending
        transferTypeID = 1; //Request

        showUsers();

        System.out.println("Enter ID of user you are requesting from (0 to cancel):");
        searchAndSetForAccountByUserID(Integer.parseInt(sc.nextLine()));
        System.out.println("Enter amount:");
        amount = BigDecimal.valueOf(Double.parseDouble(sc.nextLine()));

        //                   Senders AcctID   |  Rcpt AcctID  |  $  |  Approve, Deny, Pending  |   Receive(1) or Send(2)
        createNewTransfer(othersAccount.getAccountID(), myAccount.getAccountID(), amount, transferStatusID, transferTypeID);

        //Should create and add a new Transfer entry in the Database
        addTransferToDatabase();
    }


    /****************************************************************
     * STEP 5 View Only the Logged In User's Transfer History
     *  -Needs to be tested -GL
     *  -Needs the loop to print it out in the right format provided -GL
     *      in the README
     *      - I should've made the SQL (server-side transfer JDBC)
     *      only SELECT entries that had the Logged In User's accountID -GL
     * ***************************************************************/
    public void viewMyTransfers() {
        setTransfersList();
        System.out.println("--------------------------------------------------------------");
        System.out.println("Transfer Details");
        System.out.printf("%-15s %-25s %-25s", "ID", "From/To:",  "Amount");
        System.out.println("\n--------------------------------------------------------------");
        for (Transfer transfer : transfersList) {
            String fromTo, username;
            switch (transfer.getTransferTypeID()) {
                case 1:
                    fromTo = "From: ";
                    for (User user : usersList) {
                        searchAndSetForAccountByAcctID(transfer.getAccountFromID());
                        if (user.getId() == othersAccount.getUserID()) {
                            username = user.getUsername();
                            System.out.printf("%-15s %-25s %-25s,", transfer.getTransferID(), fromTo, username, "$ " + transfer.getAmount());
                        }
                    }
                    break;
                case 2:
                    fromTo = "To: ";
                    for (User user : usersList) {
                        searchAndSetForAccountByAcctID(transfer.getAccountToID());
                        if (user.getId() == othersAccount.getUserID()) {
                            username = user.getUsername();
                            System.out.printf("%-15s %-10s -%25s %-35s", transfer.getTransferID(), fromTo, username, "$ " + transfer.getAmount());
                        }
                    }
                    break;
            }
        }
    }

    public String findUsernameByAccountID(int accountID){
        User user = null;
        try{
            ResponseEntity<User> response =
                    restTemplate.exchange(accountsUrl + "getUserByAcctID/" + accountID, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user.getUsername();
    }

    /*********************************
    * STEP 6 SELECT TRANSFER ID
    **********************************/
    public void viewTransferInfoByID(){
        viewMyTransfers();
        System.out.println("Please enter Transfer ID to view details (0 to cancel):");
        int transferID = Integer.parseInt(sc.nextLine());
        boolean flag = true;

        while (flag){
            for (Transfer transfer : transfersList){
                if(transferID == transfer.getTransferID()){
                    String type, status;
                    System.out.println("ID: " + transfer.getTransferID());
                    System.out.println("From: " + findUsernameByAccountID(transfer.getAccountFromID()));
                    System.out.println("To: " + findUsernameByAccountID(transfer.getAccountFromID()));
                    switch (transfer.getTransferTypeID()) {
                        case 1:
                            type = "Request";
                            System.out.println("Type: " + type);
                            break;
                        case 2:
                            type = "Send";
                            System.out.println("Type: " + type);
                            break;
                    }
                    switch (transfer.getTransferStatusID()){
                        case 1:
                            status = "Pending";
                            System.out.println("Status: " + status);
                            break;
                        case 2:
                            status = "Approved";
                            System.out.println("Status: " + status);
                            break;
                        case 3:
                            status = "Rejected";
                            System.out.println("Status: " + status);
                            break;
                    }
                    System.out.println("Amount: $" + transfer.getAmount());
                    flag = false;
                    break;
                }
            }
            if(flag) {
                System.out.println("*************************************************");
                System.out.println("| Error! Invalid input! Transfer was not found! |");
                System.out.println("*************************************************\n");
                System.out.println("Please enter Transfer ID to view details (0 to cancel):");
                transferID = Integer.parseInt(sc.nextLine());
            }
        }
    }

    /************************************************************************
     * STEP 8 The Logged-in User Views THEIR PENDING Transfers
     *   -Needs the loop to print out the transfer list -GL
     *   -Needs to be tested -GL
     ************************************************************************/
    public Transfer[] viewMyPendingTransfers() {
        Transfer[] pendingTransfers = null;
        try {
            ResponseEntity<Transfer[]> myTransfers =
                    restTemplate.exchange(transfersUrl + "/viewMyTransfersByStatus=" + 1, HttpMethod.GET,
                            makeAccountEntity(myAccount), Transfer[].class);
            pendingTransfers = myTransfers.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        System.out.println("------------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.printf("%-10s %-25s %-25s", "ID", "To", "Amount");
        System.out.println("------------------------------------------------");
        for (Transfer transfer : pendingTransfers) {
            System.out.printf("%-10s %-25s %-25s", transfer.getTransferID(), findUsernameByAccountID(transfer.getAccountFromID()), "$ " + transfer.getAmount());
        }
        System.out.println();
        return pendingTransfers;
    }

    public void updateTransferStatus(){
        Transfer[] pendingTransfer = viewMyPendingTransfers();
        Transfer transferToUpdate;
        System.out.println("Please enter transfer ID to approve/reject (0 to cancel):");
        int userInput = Integer.parseInt(sc.nextLine());
        for (Transfer transfer : pendingTransfer){
            if(transfer.getTransferID() == userInput){
                transferToUpdate = transfer;
                System.out.println("\nPlease enter Transfer ID to approve/reject (0 to cancel): ");
                switch (userInput){
                    case 1:
                        transfer.setTransferStatusID(2);
                        try {
                            restTemplate.put(
                                    transfersUrl + transferToUpdate.getTransferID() + "/updateTransfer", makeTransferEntity(transfer));
                        } catch (ResourceAccessException e) {
                            System.out.println(e.getMessage());
                        } catch (RestClientResponseException e) {
                            System.out.println(e.getRawStatusCode());
                        }
                        break;
                    case 2:
                        transfer.setTransferStatusID(3);
                        try {
                            restTemplate.put(
                                    transfersUrl + transferToUpdate.getTransferID() + "/updateTransfer", makeTransferEntity(transfer));
                        } catch (ResourceAccessException e) {
                            System.out.println(e.getMessage());
                        } catch (RestClientResponseException e) {
                            System.out.println(e.getRawStatusCode());
                        }
                        break;
                    case 0:
                        transfer.setTransferStatusID(1);
                        try {
                            restTemplate.put(
                                    transfersUrl + transferToUpdate.getTransferID() + "/updateTransfer", makeTransferEntity(transfer));
                        } catch (ResourceAccessException e) {
                            System.out.println(e.getMessage());
                        } catch (RestClientResponseException e) {
                            System.out.println(e.getRawStatusCode());
                        }
                        break;
                }
            }
        }

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
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        return entity;
    }

    private HttpEntity<User> makeUserEntity(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return entity;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }


}
