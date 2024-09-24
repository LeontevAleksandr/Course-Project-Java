package com.example.oop_course_project;

import Storage.Miner;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class HashRateActivity extends AppCompatActivity {
    private Spinner minerSpinner;
    private EditText minerQuantity;
    private Button addButton, clearButton;
    private TextView totalHashrateTV;
    private TextView totalPowerConsumptionTV;
    private TableLayout minerTable;
    private double totalHashrate = 0;
    private double totalPowerConsumption = 0;
    private HashMap<String, Miner> minerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashrate);
        Button backButton = findViewById(R.id.buttonBack);

        // Установить обработчик нажатия
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Закрыть текущее активити и вернуться в предыдущее
                finish();
            }
        });

        minerSpinner = findViewById(R.id.minerSpinner);
        minerQuantity = findViewById(R.id.minerQuantity);
        addButton = findViewById(R.id.addButton);
        clearButton = findViewById(R.id.clearButton);
        totalHashrateTV = findViewById(R.id.totalHashrateTV);
        minerTable = findViewById(R.id.minerTable);
        totalPowerConsumptionTV = findViewById(R.id.totalPowerConsumptionTV);

        // Инициализируем данные майнеров, их хешрейт и потребление
        minerData = new HashMap<>();
        minerData.put("Bitmain Antminer S9", new Miner("Bitmain Antminer S9", 14.0, 1350.0));
        minerData.put("Bitmain Antminer S19 PRO", new Miner("Bitmain Antminer S19 PRO", 110.0, 3250.0));
        minerData.put("Whatsminer M30S", new Miner("Whatsminer M30S", 86.0, 3268.0));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, minerData.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minerSpinner.setAdapter(adapter);


        // Обработчик кнопки "Добавить"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedMiner = minerSpinner.getSelectedItem().toString();
                int quantity = Integer.parseInt(minerQuantity.getText().toString());

                Miner miner = minerData.get(selectedMiner);
                double hashRatePerMiner = miner.getHashrate();
                double powerPerMiner = miner.getPowerConsumption();

                double totalForThisMiner = hashRatePerMiner * quantity;
                double totalPowerForThisMiner = powerPerMiner * quantity;

                // Обновляем общий хешрейт
                totalHashrate += totalForThisMiner;
                totalHashrateTV.setText("Общий хешрейт: " + totalHashrate + " TH");

                // Обновляем общее потребление энергии
                totalPowerConsumption += totalPowerForThisMiner;
                totalPowerConsumptionTV.setText("Общее потребление энергии: " + totalPowerConsumption + " Вт");

                // Добавляем строку в таблицу
                addRowToTable(selectedMiner, quantity, totalPowerForThisMiner, totalForThisMiner);
            }
        });

        // Обработчик кнопки "Стереть"
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Сбрасываем хешрейт
                totalHashrate = 0;
                totalHashrateTV.setText("Общий хешрейт: 0 TH");

                // Сбрасываем потребление энергии
                totalPowerConsumption = 0;
                totalPowerConsumptionTV.setText("Общее потребление энергии: 0 Вт");

                // Удаляем все строки в таблице, кроме заголовка
                int childCount = minerTable.getChildCount();
                if (childCount > 1) {
                    minerTable.removeViews(0, childCount); // Удаляем все строки, начиная с 1
                }
            }
        });
    }

    // Метод для добавления строки в таблицу
    private void addRowToTable(String miner, int quantity, double powerConsumption, double hashRate) {
        TableRow row = new TableRow(this);

        // Создаем TextView для названия майнера
        TextView minerName = new TextView(this);
        minerName.setText(miner);
        minerName.setPadding(0, 8, 8, 8);

        // Создаем TextView для количества майнеров
        TextView minerQty = new TextView(this);
        minerQty.setText(String.valueOf(quantity));
        minerQty.setPadding(8, 8, 8, 8);
        minerQty.setGravity(Gravity.CENTER);

        // Создаем TextView для потребления энергии
        TextView minerPower = new TextView(this);
        minerPower.setText(String.valueOf(powerConsumption));
        minerPower.setPadding(0, 8, 20, 8);
        minerPower.setGravity(Gravity.CENTER);

        // Создаем TextView для хэшрейта
        TextView minerHashRate = new TextView(this);
        minerHashRate.setText(String.valueOf(hashRate));
        minerHashRate.setPadding(8, 8, 8, 8);
        minerHashRate.setGravity(Gravity.END);

        // Добавляем созданные TextView в строку
        row.addView(minerName);
        row.addView(minerQty);
        row.addView(minerPower);
        row.addView(minerHashRate);

        // Добавляем строку в таблицу
        minerTable.addView(row);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();  // Закрывает активити и возвращает на предыдущее
        return true;
    }
}
