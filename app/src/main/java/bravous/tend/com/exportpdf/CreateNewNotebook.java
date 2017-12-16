package bravous.tend.com.exportpdf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewNotebook extends BaseActivity {

    EditText noteNameView;
    Button createNoteBtn;
    NotebookType notebookType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_notebook);

        noteNameView = (EditText) findViewById(R.id.noteName);
        createNoteBtn = (Button) findViewById(R.id.createNote);

        //intent.putExtra()로 넘겨받은 값이 "FLOWER"(NotebookType의  상수값)라면...
        final String type_name = "FLOWER"; //

        //db에 타입값 말고 바로 이미지리소스id를 넣는 걸로 바꾸려고 했는데 id값이 고정적이지 않다고 하네요...!
        //그냥 타입 상수값을 db에 저장하는 것으로 바꿨습니다.
        //표지 이미지리소스id(R.drawable.id 값)은 이렇게 불러올 수 있어요.
        //notebookType = NotebookType.valueOf(type_name->인텐트로 넘겨받은 값);
        //int note_cover_Id = notebookType.getCoverPath();
        //근데 intent값은 이미지리소스id로 해도 될 것 같아요.(ExportSelectActivity -> ExportPdfProcess에서 그렇게 했는데 잘 되네요.
        //db에서는 id값을 저장하면 다 0이 되더라구요....
        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = noteNameView.getText().toString();
                createNewNote(newName, type_name, false, null);

                finish();

            }
        });

    }


    private void createNewNote(String noteName, String notebook_type, boolean pdfExist, String pdfPath){

        Notebook notebook = new Notebook(noteName, notebook_type, pdfExist, pdfPath);
        MyDBHelper helper = new MyDBHelper(this);

        notebook.setNotebook_name(noteName);
        notebook.setNotebook_type(notebook_type);
        notebook.setExist_pdf(pdfExist);
        notebook.setPdfPath(pdfPath);

        helper.createNotebook(notebook);
    }

}
