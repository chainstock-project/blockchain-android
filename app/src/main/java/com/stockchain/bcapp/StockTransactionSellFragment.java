package com.stockchain.bcapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stockchain.cosmos.StockTransaction;
import com.stockchain.cosmos.Tools;

import java.io.IOException;

public class StockTransactionSellFragment extends Fragment {
    ViewGroup rootView;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stock_transaction_sell, container, false);
        mainActivity = (MainActivity)getActivity();

        EditText sellCountInput = rootView.findViewById(R.id.sellCountInput);
        sellCountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")) {
                    TextView totalSellPriceView = rootView.findViewById(R.id.totalSellPrice);
                    totalSellPriceView.setText("0");

                    TextView sellRemainBalancesView = rootView.findViewById(R.id.sellRemainBalances);
                    String sellRemainBalancesString = mainActivity.stockBankInform.getBalances() + " -> " + mainActivity.stockBankInform.getBalances();
                    sellRemainBalancesView.setText(sellRemainBalancesString);

                    TextView sellCount = rootView.findViewById(R.id.sellCount);
                    int numberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
                    sellCount.setText(numberOfStock + " -> " + numberOfStock);
                }else {
                    int count = Integer.parseInt(editable.toString());
                    int numberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
                    if (count > numberOfStock) {
                        sellCountInput.setText(String.valueOf(numberOfStock));
                        return;
                    }

                    TextView totalSellPriceView = rootView.findViewById(R.id.totalSellPrice);
                    long totalSellPrice = mainActivity.inqueryStockDataInform.getAmount() * count;
                    totalSellPriceView.setText(String.valueOf(totalSellPrice));

                    TextView sellRemainBalancesView = rootView.findViewById(R.id.sellRemainBalances);
                    long afterSellRemainBalances = mainActivity.stockBankInform.getBalances() + totalSellPrice;
                    String sellRemainBalancesString = mainActivity.stockBankInform.getBalances() + " -> " + afterSellRemainBalances;
                    sellRemainBalancesView.setText(sellRemainBalancesString);

                    TextView sellCount = rootView.findViewById(R.id.sellCount);
                    long afterSellCount = numberOfStock - count;
                    String sellCountString = numberOfStock + " -> " + afterSellCount;
                    sellCount.setText(sellCountString);
                }
            }
        });


        TextView sellName = rootView.findViewById(R.id.sellName);
        sellName.setText(mainActivity.inqueryStockDataInform.getName());
        TextView sellCode = rootView.findViewById(R.id.sellCode);
        sellCode.setText(mainActivity.inqueryStockDataInform.getCode());
        TextView sellMarketType = rootView.findViewById(R.id.sellMarketType);
        sellMarketType.setText(mainActivity.inqueryStockDataInform.getMarket_type());
        TextView sellPrice = rootView.findViewById(R.id.sellPrice);
        sellPrice.setText(String.valueOf(mainActivity.inqueryStockDataInform.getAmount()));
        TextView totalSellPrice = rootView.findViewById(R.id.totalSellPrice);
        totalSellPrice.setText("0");
        TextView sellRemainBalances = rootView.findViewById(R.id.sellRemainBalances);
        String sellRemainBalancesString = mainActivity.stockBankInform.getBalances() + " -> " + mainActivity.stockBankInform.getBalances();
        sellRemainBalances.setText(sellRemainBalancesString);
        TextView sellCount = rootView.findViewById(R.id.sellCount);
        int numberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
        String sellCountString =  numberOfStock + " -> " + numberOfStock;
        sellCount.setText(sellCountString);

        Button buttonSell= (Button) rootView.findViewById(R.id.buttonSell) ;
        buttonSell.setOnClickListener(new onClickButtonSell());
        // Inflate the layout for this fragment
        return rootView;
    }

    class onClickButtonSell implements Button.OnClickListener {
        @Override
        public void onClick(cView view) {
            try {
                EditText sellCountInput = rootView.findViewById(R.id.sellCountInput);
                int sellCount = Integer.parseInt(sellCountInput.getText().toString());
                String code = mainActivity.inqueryStockDataInform.getCode();

                String txHash = mainActivity.st.deleteStockTransaction(mainActivity.username, code, sellCount);
                while(!mainActivity.bc.checkTxCommitted(txHash));

                mainActivity.stockTransactionInformList = stockTransaction.getStockTransactionInformList(mainActivity.address);
                mainActivity.stockTransactionRecordInformList = stockTransaction.getStockTransactionRecord(mainActivity.address);
                stockTransaction.addStockTransactionRecordDate(mainActivity.stockTransactionRecordInformList);
                mainActivity.stockBankInform = stockTransaction.getStockBankInform(mainActivity.stockTransactionInformList, mainActivity.address);
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().popBackStack();
            } catch (IOException e) {
                Tools.showDialog(rootView.getContext(), "매도", "매도실패!");
            }
        }

    }
}