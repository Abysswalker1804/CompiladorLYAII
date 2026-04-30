package org.example.lyaii.AST;

public class NodoIF extends Nodo{
    private boolean condicion_es_correcta;
    public NodoIF(boolean condicion_es_correcta){
        this.condicion_es_correcta=condicion_es_correcta;
    }
    @Override
    public boolean check() {
        return condicion_es_correcta;
    }
}
