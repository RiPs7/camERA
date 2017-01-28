package perimara.era.guide;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import perimara.era.utils.Animations;
import perimara.era.activities.PreviewCameraActivity;
import perimara.era.R;

public class GuidePage extends Fragment {

    int page_num;
    int page2_step = 0;

    static GuidePage newInstance(int num) {
        GuidePage f = new GuidePage();
        Bundle args = new Bundle();
        args.putInt("page_num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page_num = getArguments() != null ? getArguments().getInt("page_num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.guide_fragment, container, false);
        switch(page_num){
            case 0:
                LoadFirstPage(v);
                break;
            case 1:
                LoadSecondPage(v);
                break;
            case 2:
                LoadThirdPage(v);
                break;
            case 3:
                LoadFourthPage(v);
                break;
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void LoadFirstPage(final View v) {
        final Context c = v.getContext();

        final RelativeLayout rl = new RelativeLayout(c);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        rl.setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(rl);

        final ImageButton logoButton = new ImageButton(c);
        logoButton.setBackgroundResource(R.drawable.camera_logo);
        RelativeLayout.LayoutParams logoButtonParams = new RelativeLayout.LayoutParams(px(85), px(60));
        logoButtonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        logoButton.setLayoutParams(logoButtonParams);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(logoButton);
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
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                        RelativeLayout.LayoutParams new_logoButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        new_logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        new_logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        logoButton.setLayoutParams(new_logoButtonParams);
                        //animation 2
                        TranslateAnimation anim2 = new TranslateAnimation(
                                logoButton.getLeft(),
                                px(10),
                                logoButton.getTop(),
                                px(10));
                        anim2.setDuration(1000);
                        anim2.setFillAfter(true);
                        anim2.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                rl.setVisibility(View.VISIBLE);
                                v.findViewById(R.id.dots).setVisibility(View.VISIBLE);
                                Animations.FadeIn(v.findViewById(R.id.dots), 100);
                                Animations.FadeIn(rl, 100);
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

        TextView tv = new TextView(c);
        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tvParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tvParams.setMargins(0, px(100), 0, 0);
        tv.setLayoutParams(tvParams);
        tv.setTextSize(30);
        tv.setText("Welcome!\nThis is the beginning\nof your new\ncamERA!");
        tv.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rl.addView(tv);

        TextView tv2 = new TextView(c);
        RelativeLayout.LayoutParams tv2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tv2Params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv2Params.setMargins(0, 0, 0, px(130));
        tv2.setLayoutParams(tv2Params);
        tv2.setTextSize(30);
        tv2.setText("Press next to continue");
        tv2.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rl.addView(tv2);

        final Button nextBtn = new Button(c);
        nextBtn.setText("Next");
        RelativeLayout.LayoutParams nextBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nextBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nextBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nextBtn.setLayoutParams(nextBtnParams);
        nextBtn.setBackgroundResource(R.drawable.drawable_next_button);
        rl.addView(nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity) getActivity()).viewPager.setCurrentItem(1);
            }
        });

        v.findViewById(R.id.dots).setBackgroundResource(R.drawable.dots1);
        v.findViewById(R.id.dots).setVisibility(View.GONE);
    }

    private void LoadSecondPage(final View v){
        v.findViewById(R.id.dots).setBackgroundResource(R.drawable.dots2);

        final Context c = v.getContext();

        final ImageButton logoButton = new ImageButton(c);
        logoButton.setBackgroundResource(R.drawable.camera_logo);
        RelativeLayout.LayoutParams logoButtonParams = new RelativeLayout.LayoutParams(px(85), px(60));
        logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        logoButtonParams.setMargins(px(10), px(10), 0, 0);
        logoButton.setLayoutParams(logoButtonParams);
        ((RelativeLayout)v.findViewById(R.id.fragment_layout)).addView(logoButton);

        //Load starting components
        final TextView tv1 = new TextView(c);
        final RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv1Params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tv1Params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv1Params.setMargins(0, px(100), 0, 0);
        tv1.setLayoutParams(tv1Params);
        tv1.setTextSize(30);
        tv1.setText("Let's take a look at some functional buttons...");
        tv1.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(tv1);

        final TextView tv2 = new TextView(c);
        final RelativeLayout.LayoutParams tv2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tv2Params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv2Params.setMargins(0, 0, 0, px(130));
        tv2.setLayoutParams(tv2Params);
        tv2.setTextSize(30);
        tv2.setText("Press next to continue");
        tv2.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(tv2);

        //Load underneath components
        final ImageButton annoyBtn = new ImageButton(c);
        annoyBtn.setBackgroundResource(R.drawable.ic_annoy_btn);
        final RelativeLayout.LayoutParams annoyBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        annoyBtnParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        annoyBtnParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        annoyBtnParams.setMargins(px(40), px(60), 0, 0);
        annoyBtn.setLayoutParams(annoyBtnParams);
        annoyBtn.setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(annoyBtn);

        final ImageButton nextPersonBtn = new ImageButton(c);
        nextPersonBtn.setBackgroundResource(R.drawable.ic_next_partner);
        final RelativeLayout.LayoutParams nextPersonBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nextPersonBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nextPersonBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nextPersonBtnParams.setMargins(0, 0, px(30), px(200));
        nextPersonBtn.setLayoutParams(nextPersonBtnParams);
        nextPersonBtn.setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(nextPersonBtn);

        //Load semi-transparent layer
        final RelativeLayout semiTransparentLayout = new RelativeLayout(c);
        semiTransparentLayout.setBackgroundColor(Color.parseColor("#aa000000"));
        semiTransparentLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        semiTransparentLayout.setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(semiTransparentLayout);
        //Load bright circle
        final ImageView brightPart = new ImageView(c);
        final RelativeLayout.LayoutParams circleViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        circleViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        circleViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        circleViewParams.setMargins(0, 0, 0, 0);
        brightPart.setLayoutParams(circleViewParams);
        brightPart.setBackgroundResource(R.drawable.drawable_bright_circle);
        brightPart.setVisibility(View.GONE);
        semiTransparentLayout.addView(brightPart);
        //Load explanation message
        final TextView explanationMessage = new TextView(c);
        explanationMessage.setTextColor(Color.parseColor("#AA6939"));
        explanationMessage.setTextSize(20);
        explanationMessage.setTypeface(Typeface.createFromAsset(c.getAssets(), "Daniel-Black.otf"));
        final RelativeLayout.LayoutParams explanationMessageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        explanationMessageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        explanationMessageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        explanationMessageParams.setMargins(px(10), 0, px(10), 0);
        explanationMessage.setLayoutParams(explanationMessageParams);
        explanationMessage.setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(explanationMessage);

        //Load next button
        final Button nextStepBtn = new Button(c);
        nextStepBtn.setText("Next");
        nextStepBtn.setBackgroundResource(R.drawable.drawable_next_button);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                switch (page2_step) {
                    case 0:
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        logoButton.setVisibility(View.GONE);
                        semiTransparentLayout.setVisibility(View.VISIBLE);
                        nextStepBtn.setVisibility(View.VISIBLE);
                        explanationMessage.setVisibility(View.VISIBLE);
                        brightPart.setVisibility(View.VISIBLE);
                        annoyBtn.setVisibility(View.VISIBLE);
                        nextPersonBtn.setVisibility(View.VISIBLE);
                        //currently pointing at Annoy button
                        semiTransparentLayout.bringToFront();
                        nextStepBtn.bringToFront();
                        Animations.FadeOut(explanationMessage, 100);
                        explanationMessage.setText("This button should be used to report a person due to improper behavior during a video chat, which annoyed or disturbed you. Press next to continue.");
                        explanationMessage.bringToFront();
                        Animations.FadeIn(explanationMessage, 100);
                        brightPart.bringToFront();
                        annoyBtn.bringToFront();
                        break;
                    case 1:
                        //point at Next button
                        semiTransparentLayout.bringToFront();
                        nextStepBtn.bringToFront();
                        Animations.FadeOut(explanationMessage, 100);
                        explanationMessage.setText("This button is available while on video chat. It allows you to find another person to video chat with. Press next to continue.");
                        explanationMessage.bringToFront();
                        Animations.FadeIn(explanationMessage, 100);
                        circleViewParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                        circleViewParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        circleViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        circleViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        circleViewParams.setMargins(0, 0, 0, px(132));
                        brightPart.setLayoutParams(circleViewParams);
                        nextPersonBtn.bringToFront();
                        break;
                    case 2:
                        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).removeView(explanationMessage);
                        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).removeView(semiTransparentLayout);
                        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).removeView(annoyBtn);
                        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).removeView(nextPersonBtn);
                        tv1.setVisibility(View.VISIBLE);
                        tv1.setText("That completes the functional buttons overview.");
                        tv2.setVisibility(View.VISIBLE);
                        logoButton.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        ((GuideActivity)getActivity()).viewPager.setCurrentItem(2);
                        break;
                }
                page2_step++;
            }
        });
        RelativeLayout.LayoutParams nextStepBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nextStepBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nextStepBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nextStepBtn.setLayoutParams(nextStepBtnParams);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(nextStepBtn);
    }

    private void LoadThirdPage(View v){
        v.findViewById(R.id.dots).setBackgroundResource(R.drawable.dots3);

        final Context c = v.getContext();

        ImageButton logoButton = new ImageButton(c);
        logoButton.setBackgroundResource(R.drawable.camera_logo);
        RelativeLayout.LayoutParams logoButtonParams = new RelativeLayout.LayoutParams(px(85), px(60));
        logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        logoButtonParams.setMargins(px(10), px(10), 0, 0);
        logoButton.setLayoutParams(logoButtonParams);
        ((RelativeLayout)v.findViewById(R.id.fragment_layout)).addView(logoButton);

        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        /*RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlParams.addRule(RelativeLayout.CENTER_VERTICAL);*/
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(llParams);
        ((RelativeLayout)v.findViewById(R.id.fragment_layout)).addView(ll);

        final Button nextBtn = new Button(c);
        nextBtn.setText("Next");
        nextBtn.setBackgroundResource(R.drawable.drawable_next_button);
        RelativeLayout.LayoutParams nextBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nextBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        nextBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nextBtn.setLayoutParams(nextBtnParams);
        nextBtn.setVisibility(View.GONE);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuideActivity) getActivity()).viewPager.setCurrentItem(3);
            }
        });

        TextView genderSelect = new TextView(c);
        LinearLayout.LayoutParams genderSelectParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        genderSelectParams.setMargins(0, px(100), 0, 0);
        genderSelect.setLayoutParams(genderSelectParams);
        genderSelect.setGravity(Gravity.CENTER_HORIZONTAL);
        genderSelect.setText("Please select your gender");
        genderSelect.setTextSize(30);
        genderSelect.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        ll.addView(genderSelect);

        RadioGroup rdSexGroup = new RadioGroup(c);
        LinearLayout.LayoutParams rdSexGroupParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rdSexGroupParams.setMargins(0, px(50), 0, 0);
        rdSexGroupParams.gravity = Gravity.CENTER_HORIZONTAL;
        rdSexGroup.setLayoutParams(rdSexGroupParams);
        ll.addView(rdSexGroup);

        RadioButton rdMale = new RadioButton(c);
        rdMale.setChecked(false);
        rdMale.setText("Male");
        rdMale.setTextSize(25);
        rdMale.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#E47D0E")));
        rdMale.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
        rdMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(c).edit().putString("gender", "Male").commit();
                    nextBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        rdSexGroup.addView(rdMale);

        RadioButton rdFemale = new RadioButton(c);
        rdFemale.setChecked(false);
        rdFemale.setText("Female");
        rdFemale.setTextSize(25);
        rdFemale.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#E47D0E")));
        rdFemale.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
        rdFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceManager.getDefaultSharedPreferences(c).edit().putString("gender", "Female").commit();
                    nextBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        rdSexGroup.addView(rdFemale);

        final TextView tv2 = new TextView(c);
        final RelativeLayout.LayoutParams tv2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tv2Params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv2Params.setMargins(0, 0, 0, px(130));
        tv2.setLayoutParams(tv2Params);
        tv2.setTextSize(30);
        tv2.setText("Press next to continue");
        tv2.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(tv2);
    }

    private void LoadFourthPage(View v){
        v.findViewById(R.id.dots).setBackgroundResource(R.drawable.dots4);

        final Context c = v.getContext();

        ImageButton logoButton = new ImageButton(c);
        logoButton.setBackgroundResource(R.drawable.camera_logo);
        RelativeLayout.LayoutParams logoButtonParams = new RelativeLayout.LayoutParams(px(85), px(60));
        logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        logoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        logoButtonParams.setMargins(px(10), px(10), 0, 0);
        logoButton.setLayoutParams(logoButtonParams);
        ((RelativeLayout)v.findViewById(R.id.fragment_layout)).addView(logoButton);

        Button finishButton = new Button(c);
        finishButton.setText("Finish");
        finishButton.setBackgroundResource(R.drawable.drawable_next_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getDefaultSharedPreferences(c).getString("gender", "").equals("")) {
                    Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(c).edit().putBoolean("show_guide", true).commit();
                    startActivity(new Intent(c, PreviewCameraActivity.class).putExtra("showed_guide", true));
                    getActivity().finish();
                }
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        finishButton.setLayoutParams(params);
        ((RelativeLayout)v.findViewById(R.id.fragment_layout)).addView(finishButton);

        final TextView tv2 = new TextView(c);
        final RelativeLayout.LayoutParams tv2Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv2Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tv2Params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv2Params.setMargins(0, 0, 0, px(130));
        tv2.setLayoutParams(tv2Params);
        tv2.setTextSize(30);
        tv2.setText("Press finish to exit the guide");
        tv2.setTypeface(Typeface.createFromAsset(c.getAssets(), "Qarmic_sans_Abridged.ttf"));
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ((RelativeLayout) v.findViewById(R.id.fragment_layout)).addView(tv2);
    }

    private int px(float dips) {
        float DP = getResources().getDisplayMetrics().density;
        return Math.round(dips * DP);
    }

}
