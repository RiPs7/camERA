package perimara.era.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import perimara.era.interfaces.CustomLocationListener;
import perimara.era.R;
import perimara.era.network.SearchPartnerTask;
import perimara.era.utils.Animations;
import perimara.era.utils.AuxiliaryUtils;
import perimara.era.utils.LocationUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PreviewCameraActivity extends AppCompatActivity {

    //region location variables
    Location mLocation;
    double mLatitude, mLongitude;
    String mGender;
    String mAndroidId;
    Criteria mCriteria;
    Looper looper;
    LocationListener mLocationListener;
    String mCity;
    //endregion

    //region camera variables
    Camera mCamera;
    SurfaceTexture mCameraSurface;
    int mCameraId = 1;
    double mRatio;
    TextureView mTextureView;
    TextureView.SurfaceTextureListener mSurfaceTextListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mCameraSurface = surface;
            OpenCamera(mCameraSurface);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    int orgPreviewWidth, orgPreviewHeight;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_camera);

        //region Check for guide
        //if guide hasn't been shown before
        /*if (!PreferenceManager.getDefaultSharedPreferences(PreviewCameraActivity.this).getBoolean("show_guide", false)){
            startActivity(new Intent(PreviewCameraActivity.this, GuideActivity.class));
            finish();
        }

        if (getIntent().getExtras() == null){
            startActivity(new Intent(PreviewCameraActivity.this, GuideActivity.class));
            finish();
        }*/
        //endregion

        //region Load Shared Preferences
        mGender = PreferenceManager.getDefaultSharedPreferences(PreviewCameraActivity.this).getString("gender", "");
        //endregion

        //region Location Manager - Listener
        mLocationListener = new CustomLocationListener();

        // Now first make a criteria with your requirements
        // this is done to save the battery life of the device
        // there are various other criteria...
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        mCriteria.setPowerRequirement(Criteria.POWER_LOW);
        mCriteria.setAltitudeRequired(false);
        mCriteria.setBearingRequired(false);
        mCriteria.setSpeedRequired(false);
        mCriteria.setCostAllowed(true);
        mCriteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        mCriteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        // This is the Best And IMPORTANT part
        looper = null;
        //endregion

        //region Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 232, 144, 28)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Single update on fab click
                try {
                    mAndroidId = AuxiliaryUtils.sha1(Settings.Secure.getString(PreviewCameraActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                    PreviewCameraActivity.this.mLocation = LocationUtils.getLocation(getApplicationContext(), mLocationListener);
                    PreviewCameraActivity.this.mLatitude = PreviewCameraActivity.this.mLocation.getLatitude();
                    PreviewCameraActivity.this.mLongitude = PreviewCameraActivity.this.mLocation.getLongitude();
                    mCity = LocationUtils.GetCity(getApplicationContext(), mLocation);
                    new SearchPartnerTask(mLatitude, mLongitude, mGender, mAndroidId) {
                        ProgressDialog progressDialog = new ProgressDialog(PreviewCameraActivity.this);

                        @Override
                        protected void onPreExecute() {
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Setting up chat room...");
                            progressDialog.show();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            progressDialog.dismiss();
                            StopCamera();
                            startActivity(new Intent(PreviewCameraActivity.this, VideoChatActivity.class)
                                            .putExtra("information", "Lat: " + PreviewCameraActivity.this.mLatitude + ", Lon: " + PreviewCameraActivity.this.mLongitude + "\nYou are in: " + mCity)
                                            .putExtra("camera-ratio", mRatio)
                                            .putExtra("camera-id", mCameraId)
                                            .putExtra("latitude", PreviewCameraActivity.this.mLatitude)
                                            .putExtra("longitude", PreviewCameraActivity.this.mLongitude)
                                            .putExtra("gender", mGender)
                                            .putExtra("android-id", mAndroidId)
                                            .putExtra("destination", s)
                            );
                        }
                    }.execute();
                } catch (SecurityException se) {
                    Toast.makeText(PreviewCameraActivity.this, "An error occurred\n" + se.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(PreviewCameraActivity.this, "Could not get GPS service", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region Camera
        mTextureView = (TextureView) findViewById(R.id.fullscreen_content);

        findViewById(R.id.SwitchCameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera.getNumberOfCameras() > 1) {
                    mCameraId = mCameraId == 0 ? 1 : 0;
                    OpenCamera(mCameraSurface);
                }
            }
        });
        //endregion

        //region Popup Settings
        findViewById(R.id.SettingsDotsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PreviewCameraActivity.this, SettingsActivity.class), 1);
            }
        });
        //endregion

        //region Animate Logo Button
        findViewById(R.id.componentsLayout).setVisibility(View.GONE);
        final ImageButton logoButton = new ImageButton(PreviewCameraActivity.this);
        logoButton.setBackgroundResource(R.drawable.camera_logo);
        if (getIntent().getExtras() == null) {
            ((RelativeLayout) findViewById(R.id.outerLayout)).addView(logoButton);
            RelativeLayout.LayoutParams logoButtonParams = new RelativeLayout.LayoutParams(AuxiliaryUtils.px(getApplicationContext(), 85), AuxiliaryUtils.px(getApplicationContext(), 60));
            logoButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            logoButton.setLayoutParams(logoButtonParams);
            final ViewTreeObserver vto = logoButton.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    logoButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    //animation 1
                    ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                            logoButton,
                            PropertyValuesHolder.ofFloat("scaleX", 1.6f),
                            PropertyValuesHolder.ofFloat("scaleY", 1.6f));
                    scaleDown.setDuration(500);
                    scaleDown.setRepeatCount(3);
                    scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
                    scaleDown.start();
                    scaleDown.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            DisplayMetrics dm = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            RelativeLayout.LayoutParams new_logoButtonParams = new RelativeLayout.LayoutParams(AuxiliaryUtils.px(getApplicationContext(), 85), AuxiliaryUtils.px(getApplicationContext(), 60));
                            new_logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            new_logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            logoButton.setLayoutParams(new_logoButtonParams);
                            //animation 2
                            TranslateAnimation anim2 = new TranslateAnimation(
                                    logoButton.getLeft(),
                                    AuxiliaryUtils.px(getApplicationContext(), 10),
                                    logoButton.getTop(),
                                    AuxiliaryUtils.px(getApplicationContext(), 10));
                            anim2.setDuration(1000);
                            anim2.setFillAfter(true);
                            anim2.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    findViewById(R.id.componentsLayout).setVisibility(View.VISIBLE);
                                    Animations.FadeIn(findViewById(R.id.componentsLayout), 100);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                            logoButton.startAnimation(anim2);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
            });
        } else {
            ((RelativeLayout) findViewById(R.id.outerLayout)).addView(logoButton);
            RelativeLayout.LayoutParams logoButtonParams = new RelativeLayout.LayoutParams(AuxiliaryUtils.px(getApplicationContext(), 85), AuxiliaryUtils.px(getApplicationContext(), 60));
            logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            logoButtonParams.setMargins(AuxiliaryUtils.px(getApplicationContext(), 10), AuxiliaryUtils.px(getApplicationContext(), 10), 0, 0);
            logoButton.setLayoutParams(logoButtonParams);
            findViewById(R.id.componentsLayout).setVisibility(View.VISIBLE);
            Animations.FadeIn(findViewById(R.id.componentsLayout), 100);
        }
        //endregion
    }

    //region Overrides
    @Override
    public void onResume(){
        super.onResume();
        if (mTextureView.isAvailable()){

        } else{
            mTextureView.setSurfaceTextureListener(mSurfaceTextListener);
        }
    }

    @Override
    public void onDestroy(){
        StopCamera();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    //endregion

    //region Camera Methods
    private void OpenCamera(SurfaceTexture surface){
        StopCamera();
        mCamera = Camera.open(mCameraId);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        Point screen_size = new Point();
        getWindowManager().getDefaultDisplay().getSize(screen_size);

        Camera.Size optimalSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), screen_size.x, screen_size.y);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
        orgPreviewWidth = optimalSize.width;
        orgPreviewHeight = optimalSize.height;

        mRatio = (double)orgPreviewWidth / orgPreviewHeight;
        RelativeLayout.LayoutParams mTextureViewParams = new RelativeLayout.LayoutParams((int)Math.ceil(orgPreviewHeight * (mRatio)), (int)Math.ceil(orgPreviewWidth * (mRatio)));
        mTextureViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mTextureViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTextureView.setLayoutParams(mTextureViewParams);

        mCamera.setParameters(parameters);
        setCameraDisplayOrientation(PreviewCameraActivity.this, mCameraId, mCamera);
        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException t) {
        }
        mCamera.startPreview();
    }

    private void StopCamera(){
        if (mCamera != null){
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            } catch(Exception e) {
                e.getMessage();
            }
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);

        Camera.Parameters params = camera.getParameters();
        params.setRotation(result);
        camera.setParameters(params);
    }
    //endregion
}
