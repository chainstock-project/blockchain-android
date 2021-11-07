package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.Board;

import java.io.IOException;

public class BoardCreateFragment extends Fragment {
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_create, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);

        Button boardCreateButton = rootView.findViewById(R.id.boardCreateButton);
        boardCreateButton.setOnClickListener(new onClickBoardCreateButton());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity mainActivity = (MainActivity) getActivity();
        SearchView searchView = mainActivity.getSearchView();
        if (searchView != null) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            ActionBar actionBar = mainActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            MenuItem item = mainActivity.menu.findItem(R.id.menu_search);
            item.setVisible(true);
            BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    class onClickBoardCreateButton implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                MainActivity mainActivity = (MainActivity)getActivity();
                EditText boardTitleInput = rootView.findViewById(R.id.boardTitleInput);
                EditText boardBodyInput = rootView.findViewById(R.id.boardBodyInput);

                String txHash = mainActivity.bd.createBoard(mainActivity.username, boardTitleInput.getText().toString(), boardBodyInput.getText().toString());
                while(!mainActivity.bc.checkTxCommitted(txHash));

                mainActivity.getSupportFragmentManager().popBackStack();
            } catch (IOException e) {
                Tools.showDialog(rootView.getContext(), "게시", "게시판생성실패!!");
            }
        }
    }
}