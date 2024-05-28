package juego;
import entorno.Entorno;
import entorno.Herramientas;

public class Piso {
	Bloque[] bloques;
	double y;
	Entorno e;
	
	public Piso(double y, Entorno e) {
		this.e = e ;
		Bloque testigo = new Bloque(0, 0);
		bloques =  new Bloque [(int) (e.ancho() / testigo.ancho) + 1];
		this.y = y;
		
		for(int i = 0; i < bloques.length; i++) {
			bloques[i] = new Bloque((i+0.5)* testigo.ancho, y);
		}
		
		//Establecemos que los bloques debajo de la meta sean siempre irrompibles.
		if((bloques[10].rompible || bloques[11].rompible)  && y == e.alto()/5 ) {
			bloques[10].rompible = false;
			bloques[11].rompible = false;
			bloques[10].sprite = Herramientas.cargarImagen("roca2.png");
			bloques[11].sprite = Herramientas.cargarImagen("roca2.png");
		}
		
	}
	
	public void mostrar(Entorno e) {
		for(int i = 0; i < bloques.length; i++) {
			if(bloques[i] != null) {
				bloques[i].mostrar(e);
			}
		}
	}

}
