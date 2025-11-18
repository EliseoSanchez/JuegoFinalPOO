package ar.edu.unlu.poo.corazones.controlador;

import ar.edu.unlu.poo.corazones.modelo.Corazones;
import ar.edu.unlu.poo.corazones.modelo.Jugador;
import ar.edu.unlu.poo.corazones.vista.VistaConsola;

import java.rmi.RemoteException;

public class ControladorConsola {
    private Corazones modelo;
    private VistaConsola vista;
    /*
    public ControladorConsola(VistaConsola vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.modelo.agregarObservador(this);
    }

    public void iniciar() {
        vista.menuPrincipal();
        while (true) {
            int opcion = vista.obtenerOpcionINT();
            switch (opcion) {
                case 1:
                    vista.menuJugadores();
                    nuevoJugador();
                    vista.menuPrincipal();
                    break;
                case 2:
                    vista.mostrarJugadores(modelo.getJugadores());
                    vista.menuPrincipal();
                    break;
                case 3:

                case 0:
                    vista.mostrarMensaje("Cerrando menu . . .");
                    return;
                default:
                    vista.mostrarMensaje("Opcion no valida");
                    break;
            }
        }
    }

    private void nuevoJugador() throws RemoteException {
        for (int i = 0; i < 4; i++) {
            System.out.printf("Ingresando el jugador n°%d", i + 1);
            String nombre = vista.obtenerOpcionSTR();
            Jugador nuevojugador = new Jugador(nombre);
            //modelo.agregarJugador(nuevojugador);
        }
        return;
    }*/
}

