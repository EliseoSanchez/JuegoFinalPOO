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
    public List<Jugador> getJugadores(){
        return new ArrayList<>(jugadores);
    }
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
    public Jugador jugarRonda(List<Carta> cartasEnOrden) throws RemoteException {
        Objects.requireNonNull(cartasEnOrden, "cartasEnOrden no puede ser nulas");
        if (cartasEnOrden.size() != 4) {
            throw new IllegalArgumentException("Se requieren 4 cartas para la ronda");
        }

        Palo paloInicial = null;
        boolean contieneCorazon = false;

        // validar y remover cartas de cada jugador
        for (int i = 0; i < 4; i++) {
            int indiceJugador = (indiceLider + i + 4) % 4;
            Jugador jugador = jugadores.get(indiceJugador);
            Carta jugada = cartasEnOrden.get(i);
            Objects.requireNonNull(jugada, "Carta jugada no puede ser nula");

            List<Carta> manoVisible = jugador.getCartasMano();
            if (!manoVisible.contains(jugada)) {
                throw new IllegalArgumentException("El jugador no tiene la carta jugada en su mano: " + jugada);
            }
            if (i == 0) {
                paloInicial = jugada.getPalo();
                if (primeraMano && !Palo.Trebol.equals(paloInicial)) {
                    throw new IllegalArgumentException("En la primera baza la carta inicial debe ser del palo Trebol");
                }
                if (Palo.Corazon.equals(paloInicial) && !corazonRoto) {
                    throw new IllegalArgumentException("No se puede iniciar una baza con Corazones hasta que estén rotos");
                }
                if (primeraMano && jugada.getPuntaje() > 0) {
                    throw new IllegalArgumentException("En la primera baza no se pueden jugar cartas con puntaje");
                }
            } else {
                boolean tienePalo = tienePaloEnMano(jugador, paloInicial);
                if (tienePalo && !paloInicial.equals(jugada.getPalo())) {
                    throw new IllegalArgumentException("El jugador debe seguir el palo si puede");
                }
                if (primeraMano && jugada.getPuntaje() > 0) {
                    throw new IllegalArgumentException("En la primera baza no se pueden jugar cartas con puntaje");
                }
            }

            if (Palo.Corazon.equals(jugada.getPalo())) {
                contieneCorazon = true;
            }

            int indice = manoVisible.indexOf(jugada);
            if (indice < 0) {
                throw new IllegalStateException("No se pudo localizar la carta en la mano del jugador: " + jugada);
            }
            jugador.jugarCarta(indice);
        }
        // marcar corazones rotos
        if (contieneCorazon && !corazonRoto) {
            corazonRoto = true;
            notificarObservadores(Eventos.CORAZONES_ROTOS);
        }
        // determinar ganador de la ronda
        int ganadorIndice = 0;
        Carta mejor = cartasEnOrden.get(0);
        for (int i = 1; i < 4; i++) {
            Carta carta = cartasEnOrden.get(i);
            if (carta.getPalo().equals(mejor.getPalo()) && carta.getId() > mejor.getId()) {
                mejor = carta;
                ganadorIndice = i;
            }
        }
        int indiceGanador = (indiceLider + ganadorIndice + 4) % 4;
        Jugador ganador = jugadores.get(indiceGanador);
        // asignar las 4 cartas jugadas al ganador
        List<Carta> baza = new ArrayList<>(cartasEnOrden);
        ganador.sumarCartasGanadas(baza);
        // actualizar estado
        indiceLider = indiceGanador;
        nroRondasJugadas++;
        primeraMano = false;
        notificarObservadores(Eventos.CAMBIO_DE_RONDA);
        return ganador;
    }
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
}
