package com.example.Safari_Snap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.example.Safari_Snap.R;

public class WebViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        WebView webview = view.findViewById(R.id.wv_display);
        webview.setWebViewClient(new WebViewClient());

        String Link;
        String message;

        Bundle data = getArguments();

        message = data.getString("animal_detect").toLowerCase();

        switch (message) {
            case "antelope":
                Link = "https://en.wikipedia.org/wiki/Pronghorn";
                break;

            case "bobcat":
                Link = "https://en.wikipedia.org/wiki/Bobcat";
                break;

            case "buffalo":
                Link = "https://en.wikipedia.org/wiki/American_bison";
                break;

            case "chimpanzee":
                Link = "https://en.wikipedia.org/wiki/Chimpanzee";
                break;

            case "rhinoceros":
                Link = "https://en.wikipedia.org/wiki/Rhinoceros";
                break;

            case "wolf":
                Link = "https://en.wikipedia.org/wiki/Wolf";
                break;

            default:
                Link = "http://google.com";
                break;
        }

        webview.loadUrl(Link);

        return view;
    }
}