package ar.edu.unlu.poo.corazones.modelo;
import ar.edu.unlu.poo.corazones.observer.Observable;
import ar.edu.unlu.poo.corazones.observer.Observador;

import java.util.List;

public class Principal implements Observable {
    private List<Observador> observadores;


    @Override
    public void agregarObserver(Observador observador) {
        observadores.add(observador);
    }

    @Override
    public void eliminarObserver(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObserver() {
        for (Observador observador : observadores){
            observador.actualizar();
        }
    }
}
