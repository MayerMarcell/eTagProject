package com.example.etagproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    public String selectedTagName;
    public String selectedTagId;

    private static final String TAG = "MainActivity";
    DatabaseHelper mDatabaseHelper;

    private Button buttonEdit;
    private ImageButton buttonAdd;
    private ListView tag_list;
    private TextView txt_name;
    private TextView txt_barcode;
    private ImageView imageViewResult;
    private Button buttonBC;
    private Button buttonNFC;
    private Button buttonQR;

    ArrayList<String> listData = new ArrayList<>();
    ArrayList<String> showlistData = new ArrayList<>();

    /////////////////////////////////////////onCreate///////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        //editText = (EditText) findViewById(R.id.editTag);
        tag_list = (ListView) findViewById(R.id.tag_list);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
        txt_name = findViewById(R.id.txt_name);
        txt_barcode = findViewById(R.id.txt_barcode);
        imageViewResult = findViewById(R.id.imageViewResult);
        buttonNFC = findViewById(R.id.buttonNFC);
        buttonQR = findViewById(R.id.buttonQR);
        buttonBC = findViewById(R.id.buttonBC);

        buttonAdd.setOnClickListener(view -> openAddNewTag());

        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        mDatabaseHelper = new DatabaseHelper(this);
        Log.d(TAG, "-------------------------------------");
        if(mDatabaseHelper.getData().moveToFirst()){
            populateListView();
            selectFromListView();
        }


        /*buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewTag.class);
                startActivity(intent);
            }
        });*/
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void openAddNewTag(){
        Intent intent = new Intent(this, AddNewTag.class);
        startActivity(intent);
    }




    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView");
        Cursor data = mDatabaseHelper.getData();

        while (data.moveToNext()) {
            listData.add(data.getString(1));
            showlistData.add(data.getString(2));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        tag_list.setAdapter(adapter);
        txt_name.setText(listData.get(0));
        txt_barcode.setText(showlistData.get(0));



    }

    public void selectFromListView(){
        tag_list.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = adapterView.getItemAtPosition(i).toString();
            //String barcode = adapterView.getItemAtPosition(i).toString();
            Log.d(TAG, "onItemClick: You Clicked on " + name);

            Cursor data1 = mDatabaseHelper.getItemID(name);
            int itemID = -1;
            while(data1.moveToNext()){
                itemID = data1.getInt(0);
            }
            if(itemID > -1){
                Log.d(TAG, "onItemClick: The ID id: " + itemID);
                Intent editScreenIntent = new Intent(MainActivity.this, EditTagActivity.class);
                editScreenIntent.putExtra("id", itemID);
                editScreenIntent.putExtra("name", name);
                txt_name.setText(listData.get(i));
                txt_barcode.setText(showlistData.get(i));
                selectedTagName = txt_name.toString();
                selectedTagId = txt_barcode.toString();
                //startActivity(editScreenIntent);
                barCodeButton(view);

                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent editScreenIntent = new Intent(MainActivity.this, EditTagActivity.class);
                        startActivity(editScreenIntent);
                    }
                });
            }else{
                toastMessage("No ID associated with that name");
            }
        });
    }





    public void barCodeButton(View view){
        MultiFormatWriter barCodeWriter = new MultiFormatWriter();
        try {
            BitMatrix barCodeMatrix = barCodeWriter.encode(txt_barcode.getText().toString(), BarcodeFormat.CODE_128, imageViewResult.getWidth(), imageViewResult.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(imageViewResult.getWidth(), imageViewResult.getHeight(), Bitmap.Config.RGB_565);
            for(int i = 0; i < imageViewResult.getWidth();i++){
                for (int j = 0; j < imageViewResult.getHeight();j++){
                    bitmap.setPixel(i,j,barCodeMatrix.get(i,j)? Color.BLACK:Color.rgb(240,234,217));
                }
            }
            imageViewResult.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void qrCodeButton(View view){
        MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
        try {
            BitMatrix qrCodeMatrix = qrCodeWriter.encode(txt_barcode.getText().toString(), BarcodeFormat.QR_CODE, imageViewResult.getWidth(), imageViewResult.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(imageViewResult.getWidth(), imageViewResult.getHeight(), Bitmap.Config.RGB_565);
            for(int i = 0; i < imageViewResult.getWidth();i++){
                for (int j = 0; j < imageViewResult.getHeight();j++){
                    bitmap.setPixel(i,j,qrCodeMatrix.get(i,j)? Color.BLACK:Color.rgb(240,234,217));
                }
            }
            imageViewResult.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }





}