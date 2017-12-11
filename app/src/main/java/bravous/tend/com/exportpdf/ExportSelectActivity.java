package bravous.tend.com.exportpdf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExportSelectActivity extends BaseActivity {


    private ArrayList<Notebook> notebookArrayList;
    private NotebookViewAdapter adapter;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Notebook selectedNotebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_select);

        notebookArrayList = getAllNotebook();
        //노트가 한 권도 생성되지 않았을 때의 뷰도 따로 마련해야 해
        //처음 시작할 때 노트를 바로 만들도록 유도할 거지만 그래도 혹시 모르니까
        Log.i("NotebookList count", Integer.toString(notebookArrayList.size()));
        layoutManager = new GridLayoutManager(ExportSelectActivity.this, 2);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotebookViewAdapter(this, notebookArrayList);
        recyclerView.setAdapter(adapter);

    }


    public class NotebookViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private Context context;
        private ArrayList<Notebook> notebookList;

        public NotebookViewAdapter(Context context, ArrayList<Notebook> notebookList){
            this.context = context;
            this.notebookList = notebookList;
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(ExportSelectActivity.this).inflate(R.layout.notebook_item, parent, false);
            return new NotebookViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            selectedNotebook = notebookList.get(position);
            NotebookViewHolder viewHolder = (NotebookViewHolder)holder;
            Log.i("getNotebook_type() ", Integer.toString(selectedNotebook.getNotebook_type()));
            Log.i("getNotebookCoverPath()", Integer.toString(getNotebookCoverPath(1)));
            viewHolder.imageView.setImageResource(getNotebookCoverPath(selectedNotebook.getNotebook_type()));
            viewHolder.titleView.setText(selectedNotebook.getNotebook_name());

            if(selectedNotebook.getExist_pdf()==true){
                viewHolder.isPdfView.setText("내보내기 완료");
            }else{
                viewHolder.isPdfView.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return notebookList.size();
        }
    }



    public class NotebookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView titleView;
        TextView isPdfView;
        Notebook notebook;

        public NotebookViewHolder(View itemView) {
            super(itemView);

            imageView =(ImageView)itemView.findViewById(R.id.notebook_cover);
            titleView = (TextView)itemView.findViewById(R.id.notebook_name);
            isPdfView = (TextView)itemView.findViewById(R.id.exist_pdf);

            notebook = new Notebook();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ExportPdfProcess.class);
            intent.putExtra("NOTEBOOK_NAME", selectedNotebook.getNotebook_name());
            intent.putExtra("NOTEBOOK_TYPE", selectedNotebook.getNotebook_type());
            startActivity(intent);

        }
    }


}
