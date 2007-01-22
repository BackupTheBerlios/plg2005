package Analizador_Lexico;


//import java.util.StringTokenizer;

public class AnalizadorLexico 
{    
    private String lex = "";
    private char cBuffer;
    private int iEstado;
    private int iCharCont;
    private int iLinea;
    private char[] sExaminado;
    TiposNativos tn;
    
    
    public AnalizadorLexico(String sPrograma)
    { 
        tn = new TiposNativos();
        this.iLinea = 1;
        this.iEstado = 0;
        this.iCharCont = 0;
        this.sExaminado = sPrograma.toCharArray();
    }
    
    private void CambioDeEstado(int iEstado)
    {
        if(tn.esLetra(cBuffer) || tn.esCifra(cBuffer) || lex.equals("") || lex.equals(":")){
            lex = lex + cBuffer;
            cBuffer = otroCaracter();
        }
        this.iEstado = iEstado;
    }
    
    private char otroCaracter()
    {
      
      if (++(this.iCharCont) < this.sExaminado.length)
      {
          return this.sExaminado[this.iCharCont];  
      }
      else return ' ';
    }
    
    public Token Escanear()//throws Exception
    {   
        try{
            Token tToken = new Token();
            String tnGrupoLexico = null;
            this.cBuffer = this.sExaminado[this.iCharCont];
            while(true){
                switch(this.iEstado)
                {
                    case 0: 
                    {
                        this.lex = "";
                        if (this.cBuffer == '\t' || this.cBuffer == '\n' 
                            || this.cBuffer == ' ')
                            this.CambioDeEstado(0);
                        
                        else if (this.cBuffer == 'P')this.CambioDeEstado(8);
                        
                        else if (this.cBuffer == 'V')this.CambioDeEstado(15);
                        
                        else if (this.cBuffer == 'F')this.CambioDeEstado(17);
                        
                        else if (this.cBuffer == 'C')this.CambioDeEstado(22);
                        
                        else if (this.cBuffer == 'n')this.CambioDeEstado(29);
                        
                        else if (this.cBuffer == 'b')this.CambioDeEstado(31);
                        
                        else if (this.cBuffer == 't')this.CambioDeEstado(34);
                        
                        else if (this.cBuffer == 'f')this.CambioDeEstado(37);
                        
                        else if (this.cBuffer == 'O')this.CambioDeEstado(41);
                        
                        else if (this.cBuffer == 'A')this.CambioDeEstado(42);
                        
                        else if (tn.esLetra(this.cBuffer))this.CambioDeEstado(1);
                        
                        else if (this.cBuffer == '0')this.CambioDeEstado(2);
                        
                        else if (tn.esCifra(this.cBuffer))this.CambioDeEstado(3);
                        
                        else if (this.cBuffer == ':')this.CambioDeEstado(6);
                        
                        else if (this.cBuffer == '&' || this.cBuffer == '$' 
                            || this.cBuffer == '+' || this.cBuffer == '-'
                            || this.cBuffer == '*' || this.cBuffer == ';' 
                            || this.cBuffer == '(' || this.cBuffer == ')' 
                            || this.cBuffer == '/' || this.cBuffer == ','
                            || this.cBuffer == '=' || this.cBuffer == '<'
                            || this.cBuffer == '>' || this.cBuffer == '¬')
                        {
                            tnGrupoLexico = TiposNativos.getGrupoLexico(this.cBuffer);
                            if(tnGrupoLexico.equals(TiposNativos.PYC)) iLinea++;
                            this.CambioDeEstado(7);
                        }
                        break;
                    }
                    case 1:
                    {                           
                        if (tn.esLetra(this.cBuffer) ||
                                    tn.esCifra(this.cBuffer))
                        {
                            this.CambioDeEstado(1);                            
                        }
                        else 
                        {
                            this.iEstado = 0;
                            tToken.setGrupoLexico(TiposNativos.IDEN);
                            tToken.setLexema(this.lex);
                            this.lex = "";
                            return tToken;                    
                        }
                        break;
                    } 
                    case 2:
                    {
                        if (this.cBuffer == '.')
                        {
                            this.CambioDeEstado(4);
                        }
                        else 
                        {
                            this.iEstado = 0;
                            tToken.setGrupoLexico(TiposNativos.NUM);
                            tToken.setLexema(this.lex);
                            this.lex = "";
                            return tToken;
                            
                        }
                        break;
                    }
                    case 3:
                    {
                        if (tn.esCifra(this.cBuffer))
                        {
                            this.CambioDeEstado(3);
                        }    
                        else 
                        {  
                            this.iEstado = 0;
                            tToken.setGrupoLexico(TiposNativos.NUM);
                            tToken.setLexema(this.lex);
                            this.lex = "";
                            return tToken;
                            
                        }
                         break;
                    }
                    case 4:
                    {
                        if (tn.esCifra(this.cBuffer))
                        {
                            this.CambioDeEstado(5);
                        }    
                        else 
                        {
                            this.iEstado = 0;
                            throw new Exception("Error S4: Se esperaba [0-9]");
                        }
                        break;
                    }
                    case 5:
                    {
                        if (tn.esCifra(this.cBuffer))
                        {
                            this.CambioDeEstado(5);
                        }    
                        else
                        {
                            this.iEstado = 0;
                            tToken.setGrupoLexico(TiposNativos.NUM);
                            tToken.setLexema(this.lex);
                            this.lex = "";
                            return tToken;
                        }
                        break;
                    }
                    case 6:
                    {
                        if (this.cBuffer == '=')
                        {
                            tnGrupoLexico = TiposNativos.ASIG;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                            this.iEstado = 0;
                            tToken.setGrupoLexico(TiposNativos.NUM);
                            tToken.setLexema(this.lex);
                            this.lex = "";
                            return tToken;
                        }
                        break;
                    } 
                    case 7: 
                    {
                        this.iEstado = 0;
                        tToken.setGrupoLexico(tnGrupoLexico);
                        tToken.setLexema(this.lex);
                        this.lex = "";
                        return tToken;                        
                    }
                    case 8:
                    {
                        if (this.cBuffer == 'R')this.CambioDeEstado(9);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 9:
                    {
                        if (this.cBuffer == 'O')this.CambioDeEstado(10);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 10:
                    {
                        if (this.cBuffer == 'G')this.CambioDeEstado(11);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 11:
                    {
                        if (this.cBuffer == 'R')this.CambioDeEstado(12);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 12:
                    {
                        if (this.cBuffer == 'A')this.CambioDeEstado(13);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 13:
                    {
                        if (this.cBuffer == 'M')this.CambioDeEstado(14);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 14: 
                    {
                        if(this.cBuffer == 'A'){
                            iLinea++;
                            tnGrupoLexico = TiposNativos.PROG;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 15:
                    {
                        if (this.cBuffer == 'A')this.CambioDeEstado(16);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 16: 
                    {
                        if(this.cBuffer == 'R'){
                            iLinea++;
                            tnGrupoLexico = TiposNativos.VAR;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                     case 17:
                    {
                        if (this.cBuffer == 'I')this.CambioDeEstado(18);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 18:
                    {
                       if (this.cBuffer == 'N')this.CambioDeEstado(19);
                       else this.CambioDeEstado(1);
                       break;
                    }
                    case 19:
                    {
                        if (this.cBuffer == '.'){
                            iLinea++;
                            tnGrupoLexico = TiposNativos.FIN;
                            this.CambioDeEstado(7);
                            break;
                        }
                        if (this.cBuffer == 'V')this.CambioDeEstado(20);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 20:
                    {
                        if (this.cBuffer == 'A')this.CambioDeEstado(21);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 21: 
                    {
                        if(this.cBuffer == 'R'){
                            iLinea++;
                            tnGrupoLexico = TiposNativos.FINVAR;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 22:
                    {
                        if (this.cBuffer == 'O')this.CambioDeEstado(23);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 23:
                    {
                        if (this.cBuffer == 'M')this.CambioDeEstado(24);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 24:
                    {
                        if (this.cBuffer == 'I')this.CambioDeEstado(25);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 25:
                    {
                        if (this.cBuffer == 'E')this.CambioDeEstado(26);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 26:
                    {
                        if (this.cBuffer == 'N')this.CambioDeEstado(27);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 27:
                    {
                        if (this.cBuffer == 'Z')this.CambioDeEstado(28);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 28: 
                    {
                        if(this.cBuffer == 'O'){
                            iLinea++;
                            tnGrupoLexico = TiposNativos.COMIENZO;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 29:
                    {
                        if (this.cBuffer == 'u')this.CambioDeEstado(30);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 30: 
                    {
                        if(this.cBuffer == 'm'){
                            tnGrupoLexico = TiposNativos.NUM;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 31:
                    {
                        if (this.cBuffer == 'o')this.CambioDeEstado(32);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 32:
                    {
                        if (this.cBuffer == 'o')this.CambioDeEstado(33);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 33: 
                    {
                        if(this.cBuffer == 'l'){
                            tnGrupoLexico = TiposNativos.BOOL;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 34:
                    {
                        if (this.cBuffer == 'r')this.CambioDeEstado(35);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 35:
                    {
                        if (this.cBuffer == 'u')this.CambioDeEstado(36);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 36: 
                    {
                        if(this.cBuffer == 'e'){
                            tnGrupoLexico = TiposNativos.TRUE;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 37:
                    {
                        if (this.cBuffer == 'a')this.CambioDeEstado(38);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 38:
                    {
                        if (this.cBuffer == 'l')this.CambioDeEstado(39);
                        else this.CambioDeEstado(1);
                        break;
                    }
                     case 39:
                    {
                        if (this.cBuffer == 's')this.CambioDeEstado(40);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 40: 
                    {
                        if(this.cBuffer == 'e'){
                            tnGrupoLexico = TiposNativos.FALSE;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                    case 41: 
                    {
                        if(this.cBuffer == 'R'){
                            tnGrupoLexico = TiposNativos.OR;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                     case 42:
                    {
                        if (this.cBuffer == 'N')this.CambioDeEstado(43);
                        else this.CambioDeEstado(1);
                        break;
                    }
                    case 43: 
                    {
                        if(this.cBuffer == 'D'){
                            tnGrupoLexico = TiposNativos.AND;
                            this.CambioDeEstado(7);
                        }
                        else
                        {
                           this.CambioDeEstado(1);
                        }
                        break;
                    }
                }                
            }
        }    
        catch(Exception ex)
        {
            System.out.print(ex.getMessage());        
        }
        return null;
    } 
    
    public int DameLinea(){
        return this.iLinea;
    }
    
}
