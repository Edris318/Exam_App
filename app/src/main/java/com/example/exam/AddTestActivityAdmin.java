package com.example.exam;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Adapters.TestInfoAdapter;
import com.example.exam.Models.TestInfo;
import com.google.android.gms.tasks.OnCompleteListener;
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
        auth = FirebaseAuth.getInstance();
        // str = auth.getUid();


        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(adapter);





//                db.collection("QUIZES").document(FirebaseAuth.getInstance().getUid())
//                        .collection("TESTS_LIST").document("TESTS_INFO").addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                System.out.println(value.getData());
//                            }
//                        }).remove();

         //selectFromDb();
        // loadTestsList();
    }

    private void selectFromDb() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        query = db.collectionGroup("QUIZES");
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                testListItems.clear();
                for (DocumentSnapshot document : value.getDocuments()) {
                    TestInfo testInfo1 = document.toObject(TestInfo.class);
                    testListItems.add(testInfo1);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listenerRegistration.remove();
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




}






