package Interfaz;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import java.util.LinkedList;


public class PanelTS extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelTS(String error,JFrame v)
		{
			super();			
			JPanel pTS=dibujaTS("Ningún programa compilado");
			
			JPanel pError=dibujaError(error);
			JSplitPane sp3=new JSplitPane(JSplitPane.VERTICAL_SPLIT,pTS,pError);
			sp3.setEnabled(false);		
			sp3.setDividerSize(0);
			add(sp3);		
		}
		
		public void actualiza(int panel,LinkedList datos)
		{
			if (panel==2)
			{
				String cont=(String)datos.get(0);
				JSplitPane sp1=((JSplitPane)getComponent(0));			
							
				sp1.setTopComponent(dibujaTS(cont));
			}	
			else if (panel==4)
			{

				String cont=(String)datos.get(0);
				JSplitPane sp9=((JSplitPane)getComponent(0));			
				sp9.setBottomComponent(dibujaError(cont));
			}
		}	
		
		public JPanel dibujaTS(String cont)
		{
			JPanel pTS=new JPanel();
			pTS.setPreferredSize(new Dimension(600,372));
			pTS.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Código Máquina",TitledBorder.LEFT,TitledBorder.TOP));
			
			JButton bCompilar=new JButton("Compilar");
			bCompilar.setPreferredSize(new Dimension(100,25));
			
			JTextArea salida=new JTextArea(cont);
			salida.setEditable(false);
			JScrollPane psalida=new JScrollPane(salida);
			psalida.setPreferredSize(new Dimension(585,310));		
			
			pTS.add(bCompilar);
			pTS.add(psalida);
			
			bCompilar.addActionListener(new ActionListener()
			{	public void actionPerformed(ActionEvent e)
				{						
					Interfaz.compilar();						
				}
			});			
			
			
			return pTS;
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




