package org.example.lyaii.AST;

import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.Enums.Tipos;
import org.example.lyaii.TablaSimbolos.Simbolo;

public class NodoDeclaracion extends Nodo{
    private Palabras instruccion;
    private Tipos valor;
    private Simbolo sym;
    public NodoDeclaracion(Palabras instruccion, Tipos valor, Simbolo sym){
        this.instruccion=instruccion;
        this.valor=valor;
        this.sym=sym;

    }
    @Override
    public boolean check() {
        if(instruccion==Palabras.PR03 && valor==sym.getTipo())
            return true;
        else
            return false;
    }//hay que hacer pila de errores
}
