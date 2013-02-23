# The KSA DSL definition

# FEE MANAGEMENT DSL definitions

[keyword][]and = &&
[keyword][]equals = ==

# LHS definitions
[when][]\({constraints}\) = context : BrmContext({constraints})
[when][]Student account ID is "{userId}" = account.id == "{userId}"
[when][]Student is resident = feeManagementService.isResident(feeBase)
[when][]Student is not resident = !feeManagementService.isResident(feeBase)
[when][]Student is graduate = feeManagementService.isGraduate(feeBase)
[when][]Student is not graduate = !feeManagementService.isGraduate(feeBase)
[when][]LU code is "{luCodes}" with status "{statuses}" = feeManagementService.containsLearningUnitCode(feeBase, "{luCodes}", "{statuses}")
[when][]Major is "{majorCodes}" = feeManagementService.containsMajorCode(feeBase, "{majorCodes}")
[when][]Section is "{sectionCodes}" with status "{statuses}" = feeManagementService.containsSectionCode(feeBase, "{sectionCodes}", "{statuses}")
[when][]Number of credits is {numberOfCredits} with status "{status}" = feeManagementService.getNumOfCredits(feeBase, "{status}") == {numberOfCredits}
[when][]Number of credits > {numberOfCredits} with status "{status}" = feeManagementService.getNumOfCredits(feeBase, "{status}") > {numberOfCredits}
[when][]Number of credits < {numberOfCredits} with status "{status}" = feeManagementService.getNumOfCredits(feeBase, "{status}") < {numberOfCredits}
[when][]Key pair "{key}" is "{values}" = feeManagementService.containsKeyPair(feeBase, "{key}", "{values}")

# RHS definitions
[then][]Use code "{transactionTypeId}" to charge ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Use code "{transactionTypeId}" to credit ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Set status to "{status}", key pair "{key}" to "{value}" where code is "{luCodes}" = context.getFeeManagementService().setCourseStatusForLearningUnits(feeBase,"{luCodes}","{status}","{key}","{value}");
[then][]Set status to "{status}", key pair "{key}" to "{value}" where section is "{sectionCodes}" = context.getFeeManagementService().setCourseStatusForSections(feeBase,"{sectionCodes}","{status}","{key}","{value}");
[then][]Set status to "{newStatus}", key pair "{key}" to "{value}" where status is "{oldStatus}" = context.getFeeManagementService().setCourseStatusForStatus(feeBase,"{oldStatus}","{newStatus}","{key}","{value}");
[then][]Number of credits for LU code "{luCodes}" with status "{statuses}" = context.getFeeManagementService().getNumOfCreditsByLearningUnitCodes(feeBase,"{luCodes}","{statuses}");
[then][]Use "{transactionTypeId}" to charge ${amountPerCredit} per credit where section is "{sectionCodes}" with status "{statuses}" = context.getFeeManagementService().createTransactionForNumberOfCredits(feeBase,"{transactionTypeId}",new BigDecimal({amountPerCredit}),"{sectionCodes}","{statuses}");


# PAYMENT APPLICATION DSL definitions

# LHS definitions
[when][]Transactions exist = !transactions.isEmpty()

# RHS definitions
[then][]Initialize list of transactions "{transactions}" = context.getAttributes().put("{transactions}", new LinkedList<Transaction>());
[then][]Initialize list of GL transactions "{glTransactions}" = context.getAttributes().put("{glTransactions}", new LinkedList<GlTransaction>());
[then][]Get list of transactions from "{startDate}" to "{endDate}" and store result in "{transactions}" = context.getBrmPaymentService().getTransactions("{startDate}", "{endDate}", "{transactions}", context);
[then][]Remove allocations from "{transactions}" and add result to "{glTransactions}" = context.getBrmPaymentService().removeAllocations("{transactions}", "{glTransactions}", context);
[then][]Allocate reversals for "{transactions}" and add result to "{glTransactions}" = context.getBrmPaymentService().allocateReversals("{transactions}", "{glTransactions}", context);
[then][]Apply payments for "{transactions}" and add result to "{glTransactions}" = context.getBrmPaymentService().applyPayments("{transactions}", "{glTransactions}", context);
[then][]Apply payments for "{transactions}" with maximum amount {maxAmount} and add result to "{glTransactions}" = context.getBrmPaymentService().applyPayments(new BigDecimal({amount}), "{transactions}", "{glTransactions}", context);
[then][]Calculate matrix scores for "{transactions}" = context.getBrmPaymentService().calculateMatrixScores("{transactions}", context);
[then][]Sort "{transactions}" by matrix score in ascending order = context.getBrmPaymentService().sortByMatrixScore("{transactions}", true, context);
[then][]Sort "{transactions}" by matrix score in descending order = context.getBrmPaymentService().sortByMatrixScore("{transactions}", false, context);
[then][]Get payments from "{transactions}" for {year} year and store result in "{payments}" = context.getBrmPaymentService().filterTransactions(TransactionTypeValue.PAYMENT, {year}, "{transactions}", "{payments}", context);
[then][]Get payments from "{transactions}" and store result in "{payments}" = context.getBrmPaymentService().filterTransactions(TransactionTypeValue.PAYMENT, "{transactions}", "{payments}", context);
[then][]Get payments with tag "{tag}" from "{transactions}" for {year} year and store result in "{payments}" = context.getBrmPaymentService().filterTransactions(TransactionTypeValue.PAYMENT, {year}, "{tag}", "{transactions}", "{payments}", context);
[then][]Get charges from "{transactions}" for {year} year and store result in "{charges}" = context.getBrmPaymentService().filterTransactions(TransactionTypeValue.CHARGE, {year}, "{transactions}", "{charges}", context);
[then][]Get charges from "{transactions}" and store result in "{charges}" = context.getBrmPaymentService().filterTransactions(TransactionTypeValue.CHARGE, "{transactions}", "{charges}", context);
[then][]Get charges with tag "{tag}" from "{transactions}" for {year} year and store result in "{charges}" = context.getBrmPaymentService().filterTransactions(TransactionTypeValue.CHARGE, {year}, "{tag}", "{transactions}", "{charges}", context);
[then][]Summarize GL transactions "{glTransactions1}" and store result in "{glTransactions2}" = context.getBrmPaymentService().summarizeGlTransactions("{glTransactions1}", "{glTransactions2}", context);


# Deprecated PA definitions
[then][]Remove allocations = glTransactions.addAll(context.getTransactionService().removeAllocations(transactions));
[then][]Allocate reversals = glTransactions.addAll(context.getTransactionService().allocateReversals(transactions));
[then][]Apply payments = glTransactions.addAll(context.getPaymentService().applyPayments(transactions, remainingTransactions));
[then][]Apply remaining payments = glTransactions.addAll(context.getPaymentService().applyPayments(remainingTransactions));
[then][]Calculate matrix scores = TransactionUtils.calculateMatrixScores(remainingTransactions);
[then][]Sort by matrix score in ascending order = TransactionUtils.orderByMatrixScore(remainingTransactions, true);
[then][]Sort by matrix score in descending order = TransactionUtils.orderByMatrixScore(remainingTransactions, false);
[then][]Summarize GL transactions = glTransactions = context.getGeneralLedgerService().summarizeGlTransactions(glTransactions);