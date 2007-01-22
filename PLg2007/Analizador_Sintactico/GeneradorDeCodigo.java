package Analizador_Sintactico;


import java.util.Vector;

public class GeneradorDeCodigo {
    
    
    public Vector cod;
    /** Creates a new instance of GeneradorDeCodigo */
    public GeneradorDeCodigo() 
    {
        cod = new Vector();
    }
    public void Emite(String sInstruccion,int iMemDir, String sTipo)
    {
        Instruccion oInst = new Instruccion(sInstruccion,sTipo,iMemDir);
        this.cod.addElement((Object)oInst);
    }
    
    
}
