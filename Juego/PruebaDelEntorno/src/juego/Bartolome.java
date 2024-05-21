package juego;

import java.awt.Image;


import entorno.Entorno;
import entorno.Herramientas;

public class Bartolome extends Entidad {
	boolean estaSaltando; //false = no esta saltando
	int contadorSalto;


	public Bartolome(double x, double y) {
		super(x,y);
		this.x = x;
		this.y = y;
		spriteIzq = Herramientas.cargarImagen("bodyIzq.png");
		spriteDer = Herramientas.cargarImagen("body.png");
		contadorSalto = 0;
		estaApoyado = false;
		estaSaltando = false;
		estaCayendo = false;
		alto = spriteIzq.getHeight(null) * escala;
		ancho = spriteIzq.getWidth(null) * escala;
		
	}


	public void moverse(boolean dirMov) {
		if (estaApoyado || estaSaltando || estaCayendo) {
			if (dirMov) {
				this.x += 1;
			} else {
				this.x -= 1;
			}
			this.dir = dirMov;
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
