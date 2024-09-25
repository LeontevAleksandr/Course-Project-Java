package com.example.oop_course_project;

import Storage.Miner;
import Storage.MinerRepository;

import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

public class EditingMinerDataBaseActivity extends AppCompatActivity {
    private EditText minerNameEditText, hashrateEditText, powerConsumptionEditText;
    private Button saveButton;
    private Button backBtn;
    private Spinner minerSpinner;
    private Button deleteButton;
    private HashMap<String, Miner> minerData;
    private MinerRepository minerRepository;
    private FirebaseFirestore firestore; // Ссылка на Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editing_miner_database_activity);

        minerNameEditText = findViewById(R.id.minerNameEditText);
        hashrateEditText = findViewById(R.id.hashrateEditText);
        powerConsumptionEditText = findViewById(R.id.powerConsumptionEditText);
        saveButton = findViewById(R.id.saveBtn);
        minerSpinner = findViewById(R.id.minerSpinner);
        deleteButton = findViewById(R.id.deleteBtn);

        // Инициализация Firestore и репозитория
        firestore = FirebaseFirestore.getInstance();
        minerRepository = new MinerRepository();

        // Загружаем майнеров в Spinner
        loadMiners();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMiner(); // Вызов метода для сохранения майнера
                loadMiners();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMiner(); // Вызов метода для удаления майнера
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Завершить активити и вернуться на предыдущий экран
            }
        });
    }

    private void loadMiners() {
        minerRepository.loadMiners(new MinerRepository.OnMinersLoadedListener() {
            @Override
            public void onMinersLoaded(HashMap<String, Miner> minerData, List<String> minerNames) {
                EditingMinerDataBaseActivity.this.minerData = minerData;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditingMinerDataBaseActivity.this,
                        android.R.layout.simple_spinner_item, minerNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                minerSpinner.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(EditingMinerDataBaseActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMiner() {
        String selectedMinerName = minerSpinner.getSelectedItem().toString();
        Miner selectedMiner = minerData.get(selectedMinerName);

        if (selectedMiner != null) {
            // Получаем ID документа для удаления
            String documentId = selectedMiner.getDocumentId(); // Добавьте этот метод в класс Miner

            minerRepository.deleteMiner(documentId, new MinerRepository.OnDeleteMinerListener() {
                @Override
                public void onDeleteSuccess() {
                    Toast.makeText(EditingMinerDataBaseActivity.this, "Майнер успешно удалён", Toast.LENGTH_SHORT).show();
                    loadMiners(); // Обновляем список майнеров
                }

                @Override
                public void onDeleteError(Exception e) {
                    Toast.makeText(EditingMinerDataBaseActivity.this, "Ошибка удаления: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Ошибка: выбранный майнер не найден", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMiner() {
        String name = minerNameEditText.getText().toString().trim();
        String hashrateStr = hashrateEditText.getText().toString().trim();
        String powerStr = powerConsumptionEditText.getText().toString().trim();

        if (name.isEmpty() || hashrateStr.isEmpty() || powerStr.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        double hashrate = Double.parseDouble(hashrateStr);
        double powerConsumption = Double.parseDouble(powerStr);

        // Создание нового объекта Miner
        Miner miner = new Miner(name, hashrate, powerConsumption);

        minerRepository.addMiner(miner, new MinerRepository.OnAddMinerListener() {
            @Override
            public void onAddMinerSuccess() {
                Toast.makeText(EditingMinerDataBaseActivity.this, "Майнер успешно сохранен", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddMinerError(Exception e) {
                Toast.makeText(EditingMinerDataBaseActivity.this, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
