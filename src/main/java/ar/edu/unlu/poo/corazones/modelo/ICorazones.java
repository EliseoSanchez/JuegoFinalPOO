package ar.edu.unlu.poo.corazones.modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface ICorazones extends IObservableRemoto {

    // Agregar un jugador a la partida
    void agregarJugador(Jugador jugador) throws RemoteException;

    // Obtiene la lista de jugadores
    List<Jugador> getJugadores() throws RemoteException;

    //inicia la partida
    void iniciarPartida() throws RemoteException;

    // Jugar una ronda pasando las cartas en orden
    Jugador jugarRonda(List<Carta> cartasEnOrden) throws RemoteException;

    // Avanzar a la siguiente ronda y actualizar puntajes
    void siguienteRonda() throws RemoteException;

    // Obtener el ganador de la partida (el que tenga menos puntos)
    Jugador obtenerGanador() throws RemoteException;

    // Aplicar el intercambio de cartas según la ronda
    void aplicarIntercambio(List<List<Carta>> cartasIntercambio) throws RemoteException;

    // Obtener el número de la ronda
    int getNumeroDeMano() throws RemoteException;

    // obtiene el indice del lider, muy importante para que el controlador pida las cartas en orden
    int getIndiceLider () throws RemoteException;
}
