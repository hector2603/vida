package animales;

import java.awt.Component;
import java.awt.Image;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Comida.Comida;



public class Pez extends JLabel implements Runnable{

	private Thread Hilo;
	private JPanel canvas_pecera;
	private int posX,oldPosX=1;
	private int posY,oldPosY=1;
	private int sexo;
	private int id;
	private ImageIcon imagen;
	private int altura = 561;
	private int ancho = 661;
	public boolean vivito;
	public boolean mover;
	private int radio=100;
	private boolean reproducir;
	private long inicioDeVida;
	private int tiempoParaReproducir=5;// tiempo para ser grande y poder reproducirse 
	private int numeroReproducciones=5; // numero m�ximo de reproducciones que puede terner el pececito 
	private long tiempoUltimaReproduccion; // el tiempo en el que tuvo la ultima reproduccion para poder validar, cuando se puede volver a reproducir
	private int tiempoEntreReproduccion = 5; // tiempo entre reproduccion
	private int tiempoDeVida = 100;
	public Long cantidadDeHambre=(long) 10000000;
	private int numeroReproduccionesIniciales;
	
	public Pez(JPanel canvas,int i,int rango,int tiempoParaSerGrande,int numeroDeReproducciones, int timeEntreReproduccion, int timeOfLife){
		id = i;
		canvas_pecera = canvas;
		radio = rango;
		tiempoParaReproducir = tiempoParaSerGrande*1000;
		numeroReproducciones = numeroDeReproducciones;
		numeroReproduccionesIniciales = numeroDeReproducciones;
		tiempoEntreReproduccion = timeEntreReproduccion*1000;
		tiempoDeVida = timeOfLife*1000;
        inicializarPez();
	}
	
	public Pez(){
		id = this.id;
		canvas_pecera = this.canvas_pecera;
		radio = this.radio;
		tiempoParaReproducir = this.tiempoParaReproducir*1000;
		numeroReproducciones = this.numeroReproducciones;
		tiempoEntreReproduccion = this.tiempoEntreReproduccion*1000;
		tiempoDeVida = this.tiempoDeVida*1000;
        inicializarPez();
	}
	
