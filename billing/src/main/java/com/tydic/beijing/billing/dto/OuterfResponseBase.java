/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.billing.dto;

/**
 *
 * @author wangshida
 */
public abstract class OuterfResponseBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected String Status;
    protected String ErrorCode;
    protected String ErrorMessage;

    public OuterfResponseBase() {
        this.Status = "1";
        this.ErrorCode = "";
        this.ErrorMessage = "";
    }

    public OuterfResponseBase(String Status, String ErrorCode, String ErrorMessage) {
        this.Status = Status;
        this.ErrorCode = ErrorCode;
        this.ErrorMessage = ErrorMessage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }

    @Override
    public String toString() {
        return "OuterfResponseBase{" + "Status=" + Status + ", ErrorCode=" + ErrorCode + ", ErrorMessage=" + ErrorMessage + '}';
    }
    
}
