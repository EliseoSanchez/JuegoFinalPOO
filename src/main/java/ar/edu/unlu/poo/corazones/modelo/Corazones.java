package ar.edu.unlu.poo.corazones.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class Corazones extends ObservableRemoto implements ICorazones{
    private final List<Jugador> jugadores = new ArrayList<>(4);
    private final Map<Jugador, List<Carta>> manos = new HashMap<>();
    private final Map<Jugador, List<Carta>> cartasGanadasPorJugador = new HashMap<>();
    private final Map<Jugador, Integer> puntosAcumulados = new HashMap<>();
    private int nroRonda = 1;
    private boolean corazon_roto = false;
    private int indiceLider = 0;
    private Ronda ronda;

    public Corazones (){}

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException{
        Objects.requireNonNull(jugador, "Jugador no puede ser nulo");
        if (jugadores.size()>= 4){
            throw new IllegalStateException("Solo se permiten 4 jugadores");
        }
        jugadores.add(jugador);
        puntosAcumulados.put(jugador,0);
        cartasGanadasPorJugador.put(jugador,new ArrayList<>());
        notificarObservadores(Eventos.NUEVO_JUGADOR);
    }
    public List<Jugador> getJugadores(){
        return jugadores;
    }
    public int getNroRonda(){
        return nroRonda;
    }
    public boolean isCorazon_roto() {
        return corazon_roto;
    }
    public int getIndiceLider() {
        return indiceLider;
    }

    public void inicio() throws RemoteException{
        if (jugadores.size()!= 4){
            throw new IllegalStateException("se necesitan 4 jugadores para comenzar la partida");
        }
        Mazo mazo = new Mazo();
        mazo.repartir(jugadores);
        manos.clear();
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
