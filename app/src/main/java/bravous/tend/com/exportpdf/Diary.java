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
    String comment;
    long commentTime;

    public Diary(){ }

    public Diary(String notebook_name, String date, String emotion, String textPath, String imgPath, String comment, long commentTime){
        this.notebook_name = notebook_name;
        this.date = date;
        this.emotion = emotion;
        this.textPath = textPath;
        this.imgPath = imgPath;
        this.comment = comment;
        this.commentTime = commentTime;

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

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
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

    public String getComment() {
        return comment;
    }

    public long getCommentTime() {
        return commentTime;
    }

}
