package com.example.oop_course_project;

import LogicWork.CalculateResult;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.oop_course_project.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    CalculateResult profitabilityPerDay;
    private Button resultCalculateBtn;
    private Button hashRateCalculateBtn;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private EditText hashRateET, electricityCostET, electricityConsumptionET, btcToRubET;
    private TextView resultIncomeOnDayTV, resultIncomeOnWeekTV, resultIncomeOnMouthTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resultCalculateBtn = findViewById(R.id.resultCalculateBtn);
        hashRateET = findViewById(R.id.hashRateET);
        electricityCostET = findViewById(R.id.electricityCostET);
        electricityConsumptionET = findViewById(R.id.electricityConsumptionET);
        btcToRubET = findViewById(R.id.btcToRubET);
        resultIncomeOnDayTV = findViewById(R.id.resultIncomeOnDayTV);
        resultIncomeOnWeekTV = findViewById(R.id.resultIncomeOnWeekTV);
        resultIncomeOnMouthTV = findViewById(R.id.resultIncomeOnMouthTV);

        resultCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateProfitability();
            }
        });

//        resultCalculateBtn = findViewById(R.id.resultCalculateBtn);
//        resultCalculateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                profitabilityPerDay.profitabilityPerDay(14,5882684.03,1372,4);
//                Log.v("result", profitabilityPerDay.profitabilityPerDayToString());
//            }
//        });

        hashRateCalculateBtn = findViewById(R.id.hashRateCalculateBtn);
        hashRateCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем новое активити HashRateActivity
                Intent intent = new Intent(MainActivity.this, HashRateActivity.class);
                startActivity(intent);
            }
        });

    }

    private void calculateProfitability() {
        try {
            // Получение данных из полей ввода
            int hashRate = Integer.parseInt(hashRateET.getText().toString());
            double electricityCost = Double.parseDouble(electricityCostET.getText().toString());
            double powerConsumptionW = Double.parseDouble(electricityConsumptionET.getText().toString());
            double btcToRub = Double.parseDouble(btcToRubET.getText().toString());

            // Расчет доходности
            double dayProfitability = CalculateResult.profitabilityPerDay(hashRate, btcToRub, powerConsumptionW, electricityCost);
            double weekProfitability = CalculateResult.profitabilityPerWeek(hashRate, btcToRub, powerConsumptionW, electricityCost);
            double monthProfitability = CalculateResult.profitabilityPerMouth(hashRate, btcToRub, powerConsumptionW, electricityCost);

            // Отображение результатов
            resultIncomeOnDayTV.setText("Доход за день: " + String.format("%.2f", dayProfitability) + " RUB");
            resultIncomeOnWeekTV.setText("Доход за неделю: " + String.format("%.2f", weekProfitability) + " RUB");
            resultIncomeOnMouthTV.setText("Доход за месяц: " + String.format("%.2f", monthProfitability) + " RUB");

        } catch (NumberFormatException e) {
            Log.e("MainActivity", "Ошибка ввода: " + e.getMessage());
            // Можно добавить отображение ошибки пользователю, например с помощью Toast
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}