package MaquinaP;

import java.util.*;
import java.io.*;

import Analizador_Sintactico.Instruccion;



/**Clase que implementa a una maquina P que interpreta el lenguaje objeto
y ejecuta sus operaciones*/
public class MaquinaP {
  //Vector que guarda el programa que se va a ejecutar
  private Vector programa;
  
  //Pila de la maquina que se utiliza para trabajar con los datos
  private Stack pila;
  
  //Memoria de la maquina, cada variable ocupa una posicion de la memoria
  private Vector memoria;
  
  //Contador de programa
  private int instruccionActual;
  
  //Variable de estado de la maquina que puede ser 'r' si se esta ejecutando
  //'s' si esta parada y 'e' si se encuentra en un estado de error
  //private char estado;

  //Constructor por defecto
  public MaquinaP(){
  }

  //Metodo que carga el fichero pasado como argumento en la memoria de la maquina
  public void carga(String fichero){
    programa=new Vector();
    File f=new File(fichero);
    //String instruccion;
    try{
      FileInputStream fi=new FileInputStream(f);
      DataInputStream in=new DataInputStream(fi);
      while(in.available()>0){
//        instruccion=in.readLine();
 //       programa.add(instruccion);
      }
    }catch(IOException e){
      System.err.println(e.getMessage());
    }
  }

  //Metodo que inicializa las componentes de la maquina
  private void inicializa(){
    pila=new Stack();
    memoria=new Vector();
    for(int i=0;i<10;i++){
      memoria.add("0");
    }
    instruccionActual=0;
    //estado='s';
  }

  //Metodo que ejecuta el programa
  public void ejecuta(){
    inicializa();
    //estado='r';
    boolean stop = false;
    try{
      //Recorre las instrucciones del programa hasta que se llega al final
      while(!stop){
        stop = ejecutaInstruccion((Instruccion)(programa.get(instruccionActual)));
        instruccionActual++;
      }
      //estado='s';
    }catch(InstruccionDesconocida e){
      System.err.println(e.getMessage());
      imprimePila();
      imprimeMemoria();
      //estado='e';
    }
  }

  //Metodo que se encarga de distinguir que tipo de operacion es y llamar al
  //metodo que ejecuta dicha operacion
  private boolean ejecutaInstruccion(Instruccion instruccion) throws InstruccionDesconocida{
      boolean stop = false;
     //Obtiene el nombre de la operacion que se va a realizar
    String tipoInstruccion=dameTipoInstruccion(instruccion.sInstruccion);
    if(tipoInstruccion.equals("stop")){
      stop = true;
    }
    else{if(esOperacionBinariaAritmetica(tipoInstruccion)){
      ejecutaInstruccionBinariaAritmetica(tipoInstruccion);
    }
    else{
      if(esOperacionBinariaBoolena(tipoInstruccion)){
        ejecutaInstruccionBinariaBooleana(tipoInstruccion);
    }
      else{
      if(esOperacionBinariaComparacion(tipoInstruccion)){
        ejecutaInstruccionBinariaComparacion(tipoInstruccion);
    }
    else{
      if(esOperacionUnaria(tipoInstruccion)){
        ejecutaInstruccionUnaria(tipoInstruccion);
    }
    else{
      String operando=dameOperandoInstruccion(instruccion);
      if(esOperacionPila(tipoInstruccion)){
        ejecutaInstruccionPila(tipoInstruccion,operando);
    }
    else{
      if(esOperacionControl(tipoInstruccion)){
        ejecutaInstruccionControl(tipoInstruccion,operando);
    }
    else{
        throw new InstruccionDesconocida("Instruccion desconocida: "+instruccion);
    }}}}}}} //end of if-else`s
    return stop;
}
  //Metodo que ejecuta las operaciones aritmeticas de dos operandos
  private void ejecutaInstruccionBinariaAritmetica(String instruccion) throws InstruccionDesconocida{
    //Desapila los operandos en orden inverso
    int op2=Integer.parseInt(pila.pop().toString());
    int op1=Integer.parseInt(pila.pop().toString());
    int resultado=0;
    //En funcion de la operacion calcula el resultado
    if(instruccion.equals("+")){
      resultado=op1+op2;
    }
    else{
      if(instruccion.equals("-")){
        resultado=op1-op2;
      }
      else{
        if(instruccion.equals("*")){
          resultado=op1*op2;
        }
        else{
          if(instruccion.equals("/")){
            resultado=op1/op2;
          }
          else{
            throw new InstruccionDesconocida("Instruccion Aritmetica no valida: "+instruccion);
          }
        }
      }
    }
    //El resultado queda almacenado en la cima de la pila
    pila.push(String.valueOf(resultado));
  }

