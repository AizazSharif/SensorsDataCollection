package com.example.aizaz.collection132;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import org.achartengine.GraphicalView;

public class SubActivity extends ActionBarActivity implements SensorEventListener {

    Sensor accelerometer;
    Sensor gyroscope;
    Sensor proximity;
    SensorManager sm;
    private static GraphicalView view;

    private LineGraph line = new LineGraph();

    private static Thread thread;
    static double acc1,acc2,acc3;


    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);



        thread = new Thread() {
            public void run()
            {
                for (int i = 0; i < 200; i++)
                {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Point p = getDataforX(i); // We got new data!
                    Point p2=getDataforY(i);
                    Point p3=getDataforZ(i);




                    line.addNewPoints(p,p2,p3); // Add it to our graph

                    view.repaint();
                }
            }
        };
        thread.start();




    }



    public static Point getDataforX(int x)
    {
        return new Point(x, acc1);
    }
    public static Point getDataforY(int x)
    {
        return new Point(x, acc2);
    }
    public static Point getDataforZ(int x)
    {
        return new Point(x, acc3);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NavUtils.navigateUpFromSameTask(this);
        }
        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        acc1=event.values[0];
        acc2=event.values[1];
        acc3=event.values[2];

    }

    protected void onStart() {
        super.onStart();
        view = line.getView(this);

        setContentView(view);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
