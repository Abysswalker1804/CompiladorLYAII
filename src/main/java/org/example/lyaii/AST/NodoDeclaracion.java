package org.example.lyaii.AST;

public class NodoDeclaracion extends Nodo{
    private String dato;
    public NodoDeclaracion(String dato){
        this.dato=dato;
    }
    @Override
    public boolean check() {
        if(dato.equals("declare"))
            return true;
        else
            return false;
    }
}
