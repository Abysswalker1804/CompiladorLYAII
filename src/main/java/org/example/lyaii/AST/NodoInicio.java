package org.example.lyaii.AST;

import org.example.lyaii.Enums.Palabras;

public class NodoInicio extends Nodo{
    private Palabras palabra;
    public NodoInicio(Palabras palabra){
        this.palabra=palabra;
    }
    public NodoInicio(){}
    public void setWord(Palabras palabra){
        this.palabra=palabra;
    }
    @Override
    public boolean check() {
        return true;
    }
}
