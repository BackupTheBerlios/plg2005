package Analizador_Sintactico;

import java.util.*;
import java.util.Vector;
//import Analizador_Lexico.TiposNativos;

public class TablaDeSimbolos {
    
    class Simbolo
    {
        private String sLexema;
        private String oTipo;
        //private String sValor;
        private int iMem;
        
        public Simbolo(String sLexema, String oTipo, String sValor, int iMem)
        {
            this.sLexema = sLexema;
            this.oTipo = oTipo;
            //this.sValor = sValor;
            this.iMem = iMem;
        }
    }
    private Vector oTabla;
    private int iContadorMem;
    
    /** Creates a new instance of TablaDeSimbolos */
    public TablaDeSimbolos() 
    {
        oTabla = new Vector();
        this.iContadorMem = -1;        
    }
    public void Inserta(String sLexema, String oTipo, String sValor)
    {       
       Simbolo oSimbolo = new Simbolo(sLexema,oTipo,sValor,this.iContadorMem);
       this.oTabla.addElement(oSimbolo);
       this.iContadorMem++;
    }
    
    public boolean ExisteId(String sLexema)
    {  
       Simbolo oSimbolo; 
       Iterator it = this.oTabla.iterator();
       while(it.hasNext())
       {
           oSimbolo = (Simbolo)it.next();
           if(oSimbolo.sLexema.equals(sLexema)) 
           {    
               return true;           
           }
       }
       return false;
    }
    
    public int DamePos(String sLexema)
    {
       Simbolo oSimbolo; 
       Iterator it = this.oTabla.iterator();
       while(it.hasNext())
       {
           oSimbolo = (Simbolo)it.next();
           if(oSimbolo.sLexema.equals(sLexema)) 
           {    
               return oSimbolo.iMem;           
           }
       }
       return -1; 
    }
    
    public String DameTipo(int iPos)
    {
       Simbolo oSimbolo; 
       Iterator it = this.oTabla.iterator();
       while(it.hasNext())
       {
           oSimbolo = (Simbolo)it.next();
           if(oSimbolo.iMem == iPos) 
           {    
               return oSimbolo.oTipo;           
           }
       }
       return null; 
    }
    
}
