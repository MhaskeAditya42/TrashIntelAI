package com.example.trashintelai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trashintelai.adapter.CollectedGarbage;
import com.example.trashintelai.adapter.LocationAdapter;
import com.example.trashintelai.model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
private LocationAdapter adapter ;
    private CollectedGarbage collectedGarbageadapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViewById(R.id.logout_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, login.class));
                AdminActivity.this.finish();
            }
        });
        FirebaseDatabase.getInstance().getReference().child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Location> list = new ArrayList<>();
                for ( DataSnapshot ds : snapshot.getChildren()){
                     if (ds.hasChild("location"))
                     {
                            String address = ds.child("location").getValue().toString();
                            Location location = new Location(address,ds.getKey());
                            list.add(location);
                     }
                }


                adapter = new LocationAdapter(list);
                RecyclerView recyclerView = findViewById(R.id.location_recyclerview_id);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("collected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Location> list = new ArrayList<>();
                for ( DataSnapshot ds : snapshot.getChildren()){
                    if (ds.hasChild("location"))
                    {
                        String address = ds.child("location").getValue().toString();
                        Location location = new Location(address,ds.getKey());
                        list.add(location);
                    }
                }


                collectedGarbageadapter = new CollectedGarbage(list);
                RecyclerView recyclerView = findViewById(R.id.collection_recyclerview_id);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
                recyclerView.setAdapter(collectedGarbageadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}