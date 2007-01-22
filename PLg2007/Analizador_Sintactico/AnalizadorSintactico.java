package Analizador_Sintactico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.util.LinkedList;
import java.io.IOException;

import Analizador_Lexico.*;

/**clase que implementa el analizador sintáctico del compilador*/
public class AnalizadorSintactico {
  
	/*analizador léxico del analizador sintáctico*/
	private AnalizadorLexico anLexico;
	
	/*códigos de las palabras reservadas del lenguaje
	 * CODIGOS: 256=PROGRAMA  257=VAR  258=COMIENZO 
	 * 259=FIN  260=BOOLEANO  261=ENTERO
	 * 262=CIERTO  263=FALSO 264= '==' 
	 * 265= '<>' 266= '>=' 267= '<='
	 * 268= AND 269= OR 270= NO
	 * */
	//private int[] palabrasReservadas;
	
	/*códigos para distinguir entre identificador, número y programa leído
	  CODIGOS:	300=done  400=id  500=num  */
	//private int[] ident;
	
	/*tabla de símbolos del analizador sintáctico*/
	private TablaDeSimbolos ts;
	
	/*dirección de memoria que se asignará a los identificadores al 
	  introducirlos en la tabla de símbolos*/
	//private int dirMem;
	
	//tipo de declaracion de variables (atributo remoto)
	private String tipo;

	//Token que devuelve el anLex
	private Token oToken; 
	
    //lexema del anterior token devuelto por el an Lex
	private String lexem;  
	
	//generador de código del analizador sintáctico
	public GeneradorDeCodigo genCodigo;
	
	//
    public String codError;
	private boolean error;


/**constructor del analizador sintáctico. En él se inicializa la tabla de símbolos
con las palabras reservadas del lenguaje
Recibe:
@ fich: fichero con el programa fuente
@ nombreFich: nombre del fichero fuente
@ tokens: códigos de las palabras reservadas
@ ident:otros códigos especiales
*/
  public AnalizadorSintactico(BufferedReader fich,String nombreFich/*,int[] tokens,int[] ident*/)
	{
	  this.error=false;
	  //this.ident=ident;
	  ts=new TablaDeSimbolos();
	  genCodigo=new GeneradorDeCodigo(/*nombreFich*/);
      String sPrograma = "";
	  //-------
	  try{
            fich= new BufferedReader(new FileReader(nombreFich));
           }catch (FileNotFoundException e){
		  
	  }
          try{
            String sAux = fich.readLine();
            while(sAux != null)
            {
                sPrograma = sPrograma + sAux;
                sAux = fich.readLine();
            }
          }catch(IOException e){}
	 
          
	  //---------
	  anLexico=new AnalizadorLexico(sPrograma/*fich,ident,ts*/);
	  //otras inicializaciones
	  //dirMem=0;//direccion inicila de memoria
	  //codigoToken= -1;
	  lexem="";
	  tipo=null;
	  
	}

  

  /*función reconocedora de tokens*/
/*  public boolean concuerda(int palabra) {  
    if (codigoToken == palabra) {
    	codigoToken = anLexico.scanner();
    	return true;
    }
    else {
    	return false;
    }
  }*/
  
  /*método para mostrar los errores por pantalla*/
	public void error(String texto)throws Exception
	{
		this.error=true;
		int linea= anLexico.DameLinea();
//		LinkedList datos=new LinkedList();
		codError = "Error en la linea "+linea+" de tipo: "+texto+"\nPrograma incorrecto\n--\n";
//		datos.add(new String(Interfaz.dameCodError()));
//		Interfaz.actualizaVista(4,datos);
                throw new Exception();
	}

  /**
   * Este método implementa la producción
   * Programa ::= Cabecera Declaraciones Instrucciones
   * devuelve el numero de instrucciones generadas por la maquinaP
   */
  public int Programa()throws Exception {
     //int numInst=0;
    
         Cabecera();
         Declaraciones();
         Instrucciones();
         genCodigo.Emite("stop", -1, "");
         //System.out.println("Compilación terminada");         
     
     //generar el fichero objeto       
     return this.genCodigo.cod.size();
  }

