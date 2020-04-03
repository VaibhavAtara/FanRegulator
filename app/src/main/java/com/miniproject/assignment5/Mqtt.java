package com.miniproject.assignment5;
import android.content.Context;
import android.os.Build;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Mqtt {

    Context context;


    public Mqtt(Context context){
        this.context = context;
    }



    public MqttAndroidClient get_connection() {
        final MqttAndroidClient client = new MqttAndroidClient(context
                , "tcp://tailor.cloudmqtt.com:13968", MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setUserName("ghleymma");
        options.setPassword("jmvoCCetDGiy".toCharArray());

        try {
            IMqttToken token = client.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        String message="{"+ "\"serial\"" +":\""+ Build.SERIAL+"\","+
                                "\"manufacturer\""+ ":\""+ Build.MANUFACTURER+"\","+ "\"type\"" +":"+ "\"mobile\""+","+ "\"topic\"" +":"+"\"101\""+","+
                                "\"start\""+ ":"+"\"1581139241.9628208\""+","+ "\"end\"" +":"+ "\"1581150998.1089\""+","+ "\"message\"" +":"+ "\"ON\""+","+
                                "\"from\""+":"+ "\"mobile\""+","+
                                "\"Watt\""+":"+"\"10\""+","+"\"duty_cycle\""+":"+"\"10\"}";
                        client.publish("conf", new MqttMessage(message.getBytes()));
                    } catch (MqttException e) {

                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });


        } catch (MqttException e) {

        }

        return client;
    }
}
