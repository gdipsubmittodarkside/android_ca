package com.example.androidca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Animation fadeOut;
    List<Integer> id_of_6_selected;
    Dictionary<Integer, String> dict_of_20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id_of_6_selected = new ArrayList<>();
        dict_of_20 = new Hashtable<>();

        int[] ids={R.id.image_1,R.id.image_2,R.id.image_3,R.id.image_4,R.id.image_5,R.id.image_6,
                R.id.image_7,R.id.image_8, R.id.image_9,R.id.image_10,R.id.image_11,R.id.image_12,
                R.id.image_13,R.id.image_14, R.id.image_15,R.id.image_16,R.id.image_17,R.id.image_18,
                R.id.image_19,R.id.image_20};

        Button confirm_btn = findViewById(R.id.button);
        confirm_btn.setOnClickListener(this);

        for (int i=0; i<ids.length;i++)
        {
            ImageView imgview = findViewById(ids[i]);
            imgview.setOnClickListener(this);

            String filePath = "input_images";
            File mTargetFolder = new File(getFilesDir(), filePath);

            if (mTargetFolder.exists())
            {
                File[] images_inFolder = mTargetFolder.listFiles();

                if (images_inFolder !=null)
                {
                    for (int j=i; j<20; j++)
                    {
                        File image_to_set = images_inFolder[j];
                        imgview.setImageURI(Uri.fromFile(image_to_set));

                        int id = imgview.getId();
                        dict_of_20.put(id, image_to_set.getPath());
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View view)
    {
        int id_of_Clicked = view.getId();

        // if clicked is the confirm button
        if (id_of_Clicked == R.id.button){
            confirmBtnClicked();
            return;
        }

        // if clicked is image
        ImageView iv = (ImageView) view;

            // not selected yet. there should also be < 6 in the selected list
        if (id_of_6_selected.size() < 6 && !id_of_6_selected.contains(id_of_Clicked)){
            iv.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
//            Toast.makeText(this,"Selected",Toast.LENGTH_SHORT).show();
            id_of_6_selected.add(id_of_Clicked);
        }
            // already selected. want to deselect
        else if (id_of_6_selected.contains(id_of_Clicked)){
            iv.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

            Integer id = Integer.valueOf(id_of_Clicked);
            id_of_6_selected.remove(id);
        }
        else {
            Toast.makeText(this,"Select 6 only",Toast.LENGTH_SHORT).show();
        }

        // Enable or disable "confirm" button
        Button btnSelect = findViewById(R.id.button);
        if (id_of_6_selected.size() == 6){
            btnSelect.setEnabled(true);
        }
        else{
            btnSelect.setEnabled(false);
        }
    }

    private void confirmBtnClicked(){
        List<String> files_of_selected6 = new ArrayList<>();

        if (id_of_6_selected.size() != 6){
            Toast.makeText(this,"You need to select at least 6",Toast.LENGTH_SHORT).show();
            return;
        }

        if (id_of_6_selected.size() == 6){
            for (Integer id : id_of_6_selected){
                String imgFilePath = dict_of_20.get(id);
                files_of_selected6.add(imgFilePath);
            }
        }

        // get file objects
        String filePath = "selected_6";
        File mTargetFolder = new File(getFilesDir(), filePath);

        if (!mTargetFolder.exists()){
           mTargetFolder.mkdirs();
        }
        else{

            File[] files_inFolder = mTargetFolder.listFiles();

            // Delete files in folder if exist
            if (files_inFolder != null || files_inFolder.length > 0 ){
                for (int j=0; j<files_inFolder.length; j++){
                    new File(mTargetFolder, files_inFolder[j].getName()).delete();
                }
            }

            // Store selected 6 images into internal storage
            try{
                for (int k=0; k<6; k++){

                    // 1. Make new file
                    String targetFile_name = "selectedImg_"+Integer.valueOf(k+1).toString() + ".jpg";
                    File newImgFile = new File (mTargetFolder, targetFile_name);

                    // 2. write the file (byteArray/bitmap) to newImgFile
                    FileOutputStream fos = new FileOutputStream(newImgFile);

                    String imgFilePath = files_of_selected6.get(k);
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Launch Activity 2
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);

        }
    }

}



////        Animation to fade imageview (in case we need somewhere)
//        fadeOut= AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.fade_out);
//        iv.startAnimation(fadeOut);


//                btnSelect.setOnClickListener(new View.OnClickListener()
//                        {
//@Override
//public void onClick(View view)
//        {
//        btnSelect.setEnabled(true);
//        }
//        });
