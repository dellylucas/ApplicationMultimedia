package com.dfl.applicationmultimedia;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private static int REQUEST_GALLERY = 2;
    private static int REQUEST_OK = -1;
    private ImageView photo;
    private TextView txtruta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button gallery = view.findViewById(R.id.btnopen_gallery);
        photo = view.findViewById(R.id.img_photo);
        txtruta = view.findViewById(R.id.txtruta);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == REQUEST_OK && data != null) {
            Uri imagen = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContext().getContentResolver().query(imagen, path, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columna = cursor.getColumnIndex(path[0]);
                String pathImage = cursor.getString(columna);
                cursor.close();
                Bitmap bitImagen = BitmapFactory.decodeFile(pathImage);
                int height = bitImagen.getHeight();
                int width = bitImagen.getWidth();
                float scaleA = ((float) (width / 2)) / width;
                float scaleB = ((float) (height / 2)) / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleA, scaleB);
                Bitmap newImageScale = Bitmap.createBitmap(bitImagen, 0, 0, width, height, matrix, true);
                photo.setImageBitmap(newImageScale);
                txtruta.setText("Ruta: " + pathImage);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null)
            mainActivity.updateToolbar("Media Cloud", getString(R.string.red_two));

    }
}
