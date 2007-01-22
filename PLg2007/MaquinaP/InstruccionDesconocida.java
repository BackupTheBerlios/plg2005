package MaquinaP;

/**Clase que implementa la excepcion producida por la maquina virtual cuando encuentra
una instruccion desconocida*/
public class InstruccionDesconocida extends Exception{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InstruccionDesconocida(){
		super();
	}

	public InstruccionDesconocida(String s){
		super(s);
	}

}