package com.revolut.hiring.jaxrs.response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.revolut.hiring.bean.BankAccountTransactionInfo;

public class TransactionInfoResponse {

    private String status;
    private String time;
    private Map<String, String> request = new HashMap<>();
    private List<AccountTransactionResponse> txns = new LinkedList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, String> getRequest() {
        return request;
    }

    public void addToRequestMap(String key, String value) {
        if (key != null) {
            request.put(key, value);
        }
    }

    public List<AccountTransactionResponse> getTxns() {
        return txns;
    }

    public void addTxns(List<BankAccountTransactionInfo> txns) {
        if (txns.isEmpty()) {
            return;
        }
        final List<AccountTransactionResponse> transactions = txns.stream().map(AccountTransactionResponse::new)
                .collect(Collectors.toList());
        this.txns.addAll(transactions);
    }
}
