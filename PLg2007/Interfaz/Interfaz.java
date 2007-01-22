package Interfaz;

import javax.swing.*;
import java.util.LinkedList;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import Analizador_Sintactico.AnalizadorSintactico;
import Analizador_Sintactico.Instruccion;
import MaquinaP.MaquinaP;
import java.util.Vector;


public class Interfaz 
{
	int unidad=1500;
		
	private static PanelPrincipal pPrincipal;
	
	private static PanelTS pTS;
	
	private static PanelMemoria pMemoria;
	
	private JFrame ventana;
	
	private JTabbedPane panelVistas;
	
	private JFrame formCompilador;
	
	private JDialog formacercade;
	
	private static AnalizadorSintactico anSintactico;
	
	private static MaquinaP mqnVirtual;
		
	private static Vector codMaquina;
	
	private static String contMem;
	
	private static String codError;
	
	private static String nombreFich;
	
	private static boolean cargado;

	private static boolean compilado;
	
	
						
	public Interfaz()
	{	
		super();		
		cargado=false;
		compilado=false;
		nombreFich="";
		codMaquina=null;
		contMem="";
		codError="";		
		ventana=new JFrame("Compilador PLg2007");
		int lf=2;
		if (lf>=UIManager.getInstalledLookAndFeels().length) lf=0;
		UIManager.LookAndFeelInfo lfinfo=UIManager.getInstalledLookAndFeels()[lf];
		try
		{	UIManager.setLookAndFeel(lfinfo.getClassName());}
		catch (Exception ex)
		{	lfinfo=UIManager.getInstalledLookAndFeels()[0];
			try
			{	UIManager.setLookAndFeel(lfinfo.getClassName());}
			catch (Exception ex1)
			{ex.printStackTrace();}
		}
		SwingUtilities.updateComponentTreeUI(ventana);
		ventana.pack();
		formCompilador=new JFrame("Iniciando Compilador");
		formCompilador.setUndecorated(true);		
		formCompilador.setLocation(250,150);
		formCompilador.getContentPane().add(pantallaInicio());
		formCompilador.pack();
		formCompilador.setVisible(true);
		
		formacercade=new JDialog(ventana,true);
		formacercade.setResizable(false);
		formacercade.setUndecorated(true);		
		formacercade.setLocation(350,250);
		
		panelVistas=new JTabbedPane(JTabbedPane.TOP);
		
		
		
		pPrincipal=new PanelPrincipal("Ningún error",ventana);
		
		pTS=new PanelTS("Ningún error",ventana);
		pMemoria=new PanelMemoria("Ningún error",ventana);
		
		panelVistas.addTab("Código del programa",pPrincipal);
		panelVistas.addTab("Código Máquina",pTS);
		panelVistas.addTab("Memoria",pMemoria);
		
		ventana.getContentPane().add(panelVistas);
		ventana.pack();
		ventana.setJMenuBar(setMenu());
		ventana.pack();
		ventana.setLocation(25,30);
		ventana.setResizable(false);
		ventana.setVisible(true);
		ventana.addWindowListener(new WindowAdapter()
		{	public void windowClosing(WindowEvent e)
			{	      	
				acabarAplicacion();				
			}
		});	
		ventana.addWindowListener(new WindowAdapter()
		{
			public void windowActivated(WindowEvent e)
			{
				formCompilador.setVisible(false);		
			}			
		});		
	}
	
	/**Clase que implementa el filtro de ficheros de tipo .plg
	*/
	class FiltroPlg extends FileFilter
	{
		public FiltroPlg() {super();}
		public boolean accept(File f)
		{
			String nombre=f.getName();
			return nombre.substring(Math.max(nombre.length()-4,0)).equals(".plg");
		}
		public String getDescription()
		{
			return "Ficheros del compilador (*.plg)";
		}
	}
	
