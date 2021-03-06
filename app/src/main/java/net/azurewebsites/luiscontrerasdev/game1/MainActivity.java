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
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Animation salida;
    private Resources resource;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private TextView txtResultado;
    private String[] arrayRespuesta;
    private Random numeroRandom;
    private int movimiento = 0;
    private Button pausa;
    private LinearLayout pausaLayout;
    private LottieAnimationView play;
    private LottieAnimationView magicBall;
    private MediaPlayer sound;
    private Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtResultado = (TextView)  findViewById(R.id.salida);
        pausa = (Button) findViewById(R.id.btn_pausa);
        pausaLayout = (LinearLayout) findViewById(R.id.pt_pausa);
        play = (LottieAnimationView) findViewById(R.id.btn_play);
        magicBall = (LottieAnimationView) findViewById(R.id.magic_bola);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        pausaLayout.setVisibility(View.INVISIBLE);
        pausa.setOnClickListener(this);
        play.setOnClickListener(this);


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
        sound();
    }

    public void sound(){
        sound = MediaPlayer.create(this, R.raw.audio);
        sound.start();
        sound.setLooping(true);
    }

    private void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        pausaLayout.setVisibility(View.INVISIBLE);
        pausa.setAnimation(salida);
        pausa.setVisibility(View.VISIBLE);
        magicBall.setVisibility(View.VISIBLE);
        magicBall.setVisibility(View.VISIBLE);
        txtResultado.setVisibility(View.VISIBLE);
        sound.start();
    }

    private void stop() {
        sensorManager.unregisterListener(sensorEventListener);

        pausa.setVisibility(View.INVISIBLE);
        pausaLayout.startAnimation(salida);
        pausaLayout.setVisibility(View.VISIBLE);
        magicBall.setVisibility(View.INVISIBLE);
        txtResultado.setVisibility(View.INVISIBLE);
        sound.pause();
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

    @Override
    public void onClick(View v) {
        if(v == pausa){
           stop();
        }

        if(v == play){
            start();
        }
    }
}