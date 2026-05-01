package org.example.lyaii.Assembly8086;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.example.lyaii.TablaSimbolos.TablaSimbolos;

public class GeneradorASM {
    public static void main(String [] args){
        generar();
    }
    public static void generar(){
        BufferedWriter escritor=null;
        try{
            escritor = new BufferedWriter(new FileWriter("obj.asm"));
            escritor.write(".model small\n.stack 100h\n\n");
            escritor.write(".data\n");
            ;
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
}
