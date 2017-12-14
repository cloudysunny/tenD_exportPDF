package bravous.tend.com.exportpdf;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExportPdfProcess extends BaseActivity {

    public Bitmap bmp;
    NotebookType notebookType;
    String note_name;
    int note_type;
    String final_fileName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_export_pdf_process);

            //노트 선택 페이지에서 받아온 intent값
            Intent intent = getIntent();
            note_name = intent.getStringExtra("NOTEBOOK_NAME");
            note_type = intent.getIntExtra("NOTEBOOK_TYPE", 1);
            //Log.i("intent.getStringExtra()", note_name);
            final EditText filenameEdit = (EditText)findViewById(R.id.filename_blank);
            Button pdfBtn = (Button)findViewById(R.id.PDFexport);

            final int coverPath = getNotebookCoverPath(note_type);

        final String file_name = note_name + ".pdf";
        filenameEdit.setText(file_name);

        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //
                    final_fileName = filenameEdit.getText().toString();

                    createPDF(final_fileName, coverPath);
                    updatePdfInfo(note_name, final_fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }


    //배경이미지를 넣기 위한 페이지이벤트헬퍼 클래스
    public class Background extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document){

            PdfContentByte canvas = writer.getDirectContentUnder();
            Image image = null;
            try {
                image = getImage(R.drawable.paper2, "JPG");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadElementException e) {
                e.printStackTrace();
            }
            image.scaleAbsolute(PageSize.B5);
            image.setAbsolutePosition(0, 0);
            try {
                canvas.addImage(image);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
    }


    //pdf파일 생성 메소드
    public void createPDF(String filename, int coverPath) throws Exception{

        //저장공간 상태 확인
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(getApplicationContext(), "저장공간이 부족합니다.", Toast.LENGTH_SHORT).show();
        }
        //핸드폰 외부저장소의 tenD폴더 정보를 불러옴(폴더가 없을 경우 새로 생성)
        File pdfDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/tenD");
        if(!pdfDir.exists()){
            pdfDir.mkdirs();
        }

        //PDF문서 생성(용지 사이즈, 여백 지정)
        Document document = new Document(PageSize.B5, 50, 50, 50, 50);

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfDir+"/"+filename));
            //페이지이벤트 (마지막페이지까지 생성 후 배경이미지 넣기)
            Background event = new Background();
            pdfWriter.setPageEvent(event);

            //문서 작성 시작
            document.open();

            //한글 텍스트 입력시  오류 현상 해결 위해 한글 폰트 설정
            BaseFont baseFont = null;

            InputStream is = getAssets().open("jejuMyeongjo.ttf");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            baseFont = BaseFont.createFont("jejuMyeongjo.ttf",
                    BaseFont.IDENTITY_H,
                    true,
                    false, buffer,
                    null);

            Font font = new Font(baseFont, 16);

            //공책 표지
            Image coverImg = getImage(coverPath, "JPG");
            coverImg.scaleAbsolute(PageSize.B5);
            coverImg.setAbsolutePosition(0, 0);
            document.add(coverImg);
            document.newPage();

           //일기 데이터 가져오기
            ArrayList<Diary> diaryList = getAllDiary(note_name);
            Iterator <Diary>iterator = diaryList.iterator();


            while (iterator.hasNext()) {
                Diary diary = iterator.next();
                Log.i("getAllDiary() check", diary.getDate());
                //날짜, 감정으로 이루어진 테이블(두 요소를 나란히 놓기 위해서)
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                PdfPCell cell1 = new PdfPCell(new Paragraph(diary.getDate(), font));
                cell1.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell1);
                //PdfPCell cell2 = new PdfPCell();
                //cell2.setBorder(Rectangle.NO_BORDER);
                //table.addCell(cell2);
                PdfPCell cell3 = new PdfPCell(new Paragraph(diary.getEmotion(), font));
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setPaddingLeft(16f);
                cell3.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell3);
                table.setSpacingAfter(16f);

                //한 줄 띄우기
                document.add(table);
                Paragraph space = new Paragraph(" ");
                document.add(space);

                //일기그림
                Image todayPic = Image.getInstance(diary.getImgPath());
                todayPic.setAlignment(Image.MIDDLE);
                document.add(todayPic);

                //일기내용
                document.add(space);
                Reader reader = new FileReader(diary.getTextPath());
                BufferedReader br = new BufferedReader(reader);
                String text="";
                String line;
                while ((line=br.readLine())!=null) {
                   text+= line+"\n";
                    Log.i("readLine() test", text);
                }
                br.close();
                reader.close();

                 Paragraph diarytext = new Paragraph(text, font);
                    diarytext.setAlignment(Paragraph.ALIGN_CENTER);
                    diarytext.setSpacingBefore(32f);
                    document.add(diarytext);

                //다음 페이지로
                document.newPage();
            }
                document.newPage();
            //뒷표지
           // Image backCoverImg = getImage(R.drawable.cover3, "JPG");
           // coverImg.scaleAbsolute(PageSize.B5);
           // coverImg.setAbsolutePosition(0, 0);
            //이미지를 처음에 미리 불러두는 게 낫겠다
            document.add(coverImg);
            document.newPage();


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //문서작성 완료
            document.close();
            Toast.makeText(getApplicationContext(),"pdf파일이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }



    //이미지 가져오기(resource/drawable/에 저장된 이미지일 경우)
    public Image getImage(int resId, String extension /*대문자로*/) throws IOException, BadElementException {
        bmp = BitmapFactory.decodeResource(getResources(),resId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(extension.equals("JPG")) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }else if(extension.equals("PNG")){
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        return Image.getInstance(stream.toByteArray());
    }






    //작업완료 후 비트맵이 계속 메모리를 차지하고 있지 않도록 처리
    @Override
    protected void onStop() {
        super.onStop();
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }

    }

    private void updatePdfInfo(String note_name, String pdfname){

        String pdfPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tenD/"+pdfname;
        MyDBHelper myDBHelper = new MyDBHelper(this);
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor before = db.rawQuery("SELECT " + myDBHelper.KEY_EXIST_PDF + "," +myDBHelper.KEY_PDF_PATH
                +" FROM " + myDBHelper.TABLE_NOTEBOOK
                + " WHERE " + myDBHelper.KEY_NOTEBOOK_NAME + " = '" + note_name +"'", null);

        if(before.getCount() != 0) {
            before.moveToFirst();
            Log.i("DB update check", before.getInt(0) + ", " + before.getString(1));
        }

       db.execSQL("UPDATE " + myDBHelper.TABLE_NOTEBOOK
               + " SET " + myDBHelper.KEY_EXIST_PDF + " = 1, "
               + myDBHelper.KEY_PDF_PATH + " = '"+ pdfPath
       + "' WHERE "+ myDBHelper.KEY_NOTEBOOK_NAME + " = '" + note_name +"'");


        Cursor after = db.rawQuery("SELECT " + myDBHelper.KEY_EXIST_PDF + "," +myDBHelper.KEY_PDF_PATH
                +" FROM " + myDBHelper.TABLE_NOTEBOOK
                + " WHERE " + myDBHelper.KEY_NOTEBOOK_NAME + " = '" + note_name +"'", null);

        if(after.getCount() != 0) {
            after.moveToFirst();
            Log.i("DB update check", after.getInt(0) + ", " + after.getString(1));
        }

        db.close();
    }
}






