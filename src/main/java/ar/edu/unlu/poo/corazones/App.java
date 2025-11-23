package ar.edu.unlu.poo.corazones;
import ar.edu.unlu.poo.corazones.controlador.ControladorConsola;
import ar.edu.unlu.poo.corazones.modelo.*;
import ar.edu.unlu.poo.corazones.vista.VistaConsola;
public class App {
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola();
        ControladorConsola controlador = new ControladorConsola(vista);
        Corazones modelo = new Corazones();
        try {
            controlador.setModeloRemoto(modelo);
        } catch (Exception e) {
            System.out.println("Error al conectar el modelo: " + e.getMessage());
            return;
        }
        boolean salir = false;
        while (!salir) {
            int opcion = vista.menuPrincipal();
            switch (opcion) {
                case 1 -> {
                    String nombre = vista.obtenerSiguienteLinea("Nombre del jugador:");
                    controlador.agregarJugador(nombre);
                }
                case 2 -> {
                    try {
                        vista.mostrarJugadores(modelo.getJugadores());
                    } catch (Exception e) {
                        vista.mostrarMensaje("Error: " + e.getMessage());
                    }
                }
                case 3 -> {
                    controlador.iniciarPartida();
                }
                case 4 ->{
                    vista.mostrarReglas();
                }
                case 0 -> {
                    vista.mostrarMensaje("Saliendo del juego...");
                    salir = true;
                }
                default -> vista.mostrarMensaje("Opción inválida.");
            }
        }
    }
}
