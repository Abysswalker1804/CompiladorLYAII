package org.example.lyaii.AST;

import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.Enums.Palabras;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.Tools.PilaAST;
import org.example.lyaii.Tools.PilaErrores;

import java.util.Arrays;

public class AST {
    private boolean flag_instrucciones=false;
    private NodoPrograma programa;
    public static void main(String []args){
        AST arbol=new AST();
        //String [] tokens={"begin","if","(","true",")","{","loop","(","true",")","{","declare","int","$var","=","1",";","}","}","else","{","if","(","2","<","3",")","{","}","}","end"};
        String [] tokens={"begin","%comentario%","declare","int","$var","=","1",";","end"};
        arbol.crearAST(tokens);
        boolean flag=arbol.validar(arbol.getPrograma());
        System.out.println(flag?"Correcto":"Incorrecto");
        while(PilaErrores.peek()!=null){
            System.out.println(PilaErrores.pop());
        }
    }
    public AST(){
        programa=new NodoPrograma();
    }
    public void crearAST(String [] tokens){
        PilaAST.iniciar();//Importante para inicializar la pila de nodos
        String [] sub;
        int j;
        for(int i=0; i<tokens.length; i++){
            if(tokens[i].equals("begin")){
                addChild(Palabras.PR01);
            }else if(tokens[i].equals("end")){
                addChild(Palabras.PR02);
            }else if(tokens[i].equals("loop")){
                //Recortar un pequeño arreglo hasta ")" y mandarlo para crear el nodo
                j=i;
                do{j++;}while(!tokens[j].equals(")"));
                sub=Arrays.copyOfRange(tokens,i,j+1);
                addChild(Palabras.PR09, sub);
                i=j;
                //Hay que meter a la pila AST para saber que hay que construir los nodos dentro del ciclo del ast
            }else if(tokens[i].equals("if")){
                j=i;
                do{j++;}while(!tokens[j].equals(")"));
                sub=Arrays.copyOfRange(tokens,i,j+1);
                addChild(Palabras.PR10, sub);
                i=j;
            }else if(tokens[i].equals("else")){
                sub=Arrays.copyOfRange(tokens,i,i+1);
                addChild(Palabras.PR11, sub);
            }else{
                //Recortar un pequeño arreglo y mandarlo para crear el nodo
                switch (tokens[i]){
                    case "declare":
                        j=i;
                        do{j++;}while( j< tokens.length && !tokens[j].equals(";"));
                        sub=Arrays.copyOfRange(tokens,i,j);
                        addChild(Palabras.PR03, sub);
                        i=j;
                        break;
                    case "}":
                        //Cierre de un nodo ciclo o condicional
                        PilaAST.pop();
                        break;
                    default:
                        if(AutomataID.analizar(tokens[i])){
                            //Es una asignación
                            //Estructura: $id, =, ..., ;
                            j=i;
                            do{j++;}while(!tokens[j].equals(";") && j<tokens.length);
                            sub=Arrays.copyOfRange(tokens,i,j);
                            addAsignacion(sub);
                            i=j;
                        }
                }
            }
        }
    }
    public boolean validar(Nodo nodo) {
        if (nodo == null) return true;

        // Si este nodo falla, ya no sigue
        if (!nodo.check()) return false;

        // Revisar todos los hijos
        for (Nodo hijo : nodo.hijos) {
            if (!validar(hijo)) {
                return false;
            }
        }
        return true;
    }
    public NodoPrograma getPrograma(){
        return programa;
    }
    private void addChild(Palabras palabra){
        programa.addChild(palabra,null);
    }
    private void addChild(Palabras palabra, String[] tokens){
        programa.addChild(palabra, tokens);
    }
    private void addAsignacion(String [] sub){
        programa.addAsignacion(sub);
    }
}
