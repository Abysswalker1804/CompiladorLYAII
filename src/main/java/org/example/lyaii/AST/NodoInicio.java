package org.example.lyaii.AST;

public class NodoInicio extends Nodo{
    private String dato;
    public NodoInicio(String dato){
        this.dato=dato;
    }
    @Override
    public boolean check() {
        if(dato.equals("begin"))
            return true;
        else
            return false;
    }
}
