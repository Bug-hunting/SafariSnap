package com.example.Safari_Snap.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.Safari_Snap.Activity.Classifier;
import com.example.Safari_Snap.R;

import java.io.IOException;
import java.util.Objects;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_REQUEST_CODE = 2000;

    private ImageButton capture;
    private Uri image_uri; //format to read image from gallery
    private Bitmap photo; //to record final image

    public Classifier imageClassifier;

    private static final int CAMERA_PERMISSION_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        capture = view.findViewById(R.id.capture_image);

        try {
            imageClassifier = new Classifier(getActivity());
        } catch (IOException e) {
            Log.e("Error While Creating Classifier", "Error: " + e);
        }

        capture.setOnClickListener(this);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE: {
                    photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
                    photo = photo.copy(Bitmap.Config.ARGB_8888, true); //changing incoming image format to ARGB_888
                    photo = rotateImage(photo, 90); // overwrites the current bitmap with the rotated image

                    processData();

                }
                break;

                case GALLERY_REQUEST_CODE: {
                    image_uri = data.getData();

                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), image_uri);
                    } catch (IOException e) {
                        Log.e("Gallery Error", "File Not Found", e);
                    }

                    processData();

                }
                break;
            }
        }

    }

    private void processData() {

        //Sending Image to the Classifier
        imageClassifier.recognize_Image(photo);

        ResultFragment resultFragment = new ResultFragment(); //creating Result fragment instance
        Bundle information = new Bundle(); //creating a bundle to record data
        information.putString("animal_detect", Classifier.animal_detect); //adding animal name to bundle
        information.putParcelable("animal_image", photo);//adding image data to bundle
        resultFragment.setArguments(information);//sending data to result fragment

        //replacing current fragment with result fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, resultFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    //A function to rotate image src= bitmap angle= degree of rotation
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //a function which creates a alert dialog to display Image options
    private void selectImage() {
        //Labels for menu
        final CharSequence[] options = {"Take Photo", "Open Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //create popup menu
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //open camera
                    startActivityForResult(camera_intent, CAMERA_REQUEST_CODE); //start activity and return code when finish

                } else if (options[item].equals("Open Gallery")) {

                    Intent intent = new Intent();
                    intent.setType("image/*"); //setting image extension as flexible
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, GALLERY_REQUEST_CODE); //start activity and return code when finish
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    //a function to check required hardware and storage
    private void check_permission() {
        //check for camera permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        //check for storage permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }


    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.capture_image) {
            check_permission();
            selectImage();
        }

    }
}