package qlhtt.Enum;

public enum ChiDinhSuDung {
    KE_TOA(0),
    KHONG_KE_TOA(1);

    private int value;

    ChiDinhSuDung(int value){
        this.value = value;
    }

    public static ChiDinhSuDung fromValue(int value){
        return value==1? KHONG_KE_TOA : KE_TOA;
    }

    @Override
    public String toString() {
        return value==1? "Không kê toa" : "Kê toa";
    }
}
