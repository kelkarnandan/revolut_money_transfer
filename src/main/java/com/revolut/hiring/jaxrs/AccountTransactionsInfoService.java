package com.revolut.hiring.jaxrs;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.bean.BankAccountTransactionInfo;
import com.revolut.hiring.dao.AccountTxnDataService;
import com.revolut.hiring.jaxrs.bean.AccountTransaction;
import com.revolut.hiring.service.BankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.revolut.hiring.util.DateUtil.getDate;
import static com.revolut.hiring.util.DateUtil.getDateString;

@Path("account/txn")
public class AccountTransactionsInfoService {

    private static final Logger logger = LoggerFactory.getLogger(AccountTransactionsInfoService.class);
    private static final String QUERY_PARAM_DATE_FORMAT = "dd-MM-yyyy";

    private final BankAccountService accountService = new BankAccountService();
    private final AccountTxnDataService txnDataService = AccountTxnDataService.getInstance();

    @GET
    @Path("{acntid: [0-9]+}/{txnid: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTxnDetails(@PathParam("acntid") long accountId, @PathParam("txnid") long txnId) {

        logger.info("Received get txn request for account :: {}, txn id :: {}", accountId, txnId);
        final BankAccountInfo accountInfo = accountService.getAccountInfo(accountId);
        if (accountInfo != null) {
            final List<BankAccountTransactionInfo> accntTxns = txnDataService.getAllTransactions(accountId);
            for(BankAccountTransactionInfo accntTxn : accntTxns) {
                if (txnId == accntTxn.getId()) {
                    AccountTransaction txn = new AccountTransaction(accntTxn);
                    return Response.ok().entity(txn).build();
                }
            }
        }

        logger.error("Found no txn for account :: {}, txn id :: {}", accountId, txnId);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTxnsDetails(@PathParam("id") String accntId,
                                   @QueryParam("from") String fromDate, @QueryParam("to") String endDate) {

        List<AccountTransaction> txns = new LinkedList<>();
        if (accntId == null || accntId.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).build();

        final long accountId = Long.parseLong(accntId);

        if (fromDate == null && endDate == null) {
            txns.addAll(getTxnDetails(accountId).stream().map(AccountTransaction::new).collect(Collectors.toList()));

        } else if (fromDate != null) {
            try{

                final List<BankAccountTransactionInfo> txnDetails = new LinkedList<>();
                if(endDate == null) {
                    txnDetails.addAll(getTxnDetails(accountId, getDate(fromDate,QUERY_PARAM_DATE_FORMAT)));
                } else {
                    final Date from = getDate(fromDate, QUERY_PARAM_DATE_FORMAT);
                    final Date to = getDate(endDate, QUERY_PARAM_DATE_FORMAT);
                    if (from.after(to)) return Response.status(Response.Status.BAD_REQUEST).build();

                    txnDetails.addAll(getTxnDetails(accountId, from, to));
                }
                txns.addAll(txnDetails.stream().map(AccountTransaction::new).collect(Collectors.toList()));

            } catch (DateTimeParseException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        if (txns.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok().entity(txns).build();
    }

    private List<BankAccountTransactionInfo> getTxnDetails(long accountId, Date fromDate, Date endDate) {
        logger.info("Received get txns request for account :: {}, start date :: {}, end date :: {}", accountId, getDateString(fromDate, "dd-MM-yyyy"), getDateString(endDate, "dd-MM-yyyy"));

        final BankAccountInfo accountInfo = accountService.getAccountInfo(accountId);
        if (accountInfo != null) {
            return txnDataService.getAllTransactions(accountInfo.getAccountNumber(), fromDate, endDate);
        }

        logger.error("Found no txns for account :: {}, start date :: {}, end date :: {}", accountId, getDateString(fromDate, "dd-MM-yyyy"), getDateString(endDate, "dd-MM-yyyy"));
        return Collections.emptyList();
    }

    private List<BankAccountTransactionInfo> getTxnDetails(long accountId) {
        logger.info("Received get txns request for account :: {}", accountId);
        final BankAccountInfo accountInfo = accountService.getAccountInfo(accountId);
        if (accountInfo != null) {
            return txnDataService.getAllTransactions(accountId);
        }

        logger.error("Found no txns for account :: {}", accountId);
        return Collections.emptyList();
    }

    private List<BankAccountTransactionInfo> getTxnDetails(long accountId, Date fromDate) {
        logger.info("Received get txns request for account :: {} and start date :: {}", accountId, getDateString(fromDate, "dd-MM-yyyy"));
        final BankAccountInfo accountInfo = accountService.getAccountInfo(accountId);
        if (accountInfo != null) {
            return txnDataService.getAllTransactions(accountInfo.getAccountNumber(), fromDate);
        }

        logger.error("Found no txns for account :: {}, start date :: {}", accountId, getDateString(fromDate, "dd-MM-yyyy"));
        return Collections.emptyList();
    }
}
