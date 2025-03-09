package com.btlon.bantraicay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.btlon.bantraicay.R;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        // Cấu hình WebView
        webView.getSettings().setJavaScriptEnabled(true); // Cho phép JavaScript
        webView.getSettings().setDomStorageEnabled(true); // Bật lưu trữ DOM

        // Thiết lập WebViewClient để xử lý các URL
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // Cho phép WebView xử lý URL
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Kiểm tra nếu URL trả về là URL đích sau khi thanh toán thành công
                if (url.startsWith("https://fruitecommerce-d1aaf.web.app")) {
                    
                    // Chờ 3 giây rồi quay lại ThanhToanActivity
                    new Handler().postDelayed(() -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("paymentResult", "success");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }, 3000);
                }
            }
        });

        // Lấy URL từ Intent
        String paymentUrl = getIntent().getStringExtra("paymentUrl");
        if (paymentUrl != null) {
            webView.loadUrl(paymentUrl); // Tải URL vào WebView
        }
    }

//    private void updateOrderStatus(String orderId, String transactionStatus) {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        String trangThai = "Thành công".equals(transactionStatus) ? "Hoàn thành" : "Chưa thanh toán";
//
//        // Cập nhật trạng thái đơn hàng
//        mDatabase.child("donHang").child(orderId).child("trangThai").setValue(trangThai)
//                .addOnSuccessListener(aVoid -> Log.d("WebView", "Trạng thái đơn hàng đã được cập nhật thành công"))
//                .addOnFailureListener(e -> Log.e("WebView", "Lỗi khi cập nhật trạng thái đơn hàng", e));
//    }
}


