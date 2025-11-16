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
    private void mezclarMazo(){
        Collections.shuffle(miMazo);
    }
    public List<Carta> getMazo(){
        return miMazo;
    }
    public void repartir(Jugador j1, Jugador j2, Jugador j3, Jugador j4){
        mezclarMazo();
        for (int i = 0; i < miMazo.size()/4; i++){
            j1.recibirCarta(miMazo.get(i*4));
            j2.recibirCarta(miMazo.get(i*4+1));
            j3.recibirCarta(miMazo.get(i*4+2));
            j4.recibirCarta(miMazo.get(i*4+3));
        }
    }
}
