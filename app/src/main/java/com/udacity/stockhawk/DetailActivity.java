package com.udacity.stockhawk;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.stock_name)
    TextView symbol;

    @BindView(R.id.stock_price)
    TextView stockPrice;

    @BindView(R.id.chart)
    BarChart chart;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar=this.getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle("");
        }
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Cursor data = getContentResolver().query(getIntent().getData(),
                    null,
                    null,
                    null,
                    null);
            assert data != null;
            data.moveToFirst();
            String name= data.getString(data.getColumnIndex((Contract.Quote.COLUMN_SYMBOL)));
            String date= data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            float price= data.getFloat(data.getColumnIndex(Contract.Quote.COLUMN_PRICE));
            symbol.setText(name);
            stockPrice.setText(price+"");
            updateChart(date);
            data.close();
        }


    }
    public String lastCharacter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='x') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }
    private void updateChart(String data) {

        String[] array = data.split("@@");
        Log.v("array", Arrays.toString(array));
        String month=lastCharacter(array[0]);
        String price=lastCharacter(array[1]);
        String[] months = month.split(",");
        String[] prices = price.split(",");

//        List<Entry> entries = new ArrayList<Entry>();
//
//
//        for (int i=0;i<array.length;i++) {
//
//            entries.add(new Entry(Float.valueOf(array[i]),i));
//        }
//        LineDataSet dataSet = new LineDataSet(entries, "Label");
//        dataSet.setColor(R.color.material_blue_500);
//        LineData lineData = new LineData(dataSet);
//        chart.setData(lineData);
//        chart.invalidate();



        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0;i<prices.length;i++)
        {
            entries.add(new BarEntry(Float.valueOf(prices[i]), i));
        }
        BarDataSet dataset = new BarDataSet(entries, "");
        ArrayList<String> labels = new ArrayList<>();
        Collections.addAll(labels, months);
        BarData datas = new BarData(labels, dataset);
        chart.setData(datas);
        chart.setDescription("");
        chart.setDoubleTapToZoomEnabled(false);
        dataset.setColors(ColorTemplate.LIBERTY_COLORS);
        chart.animateY(5000);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


}
