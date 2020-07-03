package com.example.memo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    private EditText Title_text;
    private EditText Content_text;
    private String cureent_Title_value;
    private String cureent_Content_value;
    private MyDataBase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db=new MyDataBase(this);
        Title_text=(EditText)findViewById(R.id.update_title_id);
        Content_text=(EditText)findViewById(R.id.update_content_id);

        //retriving data from mainActivity (Title, Content)
        cureent_Title_value = getIntent().getStringExtra("Title");
        cureent_Content_value = getIntent().getStringExtra("Content");

        Title_text.setText(cureent_Title_value);
        Content_text.setText(cureent_Content_value);

    }


    //show the menu listoptions (save & discard) and check which of them clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_update_options,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.save){
            Save_update();
        }

        else if(id==R.id.discard){
            Title_text.setText("");
            Content_text.setText("");
        }

        return super.onOptionsItemSelected(item);
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Save_update(){
        String updated_Title=Title_text.getText().toString();
        String updated_Content=Content_text.getText().toString();
        String updated_date= new SimpleDateFormat("dd MMM  HH:mm a").format(Calendar.getInstance().getTime());

        if(updated_Title.isEmpty() || updated_Content.isEmpty()) Toast.makeText(UpdateActivity.this,"Empty cells",Toast.LENGTH_SHORT).show();
        else {
            if (db.noteIsFound(updated_Title)) {
                Toast.makeText(UpdateActivity.this, "you have a note with the same title", Toast.LENGTH_SHORT).show();
            } else {
                boolean isUpdated = db.UpdateNote(new Note(updated_Title, updated_Content, String.valueOf(updated_date)), cureent_Title_value);
                if (isUpdated) {
                    Toast.makeText(UpdateActivity.this, "Note Updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateActivity.this, ListMainActivity.class));
                    finish();
                } else
                    Toast.makeText(UpdateActivity.this, "No updated Added", Toast.LENGTH_SHORT).show();
            }
        }

    }

}