# The KSA DSL definition

[keyword][]and = &&
[keyword][]equals = ==

[when][]\({constraints}\) = context : DroolsContext({constraints})
[when][]Student account ID is "{userId}" = account.id == "{userId}"
[when][]Student is resident = feeManagementService.isResident(feeBase)
[when][]Student is not resident = !feeManagementService.isResident(feeBase)
[when][]Student is graduate = feeManagementService.isGraduate(feeBase)
[when][]Student is not graduate = !feeManagementService.isGraduate(feeBase)
[when][]LU code is "{luCodes}" with status "{statuses}" = feeManagementService.containsLearningUnitCode(feeBase, "{luCodes}", "{statuses}")
[when][]Major is "{majorCodes}" = feeManagementService.containsMajorCode(feeBase, "{majorCodes}")
[when][]Section is "{sectionCodes}" with status "{statuses}" = feeManagementService.containsSectionCode(feeBase, "{sectionCodes}", "{statuses}")
[when][]Number of credits is {numberOfCredits} for courses with status "{status}" = feeManagementService.getNumOfCredits(feeBase, "{status}") == {numberOfCredits}
[when][]Key pair "{key}" is "{values}" = feeManagementService.containsKeyPair(feeBase, "{key}", "{values}")

[then][]Use code "{transactionTypeId}" to charge ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Use code "{transactionTypeId}" to credit ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Set status to "{status}", key pair "{key}" to "{value}" where code is "{luCode}" = context.getFeeManagementService().setCourseStatusForLearningUnit("{luCode}","{status}","{key}","{value}");
[then][]Set status to "{status}", key pair "{key}" to "{value}" where section is "{sectionCode}" = context.getFeeManagementService().setCourseStatusForSection("{sectionCode}","{status}","{key}","{value}");
[then][]Number of credits for LU code "{luCodes}" with status "{statuses}" = context.getFeeManagementService().getNumOfCreditsByLearningUnitCodes(feeBase,"{luCodes}","{statuses}");
[then][]Use "{transactionTypeId}" to charge ${amountPerCredit} per credit where section is "{sectionCodes}" with status "{statuses}" = context.getFeeManagementService().createTransactionForNumberOfCredits(feeBase,"{transactionTypeId}",new BigDecimal({amountPerCredit}),"{sectionCodes}","{statuses}");