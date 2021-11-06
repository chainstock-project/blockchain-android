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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.stockchain.cosmos.StockTransaction;
import com.stockchain.cosmos.StockTransactionInform;
import com.stockchain.cosmos.Tools;

import java.io.IOException;

public class StockTransactionBuyFragment extends Fragment {
    ViewGroup rootView;
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stock_transaction_buy, container, false);
        mainActivity = (MainActivity)getActivity();

        TextView buyName = rootView.findViewById(R.id.buyName);
        buyName.setText(mainActivity.inqueryStockDataInform.getName());
        TextView buyCode = rootView.findViewById(R.id.buyCode);
        buyCode.setText(mainActivity.inqueryStockDataInform.getCode());
        TextView buyMarketType = rootView.findViewById(R.id.buyMarketType);
        buyMarketType.setText(mainActivity.inqueryStockDataInform.getMarket_type());
        TextView buyPrice = rootView.findViewById(R.id.buyPrice);
        buyPrice.setText(String.valueOf(mainActivity.inqueryStockDataInform.getAmount()));

        EditText buyCountInput = rootView.findViewById(R.id.buyCountInput);
        buyCountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")) {
                    TextView totalBuyPriceView = rootView.findViewById(R.id.totalBuyPrice);
                    totalBuyPriceView.setText("0");
                    TextView buyRemainBalancesView = rootView.findViewById(R.id.buyRemainBalances);
                    String buyRemainBalances = mainActivity.stockBankInform.getBalances() + " -> " + mainActivity.stockBankInform.getBalances();
                    buyRemainBalancesView.setText(buyRemainBalances);
                    TextView buyCount = rootView.findViewById(R.id.buyCount);
                    int numberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
                    buyCount.setText(numberOfStock + " -> " + numberOfStock);
                }else {
                    int count = Integer.parseInt(editable.toString());
                    long afterbuyRemainBalances = mainActivity.stockBankInform.getBalances() - (mainActivity.inqueryStockDataInform.getAmount() * count);
                    if (afterbuyRemainBalances < 0) {
                        while (afterbuyRemainBalances < 0) {
                            count--;
                            afterbuyRemainBalances = mainActivity.stockBankInform.getBalances() - (mainActivity.inqueryStockDataInform.getAmount() * count);
                        }
                        buyCountInput.setText(String.valueOf(count));
                        return;
                    }


                    TextView totalBuyPriceView = rootView.findViewById(R.id.totalBuyPrice);
                    long totalBuyPrice = mainActivity.inqueryStockDataInform.getAmount() * count;
                    totalBuyPriceView.setText(String.valueOf(totalBuyPrice));

                    TextView buyRemainBalancesView = rootView.findViewById(R.id.buyRemainBalances);
                    afterbuyRemainBalances = mainActivity.stockBankInform.getBalances() - totalBuyPrice;
                    String buyRemainBalancesString = mainActivity.stockBankInform.getBalances() + " -> " + afterbuyRemainBalances;
                    buyRemainBalancesView.setText(buyRemainBalancesString);

                    TextView buyCount = rootView.findViewById(R.id.buyCount);
                    int numberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
                    long afterBuyCount = numberOfStock + count;
                    String buyCountString = numberOfStock + " -> " + afterBuyCount;
                    buyCount.setText(buyCountString);
                }
            }
        });

        TextView totalBuyPrice = rootView.findViewById(R.id.totalBuyPrice);
        totalBuyPrice.setText("0");

        TextView buyRemainBalances = rootView.findViewById(R.id.buyRemainBalances);
        String buyRemainBalancesString = mainActivity.stockBankInform.getBalances() + " -> " + mainActivity.stockBankInform.getBalances();
        buyRemainBalances.setText(buyRemainBalancesString);

        TextView buyCount = rootView.findViewById(R.id.buyCount);
        int numberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
        buyCount.setText(numberOfStock + " -> " + numberOfStock);


        Button buttonBuy= (Button) rootView.findViewById(R.id.buttonBuy) ;
        buttonBuy.setOnClickListener(new onClickButtonBuy());

        return rootView;
    }

    class onClickButtonBuy implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                EditText buyCountInput = rootView.findViewById(R.id.buyCountInput);
                int buyCount = Integer.parseInt(buyCountInput.getText().toString());
                String code = mainActivity.inqueryStockDataInform.getCode();

                StockTransaction stockTransaction = new StockTransaction(mainActivity.getApplicationContext());
                stockTransaction.createStockTransaction(mainActivity.username, code, buyCount);

                int beforeNumberOfStock = StockTransaction.getNumberOfStock(mainActivity.stockTransactionInformList, mainActivity.inqueryStockDataInform.getCode());
                int afterNumberOfStock = beforeNumberOfStock + buyCount;

                if(stockTransaction.checkStockTransactionCreated(mainActivity.address, code, afterNumberOfStock)){
                    mainActivity.stockTransactionInformList = stockTransaction.getStockTransactionInformList(mainActivity.address);
                    mainActivity.stockBankInform = stockTransaction.getStockBankInform(mainActivity.stockTransactionInformList, mainActivity.address);
                    getParentFragmentManager().popBackStack();
                }
                else{
                    Tools.showDialog(rootView.getContext(), "매수", "매수실퍠!");
                }
            } catch (IOException e) {
                Tools.showDialog(rootView.getContext(), "매수", "매수실퍠!");
            }
        }
    }
}

;