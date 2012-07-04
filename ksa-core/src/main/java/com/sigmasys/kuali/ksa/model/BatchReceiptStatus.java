package com.sigmasys.kuali.ksa.model;

/**
 * Batch receipt status
 *
 * @author Michael Ivanov
 */
public enum BatchReceiptStatus {

    IN_PROCESS(BatchReceiptStatus.IN_PROCESS_CODE),
    FAILED(BatchReceiptStatus.FAILED_CODE),
    PARTIALLY_ACCEPTED(BatchReceiptStatus.PARTIALLY_ACCEPTED_CODE),
    ACCEPTED(BatchReceiptStatus.ACCEPTED_CODE);

    public static final String IN_PROCESS_CODE = "Q";
    public static final String FAILED_CODE = "F";
    public static final String PARTIALLY_ACCEPTED_CODE = "P";
    public static final String ACCEPTED_CODE = "E";

    private String code;

    private BatchReceiptStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        switch (this) {
            case IN_PROCESS:
                return "In Process";
            case FAILED:
                return "Failed";
            case PARTIALLY_ACCEPTED:
                return "Partially Accepted";
            case ACCEPTED:
                return "Accepted";
        }
        throw new IllegalStateException("No batch receipt status found for " + name() + " value");
    }

    public static BatchReceiptStatus findByCode(String code) {
        for (BatchReceiptStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
