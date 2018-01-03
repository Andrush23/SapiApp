package com.example.boti.sapiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    private ArrayList<Advertiser> advertisers;
    private String title;
    private String description;
    private ArrayList<String> photos;
    private Advertiser advertiser;
    private Context context = this;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUser;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child(uid).child("name").getValue().toString();
                String picture = dataSnapshot.child(uid).child("picture").getValue().toString();
                TextView name_text = (TextView) findViewById(R.id.nameText);
                name_text.setText("Name: " + user_name);
                ImageView picture_view = (ImageView) findViewById(R.id.profileImage);
                //picture_view.setImageURI(Uri.parse(picture));
                Glide.with(getApplicationContext()).load(picture).into(picture_view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
