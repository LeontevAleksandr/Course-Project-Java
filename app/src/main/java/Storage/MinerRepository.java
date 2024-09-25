package Storage;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MinerRepository {
    private FirebaseFirestore db;

    public MinerRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void loadMiners(final OnMinersLoadedListener listener) {
        db.collection("miners").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Miner> minerData = new HashMap<>();
                            List<String> minerNames = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    String name = document.getString("name");
                                    double powerConsumption = document.getDouble("powerConsumption");
                                    double hashrate = document.getDouble("hashrate");
                                    String documentId = document.getId(); // Получаем ID документа для удаления из БД

                                    Miner miner = new Miner(name, hashrate, powerConsumption);
                                    miner.setDocumentId(documentId); // Устанавливаем ID
                                    minerData.put(name, miner);
                                    minerNames.add(name);
                                } catch (RuntimeException ex) {
                                    Log.v("loadMiners", ex.getMessage());
                                }
                            }

                            listener.onMinersLoaded(minerData, minerNames);
                        } else {
                            // Обработка ошибок
                            listener.onError(task.getException());
                        }
                    }
                });
    }

    public void addMiner(Miner miner, OnAddMinerListener listener) {
        // Создание нового документа в коллекции "miner"
        db.collection("miner").add(miner)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            listener.onAddMinerSuccess();
                        } else {
                            listener.onAddMinerError(task.getException());
                        }
                    }
                });
    }

    public interface OnMinersLoadedListener {
        void onMinersLoaded(HashMap<String, Miner> minerData, List<String> minerNames);
        void onError(Exception e);
    }

    public interface OnAddMinerListener {
        void onAddMinerSuccess();
        void onAddMinerError(Exception e);
    }
}
