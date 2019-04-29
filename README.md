# revolut_money_transfer
Revolut hiring exercise of a RESTful API (including data model and the backing implementation) for money transfers between accounts. 
No Framework plain Java
The data store runs in-memory for testing.

Post clone, build the project and run via embedded Tomcat pluggin
```
mvn clean install
mvn tomcat7:run
```
Currently, the api will on port 8091

For testing money transfer, we'll first we need to create at least 2 account.
We can create accounts with following currencies only:
1. USD
2. EUR
3. INR
4. YEN
5. SGD

In case, one tries to create account with currency other than above ones, will get an BAD REQUEST status in response.

*Note, all POST API expects Content-Type, Accept headers with value "application/json".

Create Account API:
```
POST, {server}:8091/revolut/account/create/{currency}
```
Delete Account API:
```
POST, {server}:8091/revolut/account/delete/{account_number}
```

Accounts info API:
```
- GET, {server}:8091/revolut/account/info/all
- GET, {server}:8091/revolut/account/info/{account_number}
```

Once the accounts are create, we can request for following money exchange operation:
1. Credit into account
2. Debit from account
3. Money Transfer between accounts

Money Credit API:
```
POST, {server}:8091/revolut/account/money/credit
Body:
{
	"account" : {account_number},
	"amount" : {amount}
}
```

Money Debit API:
```
POST, {server}:8091/revolut/account/money/debit
Body:
{
	"account" : {account_number},
	"amount" : {amount}
}
```

Money Transfer API:
```
POST, {server}:8091/revolut/account/money/transfer
Body:
{
	"fromAccount" : {source_account_number},
	"toAccount" : {destination_account_number},
	"amount" : {amount}
}
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
