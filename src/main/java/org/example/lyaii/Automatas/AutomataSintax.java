package org.example.lyaii.Automatas;

public class AutomataSintax {
    private static boolean flag;
    public static boolean analizar(String[] tokens){
        flag=false;
        try{q0(tokens, 0);}catch(Exception e){flag=false;}
        return flag;
    }

    private static void q0(String [] tokens, int pos ){
        Pila pila=new Pila();
        if(tokens[pos].equals("begin")){
            pila.push("b");
            q1(tokens, pos+1);
        }
    }
    private static void q1(String [] tokens, int pos ){
        switch(tokens[pos]){
            case "}":
                q1(tokens, pos+1);
                break;
            case "end":
                q2(tokens, pos+1);
                break;
            case "print":
                q21(tokens, pos+1);
                break;
            case "id":
                q5(tokens, pos+1);
                break;
            case "declare":
                q3(tokens, pos+1);
                break;
            case "/*":
                q20(tokens, pos+1);
                break;
        }
    }
    private static void q2(String [] tokens, int pos ){}
    private static void q3(String [] tokens, int pos ){}
    private static void q4(String [] tokens, int pos ){}
    private static void q5(String [] tokens, int pos ){}
    private static void q6(String [] tokens, int pos ){}
    private static void q7(String [] tokens, int pos ){}
    private static void q8(String [] tokens, int pos ){}
    private static void q9(String [] tokens, int pos ){}
    private static void q10(String [] tokens, int pos ){}
    private static void q11(String [] tokens, int pos ){}
    private static void q12(String [] tokens, int pos ){}
    private static void q13(String [] tokens, int pos ){}
    private static void q14(String [] tokens, int pos ){}
    private static void q15(String [] tokens, int pos ){}
    private static void q16(String [] tokens, int pos ){}
    private static void q17(String [] tokens, int pos ){}
    private static void q18(String [] tokens, int pos ){}
    private static void q19(String [] tokens, int pos ){}
    private static void q20(String [] tokens, int pos ){
        if(tokens[pos].equals("*/"))
            q1(tokens, pos+1);
        else
            q20(tokens, pos+1);
    }
    private static void q21(String [] tokens, int pos ){}
    private static void q22(String [] tokens, int pos ){}
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