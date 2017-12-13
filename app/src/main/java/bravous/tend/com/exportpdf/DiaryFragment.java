package bravous.tend.com.exportpdf;

import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DiaryFragment extends Fragment {

    final static int ALARM_REQUEST_CODE =100;
    PendingIntent pendingIntent;

    View rootView;
    MaterialSheetFab materialSheetFab;
    int statusBarColor;

    String comment;
    long commentTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_diary, container, false);
        Bundle bundle = getArguments();
        String date = bundle.getString("Date");
        String emotion=bundle.getString("Emotion");
        comment= bundle.getString("Comment");
        commentTime = bundle.getLong("CommentTime");

        TextView dateView = (TextView)rootView.findViewById(R.id.dateView);
        TextView commentView = (TextView)rootView.findViewById(R.id.commentView);
        dateView.setText(date);
        commentView.setText(comment);
        //FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);


        setupFab();


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CardView commentSheet = (CardView)rootView.findViewById(R.id.commentSheet);
//                // get the center for the clipping circle
//                int cx = (commentSheet.getLeft() + commentSheet.getRight()) / 2;
//                int cy = (commentSheet.getTop() + commentSheet.getBottom()) / 2;
//
//                // get the final radius for the clipping circle
//                int dx = Math.max(cx, commentSheet.getWidth() - cx);
//                int dy = Math.max(cy, commentSheet.getHeight() - cy);
//                float finalRadius = (float) Math.hypot(dx, dy);
//
//                commentSheet.setVisibility(View.VISIBLE);
//                // Android native animator
//                Animator animator =
//                        ViewAnimationUtils.createCircularReveal(commentSheet, cx, cy, 0, finalRadius);
//                animator.setInterpolator(new AccelerateDecelerateInterpolator());
//                animator.setDuration(3000);
//                animator.start();
//
//            }
//        });

        return rootView;
    }


    private void setupFab() {

        Fab fab = (Fab) rootView.findViewById(R.id.fab);
        View sheetView = rootView.findViewById(R.id.commentSheet);
        View overlay = rootView.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.colorPrimaryDark);
        int fabColor = getResources().getColor(R.color.colorAccent);

        //db에서 받아온 comment time과 같거나 큰 시간일 때 플로팅버튼이 보이도록 설정
        fab.setVisibility(View.INVISIBLE);
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTimeInMillis(System.currentTimeMillis());
        long now = cal.getTimeInMillis();


        if(now == commentTime){
            fab.setVisibility(View.VISIBLE);
        }else if(now > commentTime){
            fab.setVisibility(View.VISIBLE);
        }

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners

    }

    private void updateFab(int selectedPage) {
                materialSheetFab.showFab();
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }


}
