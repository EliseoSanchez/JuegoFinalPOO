package ar.edu.unlu.poo.corazones;
import ar.edu.unlu.poo.corazones.controlador.ControladorConsola;
import ar.edu.unlu.poo.corazones.modelo.*;
import ar.edu.unlu.poo.corazones.vista.VistaConsola;

public class App {
    public static void main(String[] args) {
        Corazones modelo = new Corazones();
        VistaConsola vistaConsola = new VistaConsola();
        ControladorConsola controladorConsola = new ControladorConsola(modelo,vistaConsola);
        controladorConsola.iniciar();
    }
}
