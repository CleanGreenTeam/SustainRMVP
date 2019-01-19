package com.example.sustainr_mvp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.provider.FirebaseInitProvider;

public class VolunteerReg extends AppCompatActivity {

    Button go_back, register_user;

    EditText name, email, password;

    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseUsers;

    String mname,mpassword,memail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_reg);

        name = (EditText) findViewById(R.id.sign_up_name);
        email = (EditText) findViewById(R.id.sign_up_email);
        password = (EditText) findViewById(R.id.sign_up_password);
        register_user = (Button) findViewById(R.id.register_user);
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();


        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();


            }
        });

    }
    public void registerUser(){
        final String mname = name.getText().toString();
        final String memail = email.getText().toString();
        final String mpassword = password.getText().toString();

        if(mname.matches("") || memail.matches("") || mpassword.matches("")) {
            Toast.makeText(this, "Please fill out every part", Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(memail, mpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(VolunteerReg.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            String id = firebaseAuth.getUid();
                            databaseUsers.child(id).child("device_token").setValue(id);
                            Volunteer user = new Volunteer(mname,memail,mpassword,id,0);
                            databaseUsers.child(id).setValue(user);
                        } else {
                            Toast.makeText(VolunteerReg.this, "Could not register. Please try agian", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



}
