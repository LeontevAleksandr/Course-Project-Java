package com.example.oop_course_project;

import Storage.Miner;
import Storage.MinerRepository;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

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
    private MinerRepository minerRepository;
    private Button addMinerBtn;

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

        // Инициализируем репозиторий майнеров
        minerRepository = new MinerRepository();

        // Загружаем майнеров из Firestore
        loadMiners();

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

        addMinerBtn = findViewById(R.id.addMinerBtn);
        addMinerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход к новому активити
                Intent intent = new Intent(HashRateActivity.this, EditingMinerDataBaseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadMiners() {
        try {
            minerRepository.loadMiners(new MinerRepository.OnMinersLoadedListener() {
                @Override
                public void onMinersLoaded(HashMap<String, Miner> minerData, List<String> minerNames) {
                    HashRateActivity.this.minerData = minerData;

                    // Обновляем адаптер Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(HashRateActivity.this, android.R.layout.simple_spinner_item, minerNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    minerSpinner.setAdapter(adapter);

                    // Лог успешной загрузки
                    Log.d("loadMiners", "Майнеры успешно загружены");
                }

                @Override
                public void onError(Exception e) {
                    // Обработка ошибок
                    Toast.makeText(HashRateActivity.this, "Ошибка загрузки майнеров: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("loadMiners", "Ошибка при загрузке майнеров", e);
                }
            });
        } catch (Exception e) {
            // Дополнительная обработка возможных исключений
            Toast.makeText(HashRateActivity.this, "Не удалось загрузить майнеров: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("loadMiners", "Неожиданная ошибка при загрузке майнеров", e);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        // Перезагрузить майнеров при возвращении в активити
        loadMiners();
    }
}
