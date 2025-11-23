package ar.edu.unlu.poo.corazones.controlador;

import ar.edu.unlu.poo.corazones.modelo.*;
import ar.edu.unlu.poo.corazones.vista.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class ControladorConsola implements IControladorRemoto {
    private ICorazones modelo;
    private VistaConsola vista;
    private final List<List<Carta>> cartasParaIntercambiar = new ArrayList<>();

    public ControladorConsola(VistaConsola vista){
        this.vista = vista;
        this.vista.setControlador(this);
    }
    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modelo) throws RemoteException {
        this.modelo = (ICorazones) modelo;
        this.modelo.agregarObservador(this);
    }
    private boolean hayRondasPorJugar() throws RemoteException {
        List<Jugador> jugadores = modelo.getJugadores();
        if (jugadores == null || jugadores.isEmpty()) return false;
        for (Jugador jugador : jugadores) {
            if (!jugador.getCartasMano().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    private void jugarManoCompleta() throws RemoteException {
        while (hayRondasPorJugar()) {
            jugarRonda();
        }
    }
    private void solicitarIntercambioInicial() throws RemoteException {
        List<Jugador> jugadores = modelo.getJugadores();
        cartasParaIntercambiar.clear();
        vista.mostrarMensaje("\n--- Intercambio inicial de cartas (3 cartas por jugador) ---");
        for (Jugador jugador : jugadores) {
            int[] indices = vista.pedirTresCartas(jugador);
            List<Carta> cartas = new ArrayList<>();
            for (int indice : indices) {
                cartas.add(jugador.getCartasMano().get(indice));
            }
            cartasParaIntercambiar.add(cartas);
        }
        try {
            modelo.aplicarIntercambio(cartasParaIntercambiar);
        } catch (Exception e) {
            vista.mostrarMensaje("Error en intercambio: " + e.getMessage());
        }
    }
    private void jugarRonda() throws RemoteException {
        List<Jugador> jugadores = modelo.getJugadores();
        List<Integer> indicesCartasJugadas = new ArrayList<>();
        vista.mostrarMensaje("\n--- Nueva Ronda ---");
        int lider = modelo.getIndiceLider();
        for (int i = 0; i < 4; i++) {
            int indiceJugador = (lider + i)%4;
            Jugador jugador = jugadores.get(indiceJugador);
            try {
                int indiceCarta = vista.pedirCarta(jugador);
                indicesCartasJugadas.add(indiceCarta);
            } catch (Exception e) {
                vista.mostrarMensaje("Error al pedir carta: " + e.getMessage());
                return;
            }
        }
        try {
            modelo.jugarRonda(indicesCartasJugadas);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al jugar ronda: " + e.getMessage());
        }
    }
    public void agregarJugador(String nombre) {
        try {
            Jugador jugador = new Jugador(nombre);
            modelo.agregarJugador(jugador);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al agregar jugador: " + e.getMessage());
        }
    }
    public void iniciarPartida() {
        try {
            modelo.iniciarPartida();
        } catch (Exception e) {
            vista.mostrarMensaje(" No se pudo iniciar la partida: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object evento) throws RemoteException {
        Eventos eventoModelo = (Eventos) evento;
        if(evento == null){
            vista.mostrarMensaje("Evento desconocido");
            return;
        }
        switch (eventoModelo){
            case NUEVO_JUGADOR -> {
                vista.mostrarMensaje("NUEVO JUGADOR");
                vista.mostrarJugadores(modelo.getJugadores());
            }
            case MANO_INICIADA -> {
                vista.mostrarMensaje("La partida comenzó, los jugadores tienen sus cartas");
                vista.mostrarJugadores(modelo.getJugadores());
                solicitarIntercambioInicial();
            }
            case INTERCAMBIO_REALIZADO -> {
                vista.mostrarMensaje("Intercambio Realizado");
                jugarManoCompleta();
            }
            case CORAZONES_ROTOS -> {
                vista.mostrarMensaje("Corazones rotos!");
            }
            case CAMBIO_DE_RONDA -> {
                vista.mostrarMensaje("Cambio de ronda");
                if(!hayRondasPorJugar()){
                    modelo.siguienteRonda();
                }
            }
            case TIRO_A_LA_LUNA -> {
                vista.mostrarMensaje("Un jugador logro el Tiro a la luna");
                vista.mostrarMensaje("Se le agregaran 26 puntos a cada jugador");
            }
            case PUNTOS_ACTUALIZADOS -> {
                vista.mostrarMensaje("Puntos actualizados");
                vista.mostrarPuntos(modelo.getJugadores());
            }
            case PARTIDA_FINALIZADA -> {
                Jugador ganador = modelo.obtenerGanador();
                vista.mostrarMensaje("\n🏆 ¡La partida ha terminado! 🏆");
                vista.mostrarMensaje("Ganador: " + ganador.getNombre() +
                        " con " + ganador.getPuntos() + " puntos.");
            }
        }
    }
}