package com.btlon.bantraicay.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.btlon.bantraicay.R;
import com.btlon.bantraicay.models.ChiTietDonHang;
import com.btlon.bantraicay.models.DonHang;
import com.btlon.bantraicay.models.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<DonHang> orderList;
    private Map<String, String> customerNames; // Map lưu tên khách hàng

    public OrderAdapter(List<DonHang> orderList, Map<String, String> customerNames) {
        this.orderList = orderList;
        this.customerNames = customerNames;
    }

    // Cập nhật phương thức thiết lập tên khách hàng
    public void setCustomerNames(Map<String, String> customerNames) {
        this.customerNames = customerNames;
        notifyDataSetChanged();
    }

    // Phương thức cập nhật danh sách đơn hàng sau khi lọc
    public void updateOrderList(List<DonHang> filteredOrderList) {
        this.orderList = filteredOrderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DonHang order = orderList.get(position);

        holder.tvOrderName.setText("Mã ĐH: " + order.getMaDonHang());

        // Hiển thị ngày dưới dạng String, định dạng "dd/MM/yyyy"
        String orderDateString = order.getNgayTao();
        String formattedDate = null;
        try {
            formattedDate = convertDate(orderDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.tvOrderDate.setText(formattedDate);


        holder.tvOrderTotal.setText("Tổng Tiền:  "+ String.format(Locale.getDefault(), "%.0f", order.getTongTien()) + " VND");
        holder.tvOrderStatus.setText(order.getTrangThai());

        // Hiển thị tên khách hàng
        String tenKhachHang = customerNames.get(order.getMaKhachHang());
        if (tenKhachHang != null) {
            holder.tvOrderCustomerName.setText(tenKhachHang);
        } else {
            holder.tvOrderCustomerName.setText("Unknown");
        }
        holder.cardView.setOnClickListener(v -> {
            try {
                showOrderDetailsDialog(holder.itemView.getContext(), order.getMaDonHang(),
                        customerNames.get(order.getMaKhachHang()), convertDate(orderDateString), order.getTongTien() );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private String convertDate(String date) throws ParseException {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date orderDate = iso8601Format.parse(date);
            return displayFormat.format(orderDate);
    }

    private void showOrderDetailsDialog(Context context, String maDonHang, String tenKhachHang, String orderDate, double totalPay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_order_detail, null);
        builder.setView(dialogView);

//        TextView tvOrderID = dialogView.findViewById(R.id.tvOrderID1);
        TableLayout tableLayout = dialogView.findViewById(R.id.table_details);
        TextView tvCusName = dialogView.findViewById(R.id.tvOrderCustomerName);
        TextView tvOrderDate = dialogView.findViewById(R.id.tvOrderDate);
        TextView tvTotalPay = dialogView.findViewById(R.id.tvOrderTotal);

//        tvOrderID.setText("Mã DH: " + maDonHang);
        tvCusName.setText("Tên KH: " +tenKhachHang);
        tvOrderDate.setText("Ngày Tạo: " + orderDate);
        tvTotalPay.setText("Tổng Tiền: " +String.format(Locale.getDefault(), "%.0f VND", totalPay));

        DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("ChiTietDonHang");
        DatabaseReference sanPhamRef = FirebaseDatabase.getInstance().getReference("SanPham");

        chiTietDonHangRef.orderByChild("maDonHang").equalTo(maDonHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChiTietDonHang chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                    if (chiTietDonHang != null) {
                        String maSanPham = chiTietDonHang.getMaSanPham();

                        // Truy vấn tên sản phẩm từ bảng SanPham
                        sanPhamRef.child(maSanPham).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                                if (sanPham != null) {
                                    // Tạo một dòng mới trong bảng
                                    TableRow tableRow = new TableRow(context);

                                    // Tạo TextView cho cột Sản Phẩm
                                    TextView tvNameProduct = new TextView(context);
                                    tvNameProduct.setText(sanPham.getTenSP());
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);
                                    tvNameProduct.setLayoutParams(params);
                                    tvNameProduct.setBackgroundResource(R.drawable.border);
                                    tvNameProduct.setPadding(8, 8, 8, 8);
                                    tvNameProduct.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    tableRow.addView(tvNameProduct);

                                    // Tạo TextView cho cột Định Lượng
                                    TextView tvOrderQuantity = new TextView(context);
                                    tvOrderQuantity.setText(String.valueOf(chiTietDonHang.getDinhLuong()) + " Kg");
                                    tvOrderQuantity.setLayoutParams(params);
                                    tvOrderQuantity.setBackgroundResource(R.drawable.border);
                                    tvOrderQuantity.setPadding(8, 8, 8, 8);
                                    tvOrderQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    tableRow.addView(tvOrderQuantity);

                                    // Tạo TextView cho cột Thành Tiền
                                    TextView tvOrderProductTotal = new TextView(context);
                                    tvOrderProductTotal.setText(String.format(Locale.getDefault(), "%.0f", chiTietDonHang.getTongTienSp()) + " VND");
                                    tvOrderProductTotal.setLayoutParams(params);
                                    tvOrderProductTotal.setBackgroundResource(R.drawable.border);
                                    tvOrderProductTotal.setPadding(8, 8, 8, 8);
                                    tvOrderProductTotal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    tableRow.addView(tvOrderProductTotal);

                                    // Thêm TableRow vào TableLayout
                                    tableLayout.addView(tableRow);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi nếu có
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderName, tvOrderDate, tvOrderTotal, tvOrderStatus, tvOrderCustomerName;
        CardView cardView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderCustomerName = itemView.findViewById(R.id.tvOrderCustomerName); // Thêm TextView cho tên khách hàng
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

