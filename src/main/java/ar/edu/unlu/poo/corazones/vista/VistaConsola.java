package ar.edu.unlu.poo.corazones.vista;

import ar.edu.unlu.poo.corazones.modelo.Jugador;
import ar.edu.unlu.poo.corazones.modelo.Carta;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {

    private final Scanner sc = new Scanner(System.in);

    public int menuPrincipal(){
        System.out.println("\n ---Corazones---");
        System.out.println("1. Agregar Jugadores");
        System.out.println("2. Ver lista de Jugadores");
        System.out.println("3. Iniciar Partida");
        System.out.println("0. Salir");
        System.out.println("------------------");
        return obtenerOpcionINT();
    }
    public int obtenerOpcionINT(String mensaje){
        while (true){
            try{
                System.out.print(mensaje + " ");
                String siguienteLinea = sc.nextLine();
                return Integer.parseInt(siguienteLinea);
            }catch (Exception e){
                System.out.println("Entrada inválida, intente de nuevo.");
            }
        }
    }
    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }
    public void mostrarMano(Jugador jugador){
        System.out.println("Mano de " + jugador.getNombre() + ":");
        List<Carta> manoDelJugador = jugador.getCartasGanadas();
        for (int i = 0; i < manoDelJugador.size(); i++) {
            System.out.printf("[%d] %s\n", i, manoDelJugador.get(i).toString());
        }
    }
    public void mostrarJugadores(List<Jugador> jugadores){
        System.out.println("---Lista de jugadores---");
        for (int i = 0; i < 4; i++) {
            System.out.printf("%d %s (puntos: %d)%n", i + 1, jugadores.get(i).getNombre(), jugadores.get(i).getPuntos());
        }
    }
}

