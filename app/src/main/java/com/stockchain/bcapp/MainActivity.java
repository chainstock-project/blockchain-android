package com.stockchain.bcapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.Account;
import com.stockchain.cosmos.BlockChain;
import com.stockchain.cosmos.Board;
import com.stockchain.cosmos.BoardInform;
import com.stockchain.cosmos.PreferenceManager;
import com.stockchain.cosmos.StockBankInform;
import com.stockchain.cosmos.StockData;
import com.stockchain.cosmos.StockDataInform;
import com.stockchain.cosmos.StockTransaction;
import com.stockchain.cosmos.StockTransactionInform;
import com.stockchain.cosmos.StockTransactionRecordInform;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Menu menu;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;

    MainHomeFragment mainHomeFragment;
    MainFeedFragment mainFeedFragment;
    MainBoardFragment mainBoardFragment;
    MainSettingFragment mainSettingFragment;
    MainSearchFragment mainSearchFragment;
    MainStockInqueryFragment mainStockInqueryFragment;
    MainStockTransactionFragment mainStockTransactionFragment;
    BoardReadFragment boardReadFragment;

    BlockChain bc;
    Account ac;
    StockData sd;
    StockTransaction st;
    Board bd;

    String username;
    String address;
    StockBankInform stockBankInform;
    ArrayList<StockDataInform> stockDataList;
    ArrayList<StockTransactionInform> stockTransactionInformList;
    ArrayList<StockTransactionRecordInform> stockTransactionRecordInformList;
    BoardInform mainBoardInform;
    StockDataInform inqueryStockDataInform;

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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainHomeFragment).commit();
                        return true;
                    case R.id.menu_bottom_mock:
                        MainMockFragment mainMockFragment = new MainMockFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainMockFragment).commit();
                        return true;
                    case R.id.menu_bottom_feed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainFeedFragment).commit();
                        return true;
                    case R.id.menu_bottom_board:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainBoardFragment).commit();
                        return true;
                    case R.id.menu_bottom_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainSettingFragment).commit();
                        return true;
                }
                return false;
            }
        });

        mainHomeFragment = new MainHomeFragment();
        mainFeedFragment = new MainFeedFragment();
        mainBoardFragment = new MainBoardFragment();
        mainSettingFragment = new MainSettingFragment();
        mainSearchFragment = new MainSearchFragment();
        mainStockInqueryFragment = new MainStockInqueryFragment();
        mainStockTransactionFragment = new MainStockTransactionFragment();
        boardReadFragment = new BoardReadFragment();

        bc = new BlockChain(getApplicationContext(), getString(R.string.server_ip));
        ac = new Account(getApplicationContext());
        sd = new StockData(getApplicationContext());
        st = new StockTransaction(getApplicationContext());
        bd = new Board(getApplicationContext());

        PreferenceManager pm = new PreferenceManager();
        username = pm.getString(getApplicationContext(), "username");
        address = pm.getString(getApplicationContext(), "address");

        try {
            stockDataList = sd.getStockDataList();
            stockTransactionInformList = st.getStockTransactionInformList(address);
            stockBankInform = st.getStockBankInform(stockTransactionInformList, address);;
            stockTransactionRecordInformList = st.getStockTransactionRecord(address);
            st.addStockTransactionRecordDate(stockTransactionRecordInformList);

//            Board board = new Board(getApplicationContext());
//            try {
//                String tx = board.createBoard(username, "test title", "test body");
//                boolean check = bc.checkTxCommitted(tx);
//                boolean check2 = bc.checkTxCommitted(tx);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            this.finishAffinity();
            System.exit(0);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainHomeFragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("주식이름입력");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainSearchFragment).addToBackStack(null).commit();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        switch (curId){
            //back key
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }
}