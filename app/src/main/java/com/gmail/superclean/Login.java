package com.gmail.superclean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputEditText nLogEmail;
    private TextInputEditText nLogPassword;

    private TextInputLayout nLlogEmailLayout;
    private TextInputLayout nLogPasswordLayout;

    private Button nLoginBtn;

    private Toolbar nToolbar;

    private ImageView nImageView;

    private TextView nRegView;

    private ProgressDialog nLogProgressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize logo view
        nImageView = findViewById(R.id.logoView);
        nImageView.setImageResource(R.drawable.logo_no_bg8);

        //initialize clickable txtview
        nRegView = findViewById(R.id.regView);
        nRegView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(Login.this, Register.class);
                startActivity(regIntent);
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*-------------------START TOOLBAR---------------------------------*/
        nToolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(nToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");
        /*-------------------END TOOLBAR---------------------------------*/


        nLogEmail = findViewById(R.id.logEmail);
        nLogPassword = findViewById(R.id.logPassword);

        nLlogEmailLayout = findViewById(R.id.regEmailLayout);
        nLogPasswordLayout = findViewById(R.id.logPasswordLayout);

        //nLogProgressDialog = new ProgressDialog(this);

        /*--------------------START PASSWORD WATCHER--------------------------------*/

        nLogPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nLogPassword.getText().length()==0)
                {
                    nLogPasswordLayout.setPasswordVisibilityToggleEnabled(false);
                }
                else
                {
                    nLogPasswordLayout.setPasswordVisibilityToggleEnabled(true);
                }
            }
        });

        /*--------------------END OF PASSWORD WATCHER--------------------------------*/

        /*--------------------START OF LOGIN BUTTON--------------------------------*/
        nLoginBtn = findViewById(R.id.logBtn);
        nLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = nLogEmail.getText().toString();
                String password = nLogPassword.getText().toString();

                //if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                //{
                    //nLogProgressDialog.setTitle("error title here");
                    //nLogProgressDialog.setMessage("error message here");
                    //nLogProgressDialog.setCanceledOnTouchOutside(false);
                    //nLogProgressDialog.show();
                //USER LOGIN METHOD
                log_user(email, password);

                //}
            }
        });

        /*---------------------END OF LOGIN BUTTON-------------------------------*/
    }

    /*----------------------START OF USER LOGIN METHOD------------------------------*/
    private void log_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    //nLogProgressDialog.dismiss();

                    Intent mainIntent = new Intent(Login.this, MainActivity.class);
                    startActivity(mainIntent);
                    //finish();
                    //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                }else {
                    //nLogProgressDialog.hide();
                    Toast.makeText(Login.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*--------------------------ENF OF USER LOGIN METHOD--------------------------*/
}
