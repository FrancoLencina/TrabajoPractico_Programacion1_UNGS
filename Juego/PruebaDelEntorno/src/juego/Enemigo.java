package juego;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo extends Entidad{

	public Enemigo(double x, double y) {
		this.x = x;
		this.y = y;
		this.escala = 0.08;
		spriteIzq = Herramientas.cargarImagen("AngryDogLeft.png");
		spriteDer = Herramientas.cargarImagen("AngryDogRight.png");
		this.estaApoyado = true;
		this.alto = spriteIzq.getHeight(null) * escala;
		this.ancho = spriteIzq.getWidth(null) * escala;
		this.xInicial = x;
		this.yInicial = y;
		
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