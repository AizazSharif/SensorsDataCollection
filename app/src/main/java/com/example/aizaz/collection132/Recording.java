package com.example.aizaz.collection132;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Aizaz on 10/14/2015.
 */
public class Recording extends AppCompatActivity implements SensorEventListener {


    protected static final String TAG = null;

    static ArrayList<TimeDataContainer> list = new ArrayList<TimeDataContainer>();

    Sensor Accelerometer,Rotation,Magnetometer,Gyroscope,Orientation;
    SensorManager sensorManager;
    TextView txt;

    static TextView txt2;

    TextView txt3,txt4;


    public boolean bool=false;

    TextToSpeech t1;
    private android.support.v7.widget.Toolbar toolbar;


    Button btnStart, btnStop,btn,btnSeries,btnSeries2,dtw,btnCircle,btnClearSeries,
            btnTriangle,btnStartTemplate,btnStopTemplate,
            btnStartSample,btnStopSample;

    private boolean startedTemplate = false;
    private boolean startedSample = false;
    private boolean started = false;


    /////////////////////////////////
    String comma = new String(",");
    String newLine= new String("\n");
    private PrintWriter mCurrentFile;
    private PrintWriter mCurrentFile2;


    EditText FileName;
    String filename=new String();

    StringBuffer buff = new StringBuffer();
    StringBuffer buff2 = new StringBuffer();
    String nameStr=new String();

    @SuppressLint("SdCardPath")protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnClearSeries= (Button) findViewById(R.id.btnClearSeries);

        btnStartTemplate=(Button) findViewById(R.id.btnStartTemplate);

        FileName= (EditText) findViewById(R.id.edit);

        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        sensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_GAME);


        // Accelerometer=sensorManager.getDefaultSensor(Sensor.);
        //sensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        txt= (TextView) findViewById(R.id.text);
        txt2= (TextView) findViewById(R.id.textView);



        btnStartTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnStartTemplate){

                    list.clear();
                    filename= FileName.getText().toString();

                    nameStr = new String("/storage/sdcard0/" + filename + ".csv");

                    t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                t1.setLanguage(Locale.US);
                            }
                        }
                    });

                    File outputFile = new File(nameStr);
                    mCurrentFile = null;

                    try {
                        mCurrentFile = new PrintWriter(new FileOutputStream(outputFile));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    txt2.setText("File Name is "+filename+".csv");
                    String ToSpeak = "Recording Starting in 5 seconds";
                    Toast.makeText(getApplicationContext(), ToSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(ToSpeak, TextToSpeech.QUEUE_FLUSH, null);

                    new java.util.Timer().schedule(

                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    // your code here, and if you have to refresh UI put this code:
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            //your code
                                            String toSpeak = "Recording Started";
                                            Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                                            started = true;

                                        }
                                    });
                                }
                            },
                            5000

                    );



                }








            }
        });
        btnClearSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnClearSeries) {

                    list.clear();
                    started=false;




                }


            }
        });
    }
    protected void onPause() {
        super.onPause();

        if(mCurrentFile!=null) {
            mCurrentFile.close();
        }



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recording, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SubActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(started) {
            long time = System.currentTimeMillis();
            Sensor sensor = event.sensor;
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                double x = event.values[0];
                double  y = event.values[1];
                double z = event.values[2];
                TimeDataContainer container = new TimeDataContainer(time,x,y,z);
                list.add(container);
                txt.setText("S.Pt"+list.size());
                if(list.size()==2701){
                    started=false;
                    store();
                }

            }


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void store() {
        int i = 0;

        txt2.setText("looping" + i + list.size());

        for (TimeDataContainer container : list) {
            buff.delete(0, buff.length());

            //txt2.setText("looping"+i+container.getTimeString());

            buff.append(String.valueOf(list.get(i).getTime()));
            buff.append(comma);
            buff.append(String.valueOf(list.get(i).getX()));
            buff.append(comma);
            buff.append(String.valueOf(list.get(i).getY()));
            buff.append(comma);
            buff.append(String.valueOf(list.get(i).getZ()));

            mCurrentFile.println(buff.toString());


            txt2.setText("Downstairs Saved");

            i++;
            //container.getTimeString()

        }
        String toSpeak = "Its stopped";
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


    }
}
