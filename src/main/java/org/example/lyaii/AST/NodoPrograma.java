package org.example.lyaii.AST;

public class NodoPrograma extends Nodo{
    
    public NodoPrograma(String inicio, String fin){
        hijos.add(new NodoInicio(inicio));
        //Falta Instrucciones
        hijos.add(new NodoFin(fin));
    }
    @Override
    //Incompleto
    public boolean check(){
        boolean flag=false;

        return flag;
    }
}
