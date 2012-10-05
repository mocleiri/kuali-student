# The KSA DSL definition
#/when
#/then
[when][]Student account ID is "{userId}" = context : DroolsContext(account.id == "{userId}");
#[when][]Course codes are "{courseCodes}"=droolsContext.getFeeManagementService().studentHasCourses("{courseCodes}");
#[when][]Major codes are "{majorCode}"=droolsContext.getFeeManagementService().studentHasMajors("{majorCodes}");
[then][]Use "{transactionTypeId}" code to charge ${amount}=context.getTransactionService().createTransaction("{transactionTypeId}","{userId}", new Date(), new BigDecimal({amount}));
[then][]Use "{transactionTypeId}" code to credit ${amount}=context.getTransactionService().createTransaction("{transactionTypeId}","{userId}", new Date(), new BigDecimal({amount}));
