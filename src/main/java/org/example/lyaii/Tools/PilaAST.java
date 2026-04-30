package org.example.lyaii.Tools;

import org.example.lyaii.AST.Nodo;

public class PilaAST {
    private static Nodo tope=null;
    public static void iniciar(){
        tope=null;
    }
    public static void push(Nodo temp){
        if(tope!=null)
            temp.setSiguiente(tope);
        tope=temp;
    }
    public static Nodo pop(){
        if(tope!=null){
            Nodo temp = tope;
            tope=tope.getSiguiente();
            temp.setSiguiente(null);
            return temp;
        }else
            return null;
    }
    public static Nodo peek(){
        if(tope==null)
            return null;
        else
            return tope;
    }
    public boolean isEmpty(){
        return tope == null;
    }
}
