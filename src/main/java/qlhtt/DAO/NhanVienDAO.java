package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.NhanVien;
import qlhtt.Enum.VaiTro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NhanVienDAO {
    private static NhanVienDAO instance = new NhanVienDAO();

    public static NhanVienDAO getInstance() {
        return instance;
    }

    private EntityManager entityManager = JPAUtil.getEntityManager();

    /**
     * Lấy danh sách nhân viên có trạng thái tài khoản đang hoạt động
     * @return danhSachNV
     */
    public List<NhanVien> getDanhSachNhanVienTheoTrangThai() {
        String jpql = """
        SELECT nv FROM NhanVien nv
        JOIN nv.taiKhoan tk
        WHERE tk.trangThaiTaiKhoan = true
        """;
        return entityManager.createQuery(jpql, NhanVien.class).getResultList();
    }

    /**
     * Lấy nhân viên theo mã nhân viên
     * @return nhanVien
     */
    public NhanVien getNhanVienBangMaNhanVien(String maNV) {
        String jpql = "SELECT nv FROM NhanVien nv WHERE nv.maNhanVien = :maNV";
        try {
            return entityManager.createQuery(jpql, NhanVien.class)
                    .setParameter("maNV", maNV)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Lấy tất cả nhân viên
     * @return danhSachNhanVien
     */
    public List<Map<String, Object>> layTatCaNhanVien() {
        String query = """
        SELECT nv.maNhanVien AS maNhanVien, nv.tenNhanVien AS tenNhanVien, 
               nv.gioiTinh AS gioiTinh, nv.soDienThoai AS soDienThoai, 
               nv.vaiTro AS vaiTro, tk.trangThaiTaiKhoan AS trangThaiTaiKhoan
        FROM NhanVien nv
        JOIN nv.taiKhoan tk
    """;

        List<Map<String, Object>> danhSachNhanVien = new ArrayList<>();

        try {
            List<Object[]> result = entityManager.createNativeQuery(query).getResultList();
            for (Object[] row : result) {
                Map<String, Object> nhanVienMap = new HashMap<>();
                nhanVienMap.put("maNhanVien", row[0]);
                nhanVienMap.put("tenNhanVien", row[1]);
                nhanVienMap.put("gioiTinh", row[2]);
                nhanVienMap.put("soDienThoai", row[3]);
                nhanVienMap.put("vaiTro", row[4]);
                nhanVienMap.put("trangThaiTaiKhoan", row[5]);
                danhSachNhanVien.add(nhanVienMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return danhSachNhanVien;
    }

    /**
     * Tìm kiếm nhân viên theo số điện thoại
     * @return danhSachNhanVien
     */
    public List<Map<String, Object>> timKiemTheoSDT(String soDienThoai) {
        String jpql = """
        SELECT new map(
            nv.tenNhanVien as tenNhanVien,
            nv.gioiTinh as gioiTinh,
            nv.soDienThoai as soDienThoai,
            nv.vaiTro as vaiTro,
            tk.trangThaiTaiKhoan as trangThaiTaiKhoan
        )
        FROM NhanVien nv
        JOIN nv.taiKhoan tk
        WHERE nv.soDienThoai = :soDienThoai
    """;

        TypedQuery<Map<String, Object>> query = entityManager.createQuery(jpql, (Class<Map<String, Object>>) (Class<?>) Map.class);
        query.setParameter("soDienThoai", soDienThoai);

        List<Map<String, Object>> result = query.getResultList();

        if (result.isEmpty()) {
            Map<String, Object> emptyMap = new HashMap<>();
            result.add(emptyMap);
        }

        return result;
    }

    /**
     * Thêm nhân viên mới
     * @return maNhanVien
     */
    public String addNhanVien(NhanVien nhanVien) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(nhanVien);
            entityManager.getTransaction().commit();
            return nhanVien.getMaNhanVien();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cập nhật thông tin nhân viên
     * @return true if update is successful, false otherwise
     */
    public boolean updateNhanVien(NhanVien nhanVien) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(nhanVien);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }
}