  //Cabecera ::= PROGRAMA ident
  public void Cabecera()throws Exception {
   
    this.oToken= anLexico.Escanear();
    
    if (this.oToken.getGrupoLexico().equals(TiposNativos.PROG)){ // reconoce 'PROGRAMA'
    	this.oToken= anLexico.Escanear();
        if (this.oToken.getGrupoLexico().equals(TiposNativos.IDEN)){//reconoce un identificador
                lexem=this.oToken.getLexema();
    		ts.Inserta(lexem,TiposNativos.IDEN, "Nombre del programa");
    		//dirMem -1 porque no es una variable
    	
    	}else
    		error("Sintactico. \nSe esperaba identificador");
    }else
    	error("Sintactico. \nSe esperaba 'PROGRAMA'");
    
  }

  //Declaraciones ::= VAR ListaDecs
  public void Declaraciones() throws Exception{
    this.oToken= anLexico.Escanear();
    if (this.oToken.getGrupoLexico().equals(TiposNativos.VAR)){//reconoce 'VAR'
    	ListaDecs();    	
    }else
    	error("Sintactico. \nSe esperaba 'VAR'");
        while(!this.oToken.getGrupoLexico().equals(TiposNativos.FINVAR))
            this.oToken= anLexico.Escanear();
  }

//Instrucciones ::= COMIENZO ListaInstr FIN
  public void Instrucciones()throws Exception {
          this.oToken = anLexico.Escanear();
	  if(this.oToken.getGrupoLexico().equals(TiposNativos.COMIENZO)){//reconce COMIENZO
		  ListaInstr();
                  if(!(this.oToken.getGrupoLexico().equals(TiposNativos.FIN))){//reconoce FIN.
			  error("Sintactico. \nSe esperaba 'FIN'");
		  }
	  }else
		  error("Sintactico. \nSe esperaba 'COMIENZO'");
	     
  }

  //ListaDecs ::= Dec RListaDecs
  public void ListaDecs() throws Exception{

    Dec();
    RListaDecs();
  }

  //RListaDecs ::= Dec RListaDecs
  public void RListaDecs() throws Exception{

        //tipos entero y booleano
	
        if(!(this.oToken.getGrupoLexico().equals(TiposNativos.FINVAR)) && (this.oToken.getGrupoLexico() != null))
        {
            Dec();
            RListaDecs();
        }
//	si no hay un tipo se interpreta como la produccion vacía
  }
  
  //Dec ::= Tipo ListaIdent ;| vacío
  public void Dec()throws Exception {

    tipo= Tipo();//
    if((tipo != null) && !(tipo.equals(TiposNativos.FINVAR)) )
    {
        ListaIdent(tipo);        
        if (!this.oToken.getGrupoLexico().equals(TiposNativos.PYC)) //no concuerda con ';'
        {
            error("Sintactico. \nSe esperaba: ';'");
             while(!this.oToken.getGrupoLexico().equals(TiposNativos.PYC))
                this.oToken= anLexico.Escanear();
        }
    }
  }

  //ListaIdent ::= ident RListaIdent
  public void ListaIdent(String tipo)throws Exception {
          this.oToken = this.anLexico.Escanear();
	  lexem=this.oToken.getLexema();//obtiene el lexema del token actual
	  if(this.oToken.getGrupoLexico().equals(TiposNativos.IDEN)){//si se lee un identificador
		  //se añade a la Tabla de símbolos
              if(!ts.ExisteId(lexem))
              {
		 ts.Inserta(lexem, tipo,"");
                 RListaIdent(tipo);
              }else
              {//si el identificador está repetido
        	    error("Contextual \nIdentificador repetido");
              }
	  }else
		  error("Sintactico. \nSe esperaba un identificador");   
  }
  
//RListaIdent ::= , ListaIdent | vacio  
public void RListaIdent(String tipo)throws Exception {
	
          this.oToken = this.anLexico.Escanear();
	  if(this.oToken.getGrupoLexico().equals(TiposNativos.COMA)){//se lee una ','
		ListaIdent(tipo);
	  }
//	si no hay ',' se interpreta como la produccion vacía
  }

