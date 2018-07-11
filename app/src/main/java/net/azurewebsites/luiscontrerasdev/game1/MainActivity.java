package net.azurewebsites.luiscontrerasdev.game1;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Animation salida;
    private Resources resource;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private TextView txtResultado;
    private String[] arrayRespuesta;
    private Random numeroRandom;
    private int movimiento = 0;
    private Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResultado = (TextView)  findViewById(R.id.salida);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        assert sensorManager != null;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        resource = getResources();
        arrayRespuesta = resource.getStringArray(R.array.string_respuesta);
        numeroRandom = new Random();

        String fuente = "fuentes/fuente.otf";
        this.font = Typeface.createFromAsset(getAssets(), fuente);


        txtResultado.setTypeface(font);

        salida = AnimationUtils.loadAnimation(MainActivity.this, R.anim.salida);
        txtResultado.startAnimation(salida);



        if (sensor == null)
            finish();

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];

                if (x < -5 && movimiento == 0 || x > 5 && movimiento == 1) {
                    movimiento++;
                    txtResultado.setText(arrayRespuesta[numeroRandom.nextInt(20)]);
                    txtResultado.startAnimation(salida);
                }

                if (movimiento == 2) {
                    movimiento = 0;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public void sound(){
        MediaPlayer sound = MediaPlayer.create(this, R.raw.sonido);
        sound.start();
    }

    private void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        start();
        super.onResume();
    }
}