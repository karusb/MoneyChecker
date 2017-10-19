package com.example.baz.moneychecker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Settings extends AppCompatActivity {
    protected float dailylimit;
    protected float totalamount;
    protected float saveamount;
    protected long totaldays;
    protected int curedit=0;
    protected String paydaydate;
    public static final String PREFS_NAME = "UserSettings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        totalamount = settings.getFloat("totalsum",0);
        dailylimit  = settings.getFloat("daily",0);
        saveamount = settings.getFloat("saveamount",0);
        paydaydate= settings.getString("payday","dd-mm-yyyy");
        String currency = settings.getString("cur","$");
        Button save = (Button)findViewById(R.id.SaveBut);
        final EditText dailytext = (EditText)findViewById(R.id.DailyLimitInput);
        final EditText totaltext  = (EditText)findViewById(R.id.TotalAmountInput);
        final EditText curtext = (EditText)findViewById(R.id.CurrencySign);
        final TextView curview1 = (TextView)findViewById(R.id.Currencyview1);
        final TextView curview2 = (TextView)findViewById(R.id.CurrencyView2);
        final CheckBox Resetdailylimit = (CheckBox)findViewById(R.id.ResetDailyLimit);
        final Switch MonthlyorDailySwitch = (Switch)findViewById(R.id.switch1);
        final TextView dailylimittext = (TextView)findViewById(R.id.DailyLimitText);
        final TextView estimatedlimittext = (TextView)findViewById(R.id.EstimatedDailyLimitText);
        final TextView estimatedlimit = (TextView)findViewById(R.id.EstimatedDailyLimit);
        final TextView totaldaystext = (TextView)findViewById(R.id.TotalDays);
        final TextView curview3 = (TextView)findViewById(R.id.CurrencyView6);
        final TextView curview4 = (TextView)findViewById(R.id.CurrencyView7);
        final Button matchtotal = (Button)findViewById(R.id.MatchTotalBut);
        final CheckBox wipebox = (CheckBox)findViewById(R.id.WIpeCheckBox);
        final EditText saveamounttext = (EditText)findViewById(R.id.Saving);
        final EditText ddtext = (EditText)findViewById(R.id.ddtext);

        // TODO DATES INTEGRATION

        totaltext.setText(String.valueOf(totalamount));
        dailytext.setText(String.valueOf(dailylimit));
        saveamounttext.setText(String.valueOf(saveamount));
        ddtext.setText(paydaydate);
        curtext.setText(currency);
        curview1.setText(currency);
        curview2.setText(currency);
        curview3.setText(currency);
        curview4.setText(currency);





        Calendar c = Calendar.getInstance();
        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        String[] ymd =formattedDate.split("-");
        final int daysinmonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentday = Integer.valueOf(ymd[2]);


        estimatedlimit.setText(String.valueOf(dailylimit*daysinmonth));
        estimatedlimittext.setText("Estimated Monthly Limit");

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        MonthlyorDailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                                if (b) {

                                                                    dailylimittext.setText("Monthly Limit");
                                                                    estimatedlimit.setText(String.valueOf(Float.valueOf(dailytext.getText().toString())/daysinmonth));
                                                                }
                                                                else {
                                                                    estimatedlimit.setText(String.valueOf(Float.valueOf(dailylimit*daysinmonth)));
                                                                    estimatedlimittext.setText("Estimated Monthly Limit");
                                                                    dailylimittext.setText("Daily Limit");

                                                                }

                                                            }
                                                        });
        totaltext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                totalamount = Float.valueOf(totaltext.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dailytext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    curedit = 1;
                    return true;
                }
                return false;
            }
        });
        dailytext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dailylimit = Float.valueOf(dailytext.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(MonthlyorDailySwitch.isChecked() && dailytext.getText().length() != 0)
                {
                    estimatedlimit.setText(String.valueOf(Float.valueOf(dailytext.getText().toString())/daysinmonth));
                    if(curedit==1)saveamounttext.setText(String.valueOf(Float.valueOf(totalamount-((dailylimit/daysinmonth)*totaldays))));
                }
                else {
                    estimatedlimit.setText(String.valueOf(Float.valueOf(dailylimit*daysinmonth)));
                    if(curedit==1)saveamounttext.setText(String.valueOf(Float.valueOf(totalamount-(dailylimit*totaldays))));
                }

            }
        });
        saveamounttext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    curedit = 2;
                    return true;
                }
                return false;
            }
        });
        saveamounttext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveamount = Float.valueOf(saveamounttext.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            if(curedit ==2)dailytext.setText(String.valueOf(Float.valueOf((totalamount-saveamount)/totaldays)));
            }
        });
        ddtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                    Calendar c = Calendar.getInstance();
                    String[] fymd = ddtext.getText().toString().split("-");
                if((fymd.length==3)&&(TextUtils.isDigitsOnly(fymd[1])&& TextUtils.isDigitsOnly(fymd[0]) && TextUtils.isDigitsOnly(fymd[2]))&&Integer.valueOf(fymd[2] ) <=2040 &&Integer.valueOf(fymd[1]) <=12 &&Integer.valueOf(fymd[0]) <=31  ) {
                    Calendar f = new GregorianCalendar(Integer.valueOf(fymd[2]), Integer.valueOf(fymd[1])-1, Integer.valueOf(fymd[0]));
                    long diff = f.getTimeInMillis() - c.getTimeInMillis();
                    totaldays = TimeUnit.MILLISECONDS.toDays(diff);
                    totaldaystext.setText(String.valueOf(totaldays));
                }
            }
        });
        matchtotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailytext.setText(String.valueOf(Float.valueOf(totaltext.getText().toString())/totaldays));
            }
        });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        curview1.setText(curtext.getText().toString());
                        curview2.setText(curtext.getText().toString());
                        curview3.setText(curtext.getText().toString());
                        curview4.setText(curtext.getText().toString());
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        if(!wipebox.isChecked()) {
                            editor.putFloat("totalsum", Float.valueOf(totaltext.getText().toString()));
                            editor.putFloat("daily", Float.valueOf(dailytext.getText().toString()));
                            editor.putString("cur", curtext.getText().toString());
                            editor.putString("payday",ddtext.getText().toString());
                            if (Resetdailylimit.isChecked()) {

                                if (MonthlyorDailySwitch.isChecked())
                                    editor.putFloat("dailyamount", Float.valueOf(dailytext.getText().toString()) / daysinmonth);
                                else
                                    editor.putFloat("dailyamount", Float.valueOf(dailytext.getText().toString()));

                            }
                            // Commit the edits!
                            editor.commit();
                        }
                        else
                        {
                            editor.clear().commit();
                        }
                        finish();
                        //TODO UPDATE ORIGINAL ACTIVITY
                        Intent MainIntent = new Intent("com.example.baz.moneychecker.MainActivity");
                        startActivity(MainIntent);
                    }
                });





    }

}
