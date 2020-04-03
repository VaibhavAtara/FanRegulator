package com.miniproject.assignment5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.akaita.android.circularseekbar.CircularSeekBar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Button rotate, stop;
    ImageView iv;
    CircularSeekBar circularSeekBar;
    Animation aniRotateClk;
    Button subscribe ;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mqtt mqtt = new Mqtt(getApplicationContext());
        final MqttAndroidClient client = mqtt.get_connection();

        rotate=(Button) findViewById(R.id.rotbtn);
        stop = (Button) findViewById(R.id.buttonstop);
        iv=(ImageView) findViewById(R.id.rotiv);
        circularSeekBar=(CircularSeekBar)findViewById(R.id.skc);
        circularSeekBar.setProgressTextFormat(new DecimalFormat("###,###,##0"));
        subscribe = (Button)findViewById(R.id.subscribe);

        //uncomment below for recevier and comment for sender
        //subscribe.setVisibility(View.GONE);



        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //uncomment below  block for recevier and comment for sender
                    /*try{
                        String msg ="One device Connnected";
                        client.publish("device",new MqttMessage(msg.getBytes()));
                    }catch (MqttException e){

                    }
                    client.subscribe("fan", 1);*/

                    //comment below  for recevier and uncomment for sender
                    client.subscribe("device",1);



                }catch (MqttException e){}
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {

                        //uncomment below  for recevier and comment for sender
                            /*String progress = message.toString();
                            circularSeekBar.setProgress(Float.parseFloat(progress));*/

                           //comment below  for recevier and uncomment for sender
                            Toast.makeText(getApplicationContext(),"One device Connected",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });




            }
        });



        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                aniRotateClk.setDuration(3000);
                iv.startAnimation(aniRotateClk);
                circularSeekBar.setProgress(10);
                //comment below  for recevier and uncomment for sender
               // subscribe.performClick();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate.setEnabled(false);
                rotate.setBackgroundResource(R.drawable.btnstyle2);
                rotate.setElevation((float)0.0);

                value = (int)circularSeekBar.getProgress();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            if(value>60)
                        {
                            value-=10;
                            circularSeekBar.setProgress(value);
                        }
                        else if(value>20)
                        {
                            value-=8;
                            circularSeekBar.setProgress(value);
                        }
                        else if(value>3){
                            value -= 4;
                            circularSeekBar.setProgress(value);
                        }
                        else {
                            value -=1;
                            circularSeekBar.setProgress(value);
                        }



                        if(value!=0)
                            stop.performClick();

                        if(value==0) {
                            rotate.setEnabled(true);
                            rotate.setBackgroundResource(R.drawable.btnstyle);
                            rotate.setElevation((float)80.0);
                        }

                    }
                }, 3000);



            }
        });

        circularSeekBar.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar seekBar, float progress, boolean fromUser) {


                    aniRotateClk = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                    aniRotateClk.setDuration((long) (10000/progress));
                    iv.startAnimation(aniRotateClk);
                //comment below try-catch for recevier and uncomment for sender
                   try{
                        String msg =""+progress;
                        client.publish("fan",new MqttMessage(msg.getBytes()));
                    }catch (MqttException e){

                    }

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }
        });




    }
}
