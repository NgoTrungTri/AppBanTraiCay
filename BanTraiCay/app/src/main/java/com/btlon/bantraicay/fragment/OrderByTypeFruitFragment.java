//package com.btlon.bantraicay.fragment;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.btlon.bantraicay.R;
//import com.btlon.bantraicay.adapter.OrderAdapter;
//import com.btlon.bantraicay.models.ChiTietDonHang;
//import com.btlon.bantraicay.models.DonHang;
//import com.btlon.bantraicay.models.LoaiTraiCay;
//import com.btlon.bantraicay.models.SanPham;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class OrderByTypeFruitFragment extends Fragment {
//
//    private Spinner spinnerTypeFruit;
//    private RecyclerView rvOrders;
//    private OrderAdapter orderAdapter;
//    private List<DonHang> allOrders = new ArrayList<>();
//    private List<ChiTietDonHang> allChiTietDonHang = new ArrayList<>();
//    private Map<String, String> customerNames = new HashMap<>();
//    private DatabaseReference dbLoaiTraiCay, dbDonHang, dbChiTietDonHang, dbSanPham;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_order_by_type_fruit, container, false);
//
//        spinnerTypeFruit = view.findViewById(R.id.spinnertypefruit);
//        rvOrders = view.findViewById(R.id.rvOrders);
//
//        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
//        orderAdapter = new OrderAdapter(allOrders, customerNames);
//        rvOrders.setAdapter(orderAdapter);
//
//        dbLoaiTraiCay = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");
//        dbDonHang = FirebaseDatabase.getInstance().getReference("DonHang");
//        dbChiTietDonHang = FirebaseDatabase.getInstance().getReference("ChiTietDonHang");
//        dbSanPham = FirebaseDatabase.getInstance().getReference("SanPham");
//
//        loadSpinnerData();
//        loadOrderData();
//        loadChiTietDonHangData();
//
//        spinnerTypeFruit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LoaiTraiCay selectedLoaiTraiCay = (LoaiTraiCay) parent.getItemAtPosition(position);
//                filterOrdersByType(selectedLoaiTraiCay.getMaLoai());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Xử lý khi không chọn gì
//            }
//        });
//
//        return view;
//    }
//
//    private void loadSpinnerData() {
//        dbLoaiTraiCay.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<LoaiTraiCay> loaiTraiCayList = new ArrayList<>();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    LoaiTraiCay loaiTraiCay = data.getValue(LoaiTraiCay.class);
//                    loaiTraiCayList.add(loaiTraiCay);
//                }
//                ArrayAdapter<LoaiTraiCay> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, loaiTraiCayList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinnerTypeFruit.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi
//            }
//        });
//    }
//
//    private void loadOrderData() {
//        dbDonHang.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                allOrders.clear();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    DonHang donHang = data.getValue(DonHang.class);
//                    allOrders.add(donHang);
//                }
//                orderAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi
//            }
//        });
//    }
//
//    private void loadChiTietDonHangData() {
//        dbChiTietDonHang.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                allChiTietDonHang.clear();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    ChiTietDonHang chiTietDonHang = data.getValue(ChiTietDonHang.class);
//                    allChiTietDonHang.add(chiTietDonHang);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi
//            }
//        });
//    }
//
//    private void filterOrdersByType(String maLoaiTraiCay) {
//        List<String> filteredOrderIds = new ArrayList<>();
//
//        for (ChiTietDonHang chiTietDonHang : allChiTietDonHang) {
//            dbSanPham.child(chiTietDonHang.getMaSanPham()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
//                    if (sanPham != null && sanPham.getMaLoaiTraiCay().equals(maLoaiTraiCay)) {
//                        if (!filteredOrderIds.contains(chiTietDonHang.getMaDonHang())) {
//                            filteredOrderIds.add(chiTietDonHang.getMaDonHang());
//                        }
//                    }
//                    if (dataSnapshot.getChildrenCount() == allChiTietDonHang.size()) {
//                        updateOrdersList(filteredOrderIds);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Xử lý lỗi
//                }
//            });
//        }
//    }
//
//    private void updateOrdersList(List<String> filteredOrderIds) {
//        List<DonHang> filteredOrders = new ArrayList<>();
//        for (DonHang donHang : allOrders) {
//            if (filteredOrderIds.contains(donHang.getMaDonHang())) {
//                filteredOrders.add(donHang);
//            }
//        }
//        orderAdapter.updateOrderList(filteredOrders);
//    }
//}
package com.btlon.bantraicay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.bantraicay.activity.OrderActivity;
import com.btlon.bantraicay.R;
import com.btlon.bantraicay.adapter.OrderAdapter;
import com.btlon.bantraicay.models.ChiTietDonHang;
import com.btlon.bantraicay.models.DonHang;
import com.btlon.bantraicay.models.KhachHang;
import com.btlon.bantraicay.models.LoaiTraiCay;
import com.btlon.bantraicay.models.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderByTypeFruitFragment extends Fragment {

    private Spinner spinnerTypeFruit;
    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<DonHang> allOrders = new ArrayList<>();
    private List<ChiTietDonHang> allChiTietDonHang = new ArrayList<>();
    private Map<String, String> customerNames = new HashMap<>();
    private Map<String, SanPham> productMap = new HashMap<>();
    private DatabaseReference dbLoaiTraiCay, dbDonHang, dbChiTietDonHang, dbSanPham;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_by_type_fruit, container, false);

        spinnerTypeFruit = view.findViewById(R.id.spinnertypefruit);
        rvOrders = view.findViewById(R.id.rvOrders);

        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(allOrders, customerNames);
        rvOrders.setAdapter(orderAdapter);

        dbLoaiTraiCay = FirebaseDatabase.getInstance().getReference("LoaiTraiCay");
        dbDonHang = FirebaseDatabase.getInstance().getReference("DonHang");
        dbChiTietDonHang = FirebaseDatabase.getInstance().getReference("ChiTietDonHang");
        dbSanPham = FirebaseDatabase.getInstance().getReference("SanPham");

        loadSpinnerData();
        loadOrderData();
        loadChiTietDonHangData();

        spinnerTypeFruit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiTraiCay selectedLoaiTraiCay = (LoaiTraiCay) parent.getItemAtPosition(position);
                filterOrdersByType(selectedLoaiTraiCay.getMaLoai());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không chọn gì
            }
        });

        return view;
    }

    private void loadSpinnerData() {
        dbLoaiTraiCay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LoaiTraiCay> loaiTraiCayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    LoaiTraiCay loaiTraiCay = data.getValue(LoaiTraiCay.class);
                    loaiTraiCayList.add(loaiTraiCay);
                }
                ArrayAdapter<LoaiTraiCay> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, loaiTraiCayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTypeFruit.setAdapter(adapter);
                loadCustomerNames();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    private void loadOrderData() {
        dbDonHang.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOrders.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    DonHang donHang = data.getValue(DonHang.class);
                    allOrders.add(donHang);
                }

                OrderActivity activity = (OrderActivity) getActivity();
                if (activity != null) {
                    // Hiển thị hoặc ẩn thông báo "Chưa có đơn hàng nào"
                    if (allOrders.isEmpty()) {
                        activity.showErrorMessage(); // Hiển thị thông báo
                    } else {
                        activity.hideErrorMessage(); // Ẩn thông báo nếu có đơn hàng
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    private void loadChiTietDonHangData() {
        dbChiTietDonHang.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allChiTietDonHang.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ChiTietDonHang chiTietDonHang = data.getValue(ChiTietDonHang.class);
                    allChiTietDonHang.add(chiTietDonHang);
                }
                loadProductData(); // Load product data after loading chiTietDonHang
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    private void loadProductData() {
        dbSanPham.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productMap.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SanPham sanPham = data.getValue(SanPham.class);
                    if (sanPham != null) {
                        productMap.put(sanPham.getMaSanPham(), sanPham);
                    }
                }
                // Filter orders after loading product data
                String selectedLoaiTraiCay = ((LoaiTraiCay) spinnerTypeFruit.getSelectedItem()).getMaLoai();
                filterOrdersByType(selectedLoaiTraiCay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    private void filterOrdersByType(String maLoaiTraiCay) {
        List<String> filteredOrderIds = new ArrayList<>();

        for (ChiTietDonHang chiTietDonHang : allChiTietDonHang) {
            SanPham sanPham = productMap.get(chiTietDonHang.getMaSanPham());
            if (sanPham != null && sanPham.getMaLoaiTraiCay().equals(maLoaiTraiCay)) {
                if (!filteredOrderIds.contains(chiTietDonHang.getMaDonHang())) {
                    filteredOrderIds.add(chiTietDonHang.getMaDonHang());
                }
            }
        }
        updateOrdersList(filteredOrderIds);
    }

    private void updateOrdersList(List<String> filteredOrderIds) {
        List<DonHang> filteredOrders = new ArrayList<>();
        for (DonHang donHang : allOrders) {
            if (filteredOrderIds.contains(donHang.getMaDonHang())) {
                filteredOrders.add(donHang);
            }
        }
        orderAdapter.updateOrderList(filteredOrders);
    }

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
                orderAdapter.setCustomerNames(customerNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }
}
