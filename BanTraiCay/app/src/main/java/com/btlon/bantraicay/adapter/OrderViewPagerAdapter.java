package com.btlon.bantraicay.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.btlon.bantraicay.fragment.OrderByDateFragment;
import com.btlon.bantraicay.fragment.OrderByMonthFragment;
import com.btlon.bantraicay.fragment.OrderByTypeFruitFragment;

public class OrderViewPagerAdapter extends FragmentStateAdapter {

    public OrderViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderByDateFragment();  // Fragment cho ngày
            case 1:
                return new OrderByMonthFragment();  // Fragment cho tháng
            case 2:
                return new OrderByTypeFruitFragment();  // Fragment cho năm
            // Thêm các Fragment khác nếu cần
            default:
                return new OrderByDateFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tab
    }

    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Ngày";
            case 1:
                return "Tháng";
            case 2:
                return "Danh Mục";
            default:
                return "";
        }
    }
}
