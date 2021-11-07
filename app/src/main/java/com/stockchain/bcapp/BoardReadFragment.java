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
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stockchain.cosmos.Board;

import java.io.IOException;

public class BoardReadFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_read, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);


        TextView boardReadTitle = rootView.findViewById(R.id.boardReadTitle);
        boardReadTitle.setText(mainActivity.readBoardInform.getTitle());
        TextView boardReadBody = rootView.findViewById(R.id.boardReadBody);
        boardReadBody.setText(mainActivity.readBoardInform.getBody());


        Button buttonBoardDelete = rootView.findViewById(R.id.buttonBoardDelete);
        buttonBoardDelete.setOnClickListener(new onClickBoardDeleteButton());

        Button buttonBoardUpdate = rootView.findViewById(R.id.buttonBoardUpdate);
        buttonBoardUpdate.setOnClickListener(new onClickBoardUpdateButton());

        // Inflate the layout for this fragment
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

    class onClickBoardDeleteButton implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity)getActivity();
            Board board = new Board(mainActivity.getApplicationContext());
            try {
                board.deleteBoard(mainActivity.username, mainActivity.readBoardInform.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainActivity.getSupportFragmentManager().popBackStack();
        }
    }

    class onClickBoardUpdateButton implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity)getActivity();
            Board board = new Board(mainActivity.getApplicationContext());
            mainActivity.getSupportFragmentManager().popBackStack();
        }
    }
}