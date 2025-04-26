package qlhtt.Enum;

public enum PhuongThucThanhToan {
    TIENMAT(0),
    QRCODE(1);

    private int value;

    PhuongThucThanhToan(int value){
        this.value = value;
    }

    public static PhuongThucThanhToan fromValue(int value){
        return value==1? QRCODE : TIENMAT;
    }

    public int getValue(){
        return value;
    }

}
