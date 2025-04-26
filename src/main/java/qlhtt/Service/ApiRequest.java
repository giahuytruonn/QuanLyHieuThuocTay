package qlhtt.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiRequest {
    @JsonProperty("accountNo")
    public long accountNo;  //Số tài khoản
    public String accountName; //Tên tài khoản
    public int acqId; //Mã ngân hàng
    public int amount; //Số tiền
    public String addInfo; //Thng tin giao dịch
    public String format; //Định dạng
    public String template; //Mẫu

    public ApiRequest(long accountNo, String accountName, int acqId, int amount, String addInfo, String format, String template) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.acqId = acqId;
        this.amount = amount;
        this.addInfo = addInfo;
        this.format = format;
        this.template = template;
    }

    public ApiRequest() {

    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAcqId() {
        return acqId;
    }

    public void setAcqId(int acqId) {
        this.acqId = acqId;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(long accountNo) {
        this.accountNo = accountNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
