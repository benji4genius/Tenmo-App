package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public List<Transfer> getTransfers(){

    }

    public Transfer getTransferByID(int id){

    }

    public List<Transfer> getTransfersByAccountFrom(int accountFrom){

    }

    public List<Transfer> getTransfersByAccountTo(int accountTo){

    }

    public List<Transfer> getTransfersByStatus(String statusType){

    }

    public Transfer createTransfer(Transfer transfer){

    }

}
