package bravous.tend.com.exportpdf;

/**
 * Created by cloud on 2017-12-06.
 */

public class Notebook {

    String notebook_name;
    int notebook_type;
    boolean exist_pdf;
    String pdfPath;

    public Notebook(){ }

    public Notebook(String note_name, int notebook_type, boolean exist_pdf, String pdfPath){
        this.notebook_name = note_name;
        this.notebook_type = notebook_type;
        this.exist_pdf = exist_pdf;
        this.pdfPath = pdfPath;
    }

    public void setNotebook_name(String notebook_name){
        this.notebook_name = notebook_name;
    }

    public void setNotebook_type(int notebook_type){
        this.notebook_type = notebook_type;
    }

    public void setExist_pdf(boolean exist_pdf){
        this.exist_pdf = exist_pdf;
    }

    public void setPdfPath(String pdfPath){
        this.pdfPath = pdfPath;
    }

    public String getNotebook_name(){
        return notebook_name;
    }

    public int getNotebook_type(){
        return notebook_type;
    }

    public boolean getExist_pdf(){
        return exist_pdf;
    }

    public String getPdfPath(){
        return pdfPath;
    }


}
