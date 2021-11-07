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
import com.stockchain.cosmos.Tools;

import java.io.IOException;

public class BoardUpdateFragment extends Fragment {

    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_update, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
        bottomNavigationView.setVisibility(View.INVISIBLE);


        EditText boardUpdateTitleInput = rootView.findViewById(R.id.boardUpdateTitleInput);
        boardUpdateTitleInput.setText(mainActivity.mainBoardInform.getTitle());
        EditText boardUpdateBodyInput = rootView.findViewById(R.id.boardUpdateBodyInput);
        boardUpdateBodyInput.setText(mainActivity.mainBoardInform.getBody());

        Button boardUpdateButton = rootView.findViewById(R.id.boardUpdateButton);
        boardUpdateButton.setOnClickListener(new onClickBoardUpdateButton());


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

    class onClickBoardUpdateButton implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                MainActivity mainActivity = (MainActivity)getActivity();
                EditText boardUpdateTitleInput = rootView.findViewById(R.id.boardUpdateTitleInput);
                EditText boardUpdateBodyInput = rootView.findViewById(R.id.boardUpdateBodyInput);
                mainActivity.mainBoardInform.getId();
                mainActivity.mainBoardInform.setTitle(boardUpdateTitleInput.getText().toString());
                mainActivity.mainBoardInform.setBody(boardUpdateBodyInput.getText().toString());
                String txHash = mainActivity.bd.updateBoard(mainActivity.username, mainActivity.mainBoardInform.getId(), mainActivity.mainBoardInform.getTitle(), mainActivity.mainBoardInform.getBody());
                while(!mainActivity.bc.checkTxCommitted(txHash));

                mainActivity.getSupportFragmentManager().popBackStack();
            } catch (IOException e) {
                e.printStackTrace();
                Tools.showDialog(rootView.getContext(), "게시", "게시판수정실패!!");
            }
        }
    }
}