package ar.edu.unlu.poo.corazones.modelo;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private List<Carta> cartasMano = new ArrayList<>();
    private List<Carta> cartasGanadas = new ArrayList<>();
    private String nombre;
    private int puntos;

    public Jugador(String nombre){
        this.nombre= nombre;
    }
    public Carta jugarCarta(int indice){
        Carta carta = cartasMano.get(indice);
        cartasMano.remove(indice);
        return carta;
    }

    public void recibirCarta(Carta carta) {
        cartasMano.add(carta);
    }
    public void sumarCartasGanadas(List<Carta> cartas){
        cartasGanadas.addAll(cartas);
    }

    public Carta intercambiarCarta(int indice){
        Carta carta = cartasMano.get(indice);
        cartasMano.remove(indice);
        return carta;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getCartasMano() {
        return cartasMano;
    }
    public void mostrarCartas(){
        for(Carta cartas : cartasMano){
            System.out.println(cartas.toString());
        }
    }
    public int contarPuntos(){
        puntos =0;
        for (Carta carta : cartasGanadas){
            puntos += carta.getPuntaje();
        }
        return puntos;
    }
    public boolean dosTrebol() {
        for (Carta carta: cartasMano){
            if(carta.getPalo().equals(Palo.Trebol) && carta.getId()==2){
                return true;
            }
        }
        return false;
    }
}
