package com.example.ejercicio;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView pregunta;
    private EditText respuesta;
    private Button botonlindo;
    private Button reintentar;
    private pregunta p;
    private TextView puntaje;
    private TextView counter;
    private boolean counterplay = true;
    private boolean textPressed = false;
    private int counter1 = 30;
    private int punto = 0;
    private int tiempo = 1500;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pregunta = findViewById(R.id.pregunta);
        respuesta = findViewById(R.id.respuestaUser);
        botonlindo = findViewById(R.id.botonlindo);
        puntaje = findViewById(R.id.puntaje);
        counter = findViewById(R.id.contador);
        reintentar = findViewById(R.id.reintentar);

        p = new pregunta();
        pregunta.setText(p.getPregunta());

        playCounter();

        botonlindo.setOnClickListener(
                v -> {
                    responder();
                }
        );

        reintentar.setOnClickListener(
                v -> {
                    restart();
                }
        );

        pregunta.setOnTouchListener(
                (view, event) -> {


                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            textPressed = true;
                            new Thread(
                                    () -> {
                                        for (int i = 0; i < 20; i++) {
                                            try {
                                                Thread.sleep(75);
                                                if (textPressed == false) {
                                                    return;
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        counterplay = false;
                                        runOnUiThread(
                                                () -> {
                                                    nuevaPregunta();
                                                    reintentar.setVisibility(View.GONE);
                                                    respuesta.setText("");
                                                }
                                        );

                                    }

                            ).start();
                            break;

                        case MotionEvent.ACTION_UP:
                            textPressed = false;
                            break;

                    }
                    return true;
                }
        );

    }

    private void restart() {
        counterplay = false;
        nuevaPregunta();
        punto = 0;
        puntaje.setText("Tu puntaje: " + punto);
        reintentar.setVisibility(View.GONE);
        botonlindo.setEnabled(true);
        respuesta.setText("");
    }

    private void responder() {
        String res = respuesta.getText().toString();
        int resInt = Integer.parseInt(res);
        double correcta = p.getRespuesta();
        if (resInt == correcta) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            punto = punto + 5;
            puntaje.setText("Tu puntaje: " + punto);
            nuevaPregunta();
            respuesta.setText("");
            counterplay = false;
            Toast.makeText(this, "Respuesta: " + p.getRespuesta(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Inténtalo de nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    private void nuevaPregunta() {
        p = new pregunta();
        pregunta.setText(p.getPregunta());
    }

    public void playCounter() {
        counter1 = 30;
        new Thread(
                () -> {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            if (counterplay == false) {
                                counter1 = 30;
                                counterplay = true;
                            }
                            if (counter1 > 0 && counter1 <= 30) {
                                counter1--;
                            }
                            runOnUiThread(
                                    () -> {
                                        counter.setText("" + counter1);
                                    }
                            );
                            if (counter1 == 0) {
                                runOnUiThread(
                                        () -> {
                                            botonlindo.setEnabled(false);
                                            reintentar.setVisibility(View.VISIBLE);
                                        }
                                );
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

}
