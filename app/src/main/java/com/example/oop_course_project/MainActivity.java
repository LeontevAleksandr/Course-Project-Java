package com.example.oop_course_project;

import LogicWork.CalculateResult;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.example.oop_course_project.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button resultCalculateBtn;
    private Button hashRateCalculateBtn;
    private ActivityMainBinding binding;
    private EditText hashRateET, electricityCostET, electricityConsumptionET, btcToRubET;
    private TextView resultIncomeOnDayTV, resultIncomeOnWeekTV, resultIncomeOnMouthTV;
    private ActivityResultLauncher<Intent> hashRateActivityResultLauncher; // Для получения данных из HashRateActivity

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

        hashRateCalculateBtn = findViewById(R.id.hashRateCalculateBtn);
        hashRateCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем активити HashRateActivity с requestCode
                Intent intent = new Intent(MainActivity.this, HashRateActivity.class);
                hashRateActivityResultLauncher.launch(intent);
            }
        });

        hashRateActivityResultLauncher = registerForActivityResult( // Инициализация метода для получения данных из другого активити
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        double totalHashrate = result.getData().getDoubleExtra("totalHashrate", 0);
                        double totalPowerConsumption = result.getData().getDoubleExtra("totalPowerConsumption", 0);

                        hashRateET.setText(String.valueOf(totalHashrate));
                        electricityConsumptionET.setText(String.valueOf(totalPowerConsumption));
                    }
                }
        );

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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double totalHashrate = data.getDoubleExtra("totalHashrate", 0);
            double totalPowerConsumption = data.getDoubleExtra("totalPowerConsumption", 0);

            hashRateET.setText(String.valueOf(totalHashrate));
            electricityConsumptionET.setText(String.valueOf(totalPowerConsumption));
        }
    }
}