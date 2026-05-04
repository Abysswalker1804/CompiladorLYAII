package org.example.lyaii.Assembly8086;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.example.lyaii.AST.NodoPrograma;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.TablaSimbolos.TablaSimbolos;
import org.example.lyaii.Tools.Pila;
import org.example.lyaii.Tools.ShuntingYard;

public class GeneradorASM {
    private static int temporales=1;
    private static Pila variablesTemporales;
    public static void main(String [] args){
        //String [] codigo={"begin","declare","uint","$var","=","3","+","5",";","declare","fixed","$var2","=","3","+","1.5",";","end"};
        String [] codigo={"begin","declare","string","$var","=","string","(","23",")","+","\" otra cadena\"",";","end"};
        generar(codigo);
    }
    public static void generar(String [] codigo){
        temporales=0;
        variablesTemporales=new Pila();
        String codigoAEscrbir;
        BufferedWriter escritor=null;
        try{
            escritor = new BufferedWriter(new FileWriter("obj.asm"));
            codigoAEscrbir=(".model small\n.stack 100h\n\n");
            codigoAEscrbir=codigoAEscrbir+(".data\n");

            //Escribir Símbolos
            boolean hay_variables=false;
            for(int j,i=0;i<codigo.length;i++){
                String token=codigo[i];
                if(token.equals("declare")){
                    hay_variables=true;
                    j=i;
                    while(!codigo[j].equals(";")){j++;}
                    String[] sub= Arrays.copyOfRange(codigo,i,j);
                    String nombre=(sub[2].replace("$","").toLowerCase());
                    if(sub[1].equals("int") || sub[1].equals("uint") || sub[1].equals("fixed") || sub[1].equals("ufixed")){
                        double valorDec=ShuntingYard.evaluador(ShuntingYard.toPostfix(Arrays.copyOfRange(sub,4,sub.length)));
                        codigoAEscrbir=codigoAEscrbir+(nombre+" DW "+((int)(valorDec*256))+"\n");
                    }else{
                        String cadena= NodoPrograma.evaluarString(sub);
                        codigoAEscrbir=codigoAEscrbir+(nombre+" DB '"+cadena+"'\n");
                    }
                }
            }
            codigoAEscrbir=codigoAEscrbir+("\n.code\nmain PROC\n");

            //Iniciar segmento de datos:
            codigoAEscrbir=codigoAEscrbir+("MOV AX, @data\nMOV DS, AX\n");

            //Escribir código
            String [] sub;
            String instruccion;
            for(int j,i=0; i<codigo.length; i++){
                String token= codigo[i];
                switch (token){
                    case "print":
                        j=i;
                        while(!codigo[j].equals(";")){j++;}
                        sub=Arrays.copyOfRange(codigo,i,j);
                        instruccion=writePrint(sub);
                        codigoAEscrbir=codigoAEscrbir+(instruccion);
                        i=j;
                        break;
                }
            }

            //Final del programa:
            codigoAEscrbir=codigoAEscrbir+("\nMOV AH, 4Ch\nINT 21h");
            //Fin
            codigoAEscrbir=codigoAEscrbir+("\nmain ENDP\nEND main");

            //Declarar temporales
            String varTemp="\n.data\n";
            while(variablesTemporales.peek()!=null){
                varTemp=varTemp+variablesTemporales.pop()+"\n";
            }
            codigoAEscrbir=codigoAEscrbir.replace(".data", varTemp);

            escritor.write(codigoAEscrbir);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            if (escritor != null) {
                try {
                    escritor.close(); //Cerrar el flujo
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String writePrint(String [] sub){
        String cadena=NodoPrograma.evaluarString(sub)+"$";
        String temporal="temp"+temporales;
        temporales++;
        String instruccion=temporal+" DB '"+cadena+"'";
        variablesTemporales.push(instruccion.replace("\"",""));
        instruccion="\nMOV DX, OFFSET "+temporal+"\nMOV AH, 09h"+"\nINT 21h";
        return  instruccion.replace("\"","");
    }
}
