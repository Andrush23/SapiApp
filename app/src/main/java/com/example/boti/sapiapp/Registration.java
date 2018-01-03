package com.example.boti.sapiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private EditText userNameField;
    private EditText emailField;
    private EditText passwordField;
    private Button signUpButton;
    private Button gotoLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgress;
    private Firebase mRootRef;
    private Context context = this;
    private Button addImageButton;
    public static final int GALLERY_INTENT = 2;
    private Uri imageUri;
    private StorageReference mStorage;
    private String imageUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        imageUriString = "";

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mRootRef = new Firebase("https://sapiapp-5a8e2.firebaseio.com/Users");

        mStorage = FirebaseStorage.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() !=null)
                {
                    startActivity(new Intent(context,ListScreen.class));
                }
            }
        };

        userNameField = (EditText) findViewById(R.id.username);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        addImageButton = (Button) findViewById(R.id.addImageButton);

        gotoLoginButton = (Button)findViewById(R.id.gotoLoginButton);

        gotoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUriString.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_INTENT);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Already added a profile picture!",Toast.LENGTH_LONG).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUp();
            }
        });
    }

    private void startSignUp()
    {
        final String name = userNameField.getText().toString();
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^.*[0-9a-zA-Z@#$%^&+=!_-]$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Field is empty!", Toast.LENGTH_LONG).show();
        }
        else if (name.length() < 4) {
            Toast.makeText(getApplicationContext(), "Name too short, enter minimum 4 characters!", Toast.LENGTH_SHORT).show();
        }
        else if (Patterns.EMAIL_ADDRESS.matcher(email).matches() == false) {
            Toast.makeText(getApplicationContext(), "Invalid email adress!", Toast.LENGTH_LONG).show();
        }
        else if (matcher.matches() == false) {
            Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mProgress.setMessage("Signing up...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String user_id = mAuth.getCurrentUser().getUid();
                        Firebase mChild = mRootRef.child(user_id);
                        mChild.child("name").setValue(name);
                        mChild.child("picture").setValue(imageUriString);
                        mProgress.dismiss();
                        startActivity(new Intent(Registration.this, ListScreen.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Task not succesful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registration.this, Registration.class));
                    }
                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && requestCode==GALLERY_INTENT)
        {
            imageUri=data.getData();
            StorageReference filepath = mStorage.child("Photos").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(context,"Photo added",Toast.LENGTH_LONG).show();
                    imageUriString = downloadUrl.toString();
                }
            });
        }
    }
}
