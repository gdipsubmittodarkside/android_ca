package com.example.androidca;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    // playing Grid
    GridView gridView;

    // first image that is clicked
    ImageView firstImageSelected = null;
    int firstImage_Pos = -1; // 0-11

    // second image clicked
    ImageView secondImageSelected;
    int secondImage_Pos;

    //turn
    int turn = 1;

    //score
    int p1_score = 0;
    int p2_score = 0;

    //Position List
    private ArrayList<String> text = new ArrayList<>();

    //Image Uri of 12 images (from select 6 images)
    private ArrayList<Uri> imageList = new ArrayList<>();

    TextView tv_p1;
    TextView tv_p2;
    TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tv_p1 = (TextView) findViewById(R.id.tv_ply1);
        tv_p2 = (TextView) findViewById(R.id.tv_ply2);

        //starts from player 1
        tv_p1.setTextColor(Color.GREEN);
        tv_p2.setTextColor(Color.GRAY);

        // this fills a list of 12 images, with the 2 selected images from internal storage
        fillArray();

        // Sets the playing grid
        gridView = (GridView) findViewById(R.id.gridView);
        ImageAdaptor imageAdaptor = new ImageAdaptor(this);
        gridView.setAdapter(imageAdaptor);
        gridView.setEnabled(false);

        // set onItemClickListener for playing Grid
        gridView.setOnItemClickListener(this);

        // set onClickListener for other buttons
        Button startBtn = findViewById(R.id.button_StartGame);
        startBtn.setOnClickListener(this);
    }

    // FOR OTHER BUTTONS
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_StartGame){
            gridView.setEnabled(true);

            timer = findViewById(R.id.playtime);

            new CountDownTimer(240 * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    timer.setText("TIME LEFT: " + millisUntilFinished / 1000 + "s");
                }

                public void onFinish() {
                    showGameResults();
                }
            }.start();
        }
    }

    // FOR GRID VIEW
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ImageView img_view = (ImageView) view;
        
        //first clicked
        if (firstImage_Pos < 0) {

            // Diplay image
            firstImage_Pos = position;
            firstImageSelected = img_view;
            img_view.setImageURI(imageList.get(position));

            return;
        }

        // second click

            // 1) Display 2nd image clicked. Always true
        if (firstImage_Pos != position) {
            img_view.setImageURI(imageList.get(position));
            secondImageSelected = img_view;
            secondImage_Pos = position;
        }

            // 2) check if the 2 images are the same.
                // if not same, change back.
                // if same, give score.
                // finally, change turn and reset firstImage_Pos = -1.
        Runnable checker = new Runnable() {
            @Override
            public void run() {
                checkImagesAndScore();
            }
        };
        img_view.postDelayed(checker, 1000);

    }

    private void checkImagesAndScore(){
        String firstImageUri = imageList.get(firstImage_Pos).getPath();
        String secondImageUri = imageList.get(secondImage_Pos).getPath();

        if (!firstImageUri.equals(secondImageUri)) {
            // if not same
            firstImageSelected.setImageResource(R.drawable.question_mark);
            secondImageSelected.setImageResource(R.drawable.question_mark);
        }
        else{
            // if same
            firstImageSelected.setEnabled(false);
            secondImageSelected.setEnabled(false);

            // give score
            if (turn == 1) {
                p1_score++;
                tv_p1.setText("P1 : " + p1_score);
            } else if (turn == 2) {
                p2_score++;
                tv_p2.setText("P2 : " + p2_score);
            }

            // check if there is a winner
            if(p1_score+p2_score==6){
                showGameResults();
            }
        }

        // finally, reset firstImage clicked position and change turn
        firstImage_Pos = -1;

        if (turn == 1) {
            turn = 2;
            tv_p1.setTextColor(Color.GRAY);
            tv_p2.setTextColor(Color.GREEN);
        } else if (turn == 2) {
            turn = 1;
            tv_p2.setTextColor(Color.GRAY);
            tv_p1.setTextColor(Color.GREEN);
        }
    }

    public void fillArray() {

        // put integers 0-11 into array, representing the positions of the images in the grid
        // global attribute: text: List<Integer>
        for (Integer i = 0; i < 12; i++) {
            text.add(i.toString());
        }

        // put 12 resource Id of images into a list
        // global attribute: imageList: List<Integer>
        String filePath = "selected_6";
        File mTargetFolder = new File(getFilesDir(), filePath);

        if (mTargetFolder.exists()){
            File[] selected_6_images = mTargetFolder.listFiles();

            if (selected_6_images != null){
                for (int i=0; i<2; i++){
                    for (int j=0; j<6; j++){
                        File imageFile = selected_6_images[j];
                        Uri imageUri = Uri.fromFile(imageFile);
                        imageList.add(imageUri);
                    }
                }
            }
        }

        Collections.shuffle(imageList);

    }

    private void showGameResults(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity3.this);
        alertDialogBuilder
                .setMessage("Game Over!\nP1: " + p1_score+ "\nP2: " + p2_score)
                .setCancelable(false)
                .setPositiveButton("NEW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
