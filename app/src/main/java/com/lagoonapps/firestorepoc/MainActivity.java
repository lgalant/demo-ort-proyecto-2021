package com.lagoonapps.firestorepoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private String TAG="PDP-2021";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obtenerUsuarios();

    }


    private void obtenerUsuarios() {
        db = FirebaseFirestore.getInstance();
        db.collection("pedidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> datos = document.getData();
                                for (Map.Entry<String, Object> entry : datos.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    Log.d(TAG, "Key = " + key + ", Value = " + value);
                                    Log.d(TAG, "typeof value:" + value.getClass().getName());
                                    if (value.getClass().getName().equals("com.google.firebase.firestore.DocumentReference")) {
                                        Log.d(TAG, "Encontramos documentref");
                                        DocumentReference dr = (DocumentReference) value;
                                        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot ds = task.getResult();
                                                Log.d(TAG, "El get snapshot me da:" + ds.getData());
                                            }
                                        });

                                    }
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
