package animales;

import java.awt.Component;
import java.awt.Image;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tiburoncin extends JLabel implements Runnable{
	
	private Thread Hilo;
	private JPanel canvas_pecera;
	private int posX,oldPosX=1;
	private int posY,oldPosY=1;
	private int id;
	private ImageIcon imagen;
	private int altura = 561;
	private int ancho = 661;
	private int radio=100;
	private long inicioDeVida;
	private int tiempoAumentarVelocidad=7500;// tiempo para aumentar velocidad
	private long tiempoUltimaComida; // el tiempo ultimo pez comido
	private int velocidad = 10;
	
	public Tiburoncin(JPanel canvas,int i,int rango,int tiempoParaAumentarVelocidad, int timeVelocidad){
		id = i;
		canvas_pecera = canvas;
		radio = rango;
		tiempoAumentarVelocidad = tiempoParaAumentarVelocidad;
		velocidad = timeVelocidad;
        inicializarTiburon();
	}
	
	public Tiburoncin(){
		/*id = this.id;
		canvas_pecera = this.canvas_pecera;
		radio = this.radio;
		tiempoAumentarVelocidad = this.tiempoAumentarVelocidad;
        inicializarTiburon();*/
	}
	
	
	
	public void inicializarTiburon(){
		Random r = new Random(); 
		posX = r.nextInt(ancho-30)+1; // valor radom de la pos inicial en el ejeX del pez
		posY = r.nextInt(altura-30)+1; // valor random de la pos inicial en el ejeY del pez
        imagen = new ImageIcon(this.getClass().getResource("/img/tiburon.png"));
        imagen = new ImageIcon(imagen.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        this.setIcon(imagen);
        //asigna el tiburon la posición inicial
		setBounds(posX, posY, 30, 30);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
        	inicioDeVida = System.currentTimeMillis();
        	tiempoUltimaComida = inicioDeVida;
        	//System.out.println(velocidad);
            while(true){
            	//System.out.println(reproducir+"   desde pez "+id);
            	verEntorno();             
                setBounds(posX, posY, 30, 30);
                if(tiempoUltimaComida+tiempoAumentarVelocidad > System.currentTimeMillis() ){
                	if(velocidad == 1){
                		velocidad = 1;
                	}else{
                		velocidad = velocidad -1;
                	}
                	Thread.sleep(velocidad);                
                }else{
                	Thread.sleep(velocidad);
                }
                //System.out.println(velocidad+"   desde tiburon "+id);
            }
        }catch(InterruptedException e){}		
	}
	
	public void startTiburon(){
        if(Hilo == null){
            Hilo = new Thread(this);
            Hilo.start();
        }
	}
	
	public synchronized void verEntorno(){
        ArrayList<Component> pecesComida = new ArrayList<>();// arreglo de componente obtenidos del canvas 
        int areaX = posX-radio;
        int areaY = posX-radio;
        mover(); 
        for(int i = areaY; i <= areaY+(radio*2); i+=10){
            for(int j = areaX; j <= areaX+(radio*2); j+=10){
                Component objeto = canvas_pecera.getComponentAt(j, i);
                if(objeto != null && objeto != canvas_pecera && objeto != this){
                    if(!pecesComida.isEmpty()){
                        if(pecesComida.get(pecesComida.size()-1) != objeto)
                        	pecesComida.add(objeto);
                    }
                    else
                    	pecesComida.add(objeto);
                }
            }
        }
        
        if(!pecesComida.isEmpty()){
            seleccionarAccion(pecesComida);
        }
    }
	
	public void seleccionarAccion(ArrayList<Component> pecesComida){
        Pez pez = new Pez();
        boolean pezAvista = false;
        // primero se busca si en el arreglo de objetos del canvas hay peces
        for(int i = 0; i < pecesComida.size(); i++){
            if(pecesComida.get(i).getClass() != this.getClass()){// el objeto es diferente de un tiburon
            	pez = (Pez)pecesComida.get(i);
            	pezAvista = true;
                break;
            }
        }
        if(!pezAvista){
        	//si dejo que se busque pez, se dejan de mover los tiburones
        	//Pez pezComida = (Pez)pecesComida.get(0);
        	//buscarPez(pezComida);
        }
        if(pezAvista){
            comerPez(pez);  
        }
    }
	
	public void buscarPez(Pez pezComida){
        int x,y;
        //encuentra el ángulo que hay entre los dos peces 
        double angulo = anguloEntrePuntos(this.posX, this.posY, pezComida.getPosX(), pezComida.getPosY());
        if(angulo <= 20 || angulo > 340){
            x = 1; y = 0;
        }else if(angulo > 20 && angulo <= 70){
            x = 1; y = 1;
        }else if(angulo > 70 && angulo <= 110){
            x = 0; y = 1;
        }else if(angulo > 110 && angulo <= 160){
            x = -1; y = 1;
        }else if(angulo > 160 && angulo <= 200){
            x = -1; y = 0;
        }else if(angulo > 200 && angulo <= 250){
            x = -1; y = -1;
        }else if(angulo > 250 && angulo <= 290){
            x = 0; y = -1;
        }else{
            x = 1; y = -1;
        }
        this.oldPosX = x;
        this.oldPosY = y;
    }
	
	private double anguloEntrePuntos(int x1, int y1, int x2, int y2){
        double angulo = 180*Math.atan2(y2-y1, x2-x1)/Math.PI;
        if(angulo < 0)
            return angulo+360;
        else
            return angulo;
    }
	
	 public void comerPez(Pez pezDetectado){
	        Area miEspacio = new Area(this.getBounds());
	        Area EspacioPez = new Area(pezDetectado.getBounds());
	        /* Si las areas de ambos peces estan intersectadas */
	        if(miEspacio.intersects(EspacioPez.getBounds())){
	           //matar pez
	        	System.out.println("Comio");
	        	pezDetectado.vivito = false;
	        	pezDetectado.remover();
	            tiempoUltimaComida = System.currentTimeMillis();
	            velocidad=10;
	        }  
	            
	    }
	
	public void mover(){
        int probabilidad = (int)(Math.random()*1000);
        
        if(probabilidad <= 994){
           boolean invalidMove = true;
            int newX,newY;
            do{           
                newX = posX + oldPosX;
                newY = posY + oldPosY;
                if((newX <= (ancho-30) && newX >= 0) && (newY <= (altura-30) && newY >= 0)){
                    invalidMove = false;
                    posX = newX;
                    posY = newY;
                }
                else{
                    cambiarDireccion();
                    break;
                }                               
            }while(invalidMove); 
        }else{
            cambiarDireccion();  
        }
    }
	
	public void cambiarDireccion(){
        boolean invalidMove = true;
        int dx,dy,newX,newY;
        do{          
            switch((int)(Math.random()*8)){
                case 0: dx = 1; dy = 0; break;
                case 1: dx = -1; dy = 0; break;
                case 2: dx = 0; dy = 1; break;
                case 3: dx = 0; dy = -1; break;
                case 4: dx = 1; dy = 1; break;
                case 5: dx = -1; dy = 1; break;
                case 6: dx = -1; dy = -1; break;
                default:dx = 1; dy = -1; break;
            }
            newX = posX + dx;
            newY = posY + dy;
            if((newX <= (ancho-30) && newX >= 0) && (newY <= (altura-30) && newY >= 0))
                invalidMove = false;            
        }while(invalidMove);
        
        oldPosX = dx;
        oldPosY = dy;
        posX = newX;
        posY = newY;
    }
	
	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

}
