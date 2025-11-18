package ar.edu.unlu.poo.corazones.modelo;

public class Carta {
    private final Palo palo;
    private int puntaje;
    private final int id;
    private String nombre;

    public Carta(Palo palo,int id){
        this.palo=palo;
        this.id=id;
        this.setNombre();
        this.setPuntaje();
    }
    public Palo getPalo(){
        return palo;
    }
    public int getPuntaje() {
        return puntaje;
    }
    public String getNombre() {
        return nombre;
    }
    public int getId() {
        return id;
    }
    private void setNombre(){
        if(id==2){
            nombre="dos";
        } else if (id==3) {
            nombre="tres";
        }else if(id==4){
            nombre="cuatro";
        }else if(id==5){
            nombre="cinco";
        }else if(id==6){
            nombre="seis";
        }else if(id==7){
            nombre="siete";
        }else if(id==8){
            nombre="ocho";
        }else if(id==9){
            nombre="nueve";
        }else if(id==10){
            nombre="diez";
        }else if(id==11){
            nombre="sota";
        }else if(id==12){
            nombre="reina";
        }else if(id==13){
            nombre="rey";
        }else {
            nombre="as";
        }
    }
    private void setPuntaje(){
        if(this.palo.equals(Palo.Corazon)){
            puntaje=1;
        }
        if(this.palo.equals(Palo.Pica) && this.id == 12){
            puntaje=13;
        }
        else {
            puntaje=0;
        }
    }
    @Override
    public String toString() {
        return getNombre() + " de " + getPalo();
    }
}