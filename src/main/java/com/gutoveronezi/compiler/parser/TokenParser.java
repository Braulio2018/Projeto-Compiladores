package com.gutoveronezi.compiler.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class TokenParser {

    private static final Map<String, String> TOKEN_PARSING_MAP = new HashMap<>();

    static {
        TOKEN_PARSING_MAP.put("52,1", "PROGRAM|IDENTIFICADOR|;|BLOCO|.");
	TOKEN_PARSING_MAP.put("53,2", "DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO");
	TOKEN_PARSING_MAP.put("53,3", "DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO");
	TOKEN_PARSING_MAP.put("53,4", "DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO");
	TOKEN_PARSING_MAP.put("53,5", "DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO");
	TOKEN_PARSING_MAP.put("53,6", "DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO");
	TOKEN_PARSING_MAP.put("54,2", "LABEL|LID|;");
	TOKEN_PARSING_MAP.put("54,3", null);
	TOKEN_PARSING_MAP.put("54,4", null);
	TOKEN_PARSING_MAP.put("54,5", null);
	TOKEN_PARSING_MAP.put("54,6", null);
	TOKEN_PARSING_MAP.put("55,25", "IDENTIFICADOR|REPIDENT");
	TOKEN_PARSING_MAP.put("56,39", null);
	TOKEN_PARSING_MAP.put("56,46", ",|IDENTIFICADOR|REPIDENT");
	TOKEN_PARSING_MAP.put("56,47", null);
	TOKEN_PARSING_MAP.put("57,3", "CONST|IDENTIFICADOR|=|INTEIRO|;|LDCONST");
	TOKEN_PARSING_MAP.put("57,4", null);
	TOKEN_PARSING_MAP.put("57,5", null);
	TOKEN_PARSING_MAP.put("57,6", null);
	TOKEN_PARSING_MAP.put("58,25", "IDENTIFICADOR|=|INTEIRO|;|LDCONST");
	TOKEN_PARSING_MAP.put("58,4", null);
	TOKEN_PARSING_MAP.put("58,5", null);
	TOKEN_PARSING_MAP.put("58,6", null);
	TOKEN_PARSING_MAP.put("59,4", "VAR|LID|:|TIPO|;|LDVAR");
	TOKEN_PARSING_MAP.put("59,5", null);
	TOKEN_PARSING_MAP.put("59,6", null);
	TOKEN_PARSING_MAP.put("60,25", "LID|:|TIPO|;|LDVAR");
	TOKEN_PARSING_MAP.put("60,5", null);
	TOKEN_PARSING_MAP.put("60,6", null);
	TOKEN_PARSING_MAP.put("61,8", "INTEGER");
	TOKEN_PARSING_MAP.put("61,9", "ARRAY|[|INTEIRO|..|INTEIRO|]|OF|INTEGER");
	TOKEN_PARSING_MAP.put("62,5", "PROCEDURE|IDENTIFICADOR|DEFPAR|;|BLOCO|;|DCLPROC");
	TOKEN_PARSING_MAP.put("62,6", null);
	TOKEN_PARSING_MAP.put("63,36", "(|LID|:|INTEGER|)");
	TOKEN_PARSING_MAP.put("63,39", null);
	TOKEN_PARSING_MAP.put("64,6", "BEGIN|COMANDO|REPCOMANDO|END");
	TOKEN_PARSING_MAP.put("65,47", ";|COMANDO|REPCOMANDO");
	TOKEN_PARSING_MAP.put("65,7", null);
	TOKEN_PARSING_MAP.put("66,11", "CALL|IDENTIFICADOR|PARAMETROS");
	TOKEN_PARSING_MAP.put("66,12", "GOTO|IDENTIFICADOR");
	TOKEN_PARSING_MAP.put("66,13", "IF|EXPRESSAO|THEN|COMANDO|ELSEPARTE");
	TOKEN_PARSING_MAP.put("66,15", null);
	TOKEN_PARSING_MAP.put("66,16", "WHILE|EXPRESSAO|DO|COMANDO");
	TOKEN_PARSING_MAP.put("66,18", "REPEAT|COMANDO|UNTIL|EXPRESSAO");
	TOKEN_PARSING_MAP.put("66,19", null);
	TOKEN_PARSING_MAP.put("66,20", "READLN|(|VARIAVEL|REPVARIAVEL|)");
	TOKEN_PARSING_MAP.put("66,21", "WRITELN|(|ITEMSAIDA|REPITEM|)");
	TOKEN_PARSING_MAP.put("66,25", "IDENTIFICADOR|RCOMID");
	TOKEN_PARSING_MAP.put("66,27", "FOR|IDENTIFICADOR|:=|EXPRESSAO|TO|EXPRESSAO|DO|COMANDO");
	TOKEN_PARSING_MAP.put("66,29", "CASE|EXPRESSAO|OF|CONDCASE|END");
	TOKEN_PARSING_MAP.put("66,47", null);
	TOKEN_PARSING_MAP.put("66,6", "CORPO");
	TOKEN_PARSING_MAP.put("66,7", null);
	TOKEN_PARSING_MAP.put("67,34", "RVAR|:=|EXPRESSAO");
	TOKEN_PARSING_MAP.put("67,38", "RVAR|:=|EXPRESSAO");
	TOKEN_PARSING_MAP.put("67,39", ":|COMANDO");
	TOKEN_PARSING_MAP.put("68,34", "[|EXPRESSAO|]");
	TOKEN_PARSING_MAP.put("68,38", null);
	TOKEN_PARSING_MAP.put("69,15", null);
	TOKEN_PARSING_MAP.put("69,19", null);
	TOKEN_PARSING_MAP.put("69,36", "(|EXPRESSAO|REPPAR|)");
	TOKEN_PARSING_MAP.put("69,47", null);
	TOKEN_PARSING_MAP.put("69,7", null);
	TOKEN_PARSING_MAP.put("70,37", null);
	TOKEN_PARSING_MAP.put("70,46", ",|EXPRESSAO|REPPAR");
	TOKEN_PARSING_MAP.put("71,15", "ELSE|COMANDO");
	TOKEN_PARSING_MAP.put("71,19", null);
	TOKEN_PARSING_MAP.put("71,47", null);
	TOKEN_PARSING_MAP.put("71,7", null);
	TOKEN_PARSING_MAP.put("72,25", "IDENTIFICADOR|VARIAVEL1");
	TOKEN_PARSING_MAP.put("73,10", null);
	TOKEN_PARSING_MAP.put("73,14", null);
	TOKEN_PARSING_MAP.put("73,15", null);
	TOKEN_PARSING_MAP.put("73,17", null);
	TOKEN_PARSING_MAP.put("73,19", null);
	TOKEN_PARSING_MAP.put("73,22", null);
	TOKEN_PARSING_MAP.put("73,23", null);
	TOKEN_PARSING_MAP.put("73,28", null);
	TOKEN_PARSING_MAP.put("73,30", null);
	TOKEN_PARSING_MAP.put("73,31", null);
	TOKEN_PARSING_MAP.put("73,32", null);
	TOKEN_PARSING_MAP.put("73,33", null);
	TOKEN_PARSING_MAP.put("73,34", "[|EXPRESSAO|]");
	TOKEN_PARSING_MAP.put("73,35", null);
	TOKEN_PARSING_MAP.put("73,37", null);
	TOKEN_PARSING_MAP.put("73,40", null);
	TOKEN_PARSING_MAP.put("73,41", null);
	TOKEN_PARSING_MAP.put("73,42", null);
	TOKEN_PARSING_MAP.put("73,43", null);
	TOKEN_PARSING_MAP.put("73,44", null);
	TOKEN_PARSING_MAP.put("73,45", null);
	TOKEN_PARSING_MAP.put("73,46", null);
	TOKEN_PARSING_MAP.put("73,47", null);
	TOKEN_PARSING_MAP.put("73,7", null);
	TOKEN_PARSING_MAP.put("74,37", null);
	TOKEN_PARSING_MAP.put("74,46", ",|VARIAVEL|REPVARIAVEL");
	TOKEN_PARSING_MAP.put("75,24", "EXPRESSAO");
	TOKEN_PARSING_MAP.put("75,25", "EXPRESSAO");
	TOKEN_PARSING_MAP.put("75,26", "EXPRESSAO");
	TOKEN_PARSING_MAP.put("75,30", "EXPRESSAO");
	TOKEN_PARSING_MAP.put("75,31", "EXPRESSAO");
	TOKEN_PARSING_MAP.put("75,36", "EXPRESSAO");
	TOKEN_PARSING_MAP.put("75,48", "LITERAL");
	TOKEN_PARSING_MAP.put("76,37", null);
	TOKEN_PARSING_MAP.put("76,46", ",|ITEMSAIDA|REPITEM");
	TOKEN_PARSING_MAP.put("77,24", "EXPSIMP|REPEXPSIMP");
	TOKEN_PARSING_MAP.put("77,25", "EXPSIMP|REPEXPSIMP");
	TOKEN_PARSING_MAP.put("77,26", "EXPSIMP|REPEXPSIMP");
	TOKEN_PARSING_MAP.put("77,30", "EXPSIMP|REPEXPSIMP");
	TOKEN_PARSING_MAP.put("77,31", "EXPSIMP|REPEXPSIMP");
	TOKEN_PARSING_MAP.put("77,36", "EXPSIMP|REPEXPSIMP");
	TOKEN_PARSING_MAP.put("78,10", null);
	TOKEN_PARSING_MAP.put("78,14", null);
	TOKEN_PARSING_MAP.put("78,15", null);
	TOKEN_PARSING_MAP.put("78,17", null);
	TOKEN_PARSING_MAP.put("78,19", null);
	TOKEN_PARSING_MAP.put("78,28", null);
	TOKEN_PARSING_MAP.put("78,35", null);
	TOKEN_PARSING_MAP.put("78,37", null);
	TOKEN_PARSING_MAP.put("78,40", "=|EXPSIMP");
	TOKEN_PARSING_MAP.put("78,41", ">|EXPSIMP");
	TOKEN_PARSING_MAP.put("78,42", ">=|EXPSIMP");
	TOKEN_PARSING_MAP.put("78,43", "<|EXPSIMP");
	TOKEN_PARSING_MAP.put("78,44", "<=|EXPSIMP");
	TOKEN_PARSING_MAP.put("78,45", "<>|EXPSIMP");
	TOKEN_PARSING_MAP.put("78,46", null);
	TOKEN_PARSING_MAP.put("78,47", null);
	TOKEN_PARSING_MAP.put("78,7", null);
	TOKEN_PARSING_MAP.put("79,24", "TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("79,25", "TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("79,26", "TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("79,30", "+|TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("79,31", "-|TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("79,36", "TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("80,10", null);
	TOKEN_PARSING_MAP.put("80,14", null);
	TOKEN_PARSING_MAP.put("80,15", null);
	TOKEN_PARSING_MAP.put("80,17", null);
	TOKEN_PARSING_MAP.put("80,19", null);
	TOKEN_PARSING_MAP.put("80,22", "OR|TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("80,28", null);
	TOKEN_PARSING_MAP.put("80,30", "+|TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("80,31", "-|TERMO|REPEXP");
	TOKEN_PARSING_MAP.put("80,35", null);
	TOKEN_PARSING_MAP.put("80,37", null);
	TOKEN_PARSING_MAP.put("80,40", null);
	TOKEN_PARSING_MAP.put("80,41", null);
	TOKEN_PARSING_MAP.put("80,42", null);
	TOKEN_PARSING_MAP.put("80,43", null);
	TOKEN_PARSING_MAP.put("80,44", null);
	TOKEN_PARSING_MAP.put("80,45", null);
	TOKEN_PARSING_MAP.put("80,46", null);
	TOKEN_PARSING_MAP.put("80,47", null);
	TOKEN_PARSING_MAP.put("80,7", null);
	TOKEN_PARSING_MAP.put("81,24", "FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("81,25", "FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("81,26", "FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("81,36", "FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("82,10", null);
	TOKEN_PARSING_MAP.put("82,14", null);
	TOKEN_PARSING_MAP.put("82,15", null);
	TOKEN_PARSING_MAP.put("82,17", null);
	TOKEN_PARSING_MAP.put("82,19", null);
	TOKEN_PARSING_MAP.put("82,22", null);
	TOKEN_PARSING_MAP.put("82,23", "AND|FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("82,28", null);
	TOKEN_PARSING_MAP.put("82,30", null);
	TOKEN_PARSING_MAP.put("82,31", null);
	TOKEN_PARSING_MAP.put("82,32", "*|FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("82,33", "/|FATOR|REPTERMO");
	TOKEN_PARSING_MAP.put("82,35", null);
	TOKEN_PARSING_MAP.put("82,37", null);
	TOKEN_PARSING_MAP.put("82,40", null);
	TOKEN_PARSING_MAP.put("82,41", null);
	TOKEN_PARSING_MAP.put("82,42", null);
	TOKEN_PARSING_MAP.put("82,43", null);
	TOKEN_PARSING_MAP.put("82,44", null);
	TOKEN_PARSING_MAP.put("82,45", null);
	TOKEN_PARSING_MAP.put("82,46", null);
	TOKEN_PARSING_MAP.put("82,47", null);
	TOKEN_PARSING_MAP.put("82,7", null);
	TOKEN_PARSING_MAP.put("83,24", "NOT|FATOR");
	TOKEN_PARSING_MAP.put("83,25", "VARIAVEL");
	TOKEN_PARSING_MAP.put("83,26", "INTEIRO");
	TOKEN_PARSING_MAP.put("83,36", "(|EXPRESSAO|)");
	TOKEN_PARSING_MAP.put("84,26", "INTEIRO|RPINTEIRO|:|COMANDO|CONTCASE");
	TOKEN_PARSING_MAP.put("85,47", ";|CONDCASE");
	TOKEN_PARSING_MAP.put("85,7", null);
	TOKEN_PARSING_MAP.put("86,39", null);
	TOKEN_PARSING_MAP.put("86,46", ",|INTEIRO|RPINTEIRO");
    }

    public static Stack<String> getTokenCommands(String key) {
        if (!TOKEN_PARSING_MAP.containsKey(key)) {
            return null;
        }

        String value = TOKEN_PARSING_MAP.get(key);
        Stack<String> result = new Stack<>();
        if (value == null) {
            return result;
        }

        String[] values = value.split("\\|");

        for (int i = values.length - 1; i > -1; i--) {
            result.add(values[i]);
        }

        return result;
    }
}
