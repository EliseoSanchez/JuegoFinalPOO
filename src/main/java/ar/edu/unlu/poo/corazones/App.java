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


/*Tenemos un juego de cartas con naipes el cual se llama corazones, este juego se juega de a 4 jugadores. se reparten 13
cartas a cada jugador, el jugador que obtiene el 2 de trebol es el que tiene que arrancar a jugar colocando la carta a la
vista y el siguiente jugador es el de la izquierda siguiendo con el mismo "palo", en caso de no tener puede bajar otra carta,
asi hasta que los 4 hayan bajado una carta cada uno. El que tira la carta mas alta es el que se lleva as 4 cartas. las cartas
de corazones valen 1 punto cada una y la reina de pica vale 13 puntos, en la primera mano no se puede tirar ninguna carta con puntaje.
la siguiente ronda empieza con el que haya levantado todas las cartas y se juega hasta que alguien consiga 100 puntos.
No se puede arrancar una ronda con corazones hasta que alguien haya levantado una carta de corazones.
gana el que termina con menor puntaje y dato muy importante, al principio de cada ronda se pueden intercambiar 3 cartas,
en la ronda 1 se intercambia al de la izquierda, en la ronda 2 al de la derecha, en la ronda 3 al de enfrente y en la 4
no se puede, asi de manera ciclica. si alguien levanta todas las cartas de corazones y la reina de pica se lleva 0 puntos
y el resto 26 cada uno. Diagrama el sistema de clases a usar, con herencia, polimorfismo, clases abstactas, interfaz de
consola y grafica, utilizacion de MVC y observer. en lenguaje Java, trata de no dar tanto detalle del codigo.


        +------------------+
        |      <<enum>>    |
        |      Palo        |
        +------------------+
        | - TREBOL         |
        | - DIAMANTE       |
        | - CORAZON        |
        | - PICA           |
        +------------------+

        ▲
        |
        +-------------------------+
        | <<abstract>> Carta      |
        +-------------------------+
        | - palo: Palo            |
        | - valor: int            |
        +-------------------------+
        | + getPuntaje(): int     |
        +-------------------------+



        +-------------------+                    +-------------------+
        | <<interface>>     |                    | <<interface>>     |
        |   Observer        |                    |   Observable      |
        +-------------------+                    +-------------------+
        | + actualizar()    |                    | + agregarObs()    |
        |                   |                    | + eliminarObs()   |
        |                   |                    | + notificarObs()  |
        +-------------------+                    +-------------------+

        ▲                                        ▲
        |                                        |
        +---------------------+                  +-----------------------+
        |   VistaConsola      |                  |        Juego          |
        +---------------------+                  +-----------------------+
        | + mostrarEstado()   |                  | - jugadores           |
        | + mostrarMano()     |                  | - mazo                |
        | + solicitarEntrada()|
        +---------------------+                  | - numeroRonda         |
        ▲                                        | - puntajes            |
        |                                        +-----------------------+
        +---------------------+                  | + iniciarJuego()      |
        |   VistaGrafica      |                  | + jugarRonda()        |
        +---------------------+                  | + manejarIntercambio()|
        +-----------------------+
        ▲
        |
        +--------------+
        |    Ronda     |
        +--------------+
        | - jugadores  |
        | - numero     |
        +--------------+
        | + jugarRonda()|
        +--------------+

        +-------------------------+          +----------------------+
        | <<abstract>> Jugador    |          |        Mazo          |
        +-------------------------+          +----------------------+
        | - mano: List<Carta>     |          | - cartas: List<Carta>|
        | - cartasGanadas         |          +----------------------+
        | - nombre: String        |          | + barajar()          |
        +-------------------------+          | + repartir()         |
        | + jugarCarta()          |          +----------------------+
        | + seleccionarCartas()   |
        +-------------------------+
        ▲           ▲
        |           |
        +----------------+   +----------------+
        | JugadorHumano  |   |   JugadorIA    |
        +----------------+   +----------------+
        | + jugarCarta() |   | + jugarCarta() |
        +----------------+   +----------------+

        +-----------------------+
        |   ControladorJuego    |
        +-----------------------+
        | - modelo: Juego       |
        | - vista: VistaJuego   |
        +-----------------------+
        | + iniciar()           |
        | + manejarTurno()      |
        | + procesarEntrada()   |
        +-----------------------+*/
