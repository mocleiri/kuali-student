/*
* Performs downloading of exported Transactions.
*/
function downloadTransactionExport() {
	jQuery.download("/ksa/uif/exportTransactions", "methodToCall=download");
}