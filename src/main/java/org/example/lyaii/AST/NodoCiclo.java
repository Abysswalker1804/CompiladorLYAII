package org.example.lyaii.AST;

public class NodoCiclo extends Nodo{
    private boolean condicion_es_correcta;
    public NodoCiclo(boolean condicion_es_correcta){
        this.condicion_es_correcta=condicion_es_correcta;
    }
    @Override
    public boolean check() {
        return condicion_es_correcta;
    }
}
