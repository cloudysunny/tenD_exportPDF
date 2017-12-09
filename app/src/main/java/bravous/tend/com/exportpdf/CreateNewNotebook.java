package bravous.tend.com.exportpdf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewNotebook extends AppCompatActivity {

    final static int CHRISTMAS = 1;
    EditText noteNameView;
    Button createNoteBtn;
    NotebookType notebookType = NotebookType.FLOWER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_notebook);

        noteNameView = (EditText) findViewById(R.id.noteName);
        createNoteBtn = (Button) findViewById(R.id.createNote);


        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = noteNameView.getText().toString();
                createNewNote(newName, notebookType.ordinal(), false, null );
                finish();
            }
        });
    }


    private void createNewNote(String noteName, int noteType, boolean pdfExist, String pdfPath){

        Notebook notebook = new Notebook(noteName, noteType, pdfExist, pdfPath);
        MyDBHelper helper = new MyDBHelper(this);

        notebook.setNotebook_name(noteName);
        notebook.setNotebook_type(noteType);
        notebook.setExist_pdf(pdfExist);
        notebook.setPdfPath(pdfPath);

        helper.createNotebook(notebook);

    }


}
