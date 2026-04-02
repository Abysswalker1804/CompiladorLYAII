package org.example.lyaii.Automatas;

//import java.util.Scanner;

public class AutomataNumero {
    private static boolean flag;
    /*public static void main(String [] args){
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
        switch (cadena.charAt(pos)) {
            case '0':
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9':
                q1(cadena, pos+1);    
                break;
            case '-':
                q4(cadena, pos+1);
                break;
            default:
                q5(cadena, pos+1);
                break;
        }
    }
    private static void q1(String cadena, int pos){
        if(cadena.length()==pos){
            flag=true;
        }else{
            switch (cadena.charAt(pos)) {
                case '0':
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9':
                    q1(cadena, pos+1);    
                    break;
                case '.':
                    q2(cadena, pos+1);
                    break;
                default:
                    q5(cadena, pos+1);
                    break;
            }
        }
    }
    private static void q2(String cadena, int pos){
        switch (cadena.charAt(pos)) {
            case '0':
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9':
                q3(cadena, pos+1);    
                break;
            default:
                q5(cadena, pos+1);
                break;
        }
    }
    private static void q3(String cadena, int pos){
        if(cadena.length()==pos){
            flag=true;
        }else{
            switch (cadena.charAt(pos)) {
                case '0':
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9':
                    q3(cadena, pos+1);    
                    break;            
                default:
                    q5(cadena, pos+1);
                    break;
            }
        }
    }
    private static void q4(String cadena, int pos){
        switch (cadena.charAt(pos)) {
            case '0':
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9':
                q1(cadena, pos+1);    
                break;
            default:
                q5(cadena, pos+1);
                break;
        }
    }
    private static void q5(String cadena, int pos){
        flag=false;
    }
}
