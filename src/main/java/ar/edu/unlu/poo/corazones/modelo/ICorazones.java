package ar.edu.unlu.poo.corazones.modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface ICorazones extends IObservableRemoto {
    void agregarJugador(Jugador jugador) throws RemoteException;
    List<Jugador> getJugadores() throws RemoteException;
}
