package com.btlon.bantraicay.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.btlon.bantraicay.R;
import com.btlon.bantraicay.adapter.FruitTypeAdapter;
import com.btlon.bantraicay.models.LoaiTraiCay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView fruitTypesListView;
    private FruitTypeAdapter adapter;
    private List<LoaiTraiCay> fruitTypes = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        fruitTypesListView = findViewById(R.id.fruitTypesListView);
        adapter = new FruitTypeAdapter(this, fruitTypes);
        fruitTypesListView.setAdapter(adapter);

        // Khởi tạo Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");

        Button btAddNew = findViewById(R.id.addNewType);
        btAddNew.setOnClickListener(v -> addNewTypeFruit());

        loadFruitTypes();
    }

    private void loadFruitTypes() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fruitTypes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LoaiTraiCay fruitType = snapshot.getValue(LoaiTraiCay.class);
                    if (fruitType != null) {
                        fruitTypes.add(fruitType);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    ///Hàm Tạo LoaiTraiCay mới
    private void addNewTypeFruit () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_loai_traicay, null);
        builder.setView(dialogView);

        EditText etMaLoai = dialogView.findViewById(R.id.etMaLoai);
        EditText etTenLoai = dialogView.findViewById(R.id.etTenLoai);
        Button btnAddLoaiTraiCay = dialogView.findViewById(R.id.btnAddLoaiTraiCay);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAddLoaiTraiCay.setOnClickListener(v -> {
            String maLoai = etMaLoai.getText().toString().trim();
            String tenLoai = etTenLoai.getText().toString().trim();

            if (maLoai.isEmpty() || tenLoai.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng LoaiTraiCay mới
            LoaiTraiCay newLoaiTraiCay = new LoaiTraiCay(maLoai, tenLoai);

            // Lưu vào Firebase Realtime Database
            DatabaseReference loaiTraiCayRef = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");
            loaiTraiCayRef.child(maLoai)
                    .setValue(newLoaiTraiCay)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Thêm loại trái cây thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
