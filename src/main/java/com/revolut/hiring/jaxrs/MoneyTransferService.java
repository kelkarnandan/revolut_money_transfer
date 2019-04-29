package com.revolut.hiring.jaxrs;

import static com.revolut.hiring.util.DateUtil.getDateString;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

import java.text.DecimalFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.hiring.exceptions.InsufficientFundsException;
import com.revolut.hiring.jaxrs.request.AccountCreditRequest;
import com.revolut.hiring.jaxrs.request.AccountDebitRequest;
import com.revolut.hiring.jaxrs.request.MoneyTransferRequest;
import com.revolut.hiring.jaxrs.response.AccountCreditResponse;
import com.revolut.hiring.jaxrs.response.AccountDebitResponse;
import com.revolut.hiring.jaxrs.response.MoneyTransferResponse;
import com.revolut.hiring.service.AccountTransactionService;

@Path("account/money")
public class MoneyTransferService {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransferService.class);
    private final AccountTransactionService txnService = new AccountTransactionService();

    @POST
    @Path("/credit")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response credit(AccountCreditRequest request) {
        AccountCreditResponse entity = new AccountCreditResponse();
        entity.setTime(getDateString(new Date()));
        entity.setRequest(request);

        if (!isAmountValid(request.getAmount())) {
            entity.setStatus("Invalid Amount in Request");
            return Response.status(BAD_REQUEST).entity(entity).build();
        }

        try {
            long txnId = txnService.credit(request.getAccount(), request.getAmount());
            entity.setTxnId(txnId);
            entity.setStatus("Success");
        }
        catch (IllegalArgumentException cause) {
            entity.setStatus(cause.getMessage());
            return Response.status(BAD_REQUEST).entity(entity).build();

        }
        catch (Throwable cause) {
            logger.error("Error in credit", cause);
            entity.setStatus("Failed :: " + cause.getMessage());
            return Response.serverError().entity(entity).build();
        }

        return Response.ok().entity(entity).build();
    }

    @POST
    @Path("/debit")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response debit(AccountDebitRequest request) {
        AccountDebitResponse entity = new AccountDebitResponse();
        entity.setTime(getDateString(new Date()));
        entity.setRequest(request);

        if (!isAmountValid(request.getAmount())) {
            entity.setStatus("Invalid Amount in Request");
            return Response.status(BAD_REQUEST).entity(entity).build();
        }

        try {
            long txnId = txnService.debit(request.getAccount(), request.getAmount());
            entity.setTxnId(txnId);
            entity.setStatus("Success");
        }
        catch (InsufficientFundsException cause) {
            entity.setStatus(cause.getMessage());
            return Response.status(FORBIDDEN).entity(entity).build();

        }
        catch (IllegalArgumentException cause) {
            entity.setStatus(cause.getMessage());
            return Response.status(BAD_REQUEST).entity(entity).build();

        }
        catch (Throwable cause) {
            logger.error("Error in debit", cause);
            entity.setStatus("Failed :: " + cause.getMessage());
            return Response.serverError().entity(entity).build();
        }

        return Response.ok().entity(entity).build();
    }

    @POST
    @Path("/transfer")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response transfer(final MoneyTransferRequest request) {
        final MoneyTransferResponse entity = new MoneyTransferResponse();
        entity.setRequest(request);
        entity.setTime(getDateString(new Date()));

        if (request.getFromAccount() == request.getToAccount()) {
            entity.setStatus("Both Accounts are same");
            return Response.status(BAD_REQUEST).entity(entity).build();
        }
        if (!isAmountValid(request.getAmount())) {
            entity.setStatus("Invalid Amount in Request");
            return Response.status(BAD_REQUEST).entity(entity).build();
        }

        try {
            long txnId = txnService.transfer(request.getFromAccount(), request.getToAccount(),
                    request.getAmount());
            entity.setTxnId(txnId);
            entity.setStatus("Success");

        }
        catch (InsufficientFundsException cause) {
            entity.setStatus(cause.getMessage());
            return Response.status(FORBIDDEN).entity(entity).build();

        }
        catch (IllegalArgumentException cause) {
            entity.setStatus(cause.getMessage());
            return Response.status(BAD_REQUEST).entity(entity).build();

        }
        catch (Throwable cause) {
            logger.error("Error in money transfer", cause);
            entity.setStatus("Failed :: " + cause.getMessage());
            return Response.serverError().entity(entity).build();
        }

        return Response.status(ACCEPTED).entity(entity).build();
    }

    private boolean isAmountValid(Double amount) {
        boolean isValid = true;

        if (amount == null || amount.isNaN()) {
            isValid &= false;
        }

        if (isValid) {
            double d2 = amount % 1;
            final DecimalFormat df = new DecimalFormat("#.00");
            int decimalLength = (df.format(d2).length() - 1);
            if (decimalLength != 2) {
                isValid &= false;
            }
        }

        return isValid;
    }
}
