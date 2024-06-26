package juego;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo extends Entidad{
	int dirInicial = (int) (Math.random()*2 + 1);
	public Enemigo(double x, double y) {
		this.x = x;
		this.y = y;
		this.escala = 0.09;
		spriteIzq = Herramientas.cargarImagen("imagenes/AngryDogLeft.png");
		spriteDer = Herramientas.cargarImagen("imagenes/AngryDogRight.png");
		this.estaApoyado = true;
		this.alto = spriteIzq.getHeight(null) * escala;
		this.ancho = spriteIzq.getWidth(null) * escala;
		this.xInicial = x;
		this.yInicial = y;
		this.dir = true;
		if (dirInicial == 2) {
			this.dir = false;
		}
	}
	
	public void moverse( Entorno entorno) {
		
		if (estaApoyado) {
			
			if (dir) {
				this.x += 2;
			}
			if(!dir) {
				this.x -= 2;
			}
			if(getDerecho() > entorno.ancho()- 10) {
				dir = false;
			}
			if(getIzquierdo() < 10) {
				dir = true;
			}
		}
	}
	
	
}
