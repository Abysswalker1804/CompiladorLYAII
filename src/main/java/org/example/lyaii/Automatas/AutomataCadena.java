package org.example.lyaii.Automatas;

//import java.util.Scanner;

public class AutomataCadena {
    private static boolean flag;
    /*public static void main(String[] args){
        Scanner entrada=new Scanner(System.in);
        String cadena=entrada.nextLine();
        analizar(cadena);
        System.out.println(flag);
    }*/
    public static boolean analizar(String cadena){
        flag=false;
        try{q0(cadena,0);}catch(Exception e){flag=false;}
        return flag;
    }
    private static void q0(String cadena, int pos){
        if(cadena.charAt(pos)=='"'){
            q1(cadena, pos+1);
        }else{
            q2(cadena, pos+1);
        }
    }
    private static void q1(String cadena, int pos){
        if(cadena.charAt(pos)=='"'){
            q3(cadena, pos+1);
        }else{
            q1(cadena, pos+1);
        }
    }
    private static void q2(String cadena, int pos){
        flag=false;
    }
    private static void q3(String cadena, int pos){
        if(cadena.length()==pos){
            flag=true;
        }else{
            flag=false;
        }
    }

}
