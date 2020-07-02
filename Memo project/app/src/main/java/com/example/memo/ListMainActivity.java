package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListMainActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    private ArrayList<Note> note_search;
    private ListView listView;
    private EditText search_text;
    private LinearLayout searchContainer;
    private LinearLayout deleatContainer;
    private ImageButton DoneButton;
    private TextView no_notes_Found;
    private MyDataBase db;

    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);
        setTitle("Notes");

        db=new MyDataBase(this);
        no_notes_Found=(TextView)findViewById(R.id.No_Notes_id);
        listView=(ListView)findViewById(R.id.list_view);
        search_text=(EditText)findViewById(R.id.search_Edit);
        searchContainer=(LinearLayout)findViewById(R.id.search_Container);
        deleatContainer=(LinearLayout)findViewById(R.id.Delete_Container);
        DoneButton=(ImageButton)findViewById(R.id.DoneButton);

        if(db.getCurrentList().equals("Grid")){
            startActivity(new Intent(ListMainActivity.this,GridMainActivity.class));
            finish();
        }


        // check there is at least one note in the database
        if(db.restoreNumOfNotes()!=0){
            notes=new ArrayList<>();
            note_search=new ArrayList<>();
            notes=db.getAllNotes();
            final NoteAdapter adapter=new NoteAdapter(this,notes);
            listView.setAdapter(adapter);

            //there are two list options (click to note to update it OR long click to select it to delete it)


            //on clicking at any note .... so go to update activity
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //select the clicked note then send the data to update activity
                    Intent intent = new Intent(ListMainActivity.this, UpdateActivity.class);

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
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {

                    //check if the user searched and select the searched note or not
                    if (note_search.size() > 0) {
                        //select the clicked note
                        final Note note = note_search.get(index);
                        // then sign the boolean in note class that the note selected
                        note.hase_Image = true;
                        final NoteAdapter adapter = new NoteAdapter(ListMainActivity.this, note_search);
                        listView.setAdapter(adapter);
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
                        final NoteAdapter adapter = new NoteAdapter(ListMainActivity.this, notes);
                        listView.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.listoptions,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.goToGrid){
            db.UpdateLists("Grid");
            startActivity(new Intent(ListMainActivity.this,GridMainActivity.class));
            finish();
        }
        else if(id==R.id.List_add_id){
            startActivity(new Intent(ListMainActivity.this,AddActivity.class));
        }

        else if(id==R.id.List_search_id){
            Search();
        }

        else if(id==R.id.List_refresh_id){
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
                if(search_value.isEmpty()) Toast.makeText(ListMainActivity.this,"Empty cell",Toast.LENGTH_SHORT).show();
                else {
                    Note c = db.getOneNote(search_value);
                    if(c!=null) {
                        note_search=new ArrayList<>();
                        note_search.add(c);
                        final NoteAdapter adapter=new NoteAdapter(ListMainActivity.this,note_search);
                        listView.setAdapter(adapter);
                        search_text.setText("");
                        searchContainer.setVisibility(View.GONE);
                    }

                    else Toast.makeText(ListMainActivity.this,"the note doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void DeleteNote(){
        deleatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete all notes that signed by true
                for(int i=0;i<notes.size();i++){
                    if(notes.get(i).hase_Image==true) {
                        boolean isDeleted = db.DeleteNote(notes.get(i).getTitle()); }
                }

                notes=db.getAllNotes();
                final NoteAdapter adapter=new NoteAdapter(ListMainActivity.this,notes);
                listView.setAdapter(adapter);
                Toast.makeText(ListMainActivity.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                Refresh();
            }
        });

    }

    private void Refresh() {
        if (db.restoreNumOfNotes() == 0) no_notes_Found.setVisibility(View.VISIBLE);
        else {
            final NoteAdapter adapter = new NoteAdapter(ListMainActivity.this, notes);
            listView.setAdapter(adapter);
        }

        //un visible the search edit view and delete the text in the search edit view
        search_text.setText("");
        searchContainer.setVisibility(View.GONE);
        deleatContainer.setVisibility(View.GONE);
        note_search.clear();

        //delete all selection from the note class
        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).hase_Image = false;
        }

    }
}