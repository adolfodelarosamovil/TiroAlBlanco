package com.adp.tiroalblanco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button hitme_btn;
    TextView number_random_txt;
    SeekBar simpleSeekBar;
    TextView points_txt;
    TextView round_txt;
    TextView titlePopup;
    TextView messagePopup;
    ImageView trofeoPopup;
    Vibrator vibrator;

    private final int MIN_VALUE = 1;
    int progressChangedValue = 50;
    int round = 0;
    int points = 0;
    int number_random = 0;
    String titulo;
    String mensaje;
    String botonPopup = "Aceptar";
    boolean acertaste = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ocultamos la ActionBar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        inicializarControles();
        asignarNumeroAleatorio();
        prepararPunteria();
    }

    private void inicializarControles() {
        hitme_btn = findViewById(R.id.hitme);
        number_random_txt = findViewById(R.id.numero_aleatorio);
        points_txt = findViewById(R.id.points);
        round_txt = findViewById(R.id.rounds);
        simpleSeekBar=(SeekBar)findViewById(R.id.seek_bar);
    }

    // Inicializar Juego
    private void inicializarJuego() {
        round = 0;
        points = 0;
        round_txt.setText(String.valueOf(round));
        points_txt.setText(String.valueOf(points));
        asignarNumeroAleatorio();
        acertaste = false;
        botonPopup = "Aceptar";
    }

    public void inicializar_juego(View view) {
        inicializarJuego();
    }

    // Acciones al presionar botón DISPARO!
    public void hitme(View view) {
        vibrate();
        sonidoFlecha();
        calculatePoints();
        popupAlert();
    }

    // Vibrar el dispositivo
    private void vibrate() {
        long[] pattern = {0, 1000};
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(pattern,0);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                vibrator.cancel();
            }
        }, 1000);
    }

    // Reproducir archivo de sonido
    private void sonidoFlecha() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.impacto_flecha);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1300, 1300);
        mediaPlayer.start();
    }

    // Reproducir archivo de sonido
    private void sonidoGanador() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.ganador);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1300, 1300);
        mediaPlayer.start();
    }

    // Mostrar Popup
    private void popupAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Obtén el  layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate y establecer el layout para el diálogo
        // Pase null como la parent view porque va en el dialog layout
        View viewRoot = inflater.inflate(R.layout.popup_alert, null);

        //Actualizar textos del Popup
        titlePopup = viewRoot.findViewById(R.id.title_popup);
        messagePopup = viewRoot.findViewById(R.id.message_popup);
        trofeoPopup = viewRoot.findViewById(R.id.trofeo_popup);
        titlePopup.setText(titulo);
        messagePopup.setText(mensaje);
        if(acertaste){
            trofeoPopup.setVisibility(View.VISIBLE);
        }else{
            trofeoPopup.setVisibility(View.GONE);
        }

        builder.setView(viewRoot)
            // Añadir action buttons
            .setPositiveButton(botonPopup, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    if(round == 5 || acertaste){
                        inicializarJuego();
                    }
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
        if(acertaste){
            sonidoGanador();
        }
    }

    // Gestiona SeekBar
    private void prepararPunteria(){
        // Realizar el evento de escucha de cambio de barra de búsqueda utilizado para obtener el valor de progreso
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (simpleSeekBar.getProgress() < MIN_VALUE) {
                    simpleSeekBar.setProgress(MIN_VALUE);
                }
                Toast.makeText(MainActivity.this, "Apuntas al número : " + progressChangedValue,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Generar número aleatorio
    private int getNumeroAleatorio(){
        int numero;
        numero = (int)(Math.random()*100+1);
        return numero;
    }

    // Asignar número aleatorio
    private void asignarNumeroAleatorio() {
        number_random = getNumeroAleatorio();
    }

    // Ir a Activity About
    public void ir_about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_resources_start, R.anim.anim_resources_end);
    }

    // Mostrar el número oculto
    public void mostrarValor(View view) {
        Toast.makeText(MainActivity.this, "" + number_random,
                Toast.LENGTH_SHORT).show();
    }

    private void calculatePoints(){
        round++;
        int diferencia = Math.abs(progressChangedValue-number_random);

        if(diferencia == 0){
            acertaste = true;
            switch (round){
                case 1:
                    points = 1000000;
                    break;
                case 2:
                    points += 500000;
                    break;
                case 3:
                    points += 250000;
                    break;
                case 4:
                    points += 100000;
                    break;
                case 5:
                    points += 50000;
                    break;
            }
            titulo = "Acertaste en el intento : " + round;
            mensaje = "Tu puntuación total es : " + points;
            botonPopup = "Volver a Jugar";
        }else {
            int point = 0;
            if (diferencia > 50) {
                points++;
                point = 1;
                titulo = "Disparo fuera de la Diana";
            } else if (diferencia > 40) {
                points += 100;
                point = 100;
                titulo = "Disparo muy alejado";
            } else if (diferencia > 30) {
                points += 1000;
                point = 1000;
                titulo = "Disparo alejado";
            } else if (diferencia > 20) {
                points += 5000;
                point = 5000;
                titulo = "Disparo cercano";
            } else if (diferencia > 10) {
                points += 10000;
                point = 10000;
                titulo = "Disparo muy cercano";
            }else{
                points += 15000;
                point = 15000;
                titulo = "Disparo super cercano";
            }
            mensaje = "Por este disparo te damos : " + point + " puntos";
            if(round == 5){
                titulo = "No acertaste, el número es : " + number_random;
                mensaje = " Tu puntuación final es : " + points;
                botonPopup = "Volver a Jugar";
            }
        }
        points_txt.setText(String.valueOf(points));
        round_txt.setText(String.valueOf(round));
    }


}
