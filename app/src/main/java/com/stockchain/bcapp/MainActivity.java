package com.stockchain.bcapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    MainHomeFragment mainHomeFragment;
    MainMockFragment mainMockFragment;
    MainFeedFragment mainFeedFragment;
    MainRankFragment mainRankFragment;
    MainSettingFragment mainSettingFragment;
    MainSearchFragment mainSearchFragment;

    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

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
        searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("주식이름입력");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnSearchClickListener(new onClickSearchView());
        searchView.setOnQueryTextListener(new onQueryTextSearchView(this));
        return true;
    }

    class onClickSearchView implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainSearchFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        switch (curId){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}