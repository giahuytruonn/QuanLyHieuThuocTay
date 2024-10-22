package qlhtt.Entity;

public class GiaBan{
    private String maGiaBan;
    private Float giaBan;
    private final Float VAT = 0.08f;

    public GiaBan(String maGiaBan, Float giaBan) {
        this.maGiaBan = maGiaBan;
        this.giaBan = giaBan;
    }

    public String getMaGiaBan() {
        return maGiaBan;
    }

    public void setMaGiaBan(String maGiaBan) {
        this.maGiaBan = maGiaBan;
    }

    public Float getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Float giaBan) {
        this.giaBan = giaBan;
    }

    public Float getVAT() {
        return VAT;
    }

    public Float taoGiaBan(Float giaSanPhamTrenDonViTinhNhoNhat) {
        if(giaSanPhamTrenDonViTinhNhoNhat <=0) {
            return -1f;
        }else {
            if(giaSanPhamTrenDonViTinhNhoNhat <= 1000) {
                return giaSanPhamTrenDonViTinhNhoNhat * (1.15f);
            } else if(giaSanPhamTrenDonViTinhNhoNhat <= 5000) {
                return giaSanPhamTrenDonViTinhNhoNhat * (1.1f);
            } else if(giaSanPhamTrenDonViTinhNhoNhat <= 100000) {
                return giaSanPhamTrenDonViTinhNhoNhat * (1.07f);
            }else if(giaSanPhamTrenDonViTinhNhoNhat <= 1000000) {
                return giaSanPhamTrenDonViTinhNhoNhat * (1.05f);
            }else{
                return giaSanPhamTrenDonViTinhNhoNhat * (1.02f);
            }
        }
    }

}
