package app.allever.androd.lib.cropper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.edmodo.cropper.CropImageView;

public class CropMainActivity extends AppCompatActivity {

    // Private Constants ///////////////////////////////////////////////////////////////////////////

    private static final int GUIDELINES_ON_TOUCH = 1;

    // Activity Methods ////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_crop);

        // Initialize Views.
        final ToggleButton fixedAspectRatioToggleButton = (ToggleButton) findViewById(R.id.fixedAspectRatioToggle);
        final TextView aspectRatioXTextView = (TextView) findViewById(R.id.aspectRatioX);
        final SeekBar aspectRatioXSeekBar = (SeekBar) findViewById(R.id.aspectRatioXSeek);
        final TextView aspectRatioYTextView = (TextView) findViewById(R.id.aspectRatioY);
        final SeekBar aspectRatioYSeekBar = (SeekBar) findViewById(R.id.aspectRatioYSeek);
        final Spinner guidelinesSpinner = (Spinner) findViewById(R.id.showGuidelinesSpin);
        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        final ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
        final Button cropButton = (Button) findViewById(R.id.Button_crop);

        // Initializes fixedAspectRatio toggle button.
        fixedAspectRatioToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cropImageView.setFixedAspectRatio(isChecked);
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioXSeekBar.setEnabled(isChecked);
                aspectRatioYSeekBar.setEnabled(isChecked);
            }
        });
        // Set seek bars to be disabled until toggle button is checked.
        aspectRatioXSeekBar.setEnabled(false);
        aspectRatioYSeekBar.setEnabled(false);

        aspectRatioXTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));
        aspectRatioYTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));

        // Initialize aspect ratio X SeekBar.
        aspectRatioXSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioXSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    aspectRatioXSeekBar.setProgress(1);
                }
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioXTextView.setText(String.valueOf(aspectRatioXSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }
        });

        // Initialize aspect ratio Y SeekBar.
        aspectRatioYSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar aspectRatioYSeekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    aspectRatioYSeekBar.setProgress(1);
                }
                cropImageView.setAspectRatio(aspectRatioXSeekBar.getProgress(), aspectRatioYSeekBar.getProgress());
                aspectRatioYTextView.setText(String.valueOf(aspectRatioYSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing.
            }
        });

        // Set up the Guidelines Spinner.
        guidelinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropImageView.setGuidelines(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing.
            }
        });
        guidelinesSpinner.setSelection(GUIDELINES_ON_TOUCH);

        // Initialize the Crop button.
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap croppedImage = cropImageView.getCroppedImage();
                croppedImageView.setImageBitmap(croppedImage);
            }
        });
    }
}
