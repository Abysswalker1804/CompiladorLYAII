package org.example.lyaii.AST;

import org.example.lyaii.Enums.Palabras;

public class NodoElse extends Nodo{
    Palabras palabra;
    public NodoElse(Palabras palabra){
        this.palabra=palabra;
    }
    @Override
    public boolean check() {
        return palabra==Palabras.PR11;
    }
}
