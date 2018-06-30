package com.assistx.hg.sp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

public class About extends AppCompatActivity {


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bahar);
        WebView view = findViewById(R.id.textContent);
        String text;
        text = "<html><body><p align=\"justify\">";
        text+= "<i>Spined</i> is an application aimed to help people improve their Neck and Back posture. " +
                "Smartphones and devices have deteriorated our posture. Presently, we use smartphone more than 2 hours a day." +
                " This application aims at improving the back posture by sending ALERTS to the user and monitoring the activity" +
                "After various studies conducted by researchers, this application uses a new algorithm and mechanism to find out the posture" +
                " By employing the accelerometer data, and processing, filtering it through various functions, the application decides" +
                "whether the posture is acceptable or not!" +
                " On a sample size of 100, the study found commendable results and improvement." +
                " Spined has been covered in the media. </p> <p>" +
                "\n The Research paper that has explained the application theoretically can be found " +
                "<a href=\"https://www.scirp.org/journal/PaperInformation.aspx?PaperID=82471\">HERE</a>" +
                "<centre> <p> <a style =\"text-decoration:none;\" href=\"http://www.harshgupta.in/\"><b> Harsh Gupta </b> </a> </p></centre>";


        text+= "</p></body></html>";
        view.loadData(text, "text/html", "utf-8");

    }

}
