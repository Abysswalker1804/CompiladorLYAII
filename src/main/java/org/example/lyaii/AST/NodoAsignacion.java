package org.example.lyaii.AST;

import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.Enums.Tipos;
import org.example.lyaii.TablaSimbolos.Simbolo;

public class NodoAsignacion extends Nodo{
    private Tipos valor;
    private Simbolo sym;
    public NodoAsignacion(Tipos valor, Simbolo sym){
        this.valor=valor;
        this.sym=sym;
    }
    @Override
    public boolean check() {
        if(sym==null)
            return false;
        else{
            if(valor==sym.getTipo())
                return true;
            else
                return false;
        }
    }
}
