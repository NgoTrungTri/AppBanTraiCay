package com.btlon.bantraicay.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.btlon.bantraicay.R;
import com.btlon.bantraicay.models.LoaiTraiCay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FruitTypeAdapter extends ArrayAdapter<LoaiTraiCay> {

    public FruitTypeAdapter(Context context, List<LoaiTraiCay> fruitTypes) {
        super(context, 0, fruitTypes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items_fruit_type, parent, false);
        }

        LoaiTraiCay fruitType = getItem(position);

        TextView tvMaLoai = convertView.findViewById(R.id.tvMaLoai);
        TextView tvTenLoai = convertView.findViewById(R.id.tvTenLoai);
        ImageButton btnOptions = convertView.findViewById(R.id.btnOptions);

        if (fruitType != null) {
            tvMaLoai.setText("Mã: " + fruitType.getMaLoai());
            tvTenLoai.setText(fruitType.getTenLoai());
        }

        btnOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_fruit_type_options, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_edit) {
                    editFruitType(fruitType);
                    return true;
                } else if (item.getItemId() == R.id.menu_delete) {
                    deleteFruitType(fruitType);
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });

        convertView.setOnClickListener(v -> showOptionsDialog(fruitType));

        return convertView;
    }

    ////Hiện menu chọn option
    private void showOptionsDialog(LoaiTraiCay fruitType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tùy chọn");
        String[] options = {"Cập Nhật", "Xóa"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                editFruitType(fruitType);
            } else if (which == 1) {
                deleteFruitType(fruitType);
            } else if (which == 2) {
                viewProducts(fruitType);
            }
        });
        builder.show();
    }

    ///Hàm Sửa
    private void editFruitType(LoaiTraiCay fruitType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_fruit_type, null);

        EditText etMaLoai = dialogView.findViewById(R.id.etMaLoai);
        EditText etTenLoai = dialogView.findViewById(R.id.etTenLoai);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Hiển thị thông tin hiện tại để chỉnh sửa
        etMaLoai.setText(fruitType.getMaLoai());
        etMaLoai.setEnabled(false); // Không cho phép chỉnh sửa mã loại
        etTenLoai.setText(fruitType.getTenLoai());

        builder.setView(dialogView)
                .setTitle("Chỉnh sửa loại trái cây")
                .setNegativeButton("Hủy", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSave.setOnClickListener(v -> {
            String tenLoai = etTenLoai.getText().toString().trim();

            if (tenLoai.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên loại trái cây!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật đối tượng hiện tại
            LoaiTraiCay updatedFruitType = new LoaiTraiCay(fruitType.getMaLoai(), tenLoai);
            DatabaseReference loaiTraiCayRef = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");

            loaiTraiCayRef.child(fruitType.getMaLoai()).setValue(updatedFruitType)
                    .addOnSuccessListener(aVoid -> {
                        // Cập nhật đối tượng trong danh sách và làm mới giao diện
                        fruitType.setTenLoai(tenLoai);
                        notifyDataSetChanged();

                        // Cập nhật maLoaiTraiCay trong các sản phẩm liên quan
                        updateProductCategory(fruitType);

                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            dialog.dismiss();
        });
    }

    // Hàm để cập nhật maLoaiTraiCay trong các sản phẩm
    private void updateProductCategory(LoaiTraiCay fruitType) {
        DatabaseReference sanPhamRef = FirebaseDatabase.getInstance().getReference("SanPham");

        // Tìm các sản phẩm có maLoaiTraiCay bằng với maLoai của LoaiTraiCay hiện tại
        sanPhamRef.orderByChild("maLoaiTraiCay/maLoai").equalTo(fruitType.getMaLoai())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            // Cập nhật maLoaiTraiCay trong từng sản phẩm
                            productSnapshot.getRef().child("maLoaiTraiCay").setValue(fruitType);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "onCancelled: " + error.getMessage());
                    }
                });
    }

     ///Ham Xoa
    private void deleteFruitType(LoaiTraiCay fruitType) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa")
                .setMessage("Bạn có chắc chắn muốn xóa mục này? Các sản phẩm liên quan cũng sẽ bị xóa.")
                .setPositiveButton("Có", (dialog, which) -> {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                    // Xóa loại trái cây
                    dbRef.child("LoaiTraiCay").orderByChild("maLoai").equalTo(fruitType.getMaLoai())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }

                                    // Xóa các sản phẩm liên quan
                                    dbRef.child("SanPham").orderByChild("maLoaiTraiCay/maLoai").equalTo(fruitType.getMaLoai())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        dataSnapshot.getRef().removeValue();
                                                    }
                                                    // Thông báo thành công
                                                    Toast.makeText(getContext(), "Đã xóa loại trái cây và sản phẩm liên quan", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e("FirebaseError", "onCancelled: " + error.getMessage());
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("FirebaseError", "onCancelled: " + error.getMessage());
                                }
                            });
                })
                .setNegativeButton("Không", null)
                .show();
    }

    ///Ham Xem cac san pham thuoc maLoaiTraiCay
    private void viewProducts(LoaiTraiCay fruitType) {
    }
}
