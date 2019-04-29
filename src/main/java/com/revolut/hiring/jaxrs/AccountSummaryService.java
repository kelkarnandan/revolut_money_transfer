package com.revolut.hiring.jaxrs;

import static com.revolut.hiring.util.DateUtil.getDateString;
import static javax.ws.rs.core.Response.Status.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.hiring.bean.BankAccountInfo;
import com.revolut.hiring.jaxrs.response.AccountCreateDeleteResponse;
import com.revolut.hiring.jaxrs.response.AccountInfoResponse;
import com.revolut.hiring.service.BankAccountService;

@Path("account")
public class AccountSummaryService {

    private final Logger logger = LoggerFactory.getLogger(AccountSummaryService.class);

    private final BankAccountService accountService = new BankAccountService();

    @GET
    @Path("/info/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllAccounts() {

        final List<BankAccountInfo> accounts = accountService.getAccountsInfo();

        if (accounts.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok().entity(
                accounts.stream().map(AccountInfoResponse::new).collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/info/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountInfo(@PathParam("id") long id) {
        final BankAccountInfo accountInfo = accountService.getAccountInfo(id);

        if (accountInfo == null) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok().entity(new AccountInfoResponse(accountInfo)).build();
    }

    @POST
    @Path("/create/{currency: [A-Z]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(@PathParam("currency") String currency) {
        logger.info("Got request to create account with currency " + currency);
        AccountCreateDeleteResponse entity = new AccountCreateDeleteResponse();
        entity.setTime(getDateString(new Date()));

        try {
            final BankAccountInfo account = accountService.createAccount(currency);
            entity.setMessage(String.format("Created Account #%d", account.getAccountNumber()));
            return Response.ok().entity(entity).build();

        }
        catch (UnsupportedOperationException e) {
            logger.error("Error creating account", e);
            entity.setMessage(e.getMessage());
            return Response.status(BAD_REQUEST).entity(entity).build();

        }
        catch (Throwable cause) {
            logger.error("Error creating account", cause);
            entity.setMessage("Failed to create account");
            return Response.serverError().entity(entity).build();
        }
    }

    @DELETE
    @Path("/delete/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("id") long id) {
        logger.info("Got request to delete account " + id);
        AccountCreateDeleteResponse entity = new AccountCreateDeleteResponse();
        entity.setTime(getDateString(new Date()));

        final BankAccountInfo accountInfo = accountService.getAccountInfo(id);
        if (accountInfo != null && accountInfo.isActive()) {
            logger.info("Deleting account " + id);
            accountService.deleteAccount(id);
            String msg = String.format("Deleted Account #%d", accountInfo.getAccountNumber());
            entity.setMessage(msg);
            return Response.ok().entity(entity).build();
        }

        String msg = String.format("Account %d is inactive", id);
        entity.setMessage(msg);
        return Response.status(BAD_REQUEST).entity(entity).build();
    }

}
