package perimara.era.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import perimara.era.R;

public class SettingsActivity extends AppCompatActivity {

    String mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((RadioButton) findViewById(R.id.rdMale)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGender = "Male";
                }
            }
        });
        ((RadioButton) findViewById(R.id.rdFemale)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGender = "Female";
                }
            }
        });

        //Preload settings
        String gender = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).getString("gender", "");
        if (gender.equals("Male")){
            ((RadioButton)findViewById(R.id.rdMale)).setChecked(true);
        } else if (gender.equals("Female")){
            ((RadioButton)findViewById(R.id.rdFemale)).setChecked(true);
        }

        //Apply button
        findViewById(R.id.applySettingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().putString("gender", mGender).commit();
                finish();
            }
        });
    }
}
