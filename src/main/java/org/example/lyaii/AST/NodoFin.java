package org.example.lyaii.AST;

import org.example.lyaii.Enums.Palabras;

public class NodoFin  extends Nodo{
    private Palabras palabra;
    public NodoFin(){}
    public void setWord(Palabras palabra){
        this.palabra=palabra;
    }
    @Override
    public boolean check() {
        return true;
    }
}
