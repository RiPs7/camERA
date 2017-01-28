package perimara.era.activities;

import android.graphics.Point;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.video.VideoQuality;

import perimara.era.R;
import perimara.era.utils.AuxiliaryUtils;

public class VideoChatActivity extends AppCompatActivity implements Session.Callback, SurfaceHolder.Callback{

    private final String TAG = "VideoChat";

    double mLatitude, mLongitude, mRatio;
    int mCameraId;
    String mGender, mAndroidId, mDestination;

    boolean mutedAudio = false;
    int prev_volume = 1;

    SurfaceView mSurfaceView;
    Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        //region Collect User Information
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mLatitude = extras.getDouble("latitude");
            mLongitude = extras.getDouble("longitude");
            mGender = extras.getString("gender");
            mAndroidId = extras.getString("android-id");
            mRatio = extras.getDouble("camera-ratio");
            mCameraId = extras.getInt("camera-id");
            mDestination = extras.getString("destination");
        }
        //endregion

        //region Next Partner Button
        findViewById(R.id.next_partner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //endregion

        //region Change Camera Button
        findViewById(R.id.change_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //endregion

        //region Mute Button
        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mutedAudio) {
                    AudioManager audio = (AudioManager) VideoChatActivity.this.getSystemService(getApplicationContext().AUDIO_SERVICE);
                    prev_volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    ((ImageButton) v).setImageResource(R.drawable.ic_mute);
                    mutedAudio = true;
                } else {
                    AudioManager audio = (AudioManager) VideoChatActivity.this.getSystemService(getApplicationContext().AUDIO_SERVICE);
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, prev_volume, 0);
                    ((ImageButton) v).setImageResource(R.drawable.ic_unmute);
                    mutedAudio = false;
                }
            }
        });
        //endregion

        //region Set My Camera
        mSurfaceView = (SurfaceView)findViewById(R.id.mSurfaceView);
        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(outSize);
        LinearLayout.LayoutParams mSurfaceViewParams = new LinearLayout.LayoutParams(outSize.x / 3, (int)((outSize.x / 3) * mRatio));
        mSurfaceView.setLayoutParams(mSurfaceViewParams);
        mSurfaceView.getHolder().addCallback(this);
        InitializeSDP();
        //endregion

    }

    private void InitializeSDP(){
        mSession = SessionBuilder.getInstance()
                .setCallback(this)
                .setSurfaceView(mSurfaceView)
                .setPreviewOrientation(90)
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_NONE)
                .setAudioQuality(new AudioQuality(16000, 32000))
                .setVideoEncoder(SessionBuilder.VIDEO_H264)
                .setVideoQuality(new VideoQuality(720, 480, 60, 500000))
                .setCamera(mCameraId)
                .setDestination(mDestination)
                .build();
        HandleStream(true);
        mSurfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);

    }

    private void HandleStream(boolean open){
        if (open) {
            if (!mSession.isStreaming()){
                mSession.configure();
                mSession.startPreview();
                mSession.start();
            }
        } else {
            mSession.stopPreview();
            mSession.release();
            mSurfaceView.getHolder().removeCallback(this);
        }
    }

    //region Extra Override Methods
    @Override
    public void onBackPressed() {
        HandleStream(false);
        super.onBackPressed();
    }

    // -- Session Callback
    @Override
    public void onBitrateUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {
        switch (reason){
            case Session.ERROR_CAMERA_ALREADY_IN_USE:
                Log.e("ERROR", "Camera Alread In Use");
                break;
            case Session.ERROR_CAMERA_HAS_NO_FLASH:
                Log.e("ERROR", "Camera Has No Flash");
                break;
            case Session.ERROR_CONFIGURATION_NOT_SUPPORTED:
                Log.e("ERROR", "Configuration Not Supported");
                break;
            case Session.ERROR_INVALID_SURFACE:
                Log.e("ERROR", "Invalid Surface");
                break;
            case Session.ERROR_OTHER:
                Log.e("ERROR", "Other");
                break;
            case Session.ERROR_STORAGE_NOT_READY:
                Log.e("ERROR", "Storage Not Ready");
                break;
            case Session.ERROR_UNKNOWN_HOST:
                Log.e("ERROR", "Unknown Host");
                break;
        }
        if (e != null){
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void onSessionConfigured() {

    }

    @Override
    public void onSessionStarted() {

    }

    @Override
    public void onSessionStopped() {

    }

    //-- SurfaceHolder Callback Overrides --
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    //endregion
}