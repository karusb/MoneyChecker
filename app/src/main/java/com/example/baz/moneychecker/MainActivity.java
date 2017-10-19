package com.example.baz.moneychecker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.example.baz.moneychecker.Settings.PREFS_NAME;

public class MainActivity extends AppCompatActivity {
    protected float totalmoney;
    protected float spending;

    protected float dailylimit;
    protected float maxspend;
    protected float saving;
    protected float monthlyamount;
    protected String payday;
    protected long daystopayday;
    protected float remainingmoney;
    protected float lastonlineday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        setContentView(R.layout.activity_main);
        Button Spend = (Button) findViewById(R.id.SpendBut);
        Button Logs = (Button) findViewById(R.id.LogsBut);
        Button Settings = (Button) findViewById(R.id.SettingsBut);
        TextView MoneyAvailable = (TextView) findViewById(R.id.MoneyAvailable);
        TextView DailyMoney = (TextView) findViewById(R.id.MoneyAvailableToday);
        TextView Curview = (TextView) findViewById(R.id.Curview);
        TextView Date =  (TextView)findViewById(R.id.DateText);
        TextView MonthlyMoney = (TextView)findViewById(R.id.MontlyAmount);
        TextView Savings = (TextView)findViewById(R.id.Savings);
        TextView Curview3 = (TextView)findViewById(R.id.currencyview3);
        TextView Curview4 = (TextView)findViewById(R.id.CurrencyView4);
        TextView Curview5 = (TextView)findViewById(R.id.CurrencyView5);


        final ProgressBar dailyspentprogress = (ProgressBar) findViewById(R.id.progressBar);
        final ProgressBar monthlyspentprogress = (ProgressBar)findViewById(R.id.MonthlyProgressBar);
        final EditText SpendMoneyInput = (EditText) findViewById(R.id.SpentMoneyInput);

        float totalamount = settings.getFloat("totalsum", 0);
        dailylimit = settings.getFloat("daily", 0);
        spending = settings.getFloat("dailyamount", dailylimit);
        String currency = settings.getString("cur", "$");
        payday = settings.getString("payday","01-01-2020");
        remainingmoney = settings.getFloat("remaining", totalamount);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        String[] ymd =formattedDate.split("-");
        final int daysinmonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentday = Integer.valueOf(ymd[2]);
        lastonlineday = (settings.getFloat("lastonline", (currentday)));
        String[] fymd = payday.split("-");
        if((fymd.length==3)&&(TextUtils.isDigitsOnly(fymd[1])&& TextUtils.isDigitsOnly(fymd[0]) && TextUtils.isDigitsOnly(fymd[2]))&&Integer.valueOf(fymd[2] ) <=2040 &&Integer.valueOf(fymd[1]) <=12 &&Integer.valueOf(fymd[0]) <=31  ) {
            Calendar f = new GregorianCalendar(Integer.valueOf(fymd[2]), Integer.valueOf(fymd[1]) - 1, Integer.valueOf(fymd[0]));
            long diff = f.getTimeInMillis() - c.getTimeInMillis();
            daystopayday = TimeUnit.MILLISECONDS.toDays(diff);
        }
        totalmoney = totalamount;
        if(currentday != lastonlineday)spending = dailylimit;
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("lastonline", currentday).commit();
        saving = totalmoney-(dailylimit*daystopayday);
        MoneyAvailable.setText(String.valueOf(totalmoney));
        DailyMoney.setText(String.valueOf(spending));
        Curview.setText(currency);
        Curview3.setText(currency);
        Curview4.setText(currency);
        Curview5.setText(currency);
        MonthlyMoney.setText(String.valueOf ((dailylimit*(daysinmonth-currentday))+spending));
        Savings.setText(String.valueOf(saving));

        Date.setText(formattedDate);
        dailyspentprogress.setMax(Integer.valueOf((int) dailylimit));
        dailyspentprogress.setProgress(Integer.valueOf((int)spending));
        monthlyspentprogress.setMax(Integer.valueOf((int) dailylimit)*daysinmonth);
        monthlyspentprogress.setProgress(Integer.valueOf(Integer.valueOf((int)dailylimit)*daysinmonth));

        Spend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainingmoney -= Double.valueOf(SpendMoneyInput.getText().toString());
                spending -= Double.valueOf(SpendMoneyInput.getText().toString());
                saving = totalmoney-(dailylimit*daystopayday);
                // TODO SAVING OPTIONS

                dailyspentprogress.setProgress(Integer.valueOf((int)spending));
                monthlyspentprogress.setProgress(Integer.valueOf(Integer.valueOf((int)dailylimit)*daysinmonth)-Integer.valueOf(SpendMoneyInput.getText().toString()));
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putFloat("remaining", remainingmoney);
                editor.putFloat("dailyamount", spending);
                editor.commit();
                MainActivity.super.recreate();
            }
        });
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent SettingsIntent = new Intent("com.example.baz.moneychecker.Settings");
                startActivity(SettingsIntent);

            }
        });

    }
}
