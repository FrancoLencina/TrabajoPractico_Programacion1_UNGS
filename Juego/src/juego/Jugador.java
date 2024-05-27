package juego;

import java.awt.Image;


import entorno.Entorno;
import entorno.Herramientas;

public class Jugador extends Entidad {
	boolean estaSaltando; //false = no esta saltando
	int contadorSalto;
	boolean estaMuerto;
	Entorno e;


	public Jugador(double x, double y) {
		this.x = x;
		this.y = y;
		spriteIzq = Herramientas.cargarImagen("RaspberryPiLeft.png");
		spriteDer = Herramientas.cargarImagen("RaspberryPiRight.png");
		contadorSalto = 0;
		estaApoyado = false;
		estaSaltando = false;
		estaCayendo = false;
		estaMuerto = false;
		alto = spriteIzq.getHeight(null) * escala;
		ancho = spriteIzq.getWidth(null) * escala;
		this.xInicial = x;
		this.yInicial = y;
		
	}


	public void moverse(boolean dirMov, Entorno e) {
		if (estaApoyado || estaSaltando || estaCayendo) {
			if (dirMov) {
				this.x += velocidad;
			} else{
				this.x -= velocidad;
			}
			this.dir = dirMov;
			
			//Aparecer por el otro costado
			if(x > e.ancho()) {
				x = 0;
			}
			if(x < 0) {
				x = e.ancho();
			}
		}
	}

	public void movVertical(Entorno e, Piso[] p) {
		//Caer
		if (!estaApoyado) {
			this.y += e.alto()/200;
			estaCayendo=true;
		}
		if(estaSaltando) {
			this.y -= 10;
			contadorSalto++;
		}
		if(contadorSalto >= (e.alto()/p.length)/4) {
			contadorSalto = 0;
			estaSaltando = false;
			estaCayendo= true;
		}
	}
}
