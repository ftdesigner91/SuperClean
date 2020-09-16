package com.gmail.superclean;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference nDatabase;
    private ImageView imgView;
    private TextView txt;
    private Button nChatBtn;


    public JobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_jobs, container, false);

        imgView = v.findViewById(R.id.noBookingView);
        txt = v.findViewById(R.id.textView);

        nChatBtn = v.findViewById(R.id.chatBtn);
        nChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(getActivity(), ChatRoomActivity.class);
                startActivity(chatIntent);
                startActivity(chatIntent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        nDatabase = FirebaseDatabase.getInstance().getReference().child("users_submission").child(uid);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null)
                {

                    imgView.setImageResource(R.drawable.logoui14);
                    txt.setText("our employee will text you shortly");
                }
                else
                {

                    imgView.setImageResource(R.drawable.logoui13);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return v;

    }

}
