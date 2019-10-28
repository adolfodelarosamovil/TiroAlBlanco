package com.adp.tiroalblanco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView title_popup_txt;
    TextView message_popup_txt;
    Vibrator vibrator;
    private final int MIN_VALUE = 1;
    int progressChangedValue = 50;
    int round = 0;
    int points = 0;
    int number_random = 0;
    String titulo;
    String mensaje;


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
        //title_popup_txt = findViewById(R.id.title_popup);
        //message_popup_txt = findViewById(R.id.message_popup);
        simpleSeekBar=(SeekBar)findViewById(R.id.seek_bar);
    }

    private void asignarNumeroAleatorio() {
        number_random = getNumeroAleatorio();
    }

    public void hitme(View view) {
        vibrate();
        sonidoFlecha();
        calculatePoints();
        popupAlert();
    }

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

    private void sonidoFlecha() {
        //Reproducir archivo de sonido
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.impacto_flecha);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1300, 1300);
        mediaPlayer.start();
    }

    private void popupAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(mensaje);
        builder.setTitle(titulo);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(round == 5){
                    inicializarJuego();
                }
/*
                Log.d("MIAPP", "El usuario le ha dado al Botón Retroceso");
                EditText dniView = findViewById(R.id.dni);
                String dniString = dniView.getText().toString();

                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int radioId = radioGroup.getCheckedRadioButtonId();

                //Guarda valores
                SharedPreferences sp = getSharedPreferences(NOMBRE_FICHERO_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(DNI, dniString);
                editor.putInt(RADIO, radioId);
                editor.commit();

                dialog.dismiss();
                MainActivity.this.finish();*/
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.popup_alert, null))
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
*/
    }

    private void inicializarJuego() {
        round = 0;
        points = 0;
        round_txt.setText(String.valueOf(round));
        points_txt.setText(String.valueOf(points));
        asignarNumeroAleatorio();
    }

    private void prepararPunteria(){
        //realizar el evento de escucha de cambio de barra de búsqueda utilizado para obtener el valor de progreso
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

    private int getNumeroAleatorio(){
        int numero;
        numero = (int)(Math.random()*100+1);
        return numero;
    }

    private void calculatePoints(){
        round++;
        int diferencia = Math.abs(progressChangedValue-number_random);
        if(diferencia == 0){
            if(round == 1){
                points = 1000000;
            }else if(round == 2){
                points += 500000;
            }else if(round == 3){
                points += 250000;
            }else if(round == 4){
                points += 100000;
            }else if(round == 5) {
                points += 50000;
            }
            titulo = "Acertaste en el intento : " + round;
            mensaje = "Tu puntuación total es : " + points;
            inicializarJuego();
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
        }
        points_txt.setText(String.valueOf(points));
        round_txt.setText(String.valueOf(round));
        //title_popup_txt.setText(titulo);
        //message_popup_txt.setText(mensaje);
    }

    public void ir_about(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void mostrarValor(View view) {
        Toast.makeText(MainActivity.this, "" + number_random,
                Toast.LENGTH_SHORT).show();
    }
}
