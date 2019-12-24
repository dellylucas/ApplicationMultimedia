package com.dfl.applicationmultimedia;


import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends Fragment {
    private static final int PICK_SOUND = 1;
    private TextView txtruta;
    private String path = null;
    private Button playSound;
    private MediaPlayer sound;
    private MediaRecorder microphone;
    private boolean isRecording = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button openSound = view.findViewById(R.id.open_sound);
        final Button micSound = view.findViewById(R.id.mic_sound);
        sound = null;
        playSound = view.findViewById(R.id.play_sound);
        txtruta = view.findViewById(R.id.txtruta);
        openSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecciona un audio"), PICK_SOUND);
            }
        });
        micSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recording(isRecording);
                if (isRecording)
                    micSound.setText("Detener Grabacion");
                else micSound.setText("Iniciar Grabacion");


                isRecording =!isRecording;
            }
        });
        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sound == null)
                    if (path != null)
                        playSound();
                    else
                        Toast.makeText(getContext(), "seleccione o grabe audio primero", Toast.LENGTH_LONG).show();
                else {
                    sound.stop();
                    sound = null;
                    playSound.setText(R.string.rep_sound);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SOUND && resultCode == RESULT_OK) {
            if (data != null) {
                path = data.getDataString();
                playSound();
            }
        }
    }

    private void playSound() {
        try {
            if (sound != null) sound.stop();
            sound = new MediaPlayer();
            sound.setDataSource(getContext(), Uri.parse(path));
            sound.prepare();
            sound.start();
            playSound.setText(R.string.stop_sound);
            txtruta.setText("Ruta: " + path);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error al ejecutar audio", Toast.LENGTH_LONG).show();
        }

    }

    private void beginMicrophone() {
        path=Environment.getExternalStorageDirectory()+"/audio.3gp";
        microphone = new MediaRecorder();
        microphone.setAudioSource(MediaRecorder.AudioSource.MIC);
        microphone.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        microphone.setOutputFile(path);
        microphone.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            microphone.prepare();
        } catch (IOException e) {
            Toast.makeText(getContext(), "No se grabara correctamente", Toast.LENGTH_LONG).show();
        }
        microphone.start();
    }

    private void stopMicrophone() {
        microphone.stop();
        microphone.release();
        microphone = null;
        txtruta.setText("Ruta: " + path);

    }

    private void recording(boolean init) {
        if (init)
            beginMicrophone();
        else
            stopMicrophone();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null)
            mainActivity.updateToolbar("Media Cloud", getString(R.string.red_three));

    }

    @Override
    public void onPause() {
        super.onPause();
        if(microphone!=null){
            microphone.release();
            microphone=null;
        }
    }
}
