package com.btlon.bantraicay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.bantraicay.activity.OrderActivity;
import com.btlon.bantraicay.R;
import com.btlon.bantraicay.adapter.OrderAdapter;
import com.btlon.bantraicay.models.DonHang;
import com.btlon.bantraicay.models.KhachHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderByDateFragment extends Fragment {

    private RecyclerView rvOrders;
    private TextView tvCurrentDate;
    private OrderAdapter adapter;
    private List<DonHang> orders;
    private Map<String, String> customerNames; // Map to store customer names by their IDs

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_by_date, container, false);

        // Hiển thị ngày hiện tại
        tvCurrentDate = view.findViewById(R.id.tvCurrentDate);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tvCurrentDate.setText(currentDate);

        // Thiết lập RecyclerView
        rvOrders = view.findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orders = new ArrayList<>();
        adapter = new OrderAdapter(orders, new HashMap<>()); // Truyền map trống
        rvOrders.setAdapter(adapter);

        // Khởi tạo map để lưu tên khách hàng
        customerNames = new HashMap<>();

        // Kết nối tới Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DonHang");

        // Lắng nghe thay đổi dữ liệu đơn hàng
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonHang donHang = snapshot.getValue(DonHang.class);
                    if (donHang != null) {
                        ////Nếu không thuộc ngày hện tại thì sẽ không hiện các đơn hàng
                        if (isOrderFromToday(donHang.getNgayTao())) {
                            orders.add(donHang);
                        }
                    }
                }

                OrderActivity activity = (OrderActivity) getActivity();
                if (activity != null) {
                    // Hiển thị hoặc ẩn thông báo "Chưa có đơn hàng nào"
                    if (orders.isEmpty()) {
                        activity.showErrorMessage(); // Hiển thị thông báo
                    } else {
                        activity.hideErrorMessage(); // Ẩn thông báo nếu có đơn hàng
                    }
                }

                // Cập nhật adapter với đơn hàng
                adapter.notifyDataSetChanged();

                // Lấy tên khách hàng từ Firebase
                loadCustomerNames();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });

        return view;
    }

    ///Hàm so sánh ơn hàng với ngày hiện tại
    private boolean isOrderFromToday(String ngayTao) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date orderDate = iso8601Format.parse(ngayTao);
            String orderDateString = dayFormat.format(orderDate);
            String currentDate = dayFormat.format(new Date());
            return currentDate.equals(orderDateString); // So sánh ngày tạo với ngày hiện tại
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm để lấy tên khách hàng
    private void loadCustomerNames() {
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("KhachHang");

        customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    KhachHang khachHang = snapshot.getValue(KhachHang.class);
                    if (khachHang != null) {
                        customerNames.put(khachHang.getMaKhachHang(), khachHang.getTen());
                    }
                }
                // Cập nhật adapter với tên khách hàng
                adapter.setCustomerNames(customerNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }
}
