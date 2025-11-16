package ar.edu.unlu.poo.corazones.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ronda {
    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<Carta> cartasJugadas = new ArrayList<>();
    private int nroRonda;

    public Ronda(List<Jugador> jugadores) {
        if (jugadores != null) {
            this.jugadores.addAll(jugadores);
        }
        this.nroRonda = 1;
    }
    // Ejecutar la lógica base de una ronda: en la primera mano se busca el 2 de trébol,
    // luego se aplica la regla de intercambio (pasar cartas) según nroRonda.
    public void jugarRonda() {
        if (nroRonda == 1) {
            primerRonda();
        }
        intercambio();
    }

    // Busca el jugador que tiene el 2 de trébol y lo rota al primer lugar
    public void primerRonda() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            if (j != null && j.dosTrebol()) {
                Collections.rotate(jugadores, -i);
                break;
            }
        }
    }
    private void intercambio() {
        int mod = nroRonda % 4;
        switch (mod) {
            case 1:
                Collections.rotate(jugadores, -1);
                break; // pasar a la izquierda
            case 2:
                Collections.rotate(jugadores, 1);
                break; // pasar a la derecha
            case 3 :
                    Collections.rotate(jugadores, 2);
                    break; // pasar cruzado (para 4 jugadores)
            default : {
                // mod == 0 -> sin intercambio
            }
        }
    }
    public void limpiarCartasJugadas(){
        cartasJugadas.clear();
    }
    public void agregarCartaJugada(Carta carta) {
        if (carta != null) {
            cartasJugadas.add(carta);
            }
        }
    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }
    public List<Carta> getCartasJugadas() {
        return new ArrayList<>(cartasJugadas);
    }
    public int getNroRonda() {
        return nroRonda;
    }
    public void incrementarRonda(){
        nroRonda++;
    }
}