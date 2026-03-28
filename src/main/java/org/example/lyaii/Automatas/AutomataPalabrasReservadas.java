package org.example.lyaii.Automatas;

import java.util.Scanner;

public class AutomataPalabrasReservadas {
    private static boolean flag;
    public static void main(String [] args){
        Scanner entrada=new Scanner(System.in);
        String cadena=entrada.nextLine();
        System.out.println(analizar(cadena));
    }
    public static boolean analizar(String cadena){
        flag=false;
        try{q0(cadena);}catch(Exception e){}
        return flag;
    }
    private static void q0(String cad){
        if(cad.charAt(0)=='q'){
            q48(cad,1);
        }else if(cad.charAt(0)=='b'){
            q1(cad,1);
        } else if (cad.charAt(0)=='e'){
            q6(cad, 1);
        }else if (cad.charAt(0)=='d'){
            q9(cad, 1);
        }else if(cad.charAt(0)=='i'){
            q17(cad,1);
        }else if(cad.charAt(0)=='u'){
            q16(cad,1);
        }else if(cad.charAt(0)=='f'){
            q20(cad, 1);
        }else if(cad.charAt(0)=='s'){
            q25(cad,1);
        }else if(cad.charAt(0)=='l'){
            q31(cad,1);
        }else if(cad.charAt(0)=='t'){
            q40(cad,1);
        }
    }
    private static void q1(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q2(cad,pos+1);
        }
    }
    private static void q2(String cad, int pos){
        if(cad.charAt(pos)=='g'){
            q3(cad,pos+1);
        }
    }
    private static void q3(String cad, int pos){
        if(cad.charAt(pos)=='i'){
            q4(cad,pos+1);
        }
    }
    private static void q4(String cad, int pos){
        if(cad.charAt(pos)=='n'){
            q5(cad,pos+1);
        }
    }
    private static void q5(String cad, int pos){
        if(pos == cad.length()){
            flag=true;
        }
    }//final
    private static void q6(String cad, int pos){
        if(cad.charAt(pos)=='l'){
            q37(cad,pos+1);
        }else if(cad.charAt(pos)=='n'){
            q7(cad,pos+1);
        }
    }
    private static void q7(String cad, int pos){
        if(cad.charAt(pos)=='d'){
            q8(cad,pos+1);
        }
    }
    private static void q8(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q9(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q10(cad,pos+1);
        }
    }
    private static void q10(String cad, int pos){
        if(cad.charAt(pos)=='c'){
            q11(cad,pos+1);
        }
    }
    private static void q11(String cad, int pos){
        if(cad.charAt(pos)=='l'){
            q12(cad,pos+1);
        }
    }
    private static void q12(String cad, int pos){
        if(cad.charAt(pos)=='a'){
            q13(cad,pos+1);
        }
    }
    private static void q13(String cad, int pos){
        if(cad.charAt(pos)=='r'){
            q14(cad,pos+1);
        }
    }
    private static void q14(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q15(cad,pos+1);
        }
    }
    private static void q15(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q16(String cad, int pos){
        if(cad.charAt(pos)=='f'){
            q54(cad,pos+1);
        }else if(cad.charAt(pos)=='i'){
            q36(cad,pos+1);
        }
    }
    private static void q17(String cad, int pos){
        if(cad.charAt(pos)=='n'){
            q18(cad,pos+1);
        }else if(cad.charAt(pos)=='f'){
            q35(cad,pos+1);
        }
    }
    private static void q18(String cad, int pos){
        if(cad.charAt(pos)=='t'){
            q19(cad,pos+1);
        }
    }
    private static void q19(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q20(String cad, int pos){
        if(cad.charAt(pos)=='a'){
            q44(cad,pos+1);
        }else if(cad.charAt(pos)=='i'){
            q21(cad,pos+1);
        }
    }
    private static void q21(String cad, int pos){
        if(cad.charAt(pos)=='x'){
            q22(cad,pos+1);
        }
    }
    private static void q22(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q23(cad,pos+1);
        }
    }
    private static void q23(String cad, int pos){
        if(cad.charAt(pos)=='d'){
            q24(cad,pos+1);
        }
    }
    private static void q24(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q25(String cad, int pos){
        if(cad.charAt(pos)=='t'){
            q26(cad,pos+1);
        }
    }
    private static void q26(String cad, int pos){
        if(cad.charAt(pos)=='r'){
            q27(cad,pos+1);
        }
    }
    private static void q27(String cad, int pos){
        if(cad.charAt(pos)=='i'){
            q28(cad,pos+1);
        }
    }
    private static void q28(String cad, int pos){
        if(cad.charAt(pos)=='n'){
            q29(cad,pos+1);
        }
    }
    private static void q29(String cad, int pos){
        if(cad.charAt(pos)=='g'){
            q30(cad,pos+1);
        }
    }
    private static void q30(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q31(String cad, int pos){
        if(cad.charAt(pos)=='o'){
            q32(cad,pos+1);
        }
    }
    private static void q32(String cad, int pos){
        if(cad.charAt(pos)=='o'){
            q33(cad,pos+1);
        }
    }
    private static void q33(String cad, int pos){
        if(cad.charAt(pos)=='p'){
            q34(cad,pos+1);
        }
    }
    private static void q34(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q35(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q36(String cad, int pos){
        if(cad.charAt(pos)=='n'){
            q52(cad,pos+1);
        }
    }
    private static void q37(String cad, int pos){
        if(cad.charAt(pos)=='s'){
            q38(cad,pos+1);
        }
    }
    private static void q38(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q39(cad,pos+1);
        }
    }
    private static void q39(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q40(String cad, int pos){
        if(cad.charAt(pos)=='r'){
            q41(cad,pos+1);
        }
    }
    private static void q41(String cad, int pos){
        if(cad.charAt(pos)=='u'){
            q42(cad,pos+1);
        }
    }
    private static void q42(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q43(cad,pos+1);
        }
    }
    private static void q43(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q44(String cad, int pos){
        if(cad.charAt(pos)=='l'){
            q45(cad,pos+1);
        }
    }
    private static void q45(String cad, int pos){
        if(cad.charAt(pos)=='s'){
            q46(cad,pos+1);
        }
    }
    private static void q46(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q47(cad,pos+1);
        }
    }
    private static void q47(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q48(String cad, int pos){
        if(cad.charAt(pos)=='u'){
            q49(cad,pos+1);
        }
    }
    private static void q49(String cad, int pos){
        if(cad.charAt(pos)=='i'){
            q50(cad,pos+1);
        }
    }
    private static void q50(String cad, int pos){
        if(cad.charAt(pos)=='t'){
            q51(cad,pos+1);
        }
    }
    private static void q51(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }// final
    private static void q52(String cad, int pos){
        if(cad.charAt(pos)=='t'){
            q53(cad,pos+1);
        }
    }
    private static void q53(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
    private static void q54(String cad, int pos){
        if(cad.charAt(pos)=='i'){
            q55(cad,pos+1);
        }
    }
    private static void q55(String cad, int pos){
        if(cad.charAt(pos)=='x'){
            q56(cad,pos+1);
        }
    }
    private static void q56(String cad, int pos){
        if(cad.charAt(pos)=='e'){
            q57(cad,pos+1);
        }
    }
    private static void q57(String cad, int pos){
        if(cad.charAt(pos)=='d'){
            q58(cad,pos+1);
        }
    }
    private static void q58(String cad, int pos){
        if(cad.length() == pos){
            flag=true;
        }
    }//final
}
