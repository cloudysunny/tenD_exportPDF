package bravous.tend.com.exportpdf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class ViewDiaryActivity extends BaseActivity {


    ArrayList<Diary> diaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_diary);

        String current_notebook = getCurrentNotebook();
        diaryList = getAllDiary(current_notebook);

        ViewPager viewpager = (ViewPager)findViewById(R.id.viewPager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);


    }


    public class FragmentAdapter extends FragmentStatePagerAdapter{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DiaryFragment fragment = new DiaryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Date", diaryList.get(position).getDate());
            bundle.putString("Emotion", diaryList.get(position).getEmotion());
            bundle.putString("Comment", diaryList.get(position).getComment());
            bundle.putLong("CommentTime", diaryList.get(position).getCommentTime());
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return diaryList.size();
        }
    }
}
