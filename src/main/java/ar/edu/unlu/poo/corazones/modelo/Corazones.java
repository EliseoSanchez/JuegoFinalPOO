package ar.edu.unlu.poo.corazones.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class Corazones extends ObservableRemoto implements ICorazones{
    private final List<Jugador> jugadores = new ArrayList<>(4);
    //private final Map<Jugador, List<Carta>> manos = new HashMap<>();
    private final Map<Jugador, List<Carta>> cartasGanadasPorJugador = new HashMap<>();
    private final Map<Jugador, Integer> puntosAcumulados = new HashMap<>();
    private int nroRonda = 1;
    private boolean corazon_roto;
    private int indiceLider;
    private Ronda ronda;

    public Corazones (){}

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException{
        Objects.requireNonNull(jugador, "Jugador no puede ser nulo");
        if (jugadores.size()>= 4){
            throw new IllegalStateException("Solo se permiten 4 jugadores");
        }
        jugadores.add(jugador);
        puntosAcumulados.putIfAbsent(jugador,0);
        cartasGanadasPorJugador.putIfAbsent(jugador,new ArrayList<>());
        notificarObservadores(Eventos.NUEVO_JUGADOR);
    }
    public List<Jugador> getJugadores(){
        return new ArrayList<>(jugadores);
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

    public void iniciarPartida() throws RemoteException{
        if (jugadores.size()!= 4){
            throw new IllegalStateException("se necesitan 4 jugadores para comenzar la partida");
        }
        Mazo mazo = new Mazo();
        mazo.repartir(jugadores);
        //manos.clear();
        for (Jugador jugador : jugadores){
            //manos.put(jugador, jugador.getCartasMano());
            cartasGanadasPorJugador.put(jugador,new ArrayList<>());
            puntosAcumulados.put(jugador,0);
        }
        ronda = new Ronda(jugadores);
        ronda.primerRonda();
        indiceLider = 0;
        corazon_roto = false;

        notificarObservadores(Eventos.MANO_INICIADA);
    }
    public boolean partidaTerminada(){
        for (Integer puntos : puntosAcumulados.values()){
            if(puntos >= 100){
                return true;
            }
        }
        return false;
    }
    public Jugador obtenerGanador(){
        int min = 100;
        Jugador ganador = null;
        for (Jugador jugador : jugadores){
            if(jugador.getPuntos() < min){
                min = jugador.getPuntos();
                ganador = jugador;
            }
        }
        return ganador;
    }
    private boolean validarPaloEnMano(Jugador jugador, Palo palo){
        if (palo == null) {return false;}
        //List<Carta> manoJugador = manos.get(jugador);
        List<Carta> manoJugador = jugador.getCartasMano();
        if (manoJugador == null) {return false;}
        for(Carta carta : manoJugador){
            if(palo.equals(carta.getPalo())){return true;}
        }
        return false;
    }
    //metodo de intercambio de cartas finalizado, se encarga de recibir una lista de lista de cartas(de cada jugador)
    // elimar las cartas de las manos de cada jugador y de repartirlas segun la ronda.
    // ronda n1 hacia izquierda, n2 hacia derecha, n3 hacia enfrente. n4 no hay intercambio.
    public void aplicarIntercambio(List<List<Carta>> cartasIntercambio) throws RemoteException{
        Objects.requireNonNull(cartasIntercambio,"La lista de listas de cartas a intercambiar no puede ser nula");
        if(cartasIntercambio.size() != 4){
            throw new IllegalArgumentException("es necesario 4 listas de cartas para el intercambio");
        }
        int mod = getNroRonda() % 4;
        if(mod == 0){
            notificarObservadores(Eventos.INTERCAMBIO_REALIZADO);
            return;
        }
        for (List<Carta> listaDeCadaJugador : cartasIntercambio){
            if(listaDeCadaJugador == null || listaDeCadaJugador.size() != 3){
                throw new IllegalArgumentException("Cada jugador debe elegir 3 cartas");
            }
        }
        for (int i = 0; i < 4; i++) {
            Jugador origen = jugadores.get(i);
            List<Carta> manoDeOrigen = origen.getCartasMano();
            for (Carta carta : cartasIntercambio.get(i)){
                if(!manoDeOrigen.remove(carta)){
                    throw new IllegalArgumentException("La carta" + carta + "no esta en la mano del jugador");
                }
            }
        }
        int rotacion = switch (mod) {
            case 1 -> -1;// rotacion hacia la izquierda
            case 2 -> 1;// rotacion hacia la derecha
            case 3 -> 2;// rotacion hacia enfrente
            default -> 0;
        };
        for (int i = 0; i < 4; i++) {
            int indiceDeDestino = (i + rotacion + 4) % 4;
            Jugador destino = jugadores.get(indiceDeDestino);
            destino.recibirCarta(new ArrayList<>(cartasIntercambio.get(i)));
        }
        notificarObservadores(Eventos.INTERCAMBIO_REALIZADO);
    }
    public void siguienteRonda(){

    }
}
