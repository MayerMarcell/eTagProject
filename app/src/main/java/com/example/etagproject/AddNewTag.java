package com.example.etagproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddNewTag extends AppCompatActivity {
    private static final String TAG = "AddNewTag";

    DatabaseHelper mDatabaseHelper;
    private ImageButton imageButtonGoBack;
    private Button buttonSave;
    private EditText setTagName;
    private EditText setBarCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tag);
        imageButtonGoBack = (ImageButton) findViewById(R.id.imageButtonGoBack);
        setTagName = (EditText) findViewById(R.id.setTagName);
        setBarCode = (EditText) findViewById(R.id.setBarCode);
        buttonSave = (Button) findViewById(R.id.buttonSave);


        imageButtonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewTag.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDatabaseHelper = new DatabaseHelper(this);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String newEntryName = setTagName.getText().toString();
                String newEntryBarCode = setBarCode.getText().toString();
                if(setTagName.length()!=0 || setBarCode.length()!=0){
                    AddData(newEntryName, newEntryBarCode);
                    setTagName.setText("");
                    setBarCode.setText("");
                }else{
                    toastMessage("You must írj something a textmezőbe!");
                }
            }
        });
    }

        public void toastMessage(String message){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        public void AddData(String newEntryName, String newEntryBarCode){
            boolean insertData = mDatabaseHelper.addData(newEntryName, newEntryBarCode);

            if(insertData){
                toastMessage("Data Successfully Inserted!");
            }else{
                toastMessage("Valami nem jo he");
            }

        }

}