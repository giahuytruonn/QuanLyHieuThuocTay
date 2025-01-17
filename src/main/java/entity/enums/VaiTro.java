package entity.enums;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum VaiTro {
    NGUOIQUANLY("Nguoi quan ly"), NHANVIEN("Nhan vien");
    private String name;
}