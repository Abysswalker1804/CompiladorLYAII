package org.example.lyaii.AST;

import org.example.lyaii.Automatas.AutomataID;

public class NodoPrograma extends Nodo{
    
    public NodoPrograma(){}
    public NodoPrograma(String inicio, String fin){
        hijos.add(new NodoInicio(inicio));
        hijos.add(new NodoPrograma());
        hijos.add(new NodoFin(fin));
    }
    @Override
    //Incompleto
    public boolean check(){
        boolean flag=false;

        return flag;
    }
    public void addInicio(String inicio){
        hijos.add(new NodoInicio(inicio));
    }
    public void addFin(String fin){
        hijos.add(new NodoFin(fin));
    }
    public void createInstrucciones(){
        hijos.add(new NodoInstrucciones());
    }
    public void addInstruccion(String op){
        switch(op){
            case "declare":
                //Crear nodo declaracion
                hijos.add(new NodoDeclaracion(op));
                //Hay que revisar que el tipo corresponde a lo que se asigna
                break;
            case "loop":
                break;
            case "if":
                break;
            default:
                if(AutomataID.analizar(op)){
                    //Asignación
                }
        }
    }
}
