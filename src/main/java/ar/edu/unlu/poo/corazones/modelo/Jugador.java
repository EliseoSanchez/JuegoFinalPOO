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
        if (this.nombre.isEmpty()){
            throw new IllegalArgumentException("Nombre no puede estar vacio");
        }
    }
    public Carta jugarCarta(int indice){
        validarIndiceMano(indice);
        return cartasMano.remove(indice);
    }
    private void validarIndiceMano(int indice){
        if(indice < 0 || indice >= cartasMano.size()){
            throw new IndexOutOfBoundsException("indice invalido: " + indice + " (Tamaño de la mano:" + cartasMano.size() + " ).");
        }
    }
    public void recibirCarta(Carta carta){
        Objects.requireNonNull(carta,"La carta no puede ser nula");
        cartasMano.add(carta);
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
    public List<Carta> getCartasGanadas() {
        return cartasGanadas;
    }

    public void mostrarCartas(){
        for(Carta cartas : cartasMano){
            System.out.println(cartas.toString());
        }
    }
    public int contarPuntos(){
        int total =0;
        for (Carta carta : cartasGanadas){
            if(carta != null){
                puntos += carta.getPuntaje();
            }
        }
        puntos=total;
        return puntos;
    }
    public int getPuntos(){
        return puntos;
    }
    public boolean dosTrebol() {
        for (Carta carta: cartasMano){
            if(carta.getPalo().equals(Palo.Trebol) && carta.getId()==2 && carta!=null){
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
