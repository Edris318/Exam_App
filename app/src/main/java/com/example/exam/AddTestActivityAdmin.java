package com.example.exam;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Adapters.TestInfoAdapter;
import com.example.exam.Interface.MyCompleteListener;
import com.example.exam.Models.TestInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
    FirebaseAuth auth;


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

//        recyclerView2.setHasFixedSize(true);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView2.setAdapter(adapter);


        // getAllTestsData();


        // fetchTestData();


    }


    private void fetchTestData() {
        db.collection("QUIZZES")
                .document(FirebaseAuth.getInstance().getUid()) // Replace with your actual quiz document ID
                .collection("TESTS_LIST")
                .document("TESTS_INFO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Firestore", "DocumentSnapshot data: " + document.getData());

                                // Extract test data
                                testListItems.clear();
                                testListItems.add(new TestInfo(document.getString("TEST1_ID"), document.getString("TEST1_TIME")));
                                // Add more test data if needed (TEST2_ID, TEST2_TIME, etc.)

                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void getAllTestsData(MyCompleteListener completeListener) {
        testListItems.clear(); // Clear the existing list

        db.collection("QUIZES").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot quizDoc : task.getResult()) {
                        // Reference to the TESTS_LIST subcollection
                        CollectionReference testsListRef = quizDoc.getReference().collection("TESTS_LIST");

                        // Get the TESTS_INFO document
                        testsListRef.document("TESTS_INFO").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot testsInfoDoc = task.getResult();
                                    if (testsInfoDoc.exists()) {
                                        Log.d("TestData", "TESTS_INFO => " + testsInfoDoc.getData());

                                        // Extract fields
                                        String test1Id = testsInfoDoc.getString("TEST1_ID");
                                        Long test1TimeLong = testsInfoDoc.getLong("TEST1_TIME"); // Use getLong for integer type

                                        // Convert to String if necessary
                                        String test1Time = (test1TimeLong != null) ? String.valueOf(test1TimeLong) : null;

                                        // Add to the list if not null
                                        if (test1Id != null && test1Time != null) {
                                            testListItems.add(new TestInfo(test1Id, test1Time));
                                        }

                                        // Notify the adapter
                                        adapter.notifyDataSetChanged();

                                        // Call the success listener
                                        completeListener.OnSuccess();
                                    } else {
                                        Log.w("TestData", "TESTS_INFO document does not exist for quiz: " + quizDoc.getId());
                                        // Call the failure listener if TESTS_INFO doesn't exist
                                        completeListener.OnFailure();
                                    }
                                } else {
                                    Log.w("TestData", "Error getting TESTS_INFO document.", task.getException());
                                    // Call the failure listener if there's an error
                                    completeListener.OnFailure();
                                }
                            }
                        });
                    }
                } else {
                    Log.w("TestData", "Error getting QUIZES documents.", task.getException());
                    // Call the failure listener if there's an error getting QUIZES
                    completeListener.OnFailure();
                }
            }
        });
    }

    private void fetchTestInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String quizId = "FgGSUC30T7KOmHH5Lr1"; // Replace with your quiz ID

        db.collection("QUIZES").document(quizId)
                .collection("TESTS_LIST")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<TestInfo> testInfoList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get the TESTS_INFO collection for each TESTS_LIST document
                            db.collection("QUIZES").document(quizId)
                                    .collection("TESTS_LIST").document(document.getId())
                                    .collection("TESTS_INFO")
                                    .get()
                                    .addOnCompleteListener(infoTask -> {
                                        if (infoTask.isSuccessful()) {
                                            for (QueryDocumentSnapshot infoDoc : infoTask.getResult()) {
                                                String testId = infoDoc.getString("TEST1_ID");
                                                String testTime = infoDoc.getString("TEST1_TIME");
                                                testInfoList.add(new TestInfo(testId, testTime));
                                            }
                                            // Update RecyclerView after fetching data
                                            updateRecyclerView(testInfoList);
                                        }
                                    });
                        }
                    } else {
                        Log.d("Firestore Error", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void updateRecyclerView(List<TestInfo> testInfoList) {
        TestInfoAdapter adapter = new TestInfoAdapter(this,testInfoList);
        recyclerView2 = findViewById(R.id.recyclerViewAdmin_add_test);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(adapter);
    }

}









