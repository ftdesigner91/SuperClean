package com.gmail.superclean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.superclean.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserAccount extends AppCompatActivity {

    private Button nChangePicBtn;
    private Button nSubmitBtn;
    private Button nGetLocationBtn;

    private String name;
    private TextInputEditText nPhoneNum;
    private TextInputEditText nEmail;
    private TextInputEditText nAddress;
    private TextInputEditText nPassword;

    private TextInputEditText nUsernameEdit;
    private ImageView nUserImgView;
    //private TextView nPhoneNumView;

    private DatabaseReference userAccRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private StorageReference nImageStorageRef;

    private TextInputLayout nPasswordToggleLayout;

    private static final int GALLERY_PICK = 1;

    FusedLocationProviderClient fusedLocationProviderClient;


    //String username, password, image, address, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();

        nImageStorageRef = FirebaseStorage.getInstance().getReference();

        nChangePicBtn = findViewById(R.id.changeUserPicBtn);
        nSubmitBtn = findViewById(R.id.userAccSubmitBtn);
        nGetLocationBtn = findViewById(R.id.getLocationBtn);

        nPhoneNum = findViewById(R.id.editPhoneNum);
        //nUserNameDisply = findViewById(R.id.userNameView);
        nAddress = findViewById(R.id.editAddress);
        nPassword = findViewById(R.id.editPassword);
        nEmail = findViewById(R.id.editEmail);
        nUsernameEdit = findViewById(R.id.usernameEdit);

        nUserImgView = findViewById(R.id.userImgView);


        nPasswordToggleLayout = findViewById(R.id.passwordToggleLayout);

        userAccRef = FirebaseDatabase.getInstance().getReference("users_account").child(uid);

        userAccRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nPassword.setHint("••••••••");
                nPasswordToggleLayout.setPasswordVisibilityToggleEnabled(false);
                nPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if(nPassword.getText().length()==0)
                        {
                            nPasswordToggleLayout.setPasswordVisibilityToggleEnabled(false);
                        }else
                        {
                            nPasswordToggleLayout.setPasswordVisibilityToggleEnabled(true);
                        }
                    }
                });

                /*
                String password = dataSnapshot.child("password").getValue().toString();
                //nPassword.getEditableText();
                //nPassword.setHint(String.valueOf(nPassword.getInputType()));
                //nPassword.setInputType(120);

                //nPassword.setHint(password);
                //nPasswordToggleLayout.setEndIconMode(END);
               //nPasswordToggleLayout.setPasswordVisibilityToggleEnabled(false);

                //nPasswordToggleLayout.setEndIconMode(textInputLayout.getEndIconMode());
                //nPasswordToggleLayout.getEndIconMode();
                //String g =String.valueOf( nPasswordToggleLayout.);
                //nPassword.setHint(g);

                //nPasswordToggleLayout.setHintEnabled(true);
                //nPasswordToggleLayout.setHintEnabled(true);
                //nPassword.setHint(password);
                //nPasswordToggleLayout.setVisibility(View.GONE);
                //nPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //nPasswordToggleLayout.setVisibility(View.VISIBLE);

                 */

                String address = dataSnapshot.child("address").getValue().toString();
                nAddress.setHint(address);

                String email = dataSnapshot.child("email").getValue().toString();
                nEmail.setHint(email);

                String name = dataSnapshot.child("name").getValue().toString();
                nUsernameEdit.setHint(name);

                String phone = dataSnapshot.child("phone_number").getValue().toString();
                nPhoneNum.setHint(phone);

                String imageData = dataSnapshot.child("image").getValue().toString();
                Picasso.get().load(imageData).into(nUserImgView);
