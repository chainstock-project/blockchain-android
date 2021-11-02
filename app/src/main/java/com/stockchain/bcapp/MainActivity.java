package com.stockchain.bcapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    MainHomeFragment mainHomeFragment;
    MainMockFragment mainMockFragment;
    MainFeedFragment mainFeedFragment;
    MainRankFragment mainRankFragment;
    MainSearchFragment mainSearchFragment;
    MainSettingFragment mainSettingFragment;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHomeFragment = new MainHomeFragment();
        mainMockFragment = new MainMockFragment();
        mainFeedFragment = new MainFeedFragment();
        mainRankFragment = new MainRankFragment();
        mainSettingFragment = new MainSettingFragment();
        mainSearchFragment = new MainSearchFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainHomeFragment).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainHomeFragment).commit();
                        return true;
                    case R.id.menu_bottom_mock:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainMockFragment).commit();
                        return true;
                    case R.id.menu_bottom_feed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainFeedFragment).commit();
                        return true;
                    case R.id.menu_bottom_rank:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainRankFragment).commit();
                        return true;
                    case R.id.menu_bottom_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainSettingFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView sv = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        sv.setQueryHint("주식이름입력");
        sv.setMaxWidth(Integer.MAX_VALUE);
        sv.setOnSearchClickListener(new onClickSearchView());
        sv.setOnQueryTextListener(new onQueryTextSearchView());

        return true;
    }

    class onClickSearchView implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainSearchFragment).commit();
        }
    }

    class onQueryTextSearchView implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        switch (curId){
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}