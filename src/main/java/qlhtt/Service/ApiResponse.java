package qlhtt.Service;

public class ApiResponse {
    public String code;
    public String desc;
    public Data data;

    public ApiResponse(String code, String desc, Data data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public ApiResponse() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