//                Toast.makeText(UserAccount.this,"image up",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = mCurrentUser.getUid();
                userAccRef = FirebaseDatabase.getInstance().getReference().child("users_account").child(uid);
                userAccRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(TextUtils.isEmpty(nPhoneNum.getText())==false
                        || TextUtils.isEmpty(nAddress.getText())==false
                        || TextUtils.isEmpty(nPassword.getText())==false
                        || TextUtils.isEmpty(nUsernameEdit.getText())==false
                        || TextUtils.isEmpty(nEmail.getText())==false)
                        {

                            String phoneData = dataSnapshot.child("phone_number").getValue().toString();
                            String phoneField = nPhoneNum.getText().toString();
                            if(!phoneData.equals(phoneField) && TextUtils.isEmpty(phoneField)==false)
                            {
                                userAccRef.child("phone_number").setValue(nPhoneNum.getText().toString(),"0");
                            }
                            else
                            {

                            }

                            String passwordData = dataSnapshot.child("password").getValue().toString();
                            String passwordField = nPassword.getText().toString();
                            if(!passwordData.equals(passwordField) && TextUtils.isEmpty(passwordField)==false)
                            {
                                userAccRef.child("password").setValue(nPassword.getText().toString(),"0");
                                //Toast.makeText(UserAccount.this,"pass updated successfully",Toast.LENGTH_LONG).show();
                            }
                            else
                            {

                            }

                            String emailData = dataSnapshot.child("email").getValue().toString();
                            String emailField = nEmail.getText().toString();
                            if(!emailData.equals(emailField) && TextUtils.isEmpty(emailField)==false)
                            {
                                userAccRef.child("email").setValue(nEmail.getText().toString(),"0");
                            }

                            String addressData = dataSnapshot.child("address").getValue().toString();
                            String addressField = nAddress.getText().toString();
                            if(!addressData.equals(addressField) && TextUtils.isEmpty(addressField)==false)
                            {
                                userAccRef.child("address").setValue(nAddress.getText().toString(),"0");
                            }

                            String usernameData = dataSnapshot.child("name").getValue().toString();
                            String usernameField = nUsernameEdit.getText().toString();
                            if(!usernameData.equals(usernameField) && TextUtils.isEmpty(usernameField)==false)
                            {
                                userAccRef.child("name").setValue(nUsernameEdit.getText().toString(),"0");
                                //Toast.makeText(UserAccount.this,"name updated successfully",Toast.LENGTH_LONG).show();
                            }



                            Toast.makeText(UserAccount.this,"Profile updated successfully",Toast.LENGTH_LONG).show();
                        }

                    else
                        {
                            Toast.makeText(UserAccount.this,"no data found",Toast.LENGTH_LONG).show();
                        }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
/*        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        userAccRef = FirebaseDatabase.getInstance().getReference().child("users_account").child(current_uid);

        userAccRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //String name = dataSnapshot.child("name").getValue().toString();
                //nUserNameDisply.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //nUserImgView.setImageResource(utl);
        //showUserData();

        nChangePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntenet = new Intent();
                galleryIntenet.setType("image/*");
                galleryIntenet.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntenet, "SELECT IMAGE"),GALLERY_PICK);

                /*
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UserAccount.this);

                 */
            }
        });

        nGetLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(UserAccount.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();


                }else {

                    ActivityCompat.requestPermissions(UserAccount.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            //Toast.makeText(UserAccount.this,imageUri,Toast.LENGTH_LONG).show();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                final Uri resultUri = result.getUri();

                String currentUserId = mCurrentUser.getUid();



                StorageReference filepath = nImageStorageRef.child("profile_images").child(currentUserId + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            //Toast.makeText(UserAccount.this,"nailedIt",Toast.LENGTH_LONG).show();
                            String downoladUrl = resultUri.toString();
                            userAccRef.child("image").setValue(downoladUrl,"0");

                        }else
                        {
                            Toast.makeText(UserAccount.this,"try again",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                nUserImgView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }


    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if(location != null)
                {
                    try {
                        Geocoder geocoder = new Geocoder(UserAccount.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1);


                        nAddress.setText(addresses.get(0).getAddressLine(0));



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }




 /*
    public void showUserData()
    {

        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone_number");
        address = intent.getStringExtra("address");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        image = intent.getStringExtra("img");

        nUsernameEdit.setText(username);
        nPhoneNum.setText(phone);
        nAddress.setText(address);
        nEmail.setText(email);
        nPhoneNum.setText(password);
        //nUserImgView.setImageResource(utl);


    }*/

    /*public void update(View view)
    {
        if( isPhoneChanged())
        {
            Toast.makeText(this, "Data has been updated successfully",Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(this, "Data is same",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isUsernameChanged() {

        if(!username.equals(nUsernameEdit.getText().toString()))
        {
            userAccRef.child(username).setValue(nUsernameEdit.getText().toString());
            username = nUsernameEdit.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if(!phone.equals(nPhoneNum.getText().toString()))
        {
            userAccRef.child(phone).setValue(nPhoneNum.getText().toString());
            phone= nPhoneNum.getText().toString();

            return true;
        }
        else
        {
            return false;
        }
    }*/
}
