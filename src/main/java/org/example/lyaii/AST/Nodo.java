package org.example.lyaii.AST;

import java.util.ArrayList;
import java.util.List;

public abstract class Nodo {
    private Nodo siguiente;
    protected List<Nodo> hijos= new ArrayList<>();;
    public abstract boolean check();

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

}
