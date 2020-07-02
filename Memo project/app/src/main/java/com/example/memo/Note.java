package com.example.memo;

public class Note {
    private String Title;
    private String Content;
    private String Date;
    private final int select_image_Res_Id=R.drawable.select;
    public boolean hase_Image;

    public Note(){
    }

    public Note(String Title, String Content, String Date){
        this.Title=Title;
        this.Content=Content;
        this.Date=Date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getSelect_image_Res_Id() {
        return select_image_Res_Id;
    }

    public boolean hasImage(){
        return hase_Image;
    }

}
