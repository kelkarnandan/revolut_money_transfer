package com.revolut.hiring.jaxrs.bean;

import com.revolut.hiring.bean.BankAccountTransactionInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionInfoResponse {

    private String status;
    private String time;
    private Map<String, String> request = new HashMap<>();
    private List<AccountTransaction> txns = new LinkedList<>();

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
        if (key!=null) {
            this.request.put(key, value);
        }
    }

    public List<AccountTransaction> getTxns() {
        return txns;
    }

    public void addTxns(List<BankAccountTransactionInfo> txns) {
        if (txns.isEmpty()) return;
        final List<AccountTransaction> transactions = txns.stream().map(AccountTransaction::new).collect(Collectors.toList());
        this.txns.addAll(transactions);
    }
}
