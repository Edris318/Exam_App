package com.example.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Fragments.AccountFragment;
import com.example.exam.Fragments.Category_Fragment;
import com.example.exam.Fragments.leaderFragment;
import com.example.exam.Interface.MyCompleteListener;
import com.example.exam.Models.CategoryModel;
import com.example.exam.Models.Quiz;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    TextView txt_name, txt_alpha;
    FloatingActionButton btnFloat;
    Toolbar toolbar;
    DrawerLayout adminLayout;
    NavigationView navigationView;
    long firstTimeClick = System.currentTimeMillis(); // Initialize firstTimeClick here
    RecyclerView recyclerView;
    AdminRecAdapter recAdapter; // Declare recAdapter here
    FirebaseFirestore db;
    ArrayList<Quiz> quizList;
    String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // ... other initializations ...
        txt_name = findViewById(R.id.txt_name_nav_admin);
        txt_alpha = findViewById(R.id.txt_Alpha_nav_admin);
        btnFloat = findViewById(R.id.btn_float_add);
        toolbar = findViewById(R.id.toolbar_admin);
        navigationView = findViewById(R.id.nav_admin);
        adminLayout = findViewById(R.id.drawer_admin);
        recyclerView = findViewById(R.id.recyclerViewAdmin);


        Listeners();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        // Initialize quizList and recAdapter here
        quizList = new ArrayList<>();
        recAdapter = new AdminRecAdapter(this, quizList);
        recyclerView.setAdapter(recAdapter); // Set the adapter

        db = FirebaseFirestore.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, adminLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        adminLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Call getDataFromDp() to fetch data and update the adapter
        loadCategoryData(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                Toast.makeText(AdminActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure() {
                Toast.makeText(AdminActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.admin_signOut) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login_Activity.class));

                }

                DrawerLayout layout = findViewById(R.id.drawer_admin);
                layout.closeDrawer(GravityCompat.START);
                return true;
            }

        });


        // ... rest of your onCreate() method ...
    }

    // ... other methods ...

    // ... (Rest of your AdminActivity code) ...

    public void loadCategoryData(MyCompleteListener completeListener) {
        quizList.clear();
        db.collection("QUIZES").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots
                        ) {
                            docList.put(doc.getId(), doc);
                        }
                        QueryDocumentSnapshot catListDocs = docList.get("Categories");
                        long catCount = catListDocs.getLong("COUNT");

                        for (int i = 1; i <= catCount; i++) {
                             catId = catListDocs.getString("CAT" + String.valueOf(i) + "_ID");
                            QueryDocumentSnapshot catDoc = docList.get(catId);
                            int noOFTest = catDoc.getLong("NO_OF_TESTS").intValue();
                            String name = catDoc.getString("NAME");
                            quizList.add(new Quiz(catId, name, noOFTest));
                        }
                        recAdapter.notifyDataSetChanged();
                        completeListener.OnSuccess();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();

                    }
                });

    }


    private void Listeners() {
        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadCategoryActivity.class));
                //finish();
            }
        });
    }


}


// ... (Rest of your AdminActivity code) ...






