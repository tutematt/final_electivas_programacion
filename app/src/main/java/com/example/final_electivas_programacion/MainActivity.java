package com.example.final_electivas_programacion;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_menu_principal);

        tabLayout = findViewById(R.id.tabLayoutMP);
        viewPager = findViewById(R.id.viewpaged);
        tabLayout.setupWithViewPager(viewPager);
        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new fragment_vuelo(), "Vuelos");
        adapter.addFragment(new fragment_reserva(), "Reservas");
        adapter.addFragment(new fragment_config(), "Configuracion");

        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.flight_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.travel_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.settings_icon);



    }
}
