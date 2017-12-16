package bravous.tend.com.exportpdf;

/**
 * Created by cloud on 2017-12-17.
 */

public class Comment {

    String emotion_type;
    String comment;

    public Comment(){}

    public Comment(String emotion_type, String comment){
        this.emotion_type = emotion_type;
        this.comment = comment;
    }

    public void setEmotionType(String emotion){
        emotion_type = emotion;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getEmotion_type(){
        return emotion_type;
    }

    public String getComment(){
        return comment;
    }
}
