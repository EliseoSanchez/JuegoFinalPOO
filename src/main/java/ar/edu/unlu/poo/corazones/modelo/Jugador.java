package ar.edu.unlu.poo.corazones.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Jugador {
    private final List<Carta> cartasMano = new ArrayList<>();
    private final List<Carta> cartasGanadas = new ArrayList<>();
    private final String nombre;
    private int puntos = 0;

    public Jugador(String nombre){
        this.nombre= Objects.requireNonNull(nombre,"Nombre no puede ser nulo").trim();
        //Objects.requireNonNull = valída que el obj no sea nulo, luego le quita espacios adelante y al final al string.
        if (this.nombre.isEmpty()){
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
    }
    // muy importante validar siempre el indíce.
    private void validarIndiceMano(int indice){
        if(indice < 0 || indice >= cartasMano.size()){
            throw new IndexOutOfBoundsException("indice invalido: " + indice + " (Tamaño de la mano:" + cartasMano.size() + " ).");
        }
    }
    public void recibirCarta(Carta carta){
        Objects.requireNonNull(carta,"La carta no puede ser nula");
        cartasMano.add(carta);
    }
    public void recibirCarta(List<Carta> cartas){
        Objects.requireNonNull(cartas,"Las cartas no pueden ser nulas");
        cartasMano.addAll(cartas);
    }
    public void sumarCartasGanadas(List<Carta> cartas){
        if (cartas == null || cartas.isEmpty()) {return;}
        cartasGanadas.addAll(cartas);

    }
    public void tiroALaLuna(){
        puntos+=26;
    }
    public String getNombre() {
        return nombre;
    }
    public List<Carta> getCartasMano() {
        return cartasMano;
    }
    public List<Carta> getCartasGanadas() {
        return cartasGanadas;
    }
    public void sumarPuntos(){
        int total =0;
        for (Carta carta : cartasGanadas){
            if(carta != null){
                total += carta.getPuntaje();
            }
        }
        puntos+=total;
    }
    public int getPuntos(){
        return puntos;
    }
    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", puntos=" + puntos +
                ", cartasMano=" + cartasMano.size() +
                ", cartasGanadas=" + cartasGanadas.size() +
                '}';
    }
}
