package com.gmail.superclean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Register extends AppCompatActivity {



    private TextInputEditText nRegName;
    private TextInputEditText nRegEmail;
    private TextInputEditText nRegPassword;

    private TextInputLayout nRegPasswordLayout;
    private TextInputLayout nRegNameLayout;
    private TextInputLayout nRegEmailLayout;

    private Button regSubmitBtn;

    private FirebaseAuth mAuth;

    private Toolbar nToolbar;

    private ImageView nImageView;

    private ProgressDialog nRegProgressDialog;

    private DatabaseReference nDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // Initialize logo view
        nImageView = findViewById(R.id.logoView);
        nImageView.setImageResource(R.drawable.logo_no_bg8);

        nRegName = findViewById(R.id.regName);
        nRegEmail = findViewById(R.id.regEmail);
        nRegPassword = findViewById(R.id.regPassword);
        regSubmitBtn = findViewById(R.id.regSubmitBtn);

        nRegPasswordLayout = findViewById(R.id.regPasswordLayout);
        nRegNameLayout = findViewById(R.id.regNameLayout);
        nRegEmailLayout = findViewById(R.id.regEmailLayout);

        /*-------------------START TOOLBAR--------------------------*/
       nToolbar = findViewById(R.id.regToolbar);
       setSupportActionBar(nToolbar);
       getSupportActionBar().setTitle("Create an account");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*-------------------END TOOLBAR-----------------------*/

        /*-------------------START PASSWORD TEXT WATCHER-----------------------*/

        nRegPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {




            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(nRegPassword.getText().length()==0)
                {
                    nRegPasswordLayout.setPasswordVisibilityToggleEnabled(false);
                }
                else
                {
                    nRegPasswordLayout.setPasswordVisibilityToggleEnabled(true);
                }
            }
        });

        /*-------------------END PASSWORD TEXT WATCHER-----------------------*/


        /*----------------START REGISTRATION BUTTON--------------------*/
        regSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = nRegName.getText().toString();
                String Email = nRegEmail.getText().toString();
                String Password = nRegPassword.getText().toString();


                // EMPTY FIELD CHECKER
                //if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password))
                //{
          //          nRegProgressDialog.setTitle("Regestring user");
            //        nRegProgressDialog.setMessage("Please wait while we create your account");
              //      nRegProgressDialog.setCanceledOnTouchOutside(false);
                //    nRegProgressDialog.show();

                //USER REGISTRATION METHOD
                //UserAccount userAccount = new UserAccount(Name,Email,Password,"","");
                reg_user(Name,Email,Password);


                //}


            }
        });
        /*-----------------END OF REGISTRATION BUTTON------------------------*/

        //PROGRESS DIALOG
        //nRegProgressDialog = new ProgressDialog(this);
    }

    /*--------------START USER REGISTRATION METHOD----------------------*/
    private void reg_user(final String name, final String Email, final String Password) {
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Toast.makeText(Register.this, "register is successful", Toast.LENGTH_LONG).show();
                if(task.isSuccessful())
                {
                    /*----------------------START OF WRITE DATABASE------------------------------*/
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();
                    nDatabase = FirebaseDatabase.getInstance().getReference().child("users_account").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("email",Email);
                    userMap.put("password",Password);
                    userMap.put("phone_number","Enter phone number");
                    userMap.put("image","default");
                    userMap.put("address","Address");

                    nDatabase = FirebaseDatabase.getInstance().getReference().child("users_account").child(uid).child("messages");
                    HashMap<String, String> chatMap = new HashMap<>();
                    chatMap.put("name", name);
                    //userMap.put("thumb_image","default");

                    nDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                //nRegProgressDialog.dismiss();
                                Intent mainIntent = new Intent(Register.this, MainActivity.class);
                                startActivity(mainIntent);
                                //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                            }

                        }
                    });
                    /*----------------------END OF WRITE DATABASE------------------------------*/


                }else{
                    //nRegProgressDialog.hide();
                    Toast.makeText(Register.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    /*---------------------END OF USER REGISTRATION METHOD-------------------------------*/
}
