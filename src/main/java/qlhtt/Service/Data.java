package qlhtt.Service;

public class Data {
    public int acpId;
    public String accountName;
    public String qrCode;
    public String qrDataURL;

    public Data(int acpId, String accountName, String qrCode, String qrDataURL) {
        this.acpId = acpId;
        this.accountName = accountName;
        this.qrCode = qrCode;
        this.qrDataURL = qrDataURL;
    }

    public Data() {

    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAcpId() {
        return acpId;
    }

    public void setAcpId(int acpId) {
        this.acpId = acpId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrDataURL() {
        return qrDataURL;
    }

    public void setQrDataURL(String qrDataURL) {
        this.qrDataURL = qrDataURL;
    }
}
