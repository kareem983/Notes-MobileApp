package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper {


        public MyDataBase(Context context){
            super(context,Utils.DB_NAME,null,Utils.DB_VERSION);
        }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String Table1="CREATE TABLE "+Utils.TABLE1_NAME+"( "+Utils.TITLE_KEY+" TEXT UNIQUE PRIMARY KEY, "
                +Utils.CONTENT_KEY+" TEXT , "+Utils.DATE_KEY+" TEXT);";

        String Table2="CREATE TABLE "+Utils.TABLE2_NAME +" ("+Utils.View_KEY +" TEXT)";

        db.execSQL(Table1);
        db.execSQL(Table2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE2_NAME);
        onCreate(db);
    }



    public boolean AddNote(Note note){
        SQLiteDatabase db= getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(Utils.TITLE_KEY,note.getTitle());
        values.put(Utils.CONTENT_KEY,note.getContent());
        values.put(Utils.DATE_KEY,note.getDate());

        long isAdded = db.insert(Utils.TABLE1_NAME,null,values);

        //if isInserted == -1 so dont inserted else inserted
        return isAdded!=-1;
    }



    public boolean UpdateNote(Note note,String SearchingTitle){
        SQLiteDatabase db= getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(Utils.TITLE_KEY,note.getTitle());
        values.put(Utils.CONTENT_KEY,note.getContent());
        values.put(Utils.DATE_KEY,note.getDate());

        String []args={SearchingTitle+""};
        int isUpdated = db.update(Utils.TABLE1_NAME,values,"Title=?",args);

        //if isUpdated == 0 so dont updated else updated  // return number of rows that updated
        return isUpdated >0;
    }



    public boolean DeleteNote(String DeletingTitle){
        SQLiteDatabase dp= getWritableDatabase();

        String []args={DeletingTitle+""};
        int isDeleted= dp.delete(Utils.TABLE1_NAME,"Title=?",args);

        //if isDeleted == 0 so dont deleted else deleted   // return number of rows that deleted
        return isDeleted>0;
    }



    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> notes=new ArrayList<>();
        SQLiteDatabase dp=getReadableDatabase();
        Cursor cursor =dp.rawQuery("SELECT * FROM "+Utils.TABLE1_NAME,null);

        if(cursor.moveToLast()){
            //so the the table have records of persons
            do{
                String note_title = cursor.getString(cursor.getColumnIndex(Utils.TITLE_KEY));
                String note_content = cursor.getString(cursor.getColumnIndex(Utils.CONTENT_KEY));
                String note_date = cursor.getString(cursor.getColumnIndex(Utils.DATE_KEY));

                Note c=new Note(note_title,note_content,note_date);
                notes.add(c);
            }while (cursor.moveToPrevious());
            cursor.close();
        }

        return notes;
    }



    public Note getOneNote (String SearcingTitle){
        SQLiteDatabase dp=getReadableDatabase();
        Cursor cursor = dp.rawQuery("SELECT * FROM "+Utils.TABLE1_NAME,null);
        boolean isFound=false;
        Note c =new Note();
        if(cursor.moveToFirst()){
            do{
                if(SearcingTitle.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(Utils.TITLE_KEY)) )){
                    String note_title = cursor.getString(cursor.getColumnIndex(Utils.TITLE_KEY));
                    String note_content = cursor.getString(cursor.getColumnIndex(Utils.CONTENT_KEY));
                    String note_date = cursor.getString(cursor.getColumnIndex(Utils.DATE_KEY));
                    c= new Note(note_title,note_content,note_date);
                    isFound=true; break;
                }
            }while(cursor.moveToNext());

        }
        cursor.close();
        if(isFound) return c;
        else return null;
    }



    public boolean noteIsFound (String SearcingTitle){
        SQLiteDatabase dp=getReadableDatabase();
        Cursor cursor = dp.rawQuery("SELECT * FROM "+Utils.TABLE1_NAME,null);
        boolean isFound=false;
        if(cursor.moveToFirst()){
            do{
                if(SearcingTitle.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(Utils.TITLE_KEY)) )){
                    isFound=true; break;
                }
            }while(cursor.moveToNext());

        }
        cursor.close();
        return isFound;
    }



    public long restoreNumOfNotes(){
        SQLiteDatabase db=getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db,Utils.TABLE1_NAME);
    }


    //*********************************************************************
    //table2 check lists


    public boolean UpdateLists(String updatedList){
        SQLiteDatabase db= getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(Utils.View_KEY,updatedList);

        int isUpdated = db.update(Utils.TABLE2_NAME,values,null,null);

        //if isUpdated == 0 so dont updated else updated  // return number of rows that updated
        return isUpdated >0;
    }


    public String getCurrentList (){
        SQLiteDatabase dp=getReadableDatabase();
        Cursor cursor = dp.rawQuery("SELECT * FROM "+Utils.TABLE2_NAME,null);
        String ListValue="";

        if(cursor.moveToFirst()){
            ListValue = cursor.getString(cursor.getColumnIndex(Utils.View_KEY));
        }
        cursor.close();

     return ListValue;
    }


}