	public void inicializarPez(){
		Random r = new Random(); 
		sexo = r.nextInt(2)+1; // valor random de 1 o 2 para determinar el sexo
		posX = r.nextInt(ancho-30)+1; // valor radom de la pos inicial en el ejeX del pez
		posY = r.nextInt(altura-30)+1; // valor random de la pos inicial en el ejeY del pez
		// if para asignar la imagen correspondiente seg�n el sexo
        if(sexo == 1){
        	imagen = new ImageIcon(this.getClass().getResource("/img/pezMacho.png"));
        }else{
        	imagen = new ImageIcon(this.getClass().getResource("/img/pezHembra.png"));
        }
        imagen = new ImageIcon(imagen.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        this.setIcon(imagen);
        //asigna el pez la posici�n inicial
		setBounds(posX, posY, 30, 30);
		vivito = true ;
		mover = true;
		reproducir= false;
		cantidadDeHambre=(long) 500000;
	}
	
	@Override
	public void run() {
        try{
        	inicioDeVida = System.currentTimeMillis();
        	tiempoUltimaReproduccion = inicioDeVida;
            while(vivito){
            	//System.out.println(reproducir+"   desde pez "+id);
            	verEntorno();             
                setBounds(posX, posY, 30, 30);
                if (cantidadDeHambre <= 0){
                	cantidadDeHambre = (long) 0;
                	vivito=false;
                	reproducir= false;
                	System.out.println("muri� de hambre");
                }else{
                	cantidadDeHambre -=(long) 20;
                }
                if(inicioDeVida+tiempoParaReproducir<System.currentTimeMillis() && tiempoUltimaReproduccion+tiempoEntreReproduccion < System.currentTimeMillis()  ){
                	reproducir = true;
                }if(tiempoDeVida+inicioDeVida < System.currentTimeMillis()){   
                	System.out.println("muere" + this.id);
                	this.stopPez();
                }
                Thread.sleep(10);
            }
        }catch(InterruptedException e){}
	}
	
	public void startPez(){
        if(Hilo == null){
            Hilo = new Thread(this);
            Hilo.start();
        }
	}
	
	@SuppressWarnings("deprecation")
	public void stopPez(){
         Hilo.stop();
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
    
    public synchronized void verEntorno(){

        ArrayList<Component> pecesVecinos = new ArrayList<>();// arreglo de componente obtenidos del canvas
        ArrayList<Component> comida = new ArrayList<>(); //arreglo de comida en el entorno
        int areaX = posX-radio;
        int areaY = posX-radio;
        mover(); 
        for(int i = areaY; i <= areaY+(radio*2); i+=10){
            for(int j = areaX; j <= areaX+(radio*2); j+=10){
                Component objeto = canvas_pecera.getComponentAt(j, i);
                //Aqu� porque es diferente????
                if(objeto != null && objeto != canvas_pecera && objeto != this && (objeto.getClass().getName() == "animales.Pez" || objeto.getClass().getName() == "animales.Tiburoncin" )){
                	if(!pecesVecinos.isEmpty()){
                        if(pecesVecinos.get(pecesVecinos.size()-1) != objeto)
                        	pecesVecinos.add(objeto);
                    }
                    else
                    	pecesVecinos.add(objeto);
                }
                if(objeto != null && objeto != canvas_pecera && objeto != this && objeto.getClass().getName() == "Comida.Comida"){

                    if(!comida.isEmpty()){
                        if(comida.get(comida.size()-1) != objeto)
                        	comida.add(objeto);
                    }
                    else
                    	comida.add(objeto);
                }
            }
        }if(!pecesVecinos.isEmpty()){

            seleccionarAccion(pecesVecinos);
        }if(!comida.isEmpty() && cantidadDeHambre < 15000){
            buscarComida(comida);
        }
    }

    public void seleccionarAccion(ArrayList<Component> pecesVecinos){

        Tiburoncin tiburoncin = new Tiburoncin();
        boolean tiburonAvista = false;
        boolean parejaAvista = false;
        // primero se busca si en el arreglo de objetos del canvas hay un tiburon
        for(int i = 0; i < pecesVecinos.size(); i++){
        	System.out.println(pecesVecinos.get(i).getClass().getName());
            if(pecesVecinos.get(i).getClass().getName() == "animales.Tiburoncin"){// el objeto es diferente de un pez
            	tiburoncin = (Tiburoncin)pecesVecinos.get(i);
            	System.out.println("entro");
            	tiburonAvista = true;
                break;
            }
        }
                
        if(!tiburonAvista && mover){

        	//System.out.println("entro sin tiburon");
            //si no hay tiburon, busca un pez del sexo opuesto
            Pez pececito = (Pez)pecesVecinos.get(0);
            if(reproducir && numeroReproducciones > 0){

                if(!(pececito.getSexo()==this.getSexo())&& pececito.getReproducir()&& pececito.vivito){
                	
                	parejaAvista = true;
                    buscarPareja(pececito);
                    if(sexo==2)
                    	reproducir(pececito);
                }
            }
        }
        if(tiburonAvista && mover){
            evitarTiburon(tiburoncin);  
        }
        /*if(!parejaAvista && !tiburonAvista){
        	mover();// en el caso de que no hayan tiburones y los peces vecinos sean del mismo sexo, se sigue moviendo
        }
  */
    }
    
    public void buscarComida(ArrayList<Component> comida){    
            Comida comidita;
            for (int i = 0; i < comida.size(); i++) {
            	comidita = (Comida)comida.get(i);
                Area miEspacio = new Area(this.getBounds());
                //Area EspacioComida = new Area(comidita.getBounds());
                /* Si las areas de ambos estan intersectadas */
                if(miEspacio.intersects(comidita.getBounds())){
                	//System.out.println("Pez Comio Matas");
                	comidita.remover();
    	            cantidadDeHambre=(long) 500000;
                } 
			}

    }
    
    public void evitarTiburon(Tiburoncin tiburon){
        int x,y;
        //encuentra el �ngulo que hay entre los dos peces 
        double angulo = anguloEntrePuntos(this.posX, this.posY, tiburon.getPosX(), tiburon.getPosY());
        if(angulo <= 20 || angulo > 340){
            x = -1; y = 0;
        }else if(angulo > 20 && angulo <= 70){
            x = -1; y = -1;
        }else if(angulo > 70 && angulo <= 110){
            x = 0; y = -1;
        }else if(angulo > 110 && angulo <= 160){
            x = 1; y = -1;
        }else if(angulo > 160 && angulo <= 200){
            x = 1; y = 0;
        }else if(angulo > 200 && angulo <= 250){
            x = 1; y = 1;
        }else if(angulo > 250 && angulo <= 290){
            x = 0; y = 1;
        }else{
            x = -1; y = 1;
        }
        this.oldPosX = x;
        this.oldPosY = y;
    }
    
    public void buscarPareja(Pez pareja){
        int x,y;
        //encuentra el �ngulo que hay entre los dos peces 
        double angulo = anguloEntrePuntos(this.posX, this.posY, pareja.getPosX(), pareja.getPosY());
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
    
    public void reproducir(Pez pezDetectado){
        Area miEspacio = new Area(this.getBounds());
        Area EspacioPareja = new Area(pezDetectado.getBounds());
        /* Si las areas de ambos peces estan intersectadas */
        if(miEspacio.intersects(EspacioPareja.getBounds())){
        	//System.out.println("Reproduccion");
            Pez hijitoAmado = new Pez(canvas_pecera,0,radio,tiempoParaReproducir/1000,numeroReproduccionesIniciales,tiempoEntreReproduccion/1000, tiempoDeVida/1000);
            hijitoAmado.setPosX(posX);
            hijitoAmado.setPosY(posY);
            canvas_pecera.add(hijitoAmado);
            hijitoAmado.startPez();
            reproducir = false;
            numeroReproducciones--;
            tiempoUltimaReproduccion = System.currentTimeMillis();
            pezDetectado.setReproducir(false);
            pezDetectado.setNumeroReproducciones(pezDetectado.getNumeroReproducciones()-1);
            pezDetectado.setTiempoUltimaReproduccion(tiempoUltimaReproduccion);
            pezDetectado.cambiarDireccion();
            cambiarDireccion();
        }  
            
    }
    
    public void remover(){
    	canvas_pecera.remove(this);
    }
    
    private double anguloEntrePuntos(int x1, int y1, int x2, int y2){
        double angulo = 180*Math.atan2(y2-y1, x2-x1)/Math.PI;
        if(angulo < 0)
            return angulo+360;
        else
            return angulo;
    }
    
	public JPanel getCanvas_pecera() {
		return canvas_pecera;
	}


	public void setCanvas_pecera(JPanel canvas_pecera) {
		this.canvas_pecera = canvas_pecera;
	}


	public int getPosX() {
		return posX;
	}


	public void setPosX(int posX) {
		this.posX = posX;
	}


	public int getPosY() {
		return posY;
	}


	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public int getSexo() {
		return sexo;
	}

	public void setSexo(int sexo) {
		this.sexo = sexo;
	}

	public boolean getReproducir() {
		return reproducir;
	}

	public void setReproducir(boolean reproducir) {
		this.reproducir = reproducir;
	}

	public int getNumeroReproducciones() {
		return numeroReproducciones;
	}

	public void setNumeroReproducciones(int numeroReproducciones) {
		this.numeroReproducciones = numeroReproducciones;
	}

	public long getTiempoUltimaReproduccion() {
		return tiempoUltimaReproduccion;
	}

	public void setTiempoUltimaReproduccion(long tiempoUltimaReproduccion) {
		this.tiempoUltimaReproduccion = tiempoUltimaReproduccion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTiempoParaReproducir() {
		return tiempoParaReproducir;
	}

	public long getInicioDeVida() {
		return inicioDeVida;
	}

	public int getTiempoDeVida() {
		return tiempoDeVida;
	}

}
