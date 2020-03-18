package com.example.okke.Fragments;

import androidx.fragment.app.DialogFragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.okke.R;
import com.example.okke.data.ExtraContext;
import com.example.okke.data.RequestData;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadFragment extends DialogFragment {

    public DownloadFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DownloadFragment newInstance(RequestData requestOne,
                                               RequestData requestTow,
                                               RequestData requestThree) {
        DownloadFragment frag = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ExtraContext.REQ_ONE, requestOne);
        bundle.putParcelable(ExtraContext.REQ_TOW, requestTow);
        bundle.putParcelable(ExtraContext.REQ_THREE, requestThree);
        frag.setArguments(bundle);
        return frag;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blank_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button oneButton = view.findViewById(R.id.one);
        final RequestData requestDataONE = getArguments().getParcelable(ExtraContext.REQ_ONE);
        oneButton.setText(getResources().getString(R.string.Quality) + ": " + requestDataONE.getQuality() + "\n " + getResources().getString(R.string.Quality) + " = " + requestDataONE.getSize());
        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDownloadVido(requestDataONE);
            }
        });
        final RequestData requestDataTOW = getArguments().getParcelable(ExtraContext.REQ_TOW);
        Button towButton = view.findViewById(R.id.tow);
        towButton.setText(getResources().getString(R.string.Quality) + ": " + requestDataTOW.getQuality() + "\n " + getResources().getString(R.string.Size) + " = " + requestDataTOW.getSize());
        towButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDownloadVido(requestDataTOW);
            }
        });
        final RequestData requestDataTHREE = getArguments().getParcelable(ExtraContext.REQ_THREE);
        Button threeButton = view.findViewById(R.id.three);
        threeButton.setText(getResources().getString(R.string.Quality) + ": " + requestDataTHREE.getQuality() + "\n " + getResources().getString(R.string.Quality) + " = " + requestDataTHREE.getSize());
        threeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDownloadVido(requestDataTHREE);
            }
        });
    }

    public void displayDownloadVido(RequestData request) {
        //اوامر التحميل
        //
        //
        Context context = getContext();
        DownloadManager.Request downLoadRequest = new DownloadManager.Request(Uri.parse(request.getImageUrl()));
        downLoadRequest.setTitle("download: " + "TwtVideo: " + request.getmTwtId());
        downLoadRequest.setDescription(request.getQuality() + " / " + request.getSize());
        downLoadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "TwtVideo: " + request.getmTwtId());
        downLoadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(downLoadRequest);

    }
}
