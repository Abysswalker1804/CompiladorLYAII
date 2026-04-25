package org.example.lyaii.Tools;

public class Item{
    private String dato;
    private Item siguiente;
    public Item(String dato){
        this.dato=dato;
    }
    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Item getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Item siguiente) {
        this.siguiente = siguiente;
    }
}

