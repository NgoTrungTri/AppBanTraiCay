package com.btlon.bantraicay;

import android.util.Log;

import com.btlon.bantraicay.models.DanhGia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestSellingProduct {

    private static final String TAG = "BestSellingProduct";

    // Phương thức tính số sao trung bình của một sản phẩm
    public static int calculateAverageRatingForProduct(String maSanPham) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference danhGiaRef = database.getReference("DanhGia");
        final int[] totalStars = {0};
        final int[] count = {0};
        danhGiaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhGia danhGia = snapshot.getValue(DanhGia.class);
                    if (danhGia != null && danhGia.getMaSanPham().equals(maSanPham)) {
                        totalStars[0] += danhGia.getSoSao();
                        count[0]++;
                    }
                }

                if (count[0] == 0) {
                    Log.d(TAG, "Không có đánh giá nào cho sản phẩm: " + maSanPham);
                    return;
                }

                double averageRating = (double) totalStars[0] / count[0];
                Log.d(TAG, "Sản phẩm " + maSanPham + " có số sao trung bình: " + averageRating);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Lỗi khi đọc dữ liệu từ Firebase", error.toException());
            }
        });

        // Lúc này, totalStars và count sẽ chưa được cập nhật từ Firebase
        // Bạn sẽ trả về giá trị 0 hoặc giá trị mặc định khác
        return (int) Math.round((double) totalStars[0] / count[0]);
    }

    // Phương thức trả về 5 sản phẩm bán chạy nhất dựa trên số sao trung bình
    public static void getTopFiveBestSellingProducts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference danhGiaRef = database.getReference("DanhGia");
        DatabaseReference sanPhamRef = database.getReference("SanPham");

        danhGiaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sanPhamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Integer> averageRatings = new HashMap<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String maSanPham = snapshot.getKey();
                            int averageRating = calculateAverageRatingForProduct(maSanPham);
                            averageRatings.put(maSanPham, averageRating);
                        }

                        // Sắp xếp các sản phẩm theo số sao trung bình giảm dần
                        List<Map.Entry<String, Integer>> sortedRatings = new ArrayList<>(averageRatings.entrySet());
                        sortedRatings.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                        // Lấy 5 sản phẩm có số sao trung bình cao nhất
                        List<String> topFiveProducts = new ArrayList<>();
                        for (int i = 0; i < Math.min(5, sortedRatings.size()); i++) {
                            topFiveProducts.add(sortedRatings.get(i).getKey());
                        }

                        // Ghi log danh sách 5 sản phẩm bán chạy nhất
                        Log.d(TAG, "Top 5 sản phẩm bán chạy nhất: " + topFiveProducts);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Lỗi khi đọc dữ liệu sản phẩm từ Firebase", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Lỗi khi đọc dữ liệu đánh giá từ Firebase", error.toException());
            }
        });
    }
}
