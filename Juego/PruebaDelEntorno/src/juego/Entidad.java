package juego;

import java.awt.Color;

import java.awt.Image;
import java.awt.Rectangle;

import entorno.Entorno;
import entorno.Herramientas;

public class Entidad {
		double x, y;
		Image spriteIzq;
		Image spriteDer;
		boolean dir; // false = Izq
		boolean estaApoyado;
		double escala;
		double alto;
		double ancho;
		boolean estaCayendo;
		
		
		public Entidad(Double x, double y) {
			this.x = x;
			this.y = y;
			spriteIzq = Herramientas.cargarImagen("missigno.png");
			spriteDer = Herramientas.cargarImagen("missigno.png");
			dir = true;
			estaApoyado = false;
			escala = 0.2;
			alto = spriteIzq.getHeight(null) * escala;
			ancho = spriteIzq.getWidth(null) * escala;
			estaCayendo = false;
		}
		
		public void mostrar(Entorno e) {
		
			//Dibujar Sprite
			if (dir) {
				e.dibujarImagen(spriteDer, x, y, 0, escala);
			} else {
				e.dibujarImagen(spriteIzq, x, y, 0, escala);
			}
		}
		
		public void movVertical() {
			//Caer
			if (!estaApoyado) {
				
				this.y += 2.5;
				estaCayendo=true;
			}
		}
		
		//hitbox de la entidad
		public double getTecho() {
			return y - alto / 2;
		}

		public double getPiso() {
			return y + alto / 2;
		}

		public double getDerecho() {
			return x + ancho / 2;
		}

		public double getIzquierdo() {
			return x - ancho / 2;
		}
}
