package com.dfl.applicationmultimedia;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private static final int PICK_VIDEO = 1;
    private static final int REQUEST_VIDEO = 3;
    private String path = null;
    private VideoView videoview = null;
    private TextView txtruta = null;
    private Uri uriVideo = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button openVideo = view.findViewById(R.id.open_video);
        Button captureVideo = view.findViewById(R.id.capture_video);
        videoview = view.findViewById(R.id.videoview);
        txtruta = view.findViewById(R.id.txtruta);
        openVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecciona un video"), PICK_VIDEO);
            }
        });
        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File imageVideo = new File(getContext().getFilesDir(), "video.mp4");
                uriVideo = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", imageVideo);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriVideo);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_VIDEO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_VIDEO:
                    if (data != null) {
                        path = data.getDataString();
                        playSound(Uri.parse(path));
                    }
                    break;
                case REQUEST_VIDEO:
                    playSound(uriVideo);
                    break;
            }
        }
    }

    private void playSound(Uri parse) {
        MediaController contoller = new MediaController(getContext());
        videoview.setMediaController(contoller);
        videoview.setVideoURI(parse);
        videoview.start();
        txtruta.setText("Ruta: " + parse.getPath());
        contoller.setAnchorView(videoview);
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null)
            mainActivity.updateToolbar("Media Cloud", getString(R.string.red_four));

    }
}
