package com.assistx.hg.sp;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class firstTime extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new Step.Builder().setTitle("Welcome to Posture Improvement APP")
                .setContent("Run the app once, and then it will help you improve your posture by running in background")
                .setBackgroundColor(Color.parseColor("#1892a0")) // int background color
                .setDrawable(R.drawable.main) // int top drawable
//                .setSummary("")
                .build());


        addFragment(new Step.Builder().setTitle("Daily Data!")
                .setContent("Click on Today's overview to get daily posture data")
                .setBackgroundColor(Color.parseColor("#484848")) // int background color
                .setDrawable(R.drawable.screen_data) // int top drawable
//                .setSummary("")
                .build());

        addFragment(new Step.Builder().setTitle("Posture Calculation")
                .setContent("To calibrate, move your neck with the screen and see if the notifications are visible")
                .setBackgroundColor(Color.parseColor("#459ea4")) // int background color
                .setDrawable(R.drawable.screen_four) // int top drawable
//                .setSummary("")
                .build());


//        // Permission Step
//        addFragment(new PermissionStep.Builder().setTitle(getString(R.string.permission_title))
//                .setContent(getString(R.string.permission_detail))
//                .setBackgroundColor(Color.parseColor("#FF0957"))
//                .setDrawable(R.drawable.exit)
//                .setSummary(getString(R.string.continue_and_learn))
//                .setPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
//                .build());

    }


}
