package com.iloved.abcreader.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.iloved.abcreader.R;
import com.iloved.abcreader.fragment.FindBooksFragment;
import com.iloved.abcreader.fragment.ReadListFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static com.iloved.abcreader.util.Globals.initWindow;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private SystemBarTintManager tintManager;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ReadListFragment readListFragment;
    private FindBooksFragment findBooksFragment;

    ImageView menu;
    ImageView search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initWindow(this);
        findView();
        init();
        menu.setOnClickListener(this);
        search.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.message:
                       Toast.makeText(MainActivity.this, "欢迎使用ABC阅读器！", Toast.LENGTH_SHORT).show();
                       break;
                   case R.id.feedback:
                        startActivity(new Intent(MainActivity.this,FeedBack.class));
                       break;
                   case R.id.update:
                       Toast.makeText(MainActivity.this, "当前版本已最新！", Toast.LENGTH_SHORT).show();
                       break;
                   case R.id.about:
                       startActivity(new Intent(MainActivity.this,About.class));
                       break;
               }
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
//        if(tabLayout.getTabAt(0).isSelected()){
//            tabLayout.removeTab(tabLayout.getTabAt(0));
//            tabLayout.addTab(tabLayout.getTabAt(0));
//        }
    }
    void findView() {
        viewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.novel_top);
        menu = findViewById(R.id.main_menu);
        search = findViewById(R.id.main_search);
        drawerLayout = findViewById(R.id.activity_na);
        navigationView = findViewById(R.id.nav);
        View headerView = navigationView.getHeaderView(0);//获取头布局

    }
    void init() {
        readListFragment = new ReadListFragment();
        findBooksFragment = new FindBooksFragment();
        MySimpleFragmentPagerAdapter mySimpleFragmentPagerAdapter = new MySimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mySimpleFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_search:
                Log.d("main_search", "123456");
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.main_menu://点击菜单，跳出侧滑菜单
                Log.d("main_search", "123123");
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;
        }
    }


    private class MySimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        private CharSequence tabTitle[] = new CharSequence[]{"书架", "书城"};
        Fragment[] xixi = new Fragment[]{readListFragment, findBooksFragment};

        MySimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return xixi[position];
        }

        @Override
        public int getCount() {
            return xixi.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("title", position + "" + tabTitle[position]);
            return tabTitle[position];
        }

    }
}