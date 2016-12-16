package Comida;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import animales.Pez;

public class Comida extends JLabel implements Runnable{
	
	private Thread Hilo;
	private int id;
	private JPanel canvas_pecera;
	private int radio=100;
	private int tazaComida=15000;
	private int posX,oldPosX=1;
	private int posY,oldPosY=1;
	private ImageIcon imagen;
	private int altura = 561;
	private int ancho = 661;
	private long inicioDeComida;
	
	public Comida(JPanel canvas,int i,int rango,int tazaComidita){
		id = i;
		canvas_pecera = canvas;
		radio = rango;
		tazaComida = tazaComidita*1000;
        inicializarComida();
	}
	
	public void inicializarComida(){
		Random r = new Random(); 
		posX = r.nextInt(ancho-30)+1; // valor radom de la pos inicial en el ejeX del pez
		posY = r.nextInt(altura-30)+1; // valor random de la pos inicial en el ejeY del pez
        
		imagen = new ImageIcon(this.getClass().getResource("/img/comida.png"));
        imagen = new ImageIcon(imagen.getImage().getScaledInstance(13, 13, Image.SCALE_SMOOTH));
        this.setIcon(imagen);
        //asigna el pez la posición inicial
		setBounds(posX, posY, 30, 30);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
        	inicioDeComida = System.currentTimeMillis();
            while(true){          	        
                setBounds(posX, posY, 30, 30);
                if(inicioDeComida+tazaComida<System.currentTimeMillis() ){
                	//System.out.println("Abastece Comida");
                	abastecerComida();
                	inicioDeComida = System.currentTimeMillis();
                }
                Thread.sleep(10);                
            }
        }catch(InterruptedException e){}
		
	}

	public void startComida(){
        if(Hilo == null){
            Hilo = new Thread(this);
            Hilo.start();
        }
	}
	
	private void abastecerComida() {
		// TODO Auto-generated method stub
		
			Random r = new Random(); 
			posX = r.nextInt(ancho-30)+1; // valor radom de la pos inicial en el ejeX del pez
			posY = r.nextInt(altura-30)+1; // valor random de la pos inicial en el ejeY del pez
			Comida comidita = new Comida(canvas_pecera,0,radio,tazaComida);
			comidita.setPosX(posX);
			comidita.setPosY(posY);
			canvas_pecera.add(comidita);
			comidita.startComida();
		
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}


	public void setPosY(int posY) {
		this.posY = posY;
	}
	 public void remover(){
	    	canvas_pecera.remove(this);
	    }

}
