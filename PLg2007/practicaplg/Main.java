package practicaplg;

import Interfaz.Interfaz;


/**clase principal de la aplicaci�n*/
public class Main {
	
	
	public static Interfaz grafica;
	
	/**funci�n principal del programa en la que llamamos al analizador sint�ctico*/
    public static void main(String[] args) 
    {
    	System.out.println("El Compilador ha comenzado");
        grafica = new Interfaz();        
    }
    
}
