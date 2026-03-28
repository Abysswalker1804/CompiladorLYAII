package org.example.lyaii.Automatas;
import org.example.lyaii.Tokens.Tokens;

import java.util.Scanner;

public class Test1 {

    /*
    if (c == ' ') {
    System.out.println("Espacio");
    } else if (c == '\n') {
        System.out.println("Salto de línea");
    } else if (c == '\t') {
        System.out.println("Tabulación");
    }
    */

    public static void main(String[] args){
        testOperacion();
    }
    private static void testOperacion(){
        Scanner entrada = new Scanner(System.in);
        String cadena=entrada.nextLine();
        System.out.println(cadena);

        String [] tokens=cadena.split(" ");
        int i=1;
        for(String t : tokens){
            if(Tokens.TOKENS.containsKey(t) || Tokens.OTROS.containsKey(t)){
                String token=Tokens.TOKENS.get(t);
                if(token.equals("CNE") || token.equals("ID")){
                    token=token+i;
                    i++;
                    System.out.print(token);
                }
                if(token.equals("DEL") || token.equals("PR01") || token.equals("PR02")){
                    System.out.println(token);
                }else {
                    System.out.print(token);
                }
            }
        }
    }
}
