package ar.edu.unlu.poo.corazones.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Corazones extends ObservableRemoto implements ICorazones{
    private List<Jugador> jugadores;
    private Ronda ronda;
    private List<Carta> cartasDeRonda;

    private Jugador jugador;

    public Corazones(){
        this.jugadores = new ArrayList<>();
    }

    public List<Jugador> getJugadores(){
        return jugadores;
    }
    public void inicio(){
        Mazo mazo = new Mazo();
        mazo.repartir(jugadores);
        this.ronda = new Ronda(jugadores);
        ronda.primerRonda();
    }
    public void siguienteRonda(){

    }

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        notificarObservadores(Eventos.NUEVO_JUGADOR);
    }
    public int getRonda(Ronda ronda){
        return ronda.getNroRonda();
    }
}
