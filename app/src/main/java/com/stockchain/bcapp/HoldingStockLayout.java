package com.stockchain.bcapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class HoldingStockLayout extends FrameLayout {
    TextView holdingStockName;
    TextView holdingStockEarningRate;
    TextView holdingStockCount;
    TextView holdingStockCurrentPrice;
    TextView holdingStockPurchasePrice;

    public HoldingStockLayout(Context context) {
        super(context);
        init(context);
    }

    public HoldingStockLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.holding_stock, this, true);
        holdingStockName = findViewById(R.id.holdingStockName);
        holdingStockEarningRate = findViewById(R.id.holdingStockEarningRate);
        holdingStockCount = findViewById(R.id.holdingStockCount);
        holdingStockCurrentPrice = findViewById(R.id.holdingStockCurrentPrice);
        holdingStockPurchasePrice = findViewById(R.id.holdingStockPurchasePrice);
    }

    public void setHoldingStockLName(String holdingStockLName) {
        this.holdingStockName.setText(holdingStockLName);
    }

    public void setHoldingStockEarningRate(String holdingStockEarningRate) {
        this.holdingStockEarningRate.setText(holdingStockEarningRate);
    }

    public void setHoldingStockCount(String holdingStockCount) {
        this.holdingStockCount.setText(holdingStockCount);
    }

    public void setHoldingStockCurrentPrice(String holdingStockCurrentPrice) {
        this.holdingStockCurrentPrice.setText(holdingStockCurrentPrice);
    }

    public void setHoldingStockPurchasePrice(String holdingStockPurchasePrice) {
        this.holdingStockPurchasePrice.setText(holdingStockPurchasePrice);
    }
}