  //Tipo ::= Entero | Booleano
  public String Tipo() throws Exception
  {
    //String oTipo;  
    this.oToken = this.anLexico.Escanear();
    if (this.oToken.getGrupoLexico().equals(TiposNativos.BOOL))//reconoce BOOLEANO
    {    	
    	return TiposNativos.BOOL;
    }else if (this.oToken.getGrupoLexico().equals(TiposNativos.NUM)){
    		return TiposNativos.NUM;
    }else if (this.oToken.getGrupoLexico().equals(TiposNativos.FINVAR)){
    		return TiposNativos.FINVAR;
    }else
              error("Sintactico. \nSe esperaba una declaracion de tipo");
              return null;	
  }

//ListaInstr ::= Instr  RListaInstr
  public void ListaInstr()throws Exception {
    
    Instr();
    RListaInstr();
   }
 
//RListaInstr ::= ; ListaInstr | vacio
  public void RListaInstr()throws Exception {
    
    if(this.oToken.getLexema().equals(TiposNativos.PYC)){//encuentra ';'
    	ListaInstr();
    }
    else if(!this.oToken.getGrupoLexico().equals(TiposNativos.FIN))
            error("Sintactico. \nSe esperaba ';'");
   }

//Instr ::= IAsig
  public void Instr() throws Exception{
   
    IAsig();

  }

  //IAsig ::= ident = ExpComp
  public void IAsig() throws Exception{
    //coger el ident
      this.oToken = this.anLexico.Escanear();
      if(this.oToken.getGrupoLexico().equals(TiposNativos.IDEN))//si es un identificador
      {
          lexem=this.oToken.getLexema();//obtiene el lexema del token actua
    //el ident debe estar en la TS
          if(!ts.ExisteId(lexem)) //buscamos el lexema en la TS
          {
              error("Contextual. \nIdentificador no declarado");
          }else{//el ident esta en la TS
              int iMemPos = ts.DamePos(lexem);
              //comprobar que un '=' sigue al ident
              this.oToken = this.anLexico.Escanear();
              if(this.oToken.getGrupoLexico().equals(TiposNativos.ASIG))//llega el operador de asignación
	      {  		  //evaluar la expresion
                    tipo= ExpComp();
					  //comprobar que el tipo de la ExpComp coincide
					  //con el del ident al que se asigna
					  if(coinciden(tipo, ts.DameTipo(iMemPos))){
					  	//si los tipos coinciden
					  	//generar código: desapilaDir(ts[iden.lex].dir) 
					  	genCodigo.Emite("desapila-dir", iMemPos,tipo);
					  }else{//no coinciden los tipos
					  	error("Contextual. \nNo concuerda el tipo de la expresion con el del identificador");
                                                 
					  }
		  		}else{//no llega operador '='
		  			error("Sintactico \nSe esperaba operador '='");                                         
		  		}
			  }
      }else if (this.oToken.getGrupoLexico().equals(TiposNativos.FIN)){;}	//Reconoce FIN.  
            else{//no se encuentra un ident al principio de la asignacion
		  error("Sintactico. \nSe esperaba un identificador");
	    }

  }

  //devuelve cierto si el token que se le pasa por parametro
  //es un operador relacional, y en ese caso pide el siguiente
  //al an Lex
  public boolean esRelacional(String sTipo)
  {
    if(sTipo.equals(TiposNativos.IGUAL))return true;//'=='
    if(sTipo.equals(TiposNativos.MENOR))return true;//'<'
    if(sTipo.equals(TiposNativos.MAYOR))return true;//'>'   
    return false;	
  }
  
  //devuelve cierto si el token que se le pasa por parametro
  //es un operador aditivo, y en ese caso pide el siguiente
  //al an Lex
  public boolean esAditivo(String sTipo)
  {
    if(sTipo.equals(TiposNativos.SUMA))return true;//'+'
    if(sTipo.equals(TiposNativos.RESTA))return true;//'-'
    if(sTipo.equals(TiposNativos.OR))return true;//'OR'   
    return false;    
  }
  
