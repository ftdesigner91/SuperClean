package com.gmail.superclean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BookingArea extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private UserAccount userAccount;

    private Button nSelecetDateBtn;
    private TextView nTxtViewCalendar;

    private Button nLocationBtn;
    private TextView tv1;
    private TextView nAddressView;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;

    private Button nAddBtn;
    private Button nRemoveBtn;
    TextView nPriceView;
    TextView nQtyView;

    Button nUserSubmitBtn;

    int counter = 0;

    private DatabaseReference bookingsRef;
    private  DatabaseReference userRef;
    private FirebaseAuth mAuth;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_area);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nTxtViewCalendar = findViewById(R.id.txtViewCalendar);
        nSelecetDateBtn = findViewById(R.id.selectDateBtn);
        nSelecetDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        nLocationBtn = findViewById(R.id.locationBtn);
        tv1 = findViewById(R.id.textView1);
        nAddressView = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);



        //initi fused
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        nLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(BookingArea.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();

                }else {

                    ActivityCompat.requestPermissions(BookingArea.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        nPriceView = findViewById(R.id.priceView);
        nQtyView = findViewById(R.id.qtyView);

        nUserSubmitBtn = findViewById(R.id.userSubmitBtn);
        nUserSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String date = nTxtViewCalendar.getText().toString();
                String location = nAddressView.getText().toString();
                String price = nPriceView.getText().toString();

                user_submission(date, location, price);
                if(TextUtils.isEmpty(date) == false && TextUtils.isEmpty(location) == false && TextUtils.isEmpty(price) == false)
                {
                    Toast.makeText(BookingArea.this, "you have booked successfully", Toast.LENGTH_LONG).show();

                    Intent mainIntent = new Intent(BookingArea.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                else
                {
                    Toast.makeText(BookingArea.this, "one or more information is missing", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void user_submission(final String date, final String location, final String price) {
        
        
        
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        bookingsRef = FirebaseDatabase.getInstance().getReference().child("users_submission").child(uid);
        userRef = FirebaseDatabase.getInstance().getReference().child("users_account").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("name").getValue().toString();
                String phoneNumber = dataSnapshot.child("phone_number").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("date", date);
                userMap.put("location",location);
                userMap.put("image",image);
                //userMap.put("thumb_image","default");
                userMap.put("name",username);
                userMap.put("price",price);
                userMap.put("phone_number",phoneNumber);

                //bookingsRef.push().setValue(price,"price");
                //bookingsRef.push().setValue(date,"date");
                //bookingsRef.push().setValue(location,"locaton");
                if (nTxtViewCalendar.getText().length()>0
                && nPriceView.getText().length()>0
                && nQtyView.getText().length()>0
                && nAddressView.getText().length()>0)
                {
                    bookingsRef.setValue(userMap);
                }
                //bookingsRef.setValue(userMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Toast.makeText(BookingArea.this, "in user submi. method", Toast.LENGTH_LONG).show();
    }

    public void Increase(View v)
    {
        bookingsRef = FirebaseDatabase.getInstance().getReference().child("bookings_price");

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(counter>=3)
                {
                    counter=3;
                    //price = price*counter;
                    nQtyView.setText(Integer.toString(counter));
                    nQtyView.setText(Integer.toString(counter));
                    String room =dataSnapshot.child("three_room").getValue().toString();
                    nPriceView.setText(room);
                    //nPriceView.setText(Double.toString(price*counter));
                }else
                {
                    counter++;
                    //price=price*counter;
                    nQtyView.setText(Integer.toString(counter));
                    if (counter==1)
                    {
                        nQtyView.setText(Integer.toString(counter));
                        String room =dataSnapshot.child("one_room").getValue().toString();
                        nPriceView.setText(room);
                    }
                    if (counter==2)
                    {
                        nQtyView.setText(Integer.toString(counter));
                        String room =dataSnapshot.child("two_room").getValue().toString();
                        nPriceView.setText(room);
                    }
                    if (counter==3)
                    {
                        nQtyView.setText(Integer.toString(counter));
                        String room =dataSnapshot.child("three_room").getValue().toString();
                        nPriceView.setText(room);
                    }
                    //nPriceView.setText(Double.toString(price*counter));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void Decrease(View v)
    {

        bookingsRef = FirebaseDatabase.getInstance().getReference().child("bookings_price");

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(counter<=0)
                {
                    counter=0;
                    nQtyView.setText("0");
                    nPriceView.setText("₱ 0");


                }else{
                    counter--;
                    nQtyView.setText(Integer.toString(counter));

                    if (counter==0)
                    {
                        nQtyView.setText("0");
                        nPriceView.setText("₱ 0");

                    }

                    if (counter==1)
                    {
                        nQtyView.setText(Integer.toString(counter));
                        String room =dataSnapshot.child("one_room").getValue().toString();
                        nPriceView.setText(room);
                    }
                    if (counter==2)
                    {
                        nQtyView.setText(Integer.toString(counter));
                        String room =dataSnapshot.child("two_room").getValue().toString();
                        nPriceView.setText(room);
                    }
                    if (counter==3)
                    {
                        nQtyView.setText(Integer.toString(counter));
                        String room =dataSnapshot.child("three_room").getValue().toString();
                        nPriceView.setText(room);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if(location != null)
                {
                    try {
                        Geocoder geocoder = new Geocoder(BookingArea.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1);

                        //tv1.setText(Html.fromHtml("<font color= '6200ee'><b>Latitude: </b><br></font>" +
                          //      addresses.get(0).getLatitude()));

                        nAddressView.setText(Html.fromHtml("<font color= '6200ee'><b>your current location: </b><br></font>" +
                                addresses.get(0).getAddressLine(0)));

                        //tv3.setText(Html.fromHtml("<font color= '6200ee'><b>country: </b><br></font>" +
                          //      addresses.get(0).getCountryName()));

                        //tv4.setText(Html.fromHtml("<font color= '6200ee'><b>locality: </b><br></font>" +
                          //      addresses.get(0).getLocality()));

                        //tv5.setText(Html.fromHtml("<font color= '6200ee'><b>Longtitude: </b><br></font>" +
                          //      addresses.get(0).getLongitude()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());


        nTxtViewCalendar.setText(currentDateString);
    }

}
