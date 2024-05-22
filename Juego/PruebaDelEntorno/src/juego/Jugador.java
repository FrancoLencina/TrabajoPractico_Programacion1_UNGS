package juego;

import java.awt.Image;


import entorno.Entorno;
import entorno.Herramientas;

public class Jugador extends Entidad {
	boolean estaSaltando; //false = no esta saltando
	int contadorSalto;
	boolean estaMuerto;


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


	public void moverse(boolean dirMov) {
		if (estaApoyado || estaSaltando || estaCayendo) {
			if (dirMov) {
				this.x += 1;
			} else{
				this.x -= 1;
			}
			this.dir = dirMov;
			
			//Aparecer por el otro costado
			if(x > 800) {
				x = 0;
			}
			if(x < 0) {
				x = 800;
			}
		}
	}

	public void movVertical() {
		//Caer
		if (!estaApoyado) {
			this.y += 3;
			estaCayendo=true;
		}
		if(estaSaltando) {
			this.y -= 10;
			contadorSalto++;
		}
		if(contadorSalto == 20) {
			contadorSalto = 0;
			estaSaltando = false;
			estaCayendo= true;
		}
	}
	
}
