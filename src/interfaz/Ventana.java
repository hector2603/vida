package interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import animales.Pez;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Ventana extends JFrame implements ActionListener {

	static int pecesActuales;
	private int altura = 561;
	private int ancho = 661;
	private int numeroPecesIniciales;
	
	private JPanel contentPane;
	private JPanel canvas_pecera;
	private JPanel PanelBotones;
	private JLabel lblNewLabel;
	private JTextField numero_peces;
	private JTextField numero_tiburones;
	private ArrayList<Pez> peces;
	private JTextField RangoText;
	private JLabel lblTiempoParaSer;
	private JLabel lblNumeroMaxDe;
	private JLabel lblTiempoEntreReproduccion;
	private JTextField TiempoGrande;
	private JTextField TiempoEntreReproduccion;
	private JTextField MaxReproducciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana frame = new Ventana();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Ventana() {
		// inicializar datos de la simulación
		peces = new ArrayList<>();
		
		
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		canvas_pecera = new JPanel();
		canvas_pecera.setBackground(Color.CYAN);
		canvas_pecera.setForeground(new Color(255, 0, 0));
		canvas_pecera.setBorder(new LineBorder(Color.BLACK, 2, true));
		canvas_pecera.setBounds(0, 0, 661, 561);
		contentPane.add(canvas_pecera);
		canvas_pecera.setLayout(null);
		
		PanelBotones = new JPanel();
		PanelBotones.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2), "Par\u00E1metros", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 255, 0)));
		PanelBotones.setBackground(Color.WHITE);
		PanelBotones.setBounds(660, 0, 324, 561);
		contentPane.add(PanelBotones);
		PanelBotones.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.RED, 2, true));
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 26, 304, 524);
		PanelBotones.add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("Numero de Peces");
		lblNewLabel.setBounds(10, 11, 157, 14);
		panel.add(lblNewLabel);
		
		numero_peces = new JTextField();
		numero_peces.setText("10");
		numero_peces.setBounds(177, 8, 117, 20);
		panel.add(numero_peces);
		numero_peces.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Numero de tiburones");
		lblNewLabel_1.setBounds(10, 36, 157, 14);
		panel.add(lblNewLabel_1);
		
		numero_tiburones = new JTextField();
		numero_tiburones.setText("10");
		numero_tiburones.setBounds(177, 33, 117, 20);
		panel.add(numero_tiburones);
		numero_tiburones.setColumns(10);
		
		JButton btnIniciar = new JButton("INICIAR");
		btnIniciar.addActionListener(this);
		btnIniciar.setBounds(116, 490, 89, 23);
		panel.add(btnIniciar);
		
		JLabel lblNewLabel_2 = new JLabel("Radio de visi\u00F3n");
		lblNewLabel_2.setBounds(10, 61, 157, 14);
		panel.add(lblNewLabel_2);
		
		RangoText = new JTextField();
		RangoText.setText("100");
		RangoText.setBounds(177, 58, 117, 20);
		panel.add(RangoText);
		RangoText.setColumns(10);
		
		lblTiempoParaSer = new JLabel("Tiempo para ser Grande");
		lblTiempoParaSer.setBounds(10, 86, 157, 14);
		panel.add(lblTiempoParaSer);
		
		lblNumeroMaxDe = new JLabel("Numero Max de Reproducciones");
		lblNumeroMaxDe.setBounds(10, 111, 157, 14);
		panel.add(lblNumeroMaxDe);
		
		lblTiempoEntreReproduccion = new JLabel("Tiempo entre Reproduccion");
		lblTiempoEntreReproduccion.setBounds(10, 136, 157, 14);
		panel.add(lblTiempoEntreReproduccion);
		
		TiempoGrande = new JTextField();
		TiempoGrande.setText("5000");
		TiempoGrande.setColumns(10);
		TiempoGrande.setBounds(177, 83, 117, 20);
		panel.add(TiempoGrande);
		
		TiempoEntreReproduccion = new JTextField();
		TiempoEntreReproduccion.setText("5000");
		TiempoEntreReproduccion.setColumns(10);
		TiempoEntreReproduccion.setBounds(177, 133, 117, 20);
		panel.add(TiempoEntreReproduccion);
		
		MaxReproducciones = new JTextField();
		MaxReproducciones.setText("5");
		MaxReproducciones.setColumns(10);
		MaxReproducciones.setBounds(177, 108, 117, 20);
		panel.add(MaxReproducciones);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("inició");
		Pez p;
		int rango = Integer.parseInt(RangoText.getText());
		int TiempoParaSerGrande = Integer.parseInt(TiempoGrande.getText());
		int MaximoDeReproducciones = Integer.parseInt(MaxReproducciones.getText());
		int TimeEntreReproduccion = Integer.parseInt(TiempoEntreReproduccion.getText());
		if(!numero_peces.getText().toString().equals("")){
			numeroPecesIniciales = Integer.parseInt(numero_peces.getText());
	        for(int i = 0; i < numeroPecesIniciales; i++){
	        	p = new Pez(canvas_pecera,i,rango,TiempoParaSerGrande,MaximoDeReproducciones,TimeEntreReproduccion);
	        	peces.add(p);
	        	canvas_pecera.add(p);
	        	p.startPez();
	        }
			canvas_pecera.repaint();
			canvas_pecera.updateUI();
	        for(int i = 0; i < numeroPecesIniciales; i++){
	        	peces.get(i).startPez();
	        }
		}else{
			JOptionPane.showMessageDialog(this, "inserte todos los datos");
		}


		
	}
}
