package com.woovly.revealvideo;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    float oldDegress;

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView=(TextView)findViewById(R.id.textView4);
        oldDegress=0;
    }
    public void rotateClockwise(View view){
        RotateAnimation rotateAnimation = new RotateAnimation(textView.getRotation(), textView.getRotation()+20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(100);
        rotateAnimation.setFillAfter(true);
        //textView.setAnimation(rotateAnimation);
        textView.startAnimation(rotateAnimation);
        oldDegress=oldDegress+20;
    }
    public void rotateAntiClockwise(View view){
        RotateAnimation rotateAnimation = new RotateAnimation(textView.getRotation(), textView.getRotation()-20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(100);
        rotateAnimation.setFillAfter(true);
        textView.startAnimation(rotateAnimation);
        oldDegress=oldDegress-20;
    }
}
