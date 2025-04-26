package qlhtt.Enum;

public enum VaiTro {
    NGUOIQUANLY(0),
    NHANVIEN(1);

    private int value;

    VaiTro(int value){
        this.value = value;
    }
    public int getValue() {
        return value; // Phương thức trả về giá trị tương ứng
    }
    public static VaiTro fromValue(int value){
        return value==1? NGUOIQUANLY : NHANVIEN;
    }
}
