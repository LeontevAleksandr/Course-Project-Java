package com.example.oop_course_project;

import Storage.Miner;
import Storage.MinerRepository;

import android.content.Intent;
import android.os.Bundle;
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

        // Обработчик нажатия
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Передача посчитанного хешрейта и энергопотрбления в MainActivity
                Intent intent = new Intent();
                intent.putExtra("totalHashrate", totalHashrate);
                intent.putExtra("totalPowerConsumption", totalPowerConsumption);
                setResult(RESULT_OK, intent);
                // Закрыть текущее активити
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

                // Очищаем таблицу
                minerTable.removeAllViews();
            }
        });

        addMinerBtn = findViewById(R.id.editingMinerDataBaseBtn);
        addMinerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход к экрану редактирования базы данных майнеров
                Intent intent = new Intent(HashRateActivity.this, EditingMinerDataBaseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadMiners() {
        minerRepository.loadMiners(new MinerRepository.OnMinersLoadedListener() {
            @Override
            public void onMinersLoaded(HashMap<String, Miner> minerData, List<String> minerNames) {
                HashRateActivity.this.minerData = minerData;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(HashRateActivity.this,
                        android.R.layout.simple_spinner_item, minerNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                minerSpinner.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(HashRateActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRowToTable(String minerName, int quantity, double powerForThisMiner, double totalForThisMiner) {
        TableRow row = new TableRow(this);
        TextView nameTextView = new TextView(this);
        nameTextView.setText(minerName);
        nameTextView.setPadding(16, 16, 16, 16);

        TextView quantityTextView = new TextView(this);
        quantityTextView.setText(String.valueOf(quantity));
        quantityTextView.setPadding(16, 16, 16, 16);

        TextView powerTextView = new TextView(this);
        powerTextView.setText(String.valueOf(powerForThisMiner));
        powerTextView.setPadding(16, 16, 16, 16);

        TextView totalTextView = new TextView(this);
        totalTextView.setText(String.valueOf(totalForThisMiner));
        totalTextView.setPadding(16, 16, 16, 16);

        row.addView(nameTextView);
        row.addView(quantityTextView);
        row.addView(powerTextView);
        row.addView(totalTextView);
        minerTable.addView(row);
    }
}