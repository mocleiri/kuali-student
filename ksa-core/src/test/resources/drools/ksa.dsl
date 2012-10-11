# The KSA DSL definition

[keyword][]and = &&
[keyword][]equals = ==
[keyword][]"" = null

[when][]\({constraints}\) = context : DroolsContext({constraints})
[when][]Student account ID is "{userId}" = account.id == "{userId}"
[when][]Student is resident = feeManagementService.isResident(feeBase)
[when][]Student is graduate = feeManagementService.isGraduate(feeBase)
[when][]LU code is "{luCodes}" = feeManagementService.containsLearningUnitCode(feeBase, "{luCodes}")
[when][]LU code is "{luCodes}" with status "{statuses}" = feeManagementService.containsLearningUnitCode(feeBase, "{luCodes}", "{statuses}")
[when][]Major code is "{majorCodes}" = feeManagementService.containsMajorCode(feeBase, "{majorCodes}")
[when][]Section code is "{sectionCodes}" = feeManagementService.containsSectionCode(feeBase, "{sectionCodes}")
[when][]Section code is "{sectionCodes}" with status "{statuses}" = feeManagementService.containsSectionCode(feeBase, "{sectionCodes}", "{statuses}")
[when][]Number of credits is {numberOfCredits} for courses with status "{status}" = feeManagementService.getNumOfCredits(feeBase, "{status}") == {numberOfCredits}

[then][]Use "{transactionTypeId}" code to charge ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Use "{transactionTypeId}" code to credit ${amount} = context.getTransactionService().createTransaction("{transactionTypeId}",context.getAccount().getId(), new Date(), new BigDecimal({amount}));
[then][]Set status "{status}" and key pair "${key}", "${value}" for LU with code "{luCode}" and status "{status}" = context.getFeeManagementService().setCourseStatusForLearningUnit("{luCode}","{status}","${key}","${value}");
[then][]Set status "{status}" and key pair "${key}", "${value}" for LU with section "{sectionCode}" and status "{status}" = context.getFeeManagementService().setCourseStatusForSection("{sectionCode}","{status}","${key}","${value}");