package com.gmail.superclean;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private Button nBtn1;
    private Button nBtn2;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);


        /*------------------START BUTTONS----------------------------*/
        nBtn1 = v.findViewById(R.id.btn1);
        nBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accIntent = new Intent(getActivity(), BookingArea.class);
                startActivity(accIntent);
            }
        });
        nBtn2 = v.findViewById(R.id.btn2);
        nBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accIntent = new Intent(getActivity(), BookingArea.class);
                startActivity(accIntent);
            }
        });

        /*------------------END BUTTONS----------------------------*/
                return v;
    }


}
