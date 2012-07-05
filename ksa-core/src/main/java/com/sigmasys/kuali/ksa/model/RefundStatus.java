package com.sigmasys.kuali.ksa.model;

/**
 * Refund status
 *
 * @author Michael Ivanov
 */
public enum RefundStatus {

    UNVERIFIED(RefundStatus.UNVERIFIED_CODE),
    VERIFIED(RefundStatus.VERIFIED_CODE),
    REFUNDED(RefundStatus.REFUNDED_CODE),
    FAILED(RefundStatus.FAILED_CODE);

    public static final String UNVERIFIED_CODE = "U";
    public static final String VERIFIED_CODE = "V";
    public static final String REFUNDED_CODE = "R";
    public static final String FAILED_CODE = "F";

    private String code;

    private RefundStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        switch (this) {
            case UNVERIFIED:
                return "Unverified";
            case FAILED:
                return "Failed";
            case VERIFIED:
                return "Verified";
            case REFUNDED:
                return "Refunded";
        }
        throw new IllegalStateException("No refund status found for " + name() + " value");
    }

    public static RefundStatus findByCode(String code) {
        for (RefundStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