  //devuelve cierto si el token que se le pasa por parametro
  //es un operador multiplicativo, y en ese caso pide el siguiente
  //al an Lex
  public boolean esMultiplicativo(String sTipo)
  {
    if(sTipo.equals(TiposNativos.MUL))return true;//'*'
    if(sTipo.equals(TiposNativos.DIV))return true;//'/'
    if(sTipo.equals(TiposNativos.AND))return true;//'AND'   
    return false; 
  }
  
//ExpComp ::= Exp RExpComp
  public String ExpComp()throws Exception{
  	String expTipo, rexpCompTipo;//tipo de las siguientes producciones
  	expTipo= Exp();
  	rexpCompTipo= RExpComp(expTipo);
  	return rexpCompTipo;
  }
  
//ExpComp ::= opRel Exp | vacio
  public String RExpComp(String t)throws Exception{
  	String expTipo;
  	//comprobar si el token actual es un operador relacional
  	//codToken almacena el valor de el token actual
  	//(habrá llegado ahí de la ultima vez que se hizo concuerda)
//        this.oToken = this.anLexico.Escanear();
  	if (esRelacional(this.oToken.getGrupoLexico())){//si es un operador relacional
                String sGrupoLex = this.oToken.getGrupoLexico();
  		//se llama a Exp y se genera la instr apropiada
  		//para dicho opRel, si los tipos coindiden
  		expTipo=Exp();
  		if (coinciden(t,expTipo)){
  			//generar codigo
  			genCodigo.Emite(sGrupoLex,-1,sGrupoLex);
  			//------------------------
  			String tipo= TiposNativos.BOOL;
  			return tipo; //coinciden, luego dev booleano
  		}else{//no coinciden los tipos
  			error("Contextual. \nNo concuerdan los tipos a ambos lados del operador");
  			return t;// nunca entra aqui por que error aborta la ejecucion y asi el metodo no da error de compilacion
  			} 		
  	}else //no llega op relacional
  	//si no hay operador relacional
  	//lo interpretamos como la producción vacía
  		return t;  	//devuelve el tipo heredado   
  }

//Exp ::= Term RExp
  public String Exp()throws Exception{
  	String termTipo, respTipo;//tipos de las producciones
  	termTipo= Term();
  	respTipo= RExp(termTipo);
  	return respTipo;
    }

//RExp ::= opAd Term RExp | vacio
  public String RExp(String t)throws Exception{
  	String termTipo, rexpTipo;//variables aux para los tipos
  	//comprobar si el token actual es un op aditivo 
    //  codToken almacena el valor de el token actual
  	//(habrá llegado ahí de la ultima vez que se hizo concuerda)
 //       this.oToken=this.anLexico.Escanear();
  	if (esAditivo(this.oToken.getGrupoLexico())){//si es un op Ad
                String sGrupoLex = this.oToken.getGrupoLexico();
  		//generar codigo
  		termTipo= Term();
  		genCodigo.Emite(sGrupoLex,-1,sGrupoLex);
  		//comprobar los tipos y que sean del tipo apropiado para 
  		//el operador
  		if(coinciden(t, termTipo)&& //coinciden los tipos
                                ((t.equals(TiposNativos.BOOL))
      				||(t.equals(TiposNativos.NUM)))){
  			//omitimos comprobar que es entero xq es op aditivo no booleano=> entero 
  					rexpTipo= RExp(termTipo);
  					return rexpTipo;
  		}else
  			error("Contextual. \nNo concuerdan los tipos del operador aditivo");
  			return t;
  		//controlar error en caso de que no sena dele mismo tipo
  	}else {return t;}//devuelve el tipo heredado
  	//si no hay operador aditivo interpretamos
  	//que tenemos la producción vacía
  	
    }


//Term ::= Fact RTerm
    public String Term()throws Exception{
    	String factTipo, rtermTipo;//auxiliares para los tipos de las prod
    	factTipo= Fact();
        this.oToken = this.anLexico.Escanear();
    	rtermTipo= RTerm(factTipo);
    	return rtermTipo;
        
    }

// RTerm ::= opMul Fact RTerm | vacio
    public String RTerm(String t)throws Exception{
    	String factTipo, rtermTipo;//aux para los tipos de las prod
//    	comprobar si el token actual es un op multiplicativo
//      codToken almacena el valor de el token actual
      	//(habrá llegado ahí de la ultima vez que se hizo concuerda)
//      	this.oToken=this.anLexico.Escanear();
      	if (esMultiplicativo(this.oToken.getGrupoLexico())){//si es un op Mul
                String sGrupoLex = this.oToken.getGrupoLexico();
      		//generar codigo
      		factTipo= Fact();
      		genCodigo.Emite(sGrupoLex,-1,sGrupoLex);
      		//comprobar las coincidencias de tipo
      		if((coinciden(t, factTipo))&&
      				((t.equals(TiposNativos.BOOL))
      				||(t.equals(TiposNativos.NUM)))){
      			//se omite comprobar que es entero xq es un 
      			//operador multiplicativo no bool=> entero
      			rtermTipo= RTerm(factTipo);
          		return rtermTipo;
      		}else{
      			error("Contextual. \nNo coinciden los tipos a ambos lados del operador");
      			return t;
      		}
      		
      		//controlar error en caso de que no sena dele mismo tipo
      	}else{//no hay operador multiplicativo
      		return t;//devuelve lo heredado
      	}
      	//si no hay operador multiplicativo interpretamos
      	//que tenemos la producción vacía
        
    }
    
//Fact ::= ident | numero | cierto | 
//    falso | NO Fact | (ExpComp)
    public String Fact()throws Exception{
    	//obtenemos el lexema del token actual
        this.oToken = this.anLexico.Escanear();
    	lexem= this.oToken.getLexema();
    	//obtenemos su valor numerico, para el caso de que sea num
    	//y evaluamos dicho token
    	if (this.oToken.getGrupoLexico().equals(TiposNativos.IDEN)){//llega un ident
    		//comprobar restriciones contextuales y generar código
    		//miramos si esta en la TS
    		int pos= ts.DamePos(lexem);
    		if (pos != -1){//si esta en la TS
    			//guardar el tipo del identificador
    			tipo= ts.DameTipo(pos);
    			//generar codigo: apilaDir(ts[iden.lex].dir)
    			genCodigo.Emite("apila-dir",pos, tipo);
    			return tipo;
    		}else{//no esta en la TS
    			error("contextual. \nIdentificador no declarado");
    			return tipo;
    		}
    	}else if (this.oToken.getGrupoLexico().equals(TiposNativos.NUM))//llega un numero
    	{		tipo= TiposNativos.NUM;
                        //generar codigo: apila(valorDe(numero.lex))
                        genCodigo.Emite("apila",Integer.parseInt(this.oToken.getLexema()), tipo);
			return tipo;
    	}else if (this.oToken.getGrupoLexico().equals(TiposNativos.TRUE))//llega 'CIERTO'
    	{				
                        tipo= TiposNativos.BOOL;
                        //generar codigo: apila(1)
                        genCodigo.Emite("apila", 1, tipo);
                        return tipo;
    	}else if(this.oToken.getGrupoLexico().equals(TiposNativos.FALSE)){//llega 'FALSO'
                        tipo= TiposNativos.BOOL;
                        //generar codigo: apila(0)
                        genCodigo.Emite("apila", 0, tipo);
                        return tipo;

    	}else if (this.oToken.getGrupoLexico().equals(TiposNativos.PAP))//llega '('
    	{
                        tipo= ExpComp();
                        //ver que los paréntesis estan bien 
                        //pareados
                        this.oToken = this.anLexico.Escanear();
                        if (this.oToken.getGrupoLexico().equals(TiposNativos.PCI))//llega ')'
                        {
                                return tipo;
                        }else //no llega ')'
                        {
                                error("Sintactico. \nSe esperaba ')'");
                                return tipo;
                        }

    	}else if (this.oToken.getGrupoLexico().equals(TiposNativos.NOT))//lega 'NO'
    	{
            tipo= Fact();
            //generar codigo: 'NO'
            genCodigo.Emite("¬", 0, tipo);
            return tipo;
        }else{
                error("Sintactico. \nSimbolo no reconocido");
                return tipo;
        }
		
    	
     }//fin Fact

    
    //indica si los dos tipos dados coinciden
    public boolean coinciden(String tipo1, String tipo2){
    	return tipo1.equals(tipo2);
    }
    
    public boolean tieneError()
    {
    	return error;
    }
    
}




