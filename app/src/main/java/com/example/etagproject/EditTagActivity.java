package com.example.etagproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditTagActivity extends AppCompatActivity {

    private static final String TAG = "EditTagActivity";

    private Button buttonSaveModify, buttonDelete;
    private EditText editTagName;
    DatabaseHelper mDatabaseHelper;
    private String selectedTagName;
    private int selectedID;
    private String selectedBarCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.edit_tag_layout);
        buttonSaveModify = (Button) findViewById(R.id.buttonSaveModify);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        editTagName = (EditText) findViewById(R.id.editTagName);
        //editBarCode = (EditText) findViewById(R.id.editBarCode);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedTagName = receivedIntent.getStringExtra("name");
        //selectedBarCode = receivedIntent.getStringExtra("bar_code");
        editTagName.setText(selectedTagName);
        //editBarCode.setText(selectedBarCode);
        buttonSaveModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editTagName.getText().toString();
                if(!item.equals("")) {
                    mDatabaseHelper.updateTag(item,selectedID,selectedTagName);
                }else{
                      toastMessage("You must enter a name!");
                    }
                }

        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedTagName);
                editTagName.setText("");
                toastMessage("Removed from database");
            }
        });


    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
