# revolut_money_transfer
Revolut hiring exercise of a RESTful API (including data model and the backing implementation) for money transfers between accounts. 
No Framework plain Java. The data store runs in-memory for testing.

Post clone, build the project and run via embedded Tomcat pluggin
```
mvn clean install
mvn tomcat7:run
```
Currently, the api will on port 8091

For testing money transfer, we'll first we need to create at least 2 account.
We can create accounts with following currencies only:
1. USD
2. GBP
3. EUR
4. INR
5. YEN
6. SGD

In case, one tries to create account with currency other than above ones, will get an BAD REQUEST status in response.

*Note, all POST API expects Content-Type, Accept headers with value "application/json".

Create Account API:
```
- curl -X POST http://localhost:8091/revolut/account/create/<currency>

Sample: 
- Request : in this case used GBP
  curl -X POST http://localhost:8091/revolut/account/create/GBP
- Response:
  {"message":"Created Account #2","time":"2019-04-30T11:24:08"}
```

Accounts info API:
```
- curl -X GET  http://localhost:8091/revolut/account/info/all
- curl -X GET  http://localhost:8091/revolut/account/info/{account_number}

Sample: 
- Request
curl -X GET  http://localhost:8091/revolut/account/info/all
- Respnse : 
[{"accountNumber":2,"balance":0.0,"currency":"GBP","creationDate":"2019-04-30T11:22:09"},{"accountNumber":3,"balance":0.0,"currency":"GBP","creationDate":"2019-04-30T11:22:36"},{"accountNumber":4,"balance":0.0,"currency":"GBP","creationDate":"2019-04-30T11:24:08"}]
```

Delete Account API:
```
- curl -X DELETE  http://localhost:8091/revolut/account/delete/<account_number>

Sample : 
- curl -X DELETE  http://localhost:8091/revolut/account/delete/4
- {"message":"Deleted Account #4","time":"2019-05-01T01:36:27"}
```



Once the accounts are create, we can request for following money exchange operation:
1. Credit into account
2. Debit from account
3. Money Transfer between accounts

Money Credit API:
```
- curl -X POST http://localhost:8091/revolut/account/money/credit -H 'content-type: application/json' -d '{"account" : {account_number}, "amount" : {amount} }'

Sample:
curl -X POSent-type: application/json' -d '{"account" : 2 , "amount" : 100 }'
{"txnId":1,"status":"Success","time":"2019-05-01T01:42:15","request":{"account":2,"amount":100.0}}

```

Money Debit API:
```
- curl -X POST http://localhost:8091/revolut/account/money/credit -H 'content-type: application/json' -d '{"account" : {account_number}, "amount" : {amount} }'

Sample :
 curl -X POST http://localhost:8091/revolut/account/money/debit -H 'conte
nt-type: application/json' -d '{"account" : 2 , "amount" : 20 }'
{"txnId":2,"status":"Success","time":"2019-05-01T01:44:23","request":{"account":2,"amount":20.0}}
```

Money Transfer API:
```
curl -X POST http://localhost:8091/revolut/account/money/transfer -H 'content-type: application/json' -d '{"fromAccount" : {source_account_number} , "toAccount" : {destination_account_number}, "amount" : {amount} }'

Sample : 

curl -X POST http://localhost:8091/revolut/account/money/transfer -H 'content-type: application/json' -d '{"fromAccount" : 2 , "toAccount" : 3, "amount" : 30 }'
{"txnId":3,"status":"Success","time":"2019-05-01T01:45:38","request":{"fromAccount":2,"toAccount":3,"amount":30.0}}

```

*NOTE, Fx rate are hard-coded for money transfer between accounts having different currencies.

For eg, user want to transfer $100 to account that deals in EUR currency. 
So here Fx rate conversion will take place internally.

Once the users has made some transactions, we can verify the balances of account using above Account Info APIs.
And if user want accounts transactions details then we have following APIs
Account Transaction Info APIs:
```
- GET, {server}:8091/revolut/account/txn/{account_number}
- GET, {server}:8091/revolut/account/txn/{account_number}/{txn_id}
- GET, {server}:8091/revolut/account/txn/{account_number}?from={dd-MM-yyyy}
- GET, {server}:8091/revolut/account/txn/{account_number}?from={dd-MM-yyyy}&to={dd-MM-yyyy}
```
