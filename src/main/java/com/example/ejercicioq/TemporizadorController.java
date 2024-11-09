package com.example.ejercicioq;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TemporizadorController extends AnchorPane {

    private BooleanProperty fin;  // Propiedad de tipo BooleanProperty para el estado de finalización
    private IntegerProperty tiempo; // Propiedad de tipo IntegerProperty para los minutos
    private Timer timer;

    @FXML
    private Label minutosIzquierda;

    @FXML
    private Label minutosDerecha;

    @FXML
    private Label segundosIzquierda;

    @FXML
    private Label segundosDerecha;

    public TemporizadorController() {
        this.fin = new SimpleBooleanProperty(false); // Inicializa la propiedad fin como false
        this.tiempo = new SimpleIntegerProperty(-1); // Inicializa la propiedad tiempo a -1
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/temp.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setTiempo(int minutos) {
        if (minutos >= 1 && minutos <= 99) {
            this.tiempo.set(minutos);
            return true;
        }
        return false; // Devuelve false si los minutos no están entre 1 y 99
    }

    public void iniciar() {
        if (this.tiempo.get() <= 0) {
            System.err.println("Asigna los minutos antes de iniciar el temporizador");
            return;
        }

        timer = new Timer();
        int totalSegundos = this.tiempo.get() * 60;

        timer.scheduleAtFixedRate(new TimerTask() {
            private int restante = totalSegundos;

            @Override
            public void run() {
                if (restante < 0) {
                    timer.cancel();
                    estiloParado();
                    Platform.runLater(() -> fin.set(true)); // Indica que el temporizador ha terminado
                    return;
                }

                int minutos = restante / 60;
                int segundos = restante % 60;

                // Actualiza la interfaz usando Platform.runLater
                Platform.runLater(() -> {
                    minutosIzquierda.setText(String.valueOf(minutos / 10));
                    minutosDerecha.setText(String.valueOf(minutos % 10));
                    segundosIzquierda.setText(String.valueOf(segundos / 10));
                    segundosDerecha.setText(String.valueOf(segundos % 10));
                });

                restante--; // Decrementa los segundos restantes
            }
        }, 0, 1000);
    }

    public void detener() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            estiloParado();
        }
    }

    public void estiloParado() {
        this.getStyleClass().add("parado");
    }

    public BooleanProperty finProperty() {
        return fin;
    }

    public IntegerProperty tiempoProperty() {
        return tiempo;
    }

}