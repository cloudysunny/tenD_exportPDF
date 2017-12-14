package bravous.tend.com.exportpdf;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewNotebook extends BaseActivity {

    EditText noteNameView;
    Button createNoteBtn;
    NotebookType notebookType = NotebookType.FRUIT;

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
                Log.i("note name check", newName);
                createNewNote(newName, notebookType.ordinal(), false, null, "임시" );

                finish();


            }
        });

    }


    private void createNewNote(String noteName, int noteType, boolean pdfExist, String pdfPath, String coverPath){

        Notebook notebook = new Notebook(noteName, noteType, pdfExist, pdfPath, coverPath);
        MyDBHelper helper = new MyDBHelper(this);

        notebook.setNotebook_name(noteName);
        notebook.setNotebook_type(noteType);
        notebook.setExist_pdf(pdfExist);
        notebook.setPdfPath(pdfPath);
        notebook.setCoverPath(coverPath);
        helper.createNotebook(notebook);

    }


}