  //Metodo que ejecuta las operaciones booleanas de dos operandos
  private void ejecutaInstruccionBinariaBooleana(String instruccion) throws InstruccionDesconocida{
    //Desapila los operandos en orden inverso
    boolean op2=Integer.parseInt(pila.pop().toString())==1;
    boolean op1=Integer.parseInt(pila.pop().toString())==1;
    boolean resultado=false;
    //En funcion de la operacion calcula el resultado
    if(instruccion.equals("AND")){
      resultado=op1 && op2;
    }
    else{
      if(instruccion.equals("OR")){
        resultado=op1 || op2;
      }
      else{
        throw new InstruccionDesconocida("Instruccion Booleana no valida: "+instruccion);
      }
    }
    //El resultado queda almacenado en la cima de la pila
    if(resultado){
      pila.push(String.valueOf(1));
    }
    else{
      pila.push(String.valueOf(0));
    }
  }

  //Metodo que ejecuta las operaciones de comparacion
  private void ejecutaInstruccionBinariaComparacion(String instruccion) throws InstruccionDesconocida{
    //Desapila los operandos en orden inverso
    int op2=Integer.parseInt(pila.pop().toString());
    int op1=Integer.parseInt(pila.pop().toString());
    boolean resultado=false;
    //En funcion de la operacion calcula el resultado
    if(instruccion.equals("=")){
      resultado=op1==op2;
    }
    else{
      if(instruccion.equals("distinto")){
        resultado=op1!=op2;
      }
      else{
        if(instruccion.equals("<")){
          resultado=op1<op2;
        }
        else{
          if(instruccion.equals("menori")){
            resultado=op1<=op2;
          }
          else{
            if(instruccion.equals(">")){
              resultado=op1>op2;
            }
            else{
              if(instruccion.equals("mayori")){
                resultado=op1>=op2;
              }
              else{
                throw new InstruccionDesconocida("Instruccion de Comparacion no valida: "+instruccion);
              }
            }
          }
        }
      }
    }
    //El resultado queda almacenado en la cima de la pila
    if(resultado){
      pila.push(String.valueOf(1));
    }
    else{
      pila.push(String.valueOf(0));
    }
  }

  //Metodo que ejecuta las operaciones unarias
  private void ejecutaInstruccionUnaria(String instruccion) throws InstruccionDesconocida{
    //Desapila el operando
    int op=Integer.parseInt(pila.pop().toString());
    //En funcion de la operacion modifica el valor del operando y lo vuelve
    //a colocar en la cima de la pila
    if(instruccion.equals("mas")){
      pila.push(String.valueOf(op));
    }
    else{
      if(instruccion.equals("menos")){
        pila.push(String.valueOf(-op));
      }
      else{
        if(instruccion.equals("¬")){
          if(op==1){
            pila.push(String.valueOf(0));
          }
          else{
            pila.push(String.valueOf(1));
          }
        }
        else{
          throw new InstruccionDesconocida("Instruccion Unaria no valida: "+instruccion);
        }
      }
    }
  }

  //Metodo que ejecuta las operaciones de pila
  private void ejecutaInstruccionPila(String instruccion,String operando) throws InstruccionDesconocida{
    //Si la instruccion es apila, situa en la cima de la pila el valor del operando
    if(instruccion.equals("apila")){
      pila.push(operando);
    }
    else{
      //Si es apila-dir entonces toma el valor que contiene la direccion de memoria
      //indicada por operando y lo situa en la cima de la pila
      if(instruccion.equals("apila-dir")){
        String valor=memoria.get(Integer.parseInt(operando)).toString();
        pila.push(valor);
      }
      else{
        //Si es desapila-dir entonces el valor que haya en la cima de la pila
        //se almacena en la memoria en la posicion que indica el operando
        if(instruccion.equals("desapila-dir")){
          String valor=pila.pop().toString();
          int op=Integer.parseInt(operando);
          memoria.set(op,valor);
        }
        else{
          throw new InstruccionDesconocida("Instruccion de Pila no valida: "+instruccion);
        }
      }
    }
  }

