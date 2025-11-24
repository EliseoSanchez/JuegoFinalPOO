package ar.edu.unlu.poo.corazones.controlador;

import ar.edu.unlu.poo.corazones.modelo.*;
import ar.edu.unlu.poo.corazones.vista.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;

public class ControladorConsola implements IControladorRemoto {
    private ICorazones modelo;
    private final VistaConsola vista;
    private final List<List<Carta>> cartasParaIntercambiar = new ArrayList<>();
    private final String RESET = "\u001B[0m";
    private final String BRIGHT_RED = "\u001B[91m";
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
        List<Jugador> jugadoresParaVista = new ArrayList<>();
        List<Carta> cartasParaVista = new ArrayList<>();
        vista.mostrarMensaje(BRIGHT_RED + "\n--- Nueva Ronda ---" + RESET);
        int lider = modelo.getIndiceLider();
        for (int i = 0; i < 4; i++) {
            int indiceJugador = (lider + i)%4;
            Jugador jugador = jugadores.get(indiceJugador);
            try {
                int indiceCarta = vista.pedirCarta(jugador);
                indicesCartasJugadas.add(indiceCarta);
                jugadoresParaVista.add(jugador);
                cartasParaVista.add(jugador.getCartasMano().get(indiceCarta));
                if(i< 3) {
                    vista.mostrarCartasJugadas(jugadoresParaVista,cartasParaVista);
                }
            } catch (Exception e) {
                vista.mostrarMensaje("Error al pedir carta: " + e.getMessage());
                return;
            }
        }
        try {
            Jugador ganador = modelo.jugarRonda(indicesCartasJugadas);
            vista.mostrarCartasGanadas(ganador,cartasParaVista);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al jugar ronda: " + e.getMessage());
        }
    }
    public void agregarJugador(String nombre) throws RemoteException {
        boolean repetido = true;
        List<Jugador> jugadores = modelo.getJugadores();

        while (repetido) {
            repetido = false; // asumimos que NO está repetido
            for (Jugador jugador : jugadores) {
                if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                    repetido = true;
                    vista.mostrarMensaje("El nombre '" + nombre + "' ya existe. Ingrese otro nombre:");
                    nombre = vista.obtenerSiguienteLinea("Nombre del jugador:");
                    break;
                    }
                }
            }
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
                vista.mostrarMensaje(BRIGHT_RED +"NUEVO JUGADOR"+ RESET);
                vista.mostrarJugadores(modelo.getJugadores());
            }
            case MANO_INICIADA -> {
                vista.mostrarMensaje(BRIGHT_RED + "La partida comenzó, los jugadores tienen sus cartas"+RESET);
                vista.mostrarJugadores(modelo.getJugadores());
                solicitarIntercambioInicial();
            }
            case INTERCAMBIO_REALIZADO -> {
                vista.mostrarMensaje(BRIGHT_RED + "Intercambio Realizado" + RESET);
                jugarManoCompleta();
            }
            case CORAZONES_ROTOS -> {
                vista.mostrarMensaje(BRIGHT_RED + "Corazones rotos!"+ RESET);
            }
            case CAMBIO_DE_RONDA -> {
                System.out.println();
                vista.mostrarMensaje(BRIGHT_RED + "Cambio de ronda" + RESET);
                if(!hayRondasPorJugar()){
                    modelo.siguienteRonda();
                }
            }
            case TIRO_A_LA_LUNA -> {
                vista.mostrarMensaje(BRIGHT_RED + "Un jugador logro el Tiro a la luna"+ RESET);
                vista.mostrarMensaje("Se le agregaran 26 puntos a cada jugador");
            }
            case PUNTOS_ACTUALIZADOS -> {
                vista.mostrarMensaje(BRIGHT_RED + "Puntos actualizados" + RESET);
                vista.mostrarPuntos(modelo.getJugadores());
            }
            case PARTIDA_FINALIZADA -> {
                Jugador ganador = modelo.obtenerGanador();
                vista.mostrarMensaje(BRIGHT_RED + "\n🏆 ¡La partida ha terminado! 🏆" + RESET);
                vista.mostrarMensaje("Ganador: " + ganador.getNombre() +
                        " con " + ganador.getPuntos() + " puntos.");
            }
        }
    }
}