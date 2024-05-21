package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

import entorno.Entorno;
import entorno.Herramientas;

public class Bloque {
	double x, y, alto, ancho, escala;
	Image sprite;
	boolean rompible; //true = rompible
	boolean hayColision = false;
	boolean existe = true;
	Rectangle hitbox;
	
	public Bloque(double x, double y) {
		this.x = x;
		this.y = y;
		rompible = true;
		if(Math.random() > 0.8) {
			rompible = false;
		}
		if(rompible) {
			sprite = Herramientas.cargarImagen("bloque.jpg");
		}
		else {
			sprite = Herramientas.cargarImagen("bloqueIrrompible.jpg");
		}
		
		escala = 0.2;
		alto = sprite.getHeight(null)*escala;
		ancho = sprite.getWidth(null)*escala;
		hitbox = new Rectangle((int)this.x,(int)this.y,(int)this.ancho,(int)this.alto);
	}
	
	public void mostrar(Entorno e) {
		e.dibujarImagen(sprite, x, y, 0, escala);
	}
	

	
	
	public double getTecho(){
		return y - alto/2;
	}
	
	public double getPiso(){
		return y + alto/2;
	}
	
	public double getDerecho(){
		return x + ancho/2;
	}
	
	public double getIzquierdo(){
		return x - ancho/2;
	}

}