  //Metodo que ejecuta las operaciones de control
  //Hay que tener en cuenta que al realizar el salto, se salta a la direccion
  //anterior al destino ya que despues se incrementa el contador de programa
  private void ejecutaInstruccionControl(String instruccion,String operando) throws InstruccionDesconocida{
    //Si el salto es incondicional modifica el contador de programa
    if(instruccion.equals("ir-a")){
      instruccionActual=Integer.parseInt(operando)-1;
    }
    else{
      //Si es condicional entonces depende del valor que haya en la cima de la pila
      boolean condicion=Integer.parseInt(pila.pop().toString())==1;
      if(instruccion.equals("ir-v")){
        if(condicion){
          instruccionActual=Integer.parseInt(operando)-1;
        }
      }
      else{
        if(instruccion.equals("ir-f")){
          if(!condicion){
            instruccionActual=Integer.parseInt(operando)-1;
          }
        }
        else{
          throw new InstruccionDesconocida("Instruccion de Control no valida: "+instruccion);
        }
      }
    }
  }

  //Metodo que devuelve si la operacion pasada es aritmetica de dos operandos
  private boolean esOperacionBinariaAritmetica(String s){
    return s.equals("+") || s.equals("-") || s.equals("*") ||
        s.equals("/");
  }

  //Metodo que devuelve si la operacion pasada es booleana de dos operandos
  private boolean esOperacionBinariaBoolena(String s){
    return s.equals("AND") || s.equals("OR");
  }

  //Metodo que devuelve si la operacion pasada es de comparacion de dos operandos
  private boolean esOperacionBinariaComparacion(String s){
    return s.equals("=") || s.equals("distinto") || s.equals("<") ||
        s.equals("menori") || s.equals(">") || s.equals("mayori");
  }

  //Metodo que devuelve si la operacion pasada es de tipo unitario
  private boolean esOperacionUnaria(String s){
    return s.equals("mas") || s.equals("menos") || s.equals("¬");
  }

  //Metodo que devuelve si la operacion pasada es de tipo pila
  private boolean esOperacionPila(String s){
    return s.equals("apila") || s.equals("apila-dir") || s.equals("desapila-dir");
  }

  //Metodo que devuelve si la operacion pasada es de control
  private boolean esOperacionControl(String s){
    return s.equals("ir-a") || s.equals("ir-v") || s.equals("ir-f");
  }

  //Metodo que devuelve el nombre de la instruccion pasada como argumento
  private String dameTipoInstruccion(String instruccion){
    int i=0;
    while(i<instruccion.length() && instruccion.charAt(i)!='('){
      i++;
    }
    return instruccion.substring(0,i);
  }

  //Metodo que devuelve el operando de la instruccion pasada como argumento
  //Si no tiene entonces devuelve ""
  private String dameOperandoInstruccion(Instruccion instruccion){
    return String.valueOf(instruccion.iMemDir);
  }

  //Metodo que muestra la memoria de la maquina en el estado que se encuentre
  public void imprimeMemoria(){
    System.out.println("======= CONTENIDO DE LA MEMORIA ========");
    System.out.println(memoria.size());
    for(int i=0;i<memoria.size();i++){
      System.out.println("["+i+"] "+memoria.get(i));
    }
  }
  public String dameMemoria(){
    String mem = new String();
    for(int i=0;i<memoria.size();i++){
      mem = mem + "["+i+"] " + memoria.get(i) + "\n";
    }
    return mem;
  }

  //Metodo que muestra la pila de la maquina en el estado que se encuentre
  public void imprimePila(){
    System.out.println("======= CONTENIDO DE LA PILA ========");
    for(int i=0;i<pila.size();i++){
      System.out.println("["+i+"] "+pila.get(i));
    }
  }

  public void SetPrograma(Vector vPrograma)
  {
      this.programa = vPrograma;
  }

}