package Analizador_Lexico;

//import java.util.*;

public class TiposNativos {
    public static final String PROG = "PROGRAMA";
    public static final String VAR = "VAR";
    public static final String FINVAR = "FINVAR";
    public static final String COMIENZO = "COMIENZO";
    public static final String FIN = "FIN.";
    public static final String NUM = "num";
    public static final String BOOL = "bool";
    public static final String CHAR = "char";
    public static final String IDEN = "iden";
    public static final String ASIG = "asig";
    public static final String FINDEC = "$";
    public static final String PYC = ";";
    public static final String COMA = ",";
    public static final String SUMA = "+";
    public static final String RESTA = "-";
    public static final String MUL = "*";
    public static final String DIV = "/";
    public static final String PAP = "(";
    public static final String PCI = ")";
    public static final String NGR = "";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NOT = "¬";
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String MENOR = "<";
    public static final String MAYOR = ">";
    public static final String IGUAL = "=";
    
    public static String getGrupoLexico(char sEntrada)
    {
        switch(sEntrada)
        {
            case ',':return COMA;
            case ';':return PYC;
            case '+':return SUMA;        
            case '-':return RESTA;
            case '*':return MUL;
            case '/':return DIV;
            case '(':return PAP;
            case ')':return PCI;
            case 'a':return ASIG;
            case '=':return IGUAL;
            case '<':return MENOR;
            case '>':return MAYOR;
            case '¬':return NOT;
            default:return NGR;
        }
    }
    
    public boolean esLetra(char cEntrada)
    {   
        if (((cEntrada-'A')>=0 && ('Z'-cEntrada)>=0)||
                ((cEntrada-'a')>=0 && ('z'-cEntrada)>=0))
                return true;
        return false;
    }    
    public boolean esCifra(char cEntrada)
    {
        if ((cEntrada-'0')>=0 && ('9'-cEntrada)>=0)
                return true;
        return false;
    }
}
