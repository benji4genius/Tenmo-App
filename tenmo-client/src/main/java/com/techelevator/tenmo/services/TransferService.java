package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public List<Transfer> getTransfers(){
        List<Transfer> transfers = new ArrayList<>();
        try {
            //transfers = restTemplate.exchange("http://localhost:8080/");
        } catch (Exception e) {

        }


        return transfers;
    }

    public Transfer getTransferByID(int id){
        return null;
    }

    public List<Transfer> getTransfersByAccountFrom(int accountFrom){
        return null;
    }

    public List<Transfer> getTransfersByAccountTo(int accountTo){
        return null;
    }

    public List<Transfer> getTransfersByStatus(String statusType){
        return null;
    }

    public Transfer createTransfer(Transfer transfer){
        return null;
    }



}
