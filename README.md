# revolut_money_transfer
Revolut hiring exercise of a RESTful API (including data model and the backing implementation) for money transfers between accounts. 
No Framework plain Java. The data store runs in-memory for testing.

Post clone, build the project. This project contains embeded tomcat for local testing. Also war is generated which can be deployed to server. 
```
mvn clean install
mvn tomcat7:run <!-- only for local embeded tomcat server-->
```
Assuming local run, the api will run on port 8091 configured in pom.xml <build> section
  
For external server, use revolut-money-transfer.war. In this case, host url will be <hostname>:<port>/revolut-money-transfer/

*NOTE 
```
- all POST API expects Content-Type, Accept headers with value "application/json".
- Fx rate are hard-coded for money transfer between accounts having different currencies.
- For eg, user want to transfer $100 to account that deals in GBP currency. So 
  here Fx rate conversion will take place internally. GBP Account will be credited with amount GBP 77.
- <server> -> http://localhost:8080
- <app-name> -> revolut-money-transfer
```

For testing money transfer, we'll first create account with below currency only: 
1. USD
2. GBP
3. EUR
4. INR
5. YEN
6. SGD
In case, one tries to create account with currency other than above ones, will get an BAD REQUEST status in response.

Create Account API:
```
- curl -X POST <server>/<app-name>/revolut/account/create/<currency>

Sample: 
- Request : in this case used GBP
  curl -X POST http://localhost:8080/revolut-money-transfer/revolut/account/create/GBP
- Response:
  {"message":"Created Account #2","time":"2019-04-30T11:24:08"}
```

Accounts info API:
```
- curl -X GET  <server>/<app-name>/revolut/account/info/all
- curl -X GET  <server>/<app-name>/revolut/account/info/{account_number}

Sample: 
- Request
curl -X GET  http://localhost:8080/revolut-money-transfer/revolut/account/info/all
- Respnse : 
[{"accountNumber":2,"balance":0.0,"currency":"GBP","creationDate":"2019-04-30T11:22:09"},
{"accountNumber":3,"balance":0.0,"currency":"GBP","creationDate":"2019-04-30T11:22:36"},
{"accountNumber":4,"balance":0.0,"currency":"GBP","creationDate":"2019-04-30T11:24:08"}]
```

Delete Account API:
```
- curl -X DELETE  <server>/<app-name>/revolut/account/delete/<account_number>

Sample : 
- curl -X DELETE  http://localhost:8080/revolut-money-transfer/revolut/account/delete/4
- {"message":"Deleted Account #4","time":"2019-05-01T01:36:27"}
```

Once the accounts are created, we can request for following money exchange operation:
1. Credit into account
2. Debit from account
3. Money Transfer between accounts

Money Credit API:
```
- curl -X POST <server>/<app-name>/revolut/account/money/credit 
-H 'content-type: application/json' -d '{"account" : {account_number}, "amount" : {amount} }'

Sample:
curl -X POST http://localhost:8080/revolut-money-transfer/revolut/account/money/credit 
-d '{"account" : 2 , "amount" : 100 }'
{"txnId":1,"status":"Success","time":"2019-05-01T01:42:15","request":{"account":2,"amount":100.0}}

```

Money Debit API:
```
- curl -X POST <server>/<app-name>/revolut/account/money/debit 
-H 'content-type: application/json' -d '{"account" : {account_number}, "amount" : {amount} }'

Sample :
 curl -X POST http://localhost:8091/revolut/account/money/debit -H 'conte
nt-type: application/json' -d '{"account" : 2 , "amount" : 20 }'
{"txnId":2,"status":"Success","time":"2019-05-01T01:44:23","request":{"account":2,"amount":20.0}}
```

Money Transfer API:
```
curl -X POST <server>/<app-name>/revolut/account/money/transfer 
-H 'content-type: application/json' 
-d '{"fromAccount" : {source_account_number} , "toAccount" : {destination_account_number}, "amount" : {amount} }'

Sample : 

curl -X POST http://localhost:8080/revolut-money-transfer/revolut/account/money/transfer -H 'content-type: application/json' 
-d '{"fromAccount" : 2 , "toAccount" : 3, "amount" : 30 }'

{"txnId":3,"status":"Success","time":"2019-05-01T01:45:38",
"request":{"fromAccount":2,"toAccount":3,"amount":30.0}}

```
Once the users has made some transactions, we can verify the balances of account using above Account Info APIs.

And if user want accounts transactions details then we have following APIs:

Account Transaction Info APIs:
```
- curl -X <server>/<app-name>/revolut/account/txn/{account_number}
- curl -X <server>/<app-name>/revolut/account/txn/{account_number}/{txn_id}
- curl -X <server>/<app-name>/revolut/account/txn/{account_number}?from={dd-MM-yyyy}
- curl -X <server>/<app-name>/revolut/account/txn/{account_number}?from={dd-MM-yyyy}&to={dd-MM-yyyy}

Sample : 

curl -X GET  http://localhost:8080/revolut-money-transfer/revolut/account/txn/2?from="29-04-2018"
[{"txnId":1,"accountId":2,"txnType":"CREDIT","amount":100.0,"txnDate":"2019-05-01T01:42:15"},
{"txnId":2,"accountId":2,"txnType":"DEBIT","amount":20.0,"txnDate":"2019-05-01T01:44:23"},
{"txnId":3,"accountId":2,"txnType":"DEBIT","amount":30.0,"txnDate":"2019-05-01T01:45:38"}]
```
