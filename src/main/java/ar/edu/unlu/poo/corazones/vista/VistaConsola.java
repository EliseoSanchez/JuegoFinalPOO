package ar.edu.unlu.poo.corazones.vista;

import ar.edu.unlu.poo.corazones.modelo.Jugador;

import java.util.List;
import java.util.Scanner;

public class VistaConsola {

    private Scanner sc = new Scanner(System.in);

    public void menuPrincipal(){
        System.out.println("\n ---Corazones---");
        System.out.println("1. Agregar Jugadores");
        System.out.println("2. Ver lista de Jugadores");
        System.out.println("3. Iniciar Partida");
        System.out.println("0. Salir");
        System.out.println("Seleccione una opcion");
        System.out.println("\n ---Corazones---");
    }
    public int obtenerOpcionINT(){
        return sc.nextInt();
    }
    public String obtenerOpcionSTR(){
        return sc.next();
    }
    public void mostrarMensaje(String mensaje){
        System.out.println(mensaje);
    }
    public void menuJugadores(){
        System.out.println("\n ---Corazones---");
        System.out.println("ingrese el nombre de cada jugador");
    }
    public void mostrarJugadores(List<Jugador> jugadores){
        System.out.println("---Lista de jugadores---");
        for (Jugador jugadoresOBJ : jugadores){
            System.out.printf("%s\n",jugadoresOBJ.getNombre());
        }
    }
}

