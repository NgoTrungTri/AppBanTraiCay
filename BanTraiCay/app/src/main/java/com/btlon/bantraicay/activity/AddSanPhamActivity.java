package com.btlon.bantraicay.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.btlon.bantraicay.R;
import com.btlon.bantraicay.models.LoaiTraiCay;
import com.btlon.bantraicay.models.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddSanPhamActivity extends AppCompatActivity {
    private EditText etMaSanPham, etTenSP, etDinhLuong, etGia, etHinhAnh, etMoTa;
    private Spinner spinnerLoaiTraiCay;
    private Button btnSaveSanPham;
    private FirebaseDatabase database;
    private DatabaseReference loaiTraiCayRef, sanPhamRef;
    private ArrayList<LoaiTraiCay> loaiTraiCayList;
    private ArrayAdapter<LoaiTraiCay> adapterLoaiTraiCay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_san_pham);

        // Khởi tạo Firebase Realtime Database
        database = FirebaseDatabase.getInstance();
        loaiTraiCayRef = database.getReference("LoaiTraiCay");
        sanPhamRef = database.getReference("SanPham");

        // Tham chiếu các thành phần giao diện
        etMaSanPham = findViewById(R.id.etMaSanPham);
        etTenSP = findViewById(R.id.etTenSP);
        etDinhLuong = findViewById(R.id.etDinhLuong);
        etGia = findViewById(R.id.etGia);
        etHinhAnh = findViewById(R.id.etHinhAnh);
        etMoTa = findViewById(R.id.etMoTa);
        spinnerLoaiTraiCay = findViewById(R.id.spinnerLoaiTraiCay);
        btnSaveSanPham = findViewById(R.id.btnSaveSanPham);

        // Khởi tạo danh sách và adapter cho Spinner
        loaiTraiCayList = new ArrayList<>();
        adapterLoaiTraiCay = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiTraiCayList);
        adapterLoaiTraiCay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTraiCay.setAdapter(adapterLoaiTraiCay);

        // Lấy dữ liệu từ Realtime Database và hiển thị lên Spinner
        loaiTraiCayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loaiTraiCayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LoaiTraiCay loaiTraiCay = snapshot.getValue(LoaiTraiCay.class);
                    if (loaiTraiCay != null) {
                        loaiTraiCayList.add(loaiTraiCay);
                    }
                }
                adapterLoaiTraiCay.notifyDataSetChanged();
                Log.d("AddSanPhamActivity", "Loại trái cây: " + loaiTraiCayList.toString()); // Kiểm tra dữ liệu
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddSanPhamActivity.this, "Lỗi khi tải loại trái cây!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveSanPham.setOnClickListener(v -> {
            String maSanPham = etMaSanPham.getText().toString().trim();
            String tenSP = etTenSP.getText().toString().trim();
            double dinhLuong = Double.parseDouble(etDinhLuong.getText().toString().trim());
            double gia = Double.parseDouble(etGia.getText().toString().trim());
            String hinhAnh = etHinhAnh.getText().toString().trim();
            String moTa = etMoTa.getText().toString().trim();
            LoaiTraiCay selectedLoaiTraiCay = (LoaiTraiCay) spinnerLoaiTraiCay.getSelectedItem();

            if (selectedLoaiTraiCay == null) {
                Toast.makeText(AddSanPhamActivity.this, "Vui lòng chọn loại trái cây!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy mã loại trái cây từ đối tượng LoaiTraiCay đã chọn
            String maLoaiTraiCay = selectedLoaiTraiCay.getMaLoai(); // Giả sử getMaLoai() là phương thức trả về mã loại trái cây

            // Tạo đối tượng SanPham với mã loại trái cây
            SanPham sanPham = new SanPham(maSanPham, tenSP, dinhLuong, gia, hinhAnh, moTa, maLoaiTraiCay);

            // Lưu vào Realtime Database
            sanPhamRef.child(sanPham.getMaSanPham())
                    .setValue(sanPham)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(AddSanPhamActivity.this, "Lưu sản phẩm thành công!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(AddSanPhamActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
