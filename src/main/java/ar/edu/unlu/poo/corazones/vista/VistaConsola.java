package ar.edu.unlu.poo.corazones.vista;

import ar.edu.unlu.poo.corazones.controlador.ControladorConsola;
import ar.edu.unlu.poo.corazones.modelo.Jugador;
import ar.edu.unlu.poo.corazones.modelo.Carta;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {
    private ControladorConsola controlador;
    private final Scanner sc = new Scanner(System.in);

    public int menuPrincipal(){
        System.out.println("\n ---Corazones---");
        System.out.println("1. Agregar Jugador");
        System.out.println("2. Ver lista de Jugadores");
        System.out.println("3. Iniciar Partida");
        System.out.println("0. Salir");
        System.out.println("------------------");
        int opcion = obtenerOpcionINT("opcion:");
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
        System.out.println("Mano de " + jugador.getNombre() + ":");
        List<Carta> manoDelJugador = jugador.getCartasMano();
        for (int i = 0; i < manoDelJugador.size(); i++) {
            System.out.printf("[%d] %s\n", i, manoDelJugador.get(i).toString());
        }
    }
    public void mostrarJugadores(List<Jugador> jugadores){
        System.out.println("---Lista de jugadores---");
        for (int i = 0; i < jugadores.size(); i++) {
            System.out.printf("%d %s (puntos: %d)%n", i + 1, jugadores.get(i).getNombre(), jugadores.get(i).getPuntos());
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
    public void mostrarPuntos(List<Jugador> jugadores){
        System.out.println("Puntajes actuales:");
        for (Jugador jugador : jugadores){
            System.out.printf("%s : %d%n", jugador.getNombre(), jugador.getPuntos());
        }
    }
}

