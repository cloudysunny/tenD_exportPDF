package bravous.tend.com.exportpdf;

/**
 * Created by cloud on 2017-12-06.
 */

public class Diary {

    String notebook_name;
    String date;
    String emotion;
    String textPath;
    String imgPath;

    public Diary(){ }

    public Diary(String notebook_name, String date, String emotion, String textPath, String imgPath){
        this.notebook_name = notebook_name;
        this.date = date;
        this.emotion = emotion;
        this.textPath = textPath;
        this.imgPath = imgPath;
    }

    public void setNotebook_name(String notebook_name){
            this.notebook_name = notebook_name;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setEmotion(String emotion){
        this.emotion = emotion;
    }

    public void setTextPath(String textPath){
        this.textPath = textPath;
    }

    public void setImgPath(String imgPath){
        this.imgPath = imgPath;
    }

    public String getNotebook_name(){
        return notebook_name;
    }

    public String getDate(){
        return date;
    }

    public String getEmotion(){
        return emotion;
    }

    public String getTextPath(){
        return textPath;
    }

    public String getImgPath(){
        return imgPath;
    }




}
