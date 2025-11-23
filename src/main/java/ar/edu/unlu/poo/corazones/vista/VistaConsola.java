package ar.edu.unlu.poo.corazones.vista;

import ar.edu.unlu.poo.corazones.controlador.ControladorConsola;
import ar.edu.unlu.poo.corazones.modelo.Jugador;
import ar.edu.unlu.poo.corazones.modelo.Carta;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {
    private ControladorConsola controlador;
    private final Scanner sc = new Scanner(System.in);

    private final String RESET = "\u001B[0m";
    private final String YELLOW = "\u001B[33m";
    private final String CYAN = "\u001B[36m";
    private final String GREEN = "\u001B[32m";
    private final String PURPLE = "\u001B[35m";
    private final String BRIGHT_GREEN   = "\u001B[92m";
    private final String BRIGHT_RED = "\u001B[91m";

    public int menuPrincipal(){
        System.out.println(CYAN +"\n ---Corazones---");
        System.out.println("1. Agregar Jugador");
        System.out.println("2. Ver lista de Jugadores");
        System.out.println("3. Iniciar Partida");
        System.out.println("4. Reglas del Juego");
        System.out.println("0. Salir"+RESET);
        System.out.println("------------------");
        int opcion = obtenerOpcionINT(YELLOW +"opcion:" + RESET);
        return opcion;
    }
    public void setControlador(ControladorConsola controlador) {
        this.controlador = controlador;
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
    public String obtenerSiguienteLinea(String mensaje){
        System.out.print(mensaje + " ");
                return sc.nextLine();

    }
    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }
    public void mostrarMano(Jugador jugador){
        System.out.println(PURPLE +"Mano de " + jugador.getNombre() + ":"+ RESET);
        List<Carta> manoDelJugador = jugador.getCartasMano();
        for (int i = 0; i < manoDelJugador.size(); i++) {
            System.out.printf(GREEN +"[%d]" + BRIGHT_GREEN + "%s\n"+ RESET, i, manoDelJugador.get(i).toString());
        }
    }
    public void mostrarJugadores(List<Jugador> jugadores){
        System.out.println(YELLOW + "---Lista de jugadores---" + RESET);
        for (int i = 0; i < jugadores.size(); i++) {
            System.out.printf(YELLOW + "%d %s" + BRIGHT_RED + "(puntos: %d)%n"+ RESET, i + 1, jugadores.get(i).getNombre(), jugadores.get(i).getPuntos());
        }
    }
    public int[] pedirTresCartas(Jugador jugador){
        mostrarMano(jugador);
        int[] indices = new int[3];
        for (int i = 0; i < 3; i++) {
            while (true){
                int indicemano = obtenerOpcionINT(String.format("Jugador %s: índice de la carta a pasar (%d/3):", jugador.getNombre(), i + 1));
                if(indicemano < 0 ||  indicemano >= jugador.getCartasMano().size()){
                    System.out.println("indice fuera del rango");
                    continue;// el continue hace que vuelva al inicio del while
                }
                boolean duplicado = false;
                for (int j = 0; j < i; j++) {
                    if(indices[j] == indicemano){
                        duplicado = true;
                    }
                }
                if (duplicado){
                    System.out.println("Ya seleccionó ese índice, elija otro.");
                    continue;
                }
                indices[i] = indicemano;
                break;
            }
        }
        return indices;
    }
    public int pedirCarta(Jugador jugador){
        mostrarMano(jugador);
        while (true){
            int indice = obtenerOpcionINT("Ingrese el índice de la carta a jugar:");
            if(indice < 0 || indice >= jugador.getCartasMano().size()){
                System.out.println("Índice inválido. Intente de nuevo.");
                continue;
            }
            return indice;
        }
    }
    public void mostrarReglas() {
        System.out.println(CYAN + "\nReglas de Corazones" + RESET);
        System.out.println(YELLOW + "Cada jugador recibe 13 cartas y pasa tres al inicio de la mano. Comienza quien tenga el 2 de tréboles. Se debe seguir el palo si es posible, los corazones no pueden jugarse hasta que se rompan." + RESET);
        System.out.println(YELLOW + "Cada corazón vale 1 punto y la reina de picas vale 13. El objetivo es evitar puntos. El juego termina cuando alguien llega a 100, gana quien tenga menos." + RESET);
        System.out.println(YELLOW + "Si un jugador consigue todas las cartas de corazón y la reina de picas, los demás reciben 26 puntos (\"Tiro a la luna\")." + RESET);
    }
    public void mostrarCartasJugadas(List<Jugador> jugadores, List<Carta> cartas){
        System.out.println();
        System.out.println("Cartas jugadas en esta ronda:");
        for (int i = 0; i < cartas.size(); i++) {
            Jugador jugador = jugadores.get(i);
            Carta carta = cartas.get(i);
            System.out.printf(PURPLE +"%s\n" + BRIGHT_GREEN + " %s\n"+ RESET, jugador.getNombre(),carta.toString());
        }
        System.out.println();
    }
    public void mostrarCartasGanadas(Jugador jugador, List<Carta> cartas){
        System.out.println(PURPLE + "\nRonda ganada por: " + jugador.getNombre() + RESET);
        System.out.println(PURPLE + "Cartas ganadas:" + RESET);
        for(Carta carta : cartas){
            System.out.printf( BRIGHT_GREEN + "\n  %s  "+ RESET, carta.toString());
        }
        System.out.println();
    }
    public void mostrarPuntos(List<Jugador> jugadores){
        System.out.println("Puntajes actuales:");
        for (Jugador jugador : jugadores){
            System.out.printf("%s : %d%n", jugador.getNombre(), jugador.getPuntos());
        }
    }
}

