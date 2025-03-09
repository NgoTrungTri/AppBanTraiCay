package com.btlon.bantraicay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.btlon.bantraicay.R;
import com.btlon.bantraicay.adapter.OrderViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderActivity extends AppCompatActivity {
    private TabLayout tab;
    private ViewPager2 viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        tab = findViewById(R.id.tab_layout);
        viewpager = findViewById((R.id.view_pager2));

        OrderViewPagerAdapter orderViewPagerAdapter = new OrderViewPagerAdapter(this);
        viewpager.setAdapter(orderViewPagerAdapter);

        // Liên kết ViewPager2 với TabLayout
        new TabLayoutMediator(tab, viewpager,
                (tab, position) -> tab.setText(orderViewPagerAdapter.getPageTitle(position))
        ).attach();
    }

    public void showErrorMessage() {
        TextView errorTextView = findViewById(R.id.error);
        if (errorTextView != null) {
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    public void hideErrorMessage() {
        TextView errorTextView = findViewById(R.id.error);
        if (errorTextView != null) {
            errorTextView.setVisibility(View.GONE);
        }
    }
}