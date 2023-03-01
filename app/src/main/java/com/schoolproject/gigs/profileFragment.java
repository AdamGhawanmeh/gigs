package com.schoolproject.gigs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class profileFragment extends Fragment implements View.OnClickListener {
//    Button logoutBtn;
private ImageView pfp;
private FirebaseAuth mAuth;
private TextView profile;
private StorageReference storage = FirebaseStorage.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        return inflater.inflate(R.layout.fragment_profile, container, false);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        pfp=(ImageView) view.findViewById(R.id.pfp);

        logoutBtn.setOnClickListener(this);

        profile= (TextView) view.findViewById(R.id.profile);
        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("pfp/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpeg");
           String ref = "https://firebasestorage.googleapis.com/v0/b/gigs-1663613716509.appspot.com/o/pfp%2F"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpeg"+"?alt=media&token=4a057c78-7c73-45ed-bd6b-0cbaea35def6";
            profile.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/gigs-1663613716509.appspot.com/o/pfp%2F"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpeg"+"?alt=media&token=4a057c78-7c73-45ed-bd6b-0cbaea35def6").into(pfp);
        }
        return view;
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoutBtn:
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(this.getActivity(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();
                Intent intent = new Intent(getActivity(), logIn.class);
                getActivity().startActivity(intent);
    }
    }
}