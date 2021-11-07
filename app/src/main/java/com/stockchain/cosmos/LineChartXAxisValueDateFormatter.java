package com.stockchain.cosmos;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LineChartXAxisValueDateFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        Date date = new Date();
        date.setTime((long) value*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        return sdf.format(date);
    }
}