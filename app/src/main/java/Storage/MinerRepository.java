package Storage;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinerRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference minersCollection;

    public MinerRepository() {
        this.firestore = FirebaseFirestore.getInstance();
        this.minersCollection = firestore.collection("miners"); // Укажите правильное имя коллекции
    }

    public interface OnMinersLoadedListener {
        void onMinersLoaded(HashMap<String, Miner> minerData, List<String> minerNames);
        void onError(Exception e);
    }

    public interface OnAddMinerListener {
        void onAddMinerSuccess();
        void onAddMinerError(Exception e);
    }

    public interface OnDeleteMinerListener {
        void onDeleteSuccess();
        void onDeleteError(Exception e);
    }

    public void loadMiners(OnMinersLoadedListener listener) {
        minersCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap<String, Miner> minerData = new HashMap<>();
                        List<String> minerNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Miner miner = document.toObject(Miner.class);
                            miner.setDocumentId(document.getId()); // Сохраняем ID документа
                            minerData.put(miner.getName(), miner);
                            minerNames.add(miner.getName());
                        }
                        listener.onMinersLoaded(minerData, minerNames);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public void addMiner(Miner miner, OnAddMinerListener listener) {
        minersCollection.add(miner)
                .addOnSuccessListener(documentReference -> listener.onAddMinerSuccess())
                .addOnFailureListener(e -> listener.onAddMinerError(e));
    }

    public void deleteMiner(String documentId, OnDeleteMinerListener listener) {
        DocumentReference documentRef = minersCollection.document(documentId);
        documentRef.delete()
                .addOnSuccessListener(aVoid -> listener.onDeleteSuccess())
                .addOnFailureListener(e -> listener.onDeleteError(e));
    }
}