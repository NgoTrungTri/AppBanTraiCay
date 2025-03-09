package com.btlon.bantraicay;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.btlon.bantraicay.models.ChiTietDonHang;
import com.btlon.bantraicay.models.DanhGia;
import com.btlon.bantraicay.models.SanPham;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SanPhamAdapter extends ArrayAdapter<ChiTietDonHang> {
    private Context context;
    private int resource;
    private FirebaseAuth mAuth;
    private List<ChiTietDonHang> chiTietDonHangList;

    public SanPhamAdapter(@NonNull Context context, int resource, @NonNull List<ChiTietDonHang> chiTietDonHangList) {
        super(context, resource, chiTietDonHangList);
        this.context = context;
        this.resource = resource;
        this.chiTietDonHangList = chiTietDonHangList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        // Lấy các thành phần trong layout item_san_pham.xml
        ImageView imgSanPham = convertView.findViewById(R.id.imgSanPham);
        TextView tvTenSanPham = convertView.findViewById(R.id.tvTenSanPham);
        TextView tvGiaSanPham = convertView.findViewById(R.id.tvGiaSanPham);
        RatingBar ratingBarSanPham = convertView.findViewById(R.id.ratingBarSanPham);

        // Lấy chi tiết sản phẩm hiện tại
        ChiTietDonHang chiTietDonHang = chiTietDonHangList.get(position);

        // Truy vấn tên sản phẩm từ Firebase và gán dữ liệu vào view
        DatabaseReference sanPhamRef = FirebaseDatabase.getInstance().getReference("SanPham");
        sanPhamRef.child(chiTietDonHang.getMaSanPham()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                if (sanPham != null) {
                    tvTenSanPham.setText(sanPham.getTenSP());
                    tvGiaSanPham.setText("Giá: " + chiTietDonHang.getTongTienSp() + " VND");

                    // Sử dụng Glide hoặc Picasso để tải ảnh
                    Glide.with(context).load(sanPham.getHinhAnh()).into(imgSanPham);

                    updateRatingBar(sanPham.getMaSanPham(), ratingBarSanPham);

                    ratingBarSanPham.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                        // Tạo đánh giá
                        createReview(sanPham.getMaSanPham(), (int) rating);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        return convertView;
    }

    private void createReview(String maSanPham, int rating) {
        // Tạo mã đánh giá ngẫu nhiên
        String reviewId = FirebaseDatabase.getInstance().getReference("DanhGia").push().getKey();
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DanhGia danhGia = new DanhGia(reviewId, rating, maSanPham, userId);
        // Tạo đối tượng đánh giá
        DanhGia danhGia = new DanhGia(reviewId, rating, maSanPham, "KH002");

        // Lưu đánh giá vào Firebase
        DatabaseReference danhGiaRef = FirebaseDatabase.getInstance().getReference("DanhGia");
        danhGiaRef.child(reviewId).setValue(danhGia).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Đánh giá đã được lưu thành công
                Toast.makeText(context, "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý lỗi nếu có
                Toast.makeText(context, "Lỗi khi đánh giá!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRatingBar(String maSanPham, RatingBar ratingBar) {
        DatabaseReference danhGiaRef = FirebaseDatabase.getInstance().getReference("DanhGia");
//        String userId = mAuth.getCurrentUser().getUid();
        String userId = "KH002";

        danhGiaRef.orderByChild("maSanPham").equalTo(maSanPham).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhGia danhGia = snapshot.getValue(DanhGia.class);
                    if (danhGia != null && danhGia.getMaKhachHang().equals(userId)) {
                        ratingBar.setRating(danhGia.getSoSao());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}

