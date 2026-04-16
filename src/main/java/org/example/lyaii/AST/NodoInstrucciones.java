package org.example.lyaii.AST;

public class NodoInstrucciones extends Nodo{
    
    @Override
    public boolean check() {
        // TODO Auto-generated method stub
        return false;
    }
    public void addChild(Nodo hijo){
        hijos.add(hijo);
    }
}
