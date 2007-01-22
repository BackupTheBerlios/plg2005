package Analizador_Sintactico;

public class Instruccion
{
        public String sInstruccion;
        public int iMemDir;
        public String sTipo;
        
        public Instruccion(String sInstruccion, String sTipo,int iMemDir)
        {
            this.sInstruccion = sInstruccion;
            this.sTipo = sTipo;
            this.iMemDir = iMemDir;
        }
}
