package com.example.etagproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;

    private Button buttonEdit;
    private ImageButton buttonAdd;
    private ListView tag_list;
    private TextView txt_name;
    private TextView txt_barcode;
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
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewTag();
            }
        });

        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();



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
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        tag_list.setAdapter(adapter);

        tag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name);
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID id: " + itemID);
                    //Intent editScreenIntent = new Intent(MainActivity.this, EditTagActivity.class);
                    //editScreenIntent.putExtra("id", itemID);
                    //editScreenIntent.putExtra("name", name);
                    txt_name.setText(adapterView.getItemAtPosition(i).toString());
                    txt_barcode.setText(adapterView.getItemAtPosition(i).toString());
                    //startActivity(editScreenIntent);

                }else{
                    toastMessage("No ID associated with that name");
                }

            }


        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(editScreenIntent);
            }
        });

        }



    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }





}
