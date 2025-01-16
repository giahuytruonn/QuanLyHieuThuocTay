import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.EqualsAndHashCode;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Generate {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        EntityTransaction tr = em.getTransaction();
        Faker faker = new Faker(new Locale("vi"));
        try{
            tr.begin();
            for(int i = 0; i < 10; i++) {
                //HoaDon
                HoaDon hoaDon = new HoaDon();
                String maHoaDon = "HD" + faker.number().digits(6);
                Boolean trangThaiHoaDon = faker.bool().bool();
                LocalDate ngayTao = faker.date()
                        .past(30, java.util.concurrent.TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                Double tongGiaTriHoaDon = faker.number().randomDouble(2, 500, 10000);
                Double tienDaGiam = faker.number().randomDouble(2, 0, (int) (tongGiaTriHoaDon * 0.2));
                Double tongTienThanhToan = tongGiaTriHoaDon - tienDaGiam;
                Double tongTienKhachTra = faker.number().randomDouble(2, 0, (int) (tongGiaTriHoaDon * 0.1));
                Double tongTienTraLai = tongTienKhachTra - tongTienThanhToan;
                PhuongThucThanhToan phuongThucThanhToan = faker.options().option(PhuongThucThanhToan.class);

                hoaDon.setMaHoaDon(maHoaDon);
                hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
                hoaDon.setNgayTao(ngayTao);
                hoaDon.setTongGiaTriHoaDon(tongGiaTriHoaDon);
                hoaDon.setTienDaGiam(tienDaGiam);
                hoaDon.setTongTienThanhToan(tongTienThanhToan);
                hoaDon.setTongTienKhachTra(tongTienKhachTra);
                hoaDon.setTongTienTraLai(tongTienTraLai);
                hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);

                //Chiet Khau
                ChietKhau chietKhau = new ChietKhau();
                String maChietKhau = faker.idNumber().valid();
                int soLuong = faker.number().numberBetween(1, 1000);
                double giaTriChietKhau = faker.number().randomDouble(2, 5, 50);
                LocalDate ngayBatDauApDung = faker.date().past(1, TimeUnit.DAYS).toLocalDateTime().toLocalDate();
                LocalDate ngayKetThucApDung = ngayBatDauApDung.plusDays(faker.number().numberBetween(1, 365));
                Boolean trangThaiChietKhau = faker.bool().bool();
                String moTa = faker.lorem().sentence();

                chietKhau.setMaChietKhau(maChietKhau);
                chietKhau.setSoLuong(soLuong);
                chietKhau.setGiaTriChietKhau(giaTriChietKhau);
                chietKhau.setNgayBatDauApDung(ngayBatDauApDung);
                chietKhau.setNgayKetThucApDung(ngayKetThucApDung);
                chietKhau.setTrangThaiChietKhau(trangThaiChietKhau);
                chietKhau.setMoTa(moTa);

                //ChiTietHoaDon
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                double tongTienCTHD = faker.number().randomDouble(2, 5, 50);
                int soLuongCTHD = faker.number().numberBetween(10, 500);

                chiTietHoaDon.setTongTien(tongTienCTHD);
                chiTietHoaDon.setSoLuong(soLuongCTHD);

                //ChiTietPhieuNhap
                ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
                    int soLuongCTPN = faker.number().numberBetween(10, 500);
                    double giaNhap = faker.number().randomDouble(2, 50, 500);
                    String donViTinhCTPN = faker.options().option("Hộp", "Vỉ", "Chai", "Gói");
                    LocalDate ngaySanXuat = faker.date().past(365, java.util.concurrent.TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate hanSuDung = ngaySanXuat.plusMonths(faker.number().numberBetween(6, 36));
                    double thanhTien = soLuongCTPN * giaNhap;

                    chiTietPhieuNhap.setSoLuong(soLuongCTPN);
                    chiTietPhieuNhap.setGiaNhap(giaNhap);
                    chiTietPhieuNhap.setDonViTinh(donViTinhCTPN);
                    chiTietPhieuNhap.setNgaySanXuat(ngaySanXuat);
                    chiTietPhieuNhap.setHanSuDung(hanSuDung);
                    chiTietPhieuNhap.setThanhTien(thanhTien);

                //DonViTinh
                DonViTinh donViTinh = new DonViTinh();
                String maDonViTinh = faker.idNumber().valid();
                String tenDonViTinh = faker.name().fullName();

                donViTinh.setMaDonViTinh(maDonViTinh);
                donViTinh.setTenDonViTinh(tenDonViTinh);

                //KhachHang
                KhachHang khachHang = new KhachHang();
                String maKhachHang = "KH" + faker.number().digits(5);
                String hoTen = faker.name().fullName();
                boolean gioiTinh = faker.bool().bool();
                String soDienThoai = faker.phoneNumber().cellPhone();
                LocalDate ngaySinh = faker.date()
                        .birthday(18, 60)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                int diemTichLuy = faker.number().numberBetween(0, 500);
                String email = faker.internet().emailAddress();

                khachHang.setMaKhachHang(maKhachHang);
                khachHang.setHoTen(hoTen);
                khachHang.setGioiTinh(gioiTinh);
                khachHang.setSoDienThoai(soDienThoai);
                khachHang.setNgaySinh(ngaySinh);
                khachHang.setDiemTichLuy(diemTichLuy);
                khachHang.setEmail(email);

                //LoaiSanPham
                LoaiSanPham loaiSanPham = new LoaiSanPham();
                String maLoaiSp = "L" + faker.number().digits(5);
                String tenLoaiSp = faker.name().name();

                loaiSanPham.setMaLoaiSp(maLoaiSp);
                loaiSanPham.setTenLoaiSp(tenLoaiSp);

                //NhaCungCap
                NhaCungCap nhaCungCap = new NhaCungCap();
                String maNhaCungCap = "NCC" + faker.number().digits(5);
                String tenNhaCungCap = faker.company().name();
                String soDienThoaiNCC = faker.phoneNumber().cellPhone();
                String diaChi = faker.address().fullAddress();
                String emailNCC = faker.internet().emailAddress();

                nhaCungCap.setMaNhaCungCap(maNhaCungCap);
                nhaCungCap.setTenNhaCungCap(tenNhaCungCap);
                nhaCungCap.setSoDienThoai(soDienThoaiNCC);
                nhaCungCap.setDiaChi(diaChi);
                nhaCungCap.setEmail(emailNCC);

                //NhanVien
                NhanVien nhanVien = new NhanVien();
                String maNhanVien = "NV" + faker.number().digits(5);
                String tenNhanVien = faker.name().fullName();
                String soDienThoaiNV = faker.phoneNumber().cellPhone();
                String emailNV = faker.internet().emailAddress();
                String cccd = faker.number().digits(12);
                VaiTro vaiTro = faker.options().option(VaiTro.class);
                LocalDate ngaySinhNV = faker.date().birthday(18, 60).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                Boolean gioiTinhNV = faker.bool().bool();
                String duongDanAnh = faker.internet().url();

                nhanVien.setMaNhanVien(maNhanVien);
                nhanVien.setTenNhanVien(tenNhanVien);
                nhanVien.setSoDienThoai(soDienThoaiNV);
                nhanVien.setEmail(emailNV);
                nhanVien.setCccd(cccd);
                nhanVien.setVaiTro(vaiTro);
                nhanVien.setNgaySinh(ngaySinhNV);
                nhanVien.setGioiTinh(gioiTinhNV);
                nhanVien.setDuongDanAnh(duongDanAnh);

                //NhomSanPham
                NhomSanPham nhomSanPham = new NhomSanPham();
                String maNhomSp = "NSP" + faker.number().digits(5);
                String tenNhomSp = faker.phoneNumber().cellPhone();

                nhomSanPham.setMaNhomSp(maNhomSp);
                nhomSanPham.setTenNhomSp(tenNhomSp);

                //PhieuNhap
                PhieuNhap phieuNhap = new PhieuNhap();
                String maPhieuNhap = "PN" + faker.number().digits(5); // Mã phiếu nhập ví dụ: PN12345
                boolean trangThaiPhieuNhap = faker.bool().bool(); // true: đã thanh toán, false: chưa thanh toán
                LocalDate ngayTaoPN = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS).toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                Double tongTien = faker.number().randomDouble(2, 50000, 500000);
                String soLo = "LO" + faker.number().digits(4);

                phieuNhap.setMaPhieuNhap(maPhieuNhap);
                phieuNhap.setTrangThaiPhieuNhap(trangThaiPhieuNhap);
                phieuNhap.setNgayTao(ngayTaoPN);
                phieuNhap.setTongTien(tongTien);
                phieuNhap.setSoLo(soLo);
                phieuNhap.setNhaCungCap(nhaCungCap);
                phieuNhap.setNhanVien(nhanVien);

                //SanPham
                SanPham sanPham = new SanPham();
                String maSanPham = "SP" + faker.number().digits(5); // Mã sản phẩm ví dụ: SP12345
                String tenSanPham = faker.medical().medicineName();
                String quyCachDongGoi = faker.number().numberBetween(1, 100) + " viên/hộp";
                String thanhPhan = faker.food().ingredient();
                String dangBaoChe = faker.options().option("Viên nén", "Dung dịch", "Viên nang", "Thuốc bột");
                ChiDinhSuDung chiDinhSuDung = faker.options().option(ChiDinhSuDung.values());
                String nhaSanXuat = faker.company().name();
                String quocGiaSanXuat = faker.country().name();
                String doiTuongSuDung = faker.options().option("Người lớn", "Trẻ em", "Người cao tuổi");
                String moTaSanPham = faker.lorem().sentence();
                int soLuongThuoc = faker.number().numberBetween(10, 1000);
                double donGia = faker.number().randomDouble(2, 50, 1000);
                String duongDung = faker.options().option("Uống", "Tiêm", "Xịt", "Thoa ngoài");
                String hamLuong = faker.number().numberBetween(50, 500) + "mg";

                sanPham.setMaSanPham(maSanPham);
                sanPham.setTenSanPham(tenSanPham);
                sanPham.setQuyCachDongGoi(quyCachDongGoi);
                sanPham.setThanhPhan(thanhPhan);
                sanPham.setDangBaoChe(dangBaoChe);
                sanPham.setChiDinhSuDung(chiDinhSuDung);
                sanPham.setNhaSanXuat(nhaSanXuat);
                sanPham.setQuocGiaSanXuat(quocGiaSanXuat);
                sanPham.setDoiTuongSuDung(doiTuongSuDung);
                sanPham.setMoTaSanPham(moTaSanPham);
                sanPham.setSoLuong(soLuongThuoc);
                sanPham.setDonGia(donGia);
                sanPham.setDuongDung(duongDung);
                sanPham.setHamLuong(hamLuong);
                sanPham.setLoaiSanPham(loaiSanPham);
                sanPham.setNhomSanPham(nhomSanPham);
                sanPham.setDonViTinh(donViTinh);

                //Tai Khoan
                TaiKhoan taiKhoan = new TaiKhoan();
                String maTaiKhoan = faker.idNumber().valid();
                String tenDangNhap = faker.name().username();
                String matKhau = faker.internet().password(8, 16); // Mật khẩu từ 8 đến 16 ký tự
                boolean trangThaiTaiKhoan = faker.bool().bool();

                taiKhoan.setMaTaiKhoan(maTaiKhoan);
                taiKhoan.setTenDanNhap(tenDangNhap);
                taiKhoan.setMatKhau(matKhau);
                taiKhoan.setTrangThaiTaiKhoan(trangThaiTaiKhoan);
                taiKhoan.setNhanVien(nhanVien);

                //Set DanhSach

                //ChietKhau
                chietKhau.setDsHoaDon(new HashSet<>(List.of(hoaDon)));

                //ChiTietHoaDon
                chiTietHoaDon.setSanPham(sanPham);
                chiTietHoaDon.setHoaDon(hoaDon);
                System.out.println(hoaDon);

                //ChiTietPhieuNhap
                chiTietPhieuNhap.setSanPham(sanPham);
                chiTietPhieuNhap.setPhieuNhap(phieuNhap);

                //DonViTinh
                donViTinh.setDsSanPham(new HashSet<>(List.of(sanPham)));

                //HoaDon
                hoaDon.setChietKhau(chietKhau);
                hoaDon.setKhachHang(khachHang);
                hoaDon.setNhanVien(nhanVien);
                hoaDon.setDsChiTietHoaDon(new HashSet<>(List.of(chiTietHoaDon)));

                //KhachHang
                khachHang.setDsHoaDon(new HashSet<>(List.of(hoaDon)));

                //LoaiSanPham
                loaiSanPham.setDsSanPham(new HashSet<>(List.of(sanPham)));

                //NCC
                nhaCungCap.setDsPhieuNhap(new HashSet<>(List.of(phieuNhap)));

                //NhanVien
                nhanVien.setTaiKhoan(taiKhoan);
                nhanVien.setDsHoaDon(new HashSet<>(List.of(hoaDon)));
                nhanVien.setDsPhieuNhap(new HashSet<>(List.of(phieuNhap)));

                //NhomSanPham
                nhomSanPham.setDsSanPham(new HashSet<>(List.of(sanPham)));

                //SanPham
                sanPham.setDsChiTietHoaDon(new LinkedList<>(List.of(chiTietHoaDon)));
                sanPham.setDsChiTietPhieuNhap(new HashSet<>(List.of(chiTietPhieuNhap)));

                em.persist(hoaDon);
                em.persist(sanPham);
                em.persist(chietKhau);
                em.persist(phieuNhap);
                em.persist(chiTietHoaDon);
                em.persist(chiTietPhieuNhap);
                em.persist(donViTinh);
                em.persist(khachHang);
                em.persist(loaiSanPham);
                em.persist(nhaCungCap);
                em.persist(nhanVien);
                em.persist(nhomSanPham);
                em.persist(taiKhoan);
            }
            tr.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }
        em.close();
    }
}
