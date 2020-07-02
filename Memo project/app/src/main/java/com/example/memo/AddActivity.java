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

public class AddActivity extends AppCompatActivity {
    private EditText Title_text;
    private EditText Content_text;
    private MyDataBase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db=new MyDataBase(this);
        Title_text=(EditText)findViewById(R.id.Add_title_id);
        Content_text=(EditText)findViewById(R.id.Add_content_id);

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
            save_Adding();
        }

        else if(id==R.id.discard){
            Title_text.setText("");
            Content_text.setText("");
        }

        return super.onOptionsItemSelected(item);
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void save_Adding(){
        String Title_Value=Title_text.getText().toString();
        String Content_Value=Content_text.getText().toString();
        String date_value = new SimpleDateFormat("dd MMM  HH:mm a").format(Calendar.getInstance().getTime());

        if(Title_Value.isEmpty() || Content_Value.isEmpty()) Toast.makeText(AddActivity.this,"Empty cells",Toast.LENGTH_SHORT).show();
        else {
            boolean isAdded = db.AddNote(new Note(Title_Value, Content_Value, String.valueOf(date_value)));
            if(isAdded) {
                Toast.makeText(AddActivity.this,"Note Added successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddActivity.this, ListMainActivity.class));
                finish();
            }
            else Toast.makeText(AddActivity.this,"you have a note with the same title",Toast.LENGTH_SHORT).show();
        }

    }

}