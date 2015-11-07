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
public class Running extends AppCompatActivity implements SensorEventListener {

    private android.support.v7.widget.Toolbar toolbar;
    protected static final String TAG = null;

    static ArrayList<TimeDataContainer> list = new ArrayList<TimeDataContainer>();

    Sensor Accelerometer,Rotation,Magnetometer,Gyroscope,Orientation;
    SensorManager sensorManager;
    TextView txt;

    static TextView txt2;

    TextView txt3,txt4;



    public boolean bool=false;

    TextToSpeech t1;


    Button btnClearSeries,
            btnStartTemplate,Next,Previous,Finish;

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

    @SuppressLint("SdCardPath")protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnClearSeries= (Button) findViewById(R.id.btnClearSeries);

        btnStartTemplate=(Button) findViewById(R.id.btnStartTemplate);

        Next=(Button)findViewById(R.id.button);
        Previous=(Button)findViewById(R.id.button2);
        // FileName= (EditText) findViewById(R.id.edit);
       // filename= FileName.toString();

        String nameStr = new String("/storage/sdcard0/" + "Running" + ".csv");


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
        String toSpeak = "Running Dataset";
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);



        File outputFile = new File(nameStr);
        mCurrentFile = null;

        try {
            mCurrentFile = new PrintWriter(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        sensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        // Accelerometer=sensorManager.getDefaultSensor(Sensor.);
        //sensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        txt= (TextView) findViewById(R.id.text);
        txt2= (TextView) findViewById(R.id.textView);



        btnStartTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnStartTemplate){

                    list.clear();

                    String ToSpeak = "Running Dataset Starting in 5 seconds";
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
                                            String toSpeak = "Running Recording Started";
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



        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Running.this,DownStairs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_running, menu);
        return true;
    }
    protected void onPause() {
        super.onPause();

        mCurrentFile.close();




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


            txt2.setText("Running Saved");

            i++;
            //container.getTimeString()

        }
        String toSpeak = "running dataset recoding completed";
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


    }


}
