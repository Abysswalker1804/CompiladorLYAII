package org.example.lyaii.AST;

public class NodoFin  extends Nodo{
    private String dato;
    public NodoFin(String dato){
        this.dato=dato;
    }
    @Override
    public boolean check() {
        if(dato.equals("end"))
            return true;
        else
            return false;
    }
}
