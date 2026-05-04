package org.example.lyaii.Tools;

import org.example.lyaii.Automatas.AutomataID;
import org.example.lyaii.TablaSimbolos.Simbolo;
import org.example.lyaii.TablaSimbolos.TablaSimbolos;

import java.util.HashMap;
import java.util.Map;

public class ShuntingYard {
    private static final Map<String,Integer> precedencia=new HashMap<>();
    static {
        precedencia.put("+",1);
        precedencia.put("-",1);
        precedencia.put("*",2);
        precedencia.put("/",2);
        precedencia.put("**",3);
        precedencia.put("(",4);
        precedencia.put(")",4);

        precedencia.put("||",1);
        precedencia.put("&&",2);
        precedencia.put("!",3);
    }
    public static void main(String [] args){
        //String [] tokens={"3", "+", "4", "*", "2" ,"/", "(", "1", "-", "5", ")","**" ,"2", "**", "3"};
        String [] tokens={"A", "&&", "B", "||","C"};
        tokens=toPostfix(tokens);
        print(tokens);
    }
    public static String [] toPostfix(String [] expresion){
        Pila pila=new Pila();
        Cola cola=new Cola();
        String peek;
        for(String token: expresion){
            if(precedencia.containsKey(token)){
                if(token.equals("(")){
                    pila.push(token);
                } else if(token.equals(")")){
                    while(pila.peek()!=null && !pila.peek().equals("(")){cola.enqueue(pila.pop());}
                    pila.pop();
                }else{
                    peek = pila.peek();
                    if(peek==null)
                        pila.push(token);
                    else{
                        while (pila.peek() != null && !pila.peek().equals("(")) {
                            String top = pila.peek();

                            int precTop = precedencia.get(top);
                            int precToken = precedencia.get(token);

                            boolean esIzq = !token.equals("**"); // asociatividad por la derecha

                            if (
                                    (esIzq && precToken <= precTop) ||
                                            (!esIzq && precToken < precTop)
                            ) {
                                cola.enqueue(pila.pop());
                            } else {
                                break;
                            }
                        }
                        pila.push(token);
                    }
                }
            }else{
                cola.enqueue(token);
            }
        }
        while(pila.peek()!=null){
            cola.enqueue(pila.pop());
        }
        String [] aux=new String[cola.length()];
        for (int i=0; i<aux.length; i++){
            aux[i]=cola.dequeue();
        }
        return aux;
    }
    public static void print(String [] tokens){
        String cad="";
        for(String token: tokens){
            cad=(cad.isEmpty()?token:cad+" | "+token);
        }
        System.out.println(cad);
    }
    public static double evaluador(String [] postfix){
        Pila pila=new Pila();
        double temp1,temp2,res=0;
        for(String token: postfix){
            if(AutomataID.analizar(token)){
                Simbolo sym = TablaSimbolos.consultar(token);
                switch(sym.getTipo()){
                    case INT:
                    case UINT:
                        pila.push(sym.getValorInt()+"");
                        break;
                    case FIXED:
                    case UFIXED:
                        pila.push(sym.getValorFix()+"");
                        break;
                    default:
                }
            }else if(precedencia.containsKey(token)){
                temp1=Double.parseDouble(pila.pop());
                temp2=Double.parseDouble(pila.pop());
                switch (token){
                    case "+":
                        res=temp2+temp1;
                        break;
                    case "-":
                        res=temp2-temp1;
                        break;
                    case "*":
                        res=temp2*temp1;
                        break;
                    case "/":
                        res=temp2/temp1;
                        break;
                    case "**":
                        res=Math.pow(temp2,temp1);
                        break;
                }
                pila.push(""+res);
            }else{
                pila.push(token);
            }
        }
        return Double.parseDouble(pila.pop());
    }
}
