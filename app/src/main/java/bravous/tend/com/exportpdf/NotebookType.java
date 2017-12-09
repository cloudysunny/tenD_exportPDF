package bravous.tend.com.exportpdf;

/**
 * Created by cloud on 2017-12-09.
 */

public enum NotebookType {

    LEAF(R.drawable.notebook1),
    FLOWER(R.drawable.notebook2),
    PATTERN(R.drawable.notebook3),
    FRUIT(R.drawable.notebook4);

    int coverPath;

    NotebookType(int coverPath){
        this.coverPath = coverPath;
    }

    public int getCoverPath(){
        return coverPath;
    }
}
