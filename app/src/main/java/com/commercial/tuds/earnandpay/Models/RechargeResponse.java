package com.commercial.tuds.earnandpay.Models;

public class RechargeResponse {

    public String payid;
    public String operator_ref;
    public String status;
    public String message;

    public RechargeResponse(String payid, String operator_ref, String status, String message) {
        this.payid = payid;
        this.operator_ref = operator_ref;
        this.status = status;
        this.message = message;
    }

    public String getPayid() {
        return payid;
    }

    public String getOperator_ref() {
        return operator_ref;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RechargeResponse{" +
                "payid='" + payid + '\'' +
                ", operator_ref='" + operator_ref + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
