package org.example.lyaii.AST;

import java.util.ArrayList;

import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.Enums.Tipos;
import org.example.lyaii.TablaSimbolos.Simbolo;

public class NodoInstrucciones extends Nodo{
    @Override
    public boolean check() {
        return true;
    }
    public void addChild(Nodo hijo){
        hijos.add(hijo);
    }
    public void addDeclaracion(Tipos valor, Simbolo sym){
        addChild(new NodoDeclaracion(Palabras.PR03,valor,sym));
    }
    public void addAsignacion(Tipos valor, Simbolo sym){
        addChild(new NodoAsignacion(valor,sym));
    }
}
