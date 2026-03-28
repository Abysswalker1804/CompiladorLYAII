package org.example.lyaii.Tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Tokens {
    public static final Map<String,String> TOKENS = new HashMap<>();
    static{
        //Palabras reservadas
        TOKENS.put("begin","PR01");
        TOKENS.put("end","PR02");
        TOKENS.put("declare","PR03");
        TOKENS.put("uint","PR04");
        TOKENS.put("ufixed","PR05");
        TOKENS.put("int","PR06");
        TOKENS.put("fixed","PR07");
        //Operadores aritmeticos
        TOKENS.put("+","OA01");
        TOKENS.put("-","OA02");
        TOKENS.put("*","OA03");
        TOKENS.put("/","OA04");
        TOKENS.put("**","OA05");
        //Operadores relacionales
        TOKENS.put("=","OR01");
        TOKENS.put("<","OR02");
        TOKENS.put(">","OR03");
        TOKENS.put("<=","OR04");
        TOKENS.put(">=","OR05");
    }

    public static final Map<Pattern,String> OTROS = new HashMap<>();
    static {
        OTROS.put(Pattern.compile("\\$[a-zA-Z_][a-zA-Z0-9_]*"),"ID");
        OTROS.put(Pattern.compile("\\d+(\\.\\d+)?"), "CNE");
        OTROS.put(Pattern.compile(";"), "DEL");
        OTROS.put(Pattern.compile(":="),"ASG");
    }
}
