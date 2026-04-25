package org.example.lyaii.Automatas;

public class AutomataSintax {
    private static boolean flag;
    private static boolean hay_if;
    private static Pila pila;
    private static String error;

    public static void main(String [] args){
        String [] tokens={"\n"," ","begin","declare","int"," ","$id","=","34"," ",";"," ","end"};
        System.out.println(analizar(tokens));
    }
    public static boolean analizar(String[] tokens){
        flag=false;
        error=null;
        pila=new Pila();
        try{q0(tokens, 0);}catch(Exception e){flag=false;}
        return flag;
    }
    public static String getError(){
        return error;
    }

    private static void q0(String [] tokens, int pos ){
        //System.out.println("q0");
        hay_if=false;
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else if(tokens[pos].equals("begin")){
                espacio=false;
                pila.push("b");
                q1(tokens, pos+1);
            }
        }while(espacio);
    }
    private static void q1(String [] tokens, int pos ){
        //System.out.println("q1");
        boolean espacio=false;
        String item;
        do{
            switch(tokens[pos]){
                case "}":
                    espacio=false;
                    item=pila.pop();
                    if(item.equals("K"))
                        q1(tokens, pos+1);
                    break;
                case "end":
                    espacio=false;
                    item=pila.pop();
                    if(item.equals("b"))
                        q2(tokens, pos+1);
                    break;
                case "print":
                    espacio=false;
                    pila.push(";");
                    q21(tokens, pos+1);
                    break;
                case "declare":
                    espacio=false;
                    pila.push(";");
                    q3(tokens, pos+1);
                    break;
                case "%":
                    espacio=false;
                    q20(tokens, pos+1);
                    break;
                case "loop":
                    espacio=false;
                    q13(tokens, pos+1);
                    break;
                case "else":
                    espacio=false;
                    if(hay_if){
                        hay_if=false;
                        q19(tokens, pos+1);
                    }
                    break;
                case "if":
                    espacio=false;
                    hay_if=true;
                    q16(tokens, pos+1);
                    break;
                case " ":
                case "\n":
                case "\t":
                    espacio=true;
                    pos++;
                    break;
                default:
                    espacio=false;
                    if(AutomataID.analizar(tokens[pos])){
                        espacio=false;
                        pila.push(";");
                        q5(tokens, pos+1);
                    }else{
                        error=tokens[pos-1];
                        flag=false;
                    }
            }
        }while(espacio);
    }
    private static void q2(String [] tokens, int pos ){
        //System.out.println("q2");
        String item=pila.pop();
        if(tokens.length==pos && item==null){
            flag=true;
        }
    }
    private static void q3(String [] tokens, int pos ){
        //System.out.println("q3");
        boolean espacio =false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else {
                espacio=false;
                switch(tokens[pos]){
                case "uint":
                case "int":
                case "fixed":
                case "ufixed":
                case "string":
                    q4(tokens, pos+1);
                    break;
                default:
                    error=tokens[pos-1];
                    flag=false;
            }
            }
        }while(espacio);
    }
    private static void q4(String [] tokens, int pos ){
        //System.out.println("q4");
        boolean espacio =false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else {
                espacio=false;
                if(AutomataID.analizar(tokens[pos])){
                    q5(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q5(String [] tokens, int pos ){
        //System.out.println("q5");
        boolean espacio =false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                case "=":
                    q6(tokens, pos+1);
                    break;
                default:
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q6(String [] tokens, int pos ){
        //System.out.println("q6");
        boolean espacio =false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                case "(":
                    pila.push("p");
                    q6(tokens, pos+1);
                    break;
                case "string":
                    q11(tokens, pos+1);
                default:
                    if(AutomataCadena.analizar(tokens[pos])){
                        q9(tokens, pos+1);
                    }else if(AutomataID.analizar(tokens[pos]) || AutomataNumero.analizar(tokens[pos])){
                        q7(tokens, pos+1);
                    }else{
                        error=tokens[pos-1];
                        flag=false;
                    }
                }
            }
        }while(espacio);
    }
    private static void q7(String [] tokens, int pos ){
        //System.out.println("q7");
        boolean espacio=false;
        String item;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch (tokens[pos]) {
                    case ")":
                        item=pila.pop();
                        if(item.equals("p")){
                            q7(tokens, pos+1);
                        }else if(item.equals("s")){
                            q12(tokens, pos+1);
                        }
                        break;
                    case ";":
                        item=pila.pop();
                        if(item.equals(";"))
                            q1(tokens, pos+1);
                        break;
                    case "==":
                    case "!=":
                    case "<=":
                    case ">=":
                    case "<":
                    case ">":
                        item=pila.pop();
                        if(item.equals("p")){
                            item=pila.pop();
                            if(item.equals("L")){
                                pila.push("L");
                                pila.push("p");
                                q17(tokens, pos+1);
                            }else if(item.equals("I")){
                                pila.push("I");
                                pila.push("p");
                                q17(tokens, pos+1);
                            }
                        }else if(item.equals("I")){
                            pila.push("I");
                            q8(tokens, pos+1);
                        }else if(item.equals("L")){
                            pila.push("L");
                            q8(tokens, pos+1);
                        }
                        break;
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "**":
                        q8(tokens, pos+1);
                        break;
                    case "&&":
                    case "||":
                        item=pila.pop();
                        if(item.equals("L")){
                            pila.push("L");
                            q14(tokens, pos+1);
                        }else if(item.equals("I")){
                            pila.push("");
                            q18(tokens, pos+1);
                        }
                        break;
                    default:
                        error=tokens[pos-1];
                        flag=false;
                }
            }
        }while(espacio);
    }
    private static void q8(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("(")){
                    pila.push("p");
                    q8(tokens, pos+1);
                }else if(AutomataID.analizar(tokens[pos]) || AutomataNumero.analizar(tokens[pos])){
                    q7(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q9(String [] tokens, int pos ){
        boolean espacio=false;
        String item;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                    case ")":
                        item=pila.pop();
                        if(item.equals("r"))
                            q9(tokens, pos+1);
                        break;
                    case ";":
                        item=pila.pop();
                        if(item.equals(";"))
                            q1(tokens, pos+1);
                        break;
                    case "+":
                        q10(tokens, pos+1);
                        break;
                    default:
                        error=tokens[pos-1];
                        flag=false;
                }
            }
        }while(espacio);
    }
    private static void q10(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(AutomataCadena.analizar(tokens[pos])){
                    q9(tokens, pos+1);
                }else if(tokens[pos].equals("string")){
                    q11(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q11(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else {
                espacio = false;
                if(tokens[pos].equals("(")){
                    pila.push("s");
                    q12(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q12(String [] tokens, int pos ){
        boolean espacio=false;
        String item;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                    case "+":
                        q10(tokens, pos+1);
                        break;
                    case ";":
                        item=pila.pop();
                        if(item.equals(";"))
                            q1(tokens, pos+1);
                        break;
                    case ")":
                        item=pila.pop(); 
                        if(item.equals("r"))
                            q12(tokens, pos+1);
                        break;
                    default:
                        if(AutomataID.analizar(tokens[pos]) || AutomataNumero.analizar(tokens[pos]))
                            q7(tokens, pos+1);
                        else{
                            error=tokens[pos-1];
                            flag=false;
                        }
                }
            }
        }while(espacio);
    }
    private static void q13(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("("))
                    pila.push("L");
                    q14(tokens, pos+1);
            }
        }while(espacio);
    }
    private static void q14(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                    case"(":
                        pila.push("p");
                        //Cae al siguiente
                    case "!":
                        q14(tokens, pos+1);
                        break;
                    case "false":
                    case "true":
                        q15(tokens, pos+1);
                        break;
                    default:
                        if(AutomataID.analizar(tokens[pos]) || AutomataNumero.analizar(tokens[pos]))
                            q7(tokens, pos+1);
                        else{
                            error=tokens[pos-1];
                            flag=false;
                        }
                }
            }
        }while(espacio);
    }
    private static void q15(String [] tokens, int pos ){
        boolean espacio=false;
        String item;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                    case ")":
                        item=pila.pop();
                        if(item.equals("p"))
                            q15(tokens, pos+1);
                        else if(item.equals("L")){
                            pila.push("K");
                            q14(tokens, pos+1);
                        }else if(item.equals("I")){
                            pila.push("K");
                            q18(tokens, pos+1);
                        }
                        break;
                    case "&&":
                    case "||":
                        item=pila.pop();
                        if(item.equals("L")){
                            pila.push("L");
                            q14(tokens, pos+1);
                        }else if(item.equals("I")){
                            pila.push("I");
                            q18(tokens, pos+1);
                        }else{
                            error=tokens[pos-1];
                            flag=false;
                        }
                    default:
                        error=tokens[pos-1];
                        flag=false;
                }
            }
        }while(espacio);
    }
    private static void q16(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("(")){
                    pila.push("I");
                    q18(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q17(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("(")){
                    pila.push("p");
                    q17(tokens, pos+1);
                }else if(AutomataID.analizar(tokens[pos]) || AutomataNumero.analizar(tokens[pos])){
                    q7(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q18(String [] tokens, int pos ){
        boolean espacio=false;
        String item;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                switch(tokens[pos]){
                    case "(":
                        pila.push("p");
                        //Cae al siguiente
                    case"!":
                        q18(tokens, pos+1);
                        break;
                    case "true":
                    case "false":
                        q15(tokens, pos+1);
                        break;
                    case "{":
                        item=pila.pop();
                        if(item.equals("K")){
                            pila.push("K");
                            q1(tokens, pos+1);
                        }
                        break;
                    default:
                        if(AutomataID.analizar(tokens[pos]) || AutomataNumero.analizar(tokens[pos]))
                            q7(tokens, pos+1);
                        else{
                            error=tokens[pos-1];
                            flag=false;
                        }
                }
            }
        }while(espacio);
    }
    private static void q19(String [] tokens, int pos ){
        boolean espacio=false;
        String item;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("{")){
                    item=pila.pop();
                    if(item.equals("K")){
                        pila.push("K");
                        q1(tokens, pos+1);
                    }else{
                        error=tokens[pos-1];
                        flag=false;
                    }
                }else{ 
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q20(String [] tokens, int pos ){
        if(tokens[pos].charAt(0)=='%' && tokens[pos].charAt(tokens[pos].length()-1)=='%')
            q1(tokens, pos+1);
        else{
            error=tokens[pos-1];
            flag=false;
        }
    }
    private static void q21(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("(")){
                    pila.push("r");
                    q22(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
    private static void q22(String [] tokens, int pos ){
        boolean espacio=false;
        do{
            if(tokens[pos].trim().isEmpty()){
                pos++;
                espacio=true;
            }else{
                espacio=false;
                if(tokens[pos].equals("string")){
                    q11(tokens, pos+1);
                }else if(AutomataCadena.analizar(tokens[pos])){
                    q9(tokens, pos+1);
                }else{
                    error=tokens[pos-1];
                    flag=false;
                }
            }
        }while(espacio);
    }
}

class Pila{
    private Item tope;
    public Pila(){
        this.tope=null;
    }
    public Pila(String data){
        tope=new Item(data);
        tope.setSig(null);
    }
    public void push(String data){
        Item temp=new Item(data);
        if(tope!=null){
            temp.setSig(tope);
            tope=temp;
        }else
            tope=temp;
    }
    public String pop(){
        if(tope!=null){
            String data=tope.getData();
            Item temp = tope.getSig();
            tope.setSig(null);
            tope=temp;
            return data;
        }else{
            return null;
        }
    }
    public boolean isEmpty(){
        if(tope==null)
            return true;
        else
            return false;
    }
    public void print(){
        Item temp=tope;
        if(temp==null)
            System.out.println("...");
        else{
            while(temp!=null){
            System.out.println(temp.getData());
            temp=temp.getSig();
            }
        }
    }
}

class Item{
    private String data;
    private Item sig;
    public Item(String data){
        this.data=data;
    }
    public void setData(String data){
        this.data=data;
    }
    public void setSig(Item sig){
        this.sig=sig;
    }
    public String getData(){
        return this.data;
    }
    public Item getSig(){
        return this.sig;
    }
}