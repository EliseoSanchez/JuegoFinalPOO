package ar.edu.unlu.poo.corazones.modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class Corazones extends ObservableRemoto implements ICorazones{
    private final List<Jugador> jugadores = new ArrayList<>(4);
    private int nroRondasJugadas;
    private boolean corazonRoto;
    private int indiceLider;
    private boolean primeraMano;
    private int numeroMano = 1;
    public Corazones (){}
    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException{
        Objects.requireNonNull(jugador, "Jugador no puede ser nulo");
        if (jugadores.size()>= 4){
            throw new IllegalStateException("Solo se permiten 4 jugadores");
        }
        jugadores.add(jugador);
        notificarObservadores(Eventos.NUEVO_JUGADOR);
    }
    @Override
    public List<Jugador> getJugadores(){
        return new ArrayList<>(jugadores);
    }
    @Override
    public void iniciarPartida() throws RemoteException{
        if (jugadores.size()!= 4){
            throw new IllegalStateException("se necesitan 4 jugadores para comenzar la partida");
        }
        Mazo mazo = new Mazo();
        mazo.repartir(jugadores);
        indiceLider = buscarJugadorConCarta();
        corazonRoto = false;
        nroRondasJugadas=0;
        primeraMano = true;
        notificarObservadores(Eventos.MANO_INICIADA);
    }
    private int buscarJugadorConCarta() {
        for (int i = 0; i < 4; i++) {
            for (Carta carta : jugadores.get(i).getCartasMano()) {
                if (carta.getPalo().equals(Palo.Trebol) && carta.getId() == 2){
                    return i;
                }
            }
        }
        return 0;
    }
    @Override
    public Jugador jugarRonda(List<Integer> indicesCartas) throws RemoteException {

        validarCantidadDeCartas(indicesCartas);

        Palo paloInicial = null;
        boolean contieneCorazon = false;
        List<Carta> cartasEnOrden = new ArrayList<>();
        List<Jugador> jugadoresEnOrden = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int idxJugador = (indiceLider + i) % 4;
            Jugador jugador = jugadores.get(idxJugador);
            int idxCarta = indicesCartas.get(i);

            Carta carta = obtenerCartaValidada(jugador, idxCarta);
            cartasEnOrden.add(carta);
            jugadoresEnOrden.add(jugador);
        }
        for (int i = 0; i < 4; i++) {
            Jugador jugador = jugadoresEnOrden.get(i);
            Carta jugada = cartasEnOrden.get(i);

            if (i == 0) {
                validarCartaInicial(jugada);
                paloInicial = jugada.getPalo();
            } else {
                boolean tienePalo = tienePaloEnMano(jugador, paloInicial);
                if (tienePalo && !paloInicial.equals(jugada.getPalo())) {
                    throw new IllegalArgumentException("El jugador debe seguir el palo si puede");
                }
                if (primeraMano && jugada.getPuntaje() > 0) {
                    throw new IllegalArgumentException("En la primera baza no se pueden jugar cartas con puntaje");
                }
            }

            if (jugada.getPalo() == Palo.Corazon) {
                contieneCorazon = true;
            }
        }
        for (int i = 0; i < 4; i++) {
            jugadoresEnOrden.get(i).getCartasMano().remove(cartasEnOrden.get(i));
        }
        if (contieneCorazon && !corazonRoto) {
            corazonRoto = true;
            notificarObservadores(Eventos.CORAZONES_ROTOS);
        }
        int ganadorIndice = 0;
        Carta mejor = cartasEnOrden.get(0);
        for (int i = 1; i < 4; i++) {
            Carta carta = cartasEnOrden.get(i);
            if (carta.getPalo() == paloInicial && carta.getId() > mejor.getId()) {
                mejor = carta;
                ganadorIndice = i;
            }
        }
        int indiceGanador = (indiceLider + ganadorIndice) % 4;
        Jugador ganador = jugadores.get(indiceGanador);
        ganador.sumarCartasGanadas(cartasEnOrden);
        indiceLider = indiceGanador;
        nroRondasJugadas++;
        primeraMano = false;
        notificarObservadores(Eventos.CAMBIO_DE_RONDA);
        return ganador;
    }
    private Carta obtenerCartaValidada(Jugador jugador, int indiceCarta) {
        if (indiceCarta < 0 || indiceCarta >= jugador.getCartasMano().size()) {
            throw new IllegalArgumentException("Índice de carta inválido para " + jugador.getNombre());
        }
        return jugador.getCartasMano().get(indiceCarta);
    }
    private void validarCartaInicial(Carta carta) {
        if (primeraMano && !(carta.getPalo() == Palo.Trebol && carta.getId() == 2)) {
            throw new IllegalArgumentException("En la primera jugada la carta inicial debe ser el 2 de Trebol");
        }
        if (carta.getPalo() == Palo.Corazon && !corazonRoto) {
            throw new IllegalArgumentException("No se puede iniciar una ronda con Corazones hasta que estén rotos");
        }
        if (primeraMano && carta.getPuntaje() > 0) {
            throw new IllegalArgumentException("En la primera ronda no se pueden jugar cartas con puntaje");
        }
    }
    private void validarCantidadDeCartas(List<Integer> indicesCartas) {
        if (indicesCartas.size() != 4)
            throw new IllegalArgumentException("Se requieren 4 cartas para la ronda");
    }
    @Override
    public void siguienteRonda() throws RemoteException {
        if (nroRondasJugadas < 13) {
            throw new IllegalStateException("No se han jugado las 13 rondas aun: Rondas jugadas = " + nroRondasJugadas);
        }
        //primero se revisa si alguien hizo el tiro a la luna.
        Jugador jugadorConTiroALaLuna = null;
        for(Jugador jugador : jugadores){
            int corazones = 0;
            boolean reinaDePicas=false;
            for(Carta carta : jugador.getCartasGanadas()){
                if(Palo.Corazon.equals(carta.getPalo())){
                    corazones++;
                }
                if (Palo.Pica.equals(carta.getPalo()) && carta.getId()== 12){
                    reinaDePicas = true;
                }
            }
            if (corazones == 13 && reinaDePicas){
                jugadorConTiroALaLuna = jugador;
                break;
            }
        }
        // si jugadorConTiroALaLuna es diferente a null le aplico 26 puntos al resto de jugadores
        if(jugadorConTiroALaLuna != null){
            for (Jugador jugador : jugadores){
                if(!jugador.equals(jugadorConTiroALaLuna)){
                    jugador.tiroALaLuna();
                }
            }
            notificarObservadores(Eventos.TIRO_A_LA_LUNA);
        } else{
            // sino sumo el puntaje normal
            for( Jugador jugador : jugadores){
                jugador.sumarPuntos();
            }
        }
        for(Jugador jugador : jugadores){
            jugador.getCartasGanadas().clear();
        }

        //ronda.sumarCartasDeLaRonda();
        corazonRoto = false;
        nroRondasJugadas = 0;
        primeraMano = true;
        numeroMano ++;
        notificarObservadores(Eventos.PUNTOS_ACTUALIZADOS);
        if (partidaTerminada()){
            notificarObservadores(Eventos.PARTIDA_FINALIZADA);
        }
    }
    public boolean partidaTerminada(){
            for (Jugador jugador : jugadores){
                if(jugador.getPuntos()>= 100){
                    return true;
                }
            }
        return false;
    }
    @Override
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
    @Override
    public void aplicarIntercambio(List<List<Carta>> cartasIntercambio) throws RemoteException{
        // Metodo de intercambio de cartas finalizado, se encarga de recibir una lista de lista de cartas(de cada jugador)
        // elimar las cartas de las manos de cada jugador y de repartirlas segun la ronda.
        // Ronda n1 hacia izquierda, n2 hacia derecha, n3 hacia enfrente. n4 no hay intercambio.
        Objects.requireNonNull(cartasIntercambio,"La lista de listas de cartas a intercambiar no puede ser nula");
        if(cartasIntercambio.size() != 4){
            throw new IllegalArgumentException("es necesario 4 listas de cartas para el intercambio");
        }
        int mod = numeroMano % 4;
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
    @Override
    public int getNumeroDeMano() throws RemoteException {
        return 0;
    }
    private boolean tienePaloEnMano(Jugador jugador,Palo palo){
        List<Carta> manoJugador = jugador.getCartasMano();
        if (manoJugador==null){
            return false;
        }
        for(Carta carta : manoJugador){
            if(carta.getPalo().equals(palo)){
                return true;
            }
        }
        return false;
    }
    @Override
    public int getIndiceLider() throws RemoteException{
        return indiceLider;
    }
}
