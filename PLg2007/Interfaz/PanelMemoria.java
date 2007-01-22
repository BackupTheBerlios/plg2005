package Interfaz;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import java.util.LinkedList;


public class PanelMemoria extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelMemoria(String error,JFrame v)
	{
		super();
		
		JPanel pMemoria=dibujaMemoria("Ningún programa ejecutado");
		
		JPanel pError=dibujaError(error);
		JSplitPane sp3=new JSplitPane(JSplitPane.VERTICAL_SPLIT,pMemoria,pError);
		sp3.setEnabled(false);		
		sp3.setDividerSize(0);
		add(sp3);		
	}
	
	public void actualiza(int panel,LinkedList datos)
	{
		if (panel==3)
		{
			String cont=(String)datos.get(0);
			JSplitPane sp1=((JSplitPane)getComponent(0));			
			sp1.setTopComponent(dibujaMemoria(cont));			
		}		
		else if (panel==4)
		{

			String cont=(String)datos.get(0);
			JSplitPane sp9=((JSplitPane)getComponent(0));			
			sp9.setBottomComponent(dibujaError(cont));
		}
	}
	
	public JPanel dibujaMemoria(String cont)
	{
		JPanel pMem=new JPanel();
		pMem.setPreferredSize(new Dimension(600,372));
		pMem.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Memoria",TitledBorder.LEFT,TitledBorder.TOP));
		
		JButton bEjecutar=new JButton("Ejecutar");
		bEjecutar.setPreferredSize(new Dimension(100,25));
		
		JTextArea salida=new JTextArea(cont);
		salida.setEditable(false);
		JScrollPane psalida=new JScrollPane(salida);
		psalida.setPreferredSize(new Dimension(585,310));
		
		pMem.add(bEjecutar);
		pMem.add(psalida);
		
		bEjecutar.addActionListener(new ActionListener()
		{	public void actionPerformed(ActionEvent e)
			{						
				Interfaz.ejecutar();						
			}
		});			
		
		return pMem;
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

