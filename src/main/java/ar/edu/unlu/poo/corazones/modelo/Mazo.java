package ar.edu.unlu.poo.corazones.modelo;

import java.util.*;

public class Mazo {
    private final List<Carta> miMazo = new ArrayList<>();
    public Mazo(){
        crearMazo();
    }
    private void crearMazo(){
        miMazo.clear();
        for (int i = 2; i < 15; i++) {
            miMazo.add(new Carta(Palo.Trebol,i));
            miMazo.add(new Carta(Palo.Diamante,i));
            miMazo.add(new Carta(Palo.Pica,i));
            miMazo.add(new Carta(Palo.Corazon,i));
        }
    }
    //función de Collections para mezclar un array.
    private void mezclarMazo(){
        Collections.shuffle(miMazo);
    }
    //reparto la primér carta a cada jugador y la remuevo.
    public void repartir(List<Jugador> jugadores){
        mezclarMazo();
        for(Jugador jugador: jugadores){
            for (int i = 0; i < 13; i++) {
                if (miMazo.isEmpty()) {
                    throw new IllegalStateException("No hay suficientes cartas para repartir.");
                }
                Carta carta = miMazo.remove(0);
                jugador.recibirCarta(carta);
            }
        }
        //el mazo queda vacío, atención con eso!
    }
}
