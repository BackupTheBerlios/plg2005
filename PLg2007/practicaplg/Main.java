package practicaplg;

import Interfaz.Interfaz;


/**clase principal de la aplicación*/
public class Main {
	
	
	public static Interfaz grafica;
	
	/**función principal del programa en la que llamamos al analizador sintáctico*/
    public static void main(String[] args) 
    {
    	System.out.println("El Compilador ha comenzado");
        grafica = new Interfaz();        
    }
    
}
