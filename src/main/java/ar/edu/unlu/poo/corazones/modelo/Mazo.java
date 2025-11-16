package ar.edu.unlu.poo.corazones.modelo;

import java.util.*;

public class Mazo {
    private final List<Carta> miMazo = new ArrayList<>();

    public Mazo(){
        crearMazo();
    }
    private void crearMazo(){
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
    // envía el mazo en una lista que no es modificable.
    public List<Carta> getMazo(){
        return Collections.unmodifiableList(miMazo);
    }
    public void repartir(List<Jugador> jugadores){
        mezclarMazo();
        for(Jugador jugador: jugadores){
            jugador.recibirCarta(miMazo.removeFirst());
        }
    }
}
