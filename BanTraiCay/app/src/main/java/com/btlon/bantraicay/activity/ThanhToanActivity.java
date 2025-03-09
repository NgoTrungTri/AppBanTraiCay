package com.btlon.bantraicay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.btlon.bantraicay.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class ThanhToanActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        // Khởi tạo tham chiếu đến Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button btnThanhToan = findViewById(R.id.btnThanhToan);
        btnThanhToan.setOnClickListener(v -> {
            String orderId = UUID.randomUUID().toString(); // Tạo mã đơn hàng ngẫu nhiên
            int amount = 1500000; // Số tiền thanh toán (1,500,000 VNĐ)
            String paymentUrl;
            try {
                paymentUrl = createPaymentUrl(orderId, amount, "116.111.184.80");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            // Tạo đơn hàng trong Firebase
            createOrderInFirebase(orderId, amount);

            // Mở WebView để thực hiện thanh toán
            startVnpayPayment(paymentUrl);
        });
    }

    private void createOrderInFirebase(String orderId, int amount) {
        // Thông tin đơn hàng
        String maKhachHang = "0";
        String ngayTao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String trangThai = "Chưa thanh toán"; // Trạng thái mặc định khi tạo đơn hàng

        // Tạo đối tượng đơn hàng
        Map<String, Object> order = new HashMap<>();
        order.put("maKhachHang", maKhachHang);
        order.put("maDonHang", orderId);
        order.put("ngayTao", ngayTao);
        order.put("trangThai", trangThai);
        order.put("soTien", amount);

        // Lưu đơn hàng vào Firebase
        mDatabase.child("DonHang").child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> Log.d("ThanhToan", "Đơn hàng đã được lưu thành công"))
                .addOnFailureListener(e -> Log.e("ThanhToan", "Lỗi khi lưu đơn hàng", e));
    }

    private void startVnpayPayment(String paymentUrl) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("paymentUrl", paymentUrl);
        startActivity(intent);
    }

    public String createPaymentUrl(String orderId, int amount, String ipAddr) throws UnsupportedEncodingException {
        String vnp_TmnCode = "CQLBZ0FI";
        String vnp_HashSecret = "9BNVDSRH2LTL30NHN1QKK0XMPZIH5TH0";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderType = "other"; // Thay đổi theo loại đơn hàng
        String vnp_Locale = "vn";
        String vnp_CurrCode = "VND";
        String vnp_ReturnUrl = "https://fruitecommerce-d1aaf.web.app";

        // Lấy thời gian hiện tại
        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(tz);
        String vnp_CreateDate = sdf.format(new Date());

        // Tạo ngày hết hạn
        String vnp_ExpireDate = sdf.format(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); // +15 phút

        // Số tiền cần thanh toán
        String vnp_Amount = String.valueOf(amount * 100); // Số tiền phải nhân với 100

        // Tạo một Map để lưu các tham số
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Amount", vnp_Amount);
        params.put("vnp_Command", vnp_Command);
        params.put("vnp_CreateDate", vnp_CreateDate);
        params.put("vnp_CurrCode", vnp_CurrCode);
//        params.put("vnp_ExpireDate", vnp_ExpireDate);
        params.put("vnp_IpAddr", ipAddr);
        params.put("vnp_Locale", vnp_Locale);
        params.put("vnp_OrderInfo", "ThanhToan");
        params.put("vnp_OrderType", vnp_OrderType);
        params.put("vnp_ReturnUrl", URLEncoder.encode(vnp_ReturnUrl, StandardCharsets.UTF_8.toString()));
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_Version", vnp_Version);

        // Tạo checksum (SHA256 hoặc HMACSHA512)
        StringBuilder hashData = new StringBuilder();
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    if (hashData.length() > 0) {
                        hashData.append('&');
                    }
                    hashData.append(entry.getKey());
                    hashData.append('=');
                    hashData.append(entry.getValue());
                });

        String queryString = hashData.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, queryString);

        // Ghi log URL chưa có secure hash
        Log.d("VNPAY_PAYMENT_URL_BEFORE_HASH", vnp_Url + "?" + queryString);

        // Tạo URL hoàn chỉnh
        String paymentUrl = vnp_Url + "?" + queryString + "&vnp_SecureHash=" + vnp_SecureHash;

        // Ghi log URL sau khi có secure hash
        Log.d("VNPAY_PAYMENT_URL", paymentUrl);

        return paymentUrl;
    }

    @NonNull
    private String hmacSHA512(String key, String data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : rawHmac) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA-512", e);
        }
    }
}
