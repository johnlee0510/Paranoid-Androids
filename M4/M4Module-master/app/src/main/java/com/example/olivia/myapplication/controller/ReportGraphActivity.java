package com.example.olivia.myapplication.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivia.myapplication.model.Graph;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;


import static com.example.olivia.myapplication.model.RetrieveGraphData.graphs;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Olivia on 3/16/2017.
 */

public class ReportGraphActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph_new);
        final Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        final Spinner dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        final TextView endDateText = (TextView) findViewById(R.id.enddate);

        /// giving the manager the option to plot

        ArrayList<String> locationList = new ArrayList<>();
        locationList.addAll(graphs.keySet());

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);


        // start date is at least 3 years behind the end date so we can plot at least 3 points

        final LineChart chart = (LineChart) findViewById(R.id.chart);

        final List<Entry> entries = new ArrayList<>();
        //Sets interval to 1
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DefaultAxisValueFormatter(0));
        xAxis.setGranularity(1f);


        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawLabels(true);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String location = locationSpinner.getSelectedItem().toString();
                //Sets End Date text
                //Result list for specific locations
                ArrayList<Graph> result = graphs.get(location);


                Float[] yearlist = new Float[result.size()];

                for (int i = 0; i < result.size(); i++){
                    try {
                        yearlist[i] = result.get(i).getYear();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Arrays.sort(yearlist);
                endDateText.setText("" + yearlist[yearlist.length - 1]);
                //Populates spinner for start date
                ArrayAdapter<Float> dateAdapter = new ArrayAdapter<Float>(ReportGraphActivity.this, android.R.layout.simple_spinner_item, yearlist);
                dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dateSpinner.setAdapter(dateAdapter);
                entries.clear();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //does nothing
            }


        });

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String location = locationSpinner.getSelectedItem().toString();


                ArrayList<Graph> result = graphs.get(location);


                Float[] time = new Float[result.size()];
                Double[] ppm = new Double[result.size()];


                for (int i = 0; i < result.size(); i++){
                    try {
                        time[i] = result.get(i).getYear();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ppm[i] = result.get(i).getVirusPPM();
                }
                entries.clear();
                //Adds entries after selected start date
                int start = dateSpinner.getSelectedItem() != null ? dateSpinner.getSelectedItemPosition() : 0;
                if (Math.abs(start - ppm.length) >= 3) {
                    for (int i = start; i < ppm.length ; i++) {
                        // turn your data into Entry objects
                        BigDecimal ppmDecimal = new BigDecimal(ppm[i]);
                        entries.add(new Entry(time[i], ppmDecimal.floatValue()));
                    }

                    //Creates data set for entries of given location
                    LineDataSet dataSet = new LineDataSet(entries, "Purity Reports"); // add entries to dataset
                    dataSet.setColor(R.color.seaGreen);
                    dataSet.setValueTextColor(R.color.greyPink);
                    LineData lineData = new LineData(dataSet);
                    chart.setData(lineData);


                    chart.invalidate(); // refresh
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Must be at least a 3 year interval";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //does nothing
            }

        });

        // database uses a timestamp (String), right now, Joe is getting the year value and plotting it
        // on the x axis. ( he is averaging the PPM value in a year and plotting that avg for one year)
        // if we wanted to make the data more specific (montly or even daily) there needs to be more
        //coding to be done
        //

    }
}