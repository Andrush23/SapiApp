package com.example.boti.sapiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListScreen extends AppCompatActivity {

    private ArrayList<Advertiser> advertisers;
    private String title;
    private String description;
    private ArrayList<String> photos;
    private Advertiser advertiser;
    private Context context = this;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        //checkUserExist();

            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null) {

                        Intent loginintent = new Intent(ListScreen.this, ListScreen2.class);
                        loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginintent);

                    }

                }
            };


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        new LongOperation().execute("");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(ListScreen.this, AddAdvertiser.class));
        }

        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(ListScreen.this, Profile.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        mAuth.signOut();
    }

    /*public void checkUserExist(){

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(user_id)){

                    Toast.makeText(getApplicationContext(), "!dataSnapshot.hasChild(user_id)", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ListScreen.this, Login.class));

                }
                else {
                    Toast.makeText(getApplicationContext(), "else dataSnapshot.hasChild(user_id)", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ListScreen.this, Login.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ListScreen.this, Login.class));

            }
        });
    }*/

    public void setUpRecyclerView()
    {
        advertisers = new ArrayList<Advertiser>();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Advertisers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    photos=new ArrayList<String>();
                    title=childSnapshot.child("Title").getValue().toString();
                    description=childSnapshot.child("Description").getValue().toString();
                    String photoSnapshot=childSnapshot.child("Photos").getValue().toString();
                    photos=findPhoto(photoSnapshot);
                    advertiser=new Advertiser(description,title,photos);
                    advertisers.add(advertiser);
                }
                setUpAdapter(advertisers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<String> findPhoto(String from){
        int position=0;
        ArrayList<String> photos = new ArrayList<String>();
        while (from.charAt(position)!='}')
        {
            while (from.charAt(position)!='=')
            {
                position++;
            }
            position++;
            String to="";
            while (from.charAt(position)!=',' && from.charAt(position)!='}'){
                to+=from.charAt(position);
                position++;
            }
            photos.add(to);
        }
        return photos;
    }

    public void setUpAdapter(final ArrayList<Advertiser> advertisers)
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        AdvertiserAdapter adapter = new AdvertiserAdapter(context,advertisers);
        recyclerView.setAdapter(adapter);

    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            setUpRecyclerView();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.dismiss();
        }

        @Override
        protected void onPreExecute() {
            mProgress = new ProgressDialog(context);
            mProgress.setMessage("Loading advertisers...");
            mProgress.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
