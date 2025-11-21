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
    public Carta jugarCarta(int indice){
        validarIndiceMano(indice);
        return cartasMano.remove(indice);
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
        for (Carta carta : cartas){
            if(carta != null){
                puntos+= carta.getPuntaje();
            }
        }
    }
    public void tiroALaLuna(){
        puntos+=26;
    }
    private void limpiarCartasGanadas(){
        cartasGanadas.clear();
    }
    private void limpiarCartasMano(){
        cartasMano.clear();
    }
    public Carta intercambiarCarta(int indice){
        validarIndiceMano(indice);
        return cartasMano.remove(indice);
    }

    public String getNombre() {
        return nombre;
    }
    public int manoSize(){
        return cartasMano.size();
    }
    public List<Carta> getCartasMano() {
        return cartasMano;
    }
    public boolean eliminarCarta(Carta carta){
        return cartasMano.remove(carta);
    }
    public List<Carta> getCartasGanadas() {
        return cartasGanadas;
    }

    public void mostrarCartas(){
        for(Carta cartas : cartasMano){
            System.out.println(cartas.toString());
        }
    }
    public void sumarPuntos(){
        int total =0;
        for (Carta carta : cartasGanadas){
            if(carta != null){
                puntos += carta.getPuntaje();
            }
        }
    }
    public int getPuntos(){
        return puntos;
    }
    //buscamos el 2 de trébol, inicia el jugador con esa carta.
    public boolean dosTrebol() {
        for (Carta carta: cartasMano){
            if(carta!= null && carta.getPalo().equals(Palo.Trebol) && carta.getId()==2){
                return true;
            }
        }
        return false;
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
