package org.example.lyaii.Automatas;

public class AutomataID {
    private static boolean flag;
    public static boolean analizar(String cadena){
        flag=false;
        try{q0(cadena, 0);}catch(Exception e){flag=false;}
        return flag;
    }
    private static void q0(String cadena, int pos){        
        switch (cadena.charAt(pos)) {
            case '$':
                q2(cadena, pos+1);
                break;
            default:
                q1(cadena, pos+1);
                break;
        }
    }
    private static void q1(String cadena, int pos){
        flag=false;
    }
    private static void q2(String cadena, int pos){
        switch (cadena.charAt(pos)) {
            //Guion
            case '_':
            // Dígitos
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            // Minúsculas
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
            // Mayúsculas
            case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
                q3(cadena, pos+1);
                break;
            default:
                q1(cadena, pos+1);
                break;
        }
    }
    private static void q3(String cadena, int pos){
        if(cadena.length()==pos){
            flag=true;
        }else{
            switch (cadena.charAt(pos)) {
                //Guion
                case '_':
                // Dígitos
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                // Minúsculas
                case 'a': case 'b': case 'c': case 'd': case 'e':
                case 'f': case 'g': case 'h': case 'i': case 'j':
                case 'k': case 'l': case 'm': case 'n': case 'o':
                case 'p': case 'q': case 'r': case 's': case 't':
                case 'u': case 'v': case 'w': case 'x': case 'y':
                case 'z':
                // Mayúsculas
                case 'A': case 'B': case 'C': case 'D': case 'E':
                case 'F': case 'G': case 'H': case 'I': case 'J':
                case 'K': case 'L': case 'M': case 'N': case 'O':
                case 'P': case 'Q': case 'R': case 'S': case 'T':
                case 'U': case 'V': case 'W': case 'X': case 'Y':
                case 'Z':
                    q3(cadena, pos+1);
                    break;
                default:
                    q1(cadena, pos+1);
                    break;
            }
        }
    }
}
