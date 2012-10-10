# The KSA DSL definition
#/when
#/then
[when][]Student account ID is "{userId}" = context : DroolsContext(account.id == "{userId}");
[when][]LU code is in "{luCodes}" = context : DroolsContext(feeManagementService.containsLearningUnitCode(feeBase, "{luCodes}"));
[when][]Major code is in "{majorCodes}" = context : DroolsContext(feeManagementService.containsMajorCode(feeBase, "{majorCodes}"));

[then][]Use "{transactionTypeId}" code to charge ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Use "{transactionTypeId}" code to credit ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
