package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Entidad {
		double x, y;
		int velocidad = 1;
		Image spriteIzq;
		Image spriteDer;
		boolean dir; // false = Izq
		boolean estaApoyado;
		double escala;
		double alto;
		double ancho;
		boolean estaCayendo;
		double xInicial;
		double yInicial;
		
		
		public Entidad() {
			spriteIzq = Herramientas.cargarImagen("imagenes/missigno.png");
			spriteDer = Herramientas.cargarImagen("imagenes/missigno.png");
			dir = true;
			estaApoyado = false;
			escala = 0.095;
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
