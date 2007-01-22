package Analizador_Lexico;

public class Token {
    
    private String sLexema;
    private String sGrupoLexico; 

    public Token() 
    {
    }
    
    public void setLexema(String lexema)
    {
       this.sLexema = lexema; 
    }
    
    public String getLexema()
    {
        return this.sLexema;
    }
    
    public void setGrupoLexico(String grupoLexico)
    {
       this.sGrupoLexico = grupoLexico; 
    }
    
    public String getGrupoLexico()
    {
        return this.sGrupoLexico;
    }
}