	public static void compilar()
	{
		borraCodError();
		LinkedList datos1=new LinkedList();
		datos1.add(new String("Ningún error"));
		Interfaz.actualizaVista(4,datos1);
		
		if (cargado)
		{			
			try
			{
				Interfaz.añadeCodMaquina("Lexema // Dirección // Tipo // Categoría Léxica\n");				
				Interfaz.añadeCodMaquina("======    =========    ====    ================\n\n");
				anSintactico= new AnalizadorSintactico(null,nombreFich);
				anSintactico.Programa();
                Interfaz.codMaquina = anSintactico.genCodigo.cod;
                int posActual = 0;
                String cod = new String();
                while (posActual < codMaquina.size())
                {
                	cod = cod + ((Instruccion)codMaquina.get(posActual)).sInstruccion;
                	cod = cod + " " +  ((Instruccion)codMaquina.get(posActual)).iMemDir;
                	cod = cod + "\n";
                	posActual++;
                }
				LinkedList datos=new LinkedList();
				datos.add(new String(cod));				
				Interfaz.actualizaVista(2,datos);
				//Interfaz.borraCodMaquina();
				
				if (!anSintactico.tieneError())
				{
					compilado=true;				
					JOptionPane.showMessageDialog(null,"Fichero Objeto \""+(nombreFich+".o\"")+" creado.",(nombreFich+".plg"),JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			catch (NullPointerException e)
			{					
				LinkedList datos=new LinkedList();
				datos.add(new String("Error al cargar"));
				Interfaz.actualizaVista(4,datos);
			}
			catch (IOException e)
			{	
				LinkedList datos=new LinkedList();
				datos.add(new String("Error de E/S"));
				Interfaz.actualizaVista(4,datos);							
			}
			catch(Exception e){
				LinkedList datos=new LinkedList();
				datos.add(new String(anSintactico.codError));
				Interfaz.actualizaVista(4,datos);
			}

		}
		else
		{			
			LinkedList datos=new LinkedList();
			datos.add(new String("Error: No hay ningún programa cargado"));
			Interfaz.actualizaVista(4,datos);				
		}
		
	}
	
	public static void ejecutar()
	{
		if(compilado)
		{	
			mqnVirtual= new MaquinaP();
			mqnVirtual.SetPrograma(Interfaz.codMaquina);
			mqnVirtual.ejecuta();
			Interfaz.contMem = mqnVirtual.dameMemoria();
			LinkedList datos=new LinkedList();
			datos.add(new String(Interfaz.contMem));				
			Interfaz.actualizaVista(3,datos);
			JOptionPane.showMessageDialog(null,"Programa \""+nombreFich+"\" ejecutado.",(nombreFich+".plg"),JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			LinkedList datos=new LinkedList();
			datos.add(new String("Error: No hay ningún programa compilado"));
			Interfaz.actualizaVista(4,datos);
		}
	}
	
	public void cargar()
	{	
		inicializaVista();
		
		JFileChooser selector=new JFileChooser();
		selector.setFileFilter(new FiltroPlg());
		selector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		selector.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		try
		{				
			if (selector.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
				
				File f=selector.getSelectedFile();	
				nombreFich=f.getAbsolutePath();//.getName();
				//nombreFich=nombreFich.substring(0,nombreFich.length()-4);
				BufferedReader fich=new BufferedReader(new FileReader(nombreFich));
				
				String t1,t2=new String();
				t2="1: ";
				int i=1;
				while ((t1=fich.readLine())!=null)
				{	
					i=i+1;
					t2=t2+t1+"\n"+i+": ";
				}
				
				LinkedList datos2=new LinkedList();
				datos2.add(new String(t2));
				actualizaVista(1,datos2);
				cargado=true;
				compilado=false;
				JOptionPane.showMessageDialog(null,"Programa \""+(nombreFich+".plg\"")+" cargado con éxito.",(nombreFich+".plg"),JOptionPane.INFORMATION_MESSAGE);	
			}		
		}	
		
		catch (NullPointerException e)
		{	
			LinkedList datos2=new LinkedList();
			datos2.add(new String("Error al cargar"));
			Interfaz.actualizaVista(4,datos2);						
		}		
		
		catch (IOException e)
		{	
			LinkedList datos2=new LinkedList();
			datos2.add(new String("Error de E/S"));
			Interfaz.actualizaVista(4,datos2);						
		}		
	}
	
	public JMenuBar setMenu()
	{
		JMenuBar menu=new JMenuBar();
		JMenu m1=new JMenu("Archivo");
		JMenu m2=new JMenu("Herramientas");
		JMenu m3=new JMenu("Acerca de");
		JMenuItem cargar=new JMenuItem("Cargar");
		JMenuItem actualizar=new JMenuItem("Actualizar");
		JMenuItem salir=new JMenuItem("Salir");
		JMenuItem comp=new JMenuItem("Compilar");
		JMenuItem ejec=new JMenuItem("Ejecutar");
		JMenuItem acercade=new JMenuItem("Acerca del Compilador");
		m1.add(cargar);
		m1.add(actualizar);
		m1.addSeparator();
		m1.add(salir);
						
		m2.add(comp);
		m2.add(ejec);
		
		m3.add(acercade);
		
		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		
		cargar.addActionListener(new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{	
						cargar();
					}
				});
		
		actualizar.addActionListener(new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{	
						actualizar();
					}
				});
		
		comp.addActionListener(new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{	
						compilar();						
					}
				});
		
		ejec.addActionListener(new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{	
						ejecutar();
					}
				});
		
		acercade.addActionListener(new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{
						formacercade.getContentPane().add(panelacercade());
						formacercade.pack();	
						formacercade.setVisible(true);				
					}
				});
		
