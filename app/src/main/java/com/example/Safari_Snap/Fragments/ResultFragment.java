package com.example.Safari_Snap.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.Safari_Snap.Activity.Classifier;
import com.example.Safari_Snap.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    public static int correct;
    public static int incorrect;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);

        ImageView imageView = view.findViewById(R.id.iv_capture); //to display animal image
        TextView textView = view.findViewById(R.id.tv_name); // to display animal name

        Button button_yes = view.findViewById(R.id.btn_yes);
        Button button_no = view.findViewById(R.id.btn_no);

        //to receive data from home fragment
        Bundle data = getArguments();

        if (data == null) {
            Toast.makeText(getContext(), "Data not received from Home fragment", Toast.LENGTH_SHORT).show();
        } else {

            Bitmap bitmap = data.getParcelable("animal_image"); //get data from home fragment
            imageView.setImageBitmap(bitmap); //display image on Result Fragment

            String message = data.getString("animal_detect");
            textView.setText(message.toUpperCase());
        }


        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correct++;

                WebViewFragment webViewFragment = new WebViewFragment(); //create fragment instance
                Bundle information = new Bundle(); //bundle to collect data
                information.putString("animal_detect", Classifier.animal_detect); //add data to bundle
                webViewFragment.setArguments(information);//send data to webview fragment

                //switch fragment to webView fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, webViewFragment);
                fragmentTransaction.commit();
            }
        });


        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update result in stats and go back to home screen
                incorrect++;

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack();
            }
        });


        return view;
    }


}