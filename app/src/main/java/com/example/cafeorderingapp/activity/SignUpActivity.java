package com.example.cafeorderingapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText signUpName, signUpEmail, signUpPassword;
    Button signUpBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpName = findViewById(R.id.signUpName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);

        signUpBtn = findViewById(R.id.signUpBtn);

        TextView signInRedirect = findViewById(R.id.signInRedirect);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        signInRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {



        String name = signUpName.getText().toString().trim();
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();

        if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty()){
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Cooking up your account...");
            pd.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        pd.dismiss();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("username", name);
                        intent.putExtra("email", email);
                        intent.putExtra("flag", true);
                        startActivity(intent);
                    }else {
                        Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
        }

    }
}