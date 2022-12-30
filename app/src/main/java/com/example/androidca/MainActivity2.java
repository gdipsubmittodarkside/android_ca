package com.example.androidca;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    List<Integer> imageView_id_list;
    List<File> imageList;
    List<Integer> checkList; // 12 int, 0-5 x 2 sets, corresponds to imageList attribute
    int clickCount;
    int index_firstImageClicked; // 0-11, corresponds to "imageView_id_list", "imageList" and "checkList"
    int index_secondImageClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialise all the lists
        imageView_id_list = new ArrayList<>();
        imageList = new ArrayList<>();
        checkList = new ArrayList<>();

        // Get all imageView ids
        imageView_id_list = Arrays.asList(
                R.id.selectedImg_1, R.id.selectedImg_2, R.id.selectedImg_3, R.id.selectedImg_4,
                R.id.selectedImg_5, R.id.selectedImg_6, R.id.selectedImg_7, R.id.selectedImg_8,
                R.id.selectedImg_9, R.id.selectedImg_10, R.id.selectedImg_11, R.id.selectedImg_12
        );

        // set onClickListener for ImageViews
        for (int k=0; k<imageView_id_list.size(); k++){
            ImageView iv = findViewById(imageView_id_list.get(k));
            iv.setOnClickListener(this);
        }

        // Set checkList
        checkList = Shuffle0to5Twice();

        // Get images from internal storage and set the position of these 12 images
        setPositionOf12Images();

        // Set clickCount = 0
        clickCount = 0;

//        set12Images(imageView_id_list);
    }

    @Override
    public void onClick(View view) {
        clickCount++;
        int id_ofClicked = view.getId();

        // Display clicked image
        ImageView iv = (ImageView) view;
        int index = imageView_id_list.indexOf(id_ofClicked); // 0-11
        File image_toSet = imageList.get(index); // image to reveal
        iv.setImageURI(Uri.fromFile(image_toSet));


        if (clickCount == 1){
            index_firstImageClicked = index;
        }

        if (clickCount == 2){
            index_secondImageClicked = index;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if images are the same
                checkImages();
            }
        }, 4000);
    }

    private void checkImages() {

        if (clickCount == 2){
            int checkList_num_secondImageClicked = checkList.get(index_secondImageClicked);
            int checkList_num_firstImageClicked = checkList.get(index_firstImageClicked);

            ImageView iv_first = findViewById(imageView_id_list.get(index_firstImageClicked));
            ImageView iv_second = findViewById(imageView_id_list.get(index_secondImageClicked));

            if (checkList_num_firstImageClicked != checkList_num_secondImageClicked){

                iv_first.setImageResource(R.drawable.question_mark);
                iv_second.setImageResource(R.drawable.question_mark);
            }
            else{
                iv_first.setEnabled(false);
                iv_second.setEnabled(false);
            }

            clickCount = 0;
        }
    }

    private void setPositionOf12Images(){
        // Get file objects
        String filePath = "selected_6";
        File mTargetFolder = new File(getFilesDir(), filePath);

        // Add images into imageList according to checkList
        // Eg: checkList = {0, 2, 4, 5, 1, 3, 2, 3, 0, 1, 5}
        if (mTargetFolder.exists()){
            File[] images_inFolder = mTargetFolder.listFiles();

            if (images_inFolder != null){
                for (int i=0; i<12; i++){
                    File image_toAdd = images_inFolder[checkList.get(i)];
                    imageList.add(image_toAdd);
                }
            }
        }
    }

    private List<Integer> Shuffle0to5Twice(){
        List<Integer> intList = new ArrayList<Integer>();

        for (int i=0; i<2; i++){
            for (int j=0;j<6;j++){
                intList.add(j);
            }
        }
        Collections.shuffle(intList);

        return intList;
    }

}