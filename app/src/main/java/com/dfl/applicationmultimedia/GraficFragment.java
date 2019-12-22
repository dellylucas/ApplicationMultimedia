package com.dfl.applicationmultimedia;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dfl.applicationmultimedia.grafic.MyRenderer;

public class GraficFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null)
            mainActivity.updateToolbar("Media Cloud", getString(R.string.init));
        return inflater.inflate(R.layout.fragment_grafic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GLSurfaceView re = view.findViewById(R.id.relative_grafic);
        re.setRenderer(new MyRenderer(view.getContext()));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null)
            mainActivity.updateToolbar("Media Cloud", getString(R.string.red_one));

    }
}
