package com.example.exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Adapters.TestInfoAdapter;
import com.example.exam.Models.Quiz;
import com.example.exam.Models.TestInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddTestActivityAdmin extends AppCompatActivity {

    RecyclerView recyclerView2;
    Toolbar toolbar_add_test;
    FloatingActionButton btn_float_add_test;
    List<TestInfo> testListItems;
    TestInfoAdapter adapter;
    FirebaseFirestore db;
    TestInfo testInfo;
    Query query;
    ListenerRegistration listenerRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test_acitivity_admin);

        toolbar_add_test = findViewById(R.id.toolbar_admin_add_test);
        recyclerView2 = findViewById(R.id.recyclerViewAdmin_add_test);
        btn_float_add_test = findViewById(R.id.btn_float_add_test);

        db = FirebaseFirestore.getInstance();
        testListItems = new ArrayList<>();
        adapter = new TestInfoAdapter(this, testListItems);
//        auth = FirebaseAuth.getInstance();


        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(adapter);


//                db.collection("QUIZES").document(FirebaseAuth.getInstance().getUid())
//                        .collection("TESTS_LIST").document("TESTS_INFO").addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                System.out.println(value.getData());
//                            }
//                        }).remove();

        String quizId = "gUEdZHXlAnnU3Du1F5DK";
        selectFromDb(quizId);
//         loadTestsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        listenerRegistration.remove();
    }

    private void loadTestsList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db.collection("QUIZES")
                .document(auth.getCurrentUser().getUid())
                .collection("TESTS_LIST")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            testListItems.clear();
                            Toast.makeText(AddTestActivityAdmin.this, "Hiii", Toast.LENGTH_SHORT).show();

                            List<DocumentSnapshot> dd = task.getResult().getDocuments();
                            for (DocumentSnapshot document : task.getResult()) {
                                TestInfo testModel = document.toObject(TestInfo.class);
                                testListItems.add(testModel);
                            }


                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AddTestActivityAdmin.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void selectFromDb(String quizId) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        testListItems.clear();
        db.collection("QUIZES").document(quizId).collection("TESTS_LIST").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot testSnapshots) {
                        if (testSnapshots.isEmpty()) {
                            Log.e("FirestoreError", "No TESTS_LIST subcollection for quiz: " + quizId);
                        } else {
                            // Loop through each document in the TESTS_LIST sub-collection
                            for (QueryDocumentSnapshot testDoc : testSnapshots) {
                                if (testDoc.exists()) {
                                    Map<String, Object> testData = testDoc.getData();
                                    Log.d("TESTS_LAST_FIELDS", testData.size()+ " Fields for TESTS_LAST in quiz " + quizId + ": " + testData.toString());
                                    for (int i = 1; i <= testData.size()/2; i++) {
                                        String testId = "TEST" + String.valueOf(i) + "_ID";
                                        String timeId = "TEST" + String.valueOf(i) + "_TIME";
                                        String id = testData.get(testId).toString();
                                        String time = testData.get(timeId).toString();

                                        testListItems.add(new TestInfo(id, time));
                                    }

                                } else {
                                    Log.e("FirestoreError", "Document does not exist in TESTS_LAST for quiz: " + quizId);
                                }
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirestoreError", "Error fetching TESTS_LIST sub-collection for quiz: " + quizId, e);
                    }
                });

    }


}