		salir.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	
				acabarAplicacion();		
			}
		});
		
		return menu;	
	}
	
	
	public void acabarAplicacion()
	{				
		JOptionPane.showMessageDialog(null,"Compilador Finalizado.","PLg2007",JOptionPane.INFORMATION_MESSAGE);
		System.out.println("El Compilador ha terminado");
		System.exit(0);
	}
	
	public void activa()
	{
		ventana.setVisible(true);
	}
	
	public static void inicializaVista()
	{
		LinkedList datos1=new LinkedList();
		datos1.add("Ningún programa compilado");
		actualizaVista(2,datos1);
		
		LinkedList datos2=new LinkedList();
		datos2.add("Ningún programa ejecutado");
		actualizaVista(3,datos2);
		
		LinkedList datos3=new LinkedList();
		datos3.add("Ningún error");
		actualizaVista(4,datos3);
	}
	
	public static void actualizaVista(int panel,LinkedList datos)
	{		
		if(panel==1)
			pPrincipal.actualiza(panel,datos);
		else if(panel==2)
			pTS.actualiza(panel,datos);
		else if(panel==3)		
			pMemoria.actualiza(panel,datos);
		else if(panel==4)
		{
			pPrincipal.actualiza(panel,datos);			
			pTS.actualiza(panel,datos);
			pMemoria.actualiza(panel,datos);
		}
		
	}
	
	public JPanel pantallaInicio()
	{	
		JLabel l1=new JLabel(new ImageIcon("CompiladorPLg.jpg"));
		l1.setPreferredSize(new Dimension(402,181));
		JLabel l2=new JLabel("Iniciando Compilador...");
		
		JSplitPane sp=new JSplitPane(JSplitPane.VERTICAL_SPLIT,l1,l2);
		sp.setEnabled(false);
		sp.setDividerSize(4);
		JPanel panel=new JPanel();
		panel.add(sp);
		return panel;		
	}
	
	public JPanel panelacercade()
	{		
		JPanel pad=new JPanel();
		formacercade.setTitle("Acerca de... Compilador PLg");
		JButton b=new JButton(new ImageIcon("CompiladorPLg.jpg"));
		b.setPreferredSize(new Dimension(402,181));
		formacercade.getContentPane().add(b);
		formacercade.pack();	
		b.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{
				formacercade.setVisible(false);
				formacercade.getContentPane().removeAll();
			}
		});
		pad.add(b);
		return pad;
	}
	
	public static void añadeCodMaquina(String s)
	{
	//	codMaquina=(codMaquina+s);
	}
	
	public static Vector dameCodMaquina()
	{
		return codMaquina;
	}
	
	public static void borraCodMaquina()
	{
		codMaquina=null;
	}
	
	public static void añadeContMem(String s)
	{
		contMem=(contMem+s);
	}
	
	public static String dameContMem()
	{
		return contMem;
	}
	
	public static void borraContMem()
	{
		contMem="";
	}
	
	public static void añadeCodError(String s)
	{
		codError=(codError+s);
	}
	
	public static String dameCodError()
	{
		return codError;
	}
	
	public static void borraCodError()
	{
		codError="";
	}
	
	public static void actualizar()
	{
		if (cargado){
		inicializaVista();
		cargado=false;
		try
		{						
				BufferedReader fich=new BufferedReader(new FileReader(nombreFich));
				
				String t1,t2=new String();
				t2="1: ";
				int i=1;
				while ((t1=fich.readLine())!=null)
				{	
					i=i+1;
					t2=t2+t1+"\n"+i+": ";
				}
				
				LinkedList datos2=new LinkedList();
				datos2.add(new String(t2));
				actualizaVista(1,datos2);				
				cargado=true;
				compilado=false;
				JOptionPane.showMessageDialog(null,"Programa \""+(nombreFich+".plg\"")+" actualizado con éxito.",(nombreFich+".plg"),JOptionPane.INFORMATION_MESSAGE);
					
		}	
		
		catch (NullPointerException e)
		{	
			LinkedList datos2=new LinkedList();
			datos2.add(new String("Error al cargar"));
			Interfaz.actualizaVista(4,datos2);						
		}		
		
		catch (IOException e)
		{	
			LinkedList datos2=new LinkedList();
			datos2.add(new String("Error de E/S"));
			Interfaz.actualizaVista(4,datos2);						
		}
		}
		else
		{
			LinkedList datos=new LinkedList();
			datos.add(new String("Error: No hay ningún programa cargado para poder actualizar"));
			Interfaz.actualizaVista(4,datos);
		}
	}
	
	public static String dameNombreFich()
	{
		return nombreFich;
	}
	
}