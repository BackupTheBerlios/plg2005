package Interfaz;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import java.util.LinkedList;


public class PanelPrincipal extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelPrincipal(String error,JFrame v)
	{
		super();
		JPanel pCodigo=dibujaCodigo("Ningún programa cargado");
		
		JPanel pError=dibujaError(error);
		JSplitPane sp3=new JSplitPane(JSplitPane.VERTICAL_SPLIT,pCodigo,pError);
		sp3.setEnabled(false);		
		sp3.setDividerSize(0);
		add(sp3);		
	}
	
	public void actualiza(int panel,LinkedList datos)
	{
		if (panel==1)
		{
			String cont=(String)datos.get(0);
			JSplitPane sp1=((JSplitPane)getComponent(0));								
			sp1.setTopComponent(dibujaCodigo(cont));			
		}				
		else if (panel==4)
		{

			String cont=(String)datos.get(0);
			JSplitPane sp9=((JSplitPane)getComponent(0));			
			sp9.setBottomComponent(dibujaError(cont));
		}
	}	
	
	public JPanel dibujaCodigo(String cod)
	{
		JPanel pCod=new JPanel();
		pCod.setPreferredSize(new Dimension(600,372));
		pCod.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),("Código del programa "+Interfaz.dameNombreFich()),TitledBorder.LEFT,TitledBorder.TOP));
		
		JButton bActualizar=new JButton("Actualizar");
		bActualizar.setPreferredSize(new Dimension(100,25));
		JButton bCompilar=new JButton("Compilar");
		bCompilar.setPreferredSize(new Dimension(100,25));
		JButton bEjecutar=new JButton("Ejecutar");
		bEjecutar.setPreferredSize(new Dimension(100,25));
		
		JTextArea salida=new JTextArea(cod);
		salida.setEditable(false);
		JScrollPane psalida=new JScrollPane(salida);
		psalida.setPreferredSize(new Dimension(585,310));

		pCod.add(bActualizar);
		pCod.add(bCompilar);
		pCod.add(bEjecutar);
		pCod.add(psalida);
		
		bActualizar.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{	
				Interfaz.actualizar();
			}
		});		
		
		bCompilar.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{						
				Interfaz.compilar();						
			}
		});	
		
		bEjecutar.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{						
				Interfaz.ejecutar();						
			}
		});	
		
		return pCod;
	}
	
	public JPanel dibujaError(String s)
	{
		JPanel pErr=new JPanel();
		pErr.setPreferredSize(new Dimension(600,135));
		pErr.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Información errores",TitledBorder.LEFT,TitledBorder.TOP));
				
		JTextArea salida=new JTextArea(s);
		salida.setEditable(false);
		JScrollPane psalida=new JScrollPane(salida);
		psalida.setPreferredSize(new Dimension(585,105));		
		pErr.add(psalida);
				
		return pErr;
	}
}

