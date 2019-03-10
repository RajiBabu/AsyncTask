package com.example.android.assginment3.asyntask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView temp, humidity, activity, output;
    Integer sensorCount;
    EditText sensorReadings;
    private RandomNumberGenerator randomNumberGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void generateRandomNum(View view) {
        EditText editText = findViewById(R.id.sensorReadings);
        String sensorCountStr = editText.getText().toString();
        sensorCount = Integer.parseInt(sensorCountStr);

        temp = (TextView) findViewById(R.id.temperatureValueText);
        humidity = (TextView) findViewById(R.id.humidityValueText);
        activity = (TextView) findViewById(R.id.activityValueText);
        sensorReadings = (EditText) findViewById(R.id.sensorReadings);
        output = (TextView) findViewById(R.id.logReport);

        temp.setText("");
        humidity.setText("");
        activity.setText("");
        output.setText("");
        output.setMovementMethod(new ScrollingMovementMethod());

        randomNumberGenerator = new RandomNumberGenerator(sensorCount, temp, humidity, activity, output);
        randomNumberGenerator.execute();
    }

    public void cancelTask(View view) {
        randomNumberGenerator.cancel(true);
    }
    public class RandomNumberGenerator extends AsyncTask<String, List<Integer>, String> {

        private Integer sensorCount;
        private List<Integer> displayValues;
        private int index = 0;
        private TextView tempText;
        private TextView humidityText;
        private TextView activityText;
        private TextView outputText;

        public RandomNumberGenerator(Integer sensorCount, TextView temp, TextView humidity, TextView activity, TextView output) {
            this.sensorCount = sensorCount;
            this.tempText = temp;
            this.humidityText = humidity;
            this.activityText = activity;
            this.outputText = output;
            this.displayValues = new ArrayList<Integer>();
        }

        @Override
        protected String doInBackground(String... params) {
            if (sensorCount <= 0) {
                return null;
            }

            Random random = new Random();
            Integer temp = null;
            Integer humidity = null;
            Integer activity = null;

            for (index = 0; index < sensorCount; index++) {
                if (isCancelled()) {
                    break;
                }
                temp = random.nextInt(100-30)+30;
                humidity = random.nextInt(100-60)+60;
                activity = random.nextInt(500-0)+0;


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
                displayValues.add(0, temp);
                displayValues.add(1, humidity);
                displayValues.add(2, activity);

                publishProgress(displayValues);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(List<Integer>... progress) {
            tempText.setText(displayValues.get(0).toString());
            humidityText.setText(displayValues.get(1).toString());
            activityText.setText(displayValues.get(2).toString());

            String text = outputText.getText().toString();
            text = text + "Output " + index + ":\n";
            text = text + "Temperature:" + tempText.getText().toString() + "\n" + "Humidity:" + humidityText.getText().toString() + "\n" + "Activity:" + activityText.getText().toString() + "\n";
            outputText.setText(text);


        }

    }
}
