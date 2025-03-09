package com.btlon.bantraicay;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.btlon.bantraicay.DonHangAdapter;
import com.btlon.bantraicay.models.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ActivityLichSuMuaHang extends AppCompatActivity {

    private RecyclerView rvOrders;
    private DonHangAdapter donHangAdapter;
    private List<DonHang> donHangList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_mua_hang);

        rvOrders = findViewById(R.id.rvOrders);
        donHangList = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(donHangList);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(donHangAdapter);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("DonHang");

        // Assume we have the user's ID after login
        String userId = getUserId(); // Implement this method to get current user's ID

        // Load orders for the logged-in user
        loadUserOrders(userId);
    }

    private void loadUserOrders(String userId) {
        databaseReference.orderByChild("maKhachHang").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donHangList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonHang donHang = snapshot.getValue(DonHang.class);
                    donHangList.add(donHang);
                }
                donHangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActivityLichSuMuaHang.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUserId() {
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        return userId;
        return "KH002";
    }
}
