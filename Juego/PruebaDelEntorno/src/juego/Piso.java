package juego;

import java.awt.Color;

import entorno.Entorno;

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
		
	}
	
	public void mostrar(Entorno e) {
		for(int i = 0; i < bloques.length; i++) {
			if(bloques[i] != null) {
				bloques[i].mostrar(e);
			}
		}
	}

}
