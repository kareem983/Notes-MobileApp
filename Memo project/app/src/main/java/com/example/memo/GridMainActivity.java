package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GridMainActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    private ArrayList<Note> note_search;
    private GridView gridView;
    private EditText search_text;
    private LinearLayout searchContainer;
    private LinearLayout deleatContainer;
    private ImageButton DoneButton;
    private TextView no_notes_Found;
    private MyDataBase db;
    private long backPressed;

    //AlertDialog variables
    private int numberOfNotedSelected;
    private String AlertMessage;
    private String AlertSuccessfullyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_main);

        db=new MyDataBase(this);
        no_notes_Found=(TextView)findViewById(R.id.Grid_No_Notes_id);
        gridView=(GridView) findViewById(R.id.grid_view);
        search_text=(EditText)findViewById(R.id.Grid_search_Edit);
        searchContainer=(LinearLayout)findViewById(R.id.Grid_search_Container);
        deleatContainer=(LinearLayout)findViewById(R.id.Grid_Delete_Container);
        DoneButton=(ImageButton)findViewById(R.id.Grid_DoneButton);

        numberOfNotedSelected=0;

        if(db.getCurrentList().equals("List")){
            startActivity(new Intent(GridMainActivity.this, ListMainActivity.class));
            finish();
        }



        if(db.restoreNumOfNotes()!=0){
            notes=new ArrayList<>();
            note_search=new ArrayList<>();
            notes=db.getAllNotes();
            final NoteAdapter adapter=new NoteAdapter(this,notes);
            gridView.setAdapter(adapter);

            //there are two grid options (click to note to update it OR long click to select it to delete it)



            //on clicking at any note .... so go to update activity
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(GridMainActivity.this, UpdateActivity.class);

                    //check if the user searched and click or not
                    if (note_search.size() > 0) {
                        Note note = note_search.get(position);
                        intent.putExtra("Title", String.valueOf(note.getTitle()));
                        intent.putExtra("Content", String.valueOf(note.getContent()));
                    }
                    else {
                        Note note = notes.get(position);
                        intent.putExtra("Title", String.valueOf(note.getTitle()));
                        intent.putExtra("Content", String.valueOf(note.getContent()));
                    }

                    startActivity(intent);
                }
            });


            //on Long clicking at any note .... so delete this note
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

                    //check if the user searched and select the searched note or not
                    if (note_search.size() > 0) {
                        //select the clicked note
                        final Note note = note_search.get(index);
                        // then sign the boolean in note class that the note selected
                        note.hase_Image = true;
                        final NoteAdapter adapter = new NoteAdapter(GridMainActivity.this, note_search);
                        gridView.setAdapter(adapter);
                        for(int i=0;i<notes.size();i++){
                            if(note.getTitle().equalsIgnoreCase(notes.get(i).getTitle())){
                                notes.get(i).hase_Image=true;
                            }
                        }
                    }
                    else {
                        //select the clicked note
                        final Note note = notes.get(index);
                        // then sign the boolean in note class that the note selected
                        note.hase_Image = true;
                        final NoteAdapter adapter = new NoteAdapter(GridMainActivity.this, notes);
                        gridView.setAdapter(adapter);
                    }

                    deleatContainer.setVisibility(View.VISIBLE);
                    // then check is delete LinearLayout clicked or not
                    DeleteNote();
                    return true;
                }
            });
        }

        else no_notes_Found.setVisibility(View.VISIBLE);

    }


    //on clicking back button finish the app
    @Override
    public void onBackPressed() {
        if(backPressed+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            finish();
        }

        backPressed=System.currentTimeMillis();
    }




    //show the menu listoptions (Add & search & refresh) and check which of them clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gridoptions,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.goToList){
            db.UpdateLists("List");
            startActivity(new Intent(GridMainActivity.this, ListMainActivity.class));
            finish();
        }
        else if(id==R.id.Grid_add_id){
            startActivity(new Intent(GridMainActivity.this,AddActivity.class));
        }

        else if(id==R.id.Grid_search_id){
            Search();
        }

        else if(id==R.id.Grid_refresh_id){
            Refresh();
        }

        return super.onOptionsItemSelected(item);
    }



    private void Search(){
        searchContainer.setVisibility(View.VISIBLE);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_value=search_text.getText().toString();
                if(search_value.isEmpty()) Toast.makeText(GridMainActivity.this,"Empty cell",Toast.LENGTH_SHORT).show();
                else {
                    Note c = db.getOneNote(search_value);
                    if(c!=null) {
                        note_search=new ArrayList<>();
                        note_search.add(c);
                        final NoteAdapter adapter=new NoteAdapter(GridMainActivity.this,note_search);
                        gridView.setAdapter(adapter);
                        search_text.setText("");
                        searchContainer.setVisibility(View.GONE);
                    }

                    else Toast.makeText(GridMainActivity.this,"the note doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void DeleteNote(){
        deleatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check how many note selected then provide message that will show to the user
                for(int i=0;i<notes.size();i++){ if(notes.get(i).hase_Image==true)numberOfNotedSelected++; }

                if(numberOfNotedSelected==1) {
                    AlertMessage="Do you want to delete "+String.valueOf(numberOfNotedSelected)+" Note?";
                    AlertSuccessfullyMessage=String.valueOf(numberOfNotedSelected)+" Note Deleted Successfully";
                }
                else {
                    AlertMessage="Do you want to delete "+String.valueOf(numberOfNotedSelected)+" Notes?";
                    AlertSuccessfullyMessage=String.valueOf(numberOfNotedSelected)+" Notes Deleted Successfully";
                }

                //create the AlertDialog then check the user choose yes or no
                AlertDialog.Builder checkAlert = new AlertDialog.Builder(GridMainActivity.this);
                checkAlert.setMessage(AlertMessage)
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete all notes that signed by true
                        for(int i=0;i<notes.size();i++){
                            if(notes.get(i).hase_Image==true) {
                                boolean isDeleted = db.DeleteNote(notes.get(i).getTitle()); }
                        }

                        notes=db.getAllNotes();
                        final NoteAdapter adapter=new NoteAdapter(GridMainActivity.this,notes);
                        gridView.setAdapter(adapter);
                        Toast.makeText(GridMainActivity.this,AlertSuccessfullyMessage,Toast.LENGTH_SHORT).show();
                        Refresh();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Refresh();
                    }
                });
                AlertDialog alert = checkAlert.create();
                alert.setTitle("Alert Message");
                alert.show();
            }

        });

    }

    private void Refresh() {
        if (db.restoreNumOfNotes() == 0) no_notes_Found.setVisibility(View.VISIBLE);
        else {
            final NoteAdapter adapter = new NoteAdapter(GridMainActivity.this, notes);
            gridView.setAdapter(adapter);
        }

        //un visible the search edit view and delete the text in the search edit view
        search_text.setText("");
        searchContainer.setVisibility(View.GONE);
        deleatContainer.setVisibility(View.GONE);
        note_search.clear();
        numberOfNotedSelected=0;

        //delete all selection from the note class
        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).hase_Image = false;
        }

    }


}