package bravous.tend.com.exportpdf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ExportSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_select);


        int image[] = {};

        NotebookViewAdapter adapter = new NotebookViewAdapter(getApplicationContext(),image);

        GridView gridView = (GridView)findViewById(R.id.exportSelectView);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();

                startActivity(intent);
            }
        });
    }


    class NotebookViewAdapter extends BaseAdapter{

        Context context;
        int image[];

        public NotebookViewAdapter(Context context, int image[]){
            this.context = context;
            this.image = image;
        }


        @Override
        public int getCount() {
            return image.length;
        }

        @Override
        public Object getItem(int position) {
            return image[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view == null){
                view = LayoutInflater.from(ExportSelectActivity.this)
                        .inflate(R.layout.notebook_item, viewGroup, false);
              //  ImageView imageView = (ImageView)view.findViewById(R.id.notbook);
               // imageView.setImageResource(image[position]);
            }
            return view;
        }
    }
}
