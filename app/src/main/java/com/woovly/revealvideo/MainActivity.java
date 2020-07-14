package com.woovly.revealvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.drawable.RotateDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    PlayerView playerView;
    SensorManager mySensorManager;
    boolean sersorrunning;
    float prev=0;
    float oldDegrees;
    Display display;
    boolean canRotate;
    RotateAnimation rotateAnimation;
    WindowManager windowManager;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        playerView = findViewById(R.id.video_view);
        windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
        canRotate=true;
        display=windowManager.getDefaultDisplay();
        MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        oldDegrees=360;
        //playerView.getLayoutParams().height=height;
        //playerView.getLayoutParams().width=width;
        double x = Math.pow(height , 2);
        double y = Math.pow(width , 2);
        double screenInches = Math.sqrt(x + y);
        playerView.getLayoutParams().height=(int)screenInches;
        playerView.requestLayout();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        /*playerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                playerView.getVideoSurfaceView().setLeft(left);
                playerView.getVideoSurfaceView().setRight(right);
                playerView.getVideoSurfaceView().setTop(top);
                playerView.getVideoSurfaceView().setBottom(bottom);
                //playerView.getOverlayFrameLayout().setRotation();
            }
        });*/

        final OrientationEventListener orientationEventListener=new OrientationEventListener(this,0) {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onOrientationChanged( int orientation) {


                if(canRotate && (Math.abs(orientation-oldDegrees)==10)){
                    Toast.makeText(getApplicationContext(),""+(orientation),Toast.LENGTH_SHORT).show();

                    //playerView.getVideoSurfaceView().animate().setInterpolator(new ReverseInterpolator());
                    //RotateAnimation rotate = new RotateAnimation(-oldDegrees, -oldDegrees+20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    //rotate.setDuration(5000);

                    /*RotateAnimation rotateAnimation = new RotateAnimation(-(oldDegrees),-(orientation),
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                    rotateAnimation.setDuration(0);
                    rotateAnimation.setRepeatCount(0);
                    rotateAnimation.setFillAfter(true);*/
                    //playerView.getVideoSurfaceView().startAnimation(rotateAnimation);
                    //playerView.getVideoSurfaceView().animate().rotationBy(-(orientation-oldDegrees)).setDuration(10).setInterpolator(new LinearInterpolator()).start();

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(playerView.getVideoSurfaceView() ,
                            "rotation", -oldDegrees, -orientation);
                    objectAnimator.setDuration(100);
                    objectAnimator.start();
                    oldDegrees=orientation;
                    //playerView.getVideoSurfaceView().animate().rotationBy((orientation-oldDegrees)).setDuration(100).start();
                    /*if(-orientation>-oldDegrees){
                        playerView.getVideoSurfaceView().animate().rotation(360).setInterpolator(new ReverseInterpolator());
                    }*/
                   // RotateAnimation rotateAnimation= (RotateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                    //ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(playerView.getVideoSurfaceView(),"rotation",-oldDegrees,-orientation);
                    //objectAnimator.setInterpolator(new LinearInterpolator());
                    //objectAnimator.start();
                    //playerView.setAnimation();
                    //playerView.getVideoSurfaceView().setRotation(-orientation);

                    //playerView.getVideoSurfaceView().animate().setInterpolator(new LinearInterpolator());
                    //float value= (oldDegrees-orientation);
                    //Toast.makeText(getApplicationContext(),""+value,Toast.LENGTH_SHORT).show();
                    //playerView.getVideoSurfaceView().animate().rotation(20).start();
                    //oldDegrees = orientation;
                    //ViewPropertyAnimator rotateAnimation=playerView.getVideoSurfaceView().animate().rotation(360);
                    //playerView.getVideoSurfaceView().startAnimation(rotate);

                    //ObjectAnimator.ofFloat(playerView.getVideoSurfaceView(),View.ROTATION,-oldDegrees,-orientation).setDuration(300).start();
                    /*if(-orientation>-oldDegrees){
                        for(float i=-oldDegrees+1;i<=-orientation;i=i+3){
                            playerView.getAnimation().set
                        }
                    }else{
                        for(float i=-oldDegrees-1;i>=-orientation;i=i-3){
                            playerView.getVideoSurfaceView().setRotation(i);
                        }
                    }*/

                    //playerView.getVideoSurfaceView().setRotation(-orientation);

                }else if(canRotate && Math.abs(orientation-oldDegrees)>10){
                    //playerView.getVideoSurfaceView().animate().rotationBy()
                    //problem around 0 degree point
                    int temp=orientation;
                    /*if(orientation==0){
                        orientation=360;
                    }else if(oldDegrees==0){
                        oldDegrees=360;
                    }
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(playerView.getVideoSurfaceView() ,
                            "rotation", -oldDegrees, -orientation);
                    objectAnimator.setDuration(100);
                    objectAnimator.start();*/
                    oldDegrees=temp;
                }

            }
        };
        orientationEventListener.enable();



        initializePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    protected void onStop() {
        player.setPlayWhenReady(false);
        super.onStop();
    }

    SimpleExoPlayer player;
    public void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        String videoPath = RawResourceDataSource.buildRawResourceUri(R.raw.sample_video).toString();

        Uri uri = RawResourceDataSource.buildRawResourceUri(R.raw.sample_video);

        ExtractorMediaSource audioSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(this, "MyExoplayer"),
                new DefaultExtractorsFactory(),
                null,
                null
        );

        player.prepare(audioSource);
        playerView.setPlayer(player);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        //player.setVideoScalingMode(C.Vi);
        //player.setPlayWhenReady(true);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    canRotate=true;
                    // media actually playing
                } else if (playWhenReady) {
                    canRotate=true;
                    // might be idle (plays after prepare()),
                    // buffering (plays when data available)
                    // or ended (plays when seek away from end)
                } else {
                    // player paused in any state
                    canRotate=false;
                }
            }
        });
        //SurfaceView surfaceView=new SurfaceView();


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       // playerView.getVideoSurfaceView().set(getWindowManager().getDefaultDisplay().getRotation());
        //playerView.setPlayer(player);
       // Toast.makeText(MainActivity.this,"config changed!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat -1f);
        }
    }
   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //unhide your objects here.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=600;
            playerView.setLayoutParams(params);
        }
    }*/
}
