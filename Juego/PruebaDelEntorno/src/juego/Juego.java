package juego;

import java.awt.Color;
import java.awt.Rectangle;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	Bartolome bart;
	Piso[] p;
	Bala bala;
	Antizanahorias numel;

	
	
	
	public Juego() {
		//Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Super Duper Juego", 800, 600);
		
		//Inicialización de las Entidades
		bart = new Bartolome(300, 542);
		numel = new Antizanahorias(150,420);
		
		
		//Generación de pisos
		p = new Piso[5];
		
		for(int i = 0; i < p.length; i++) {
			p[i] = new Piso(120 + i * (entorno.alto() / p.length));
		}
		

		
		
		
		//Inicia el juego!
		this.entorno.iniciar();
	}

	public void tick() {
		//Procesamiento de un instante de tiempo
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			bart.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			bart.moverse(false);
		}
		if(bala == null && entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
			bala = new Bala(bart.x, bart.y, bart.dir);
		}
		if(entorno.estaPresionada(entorno.TECLA_ARRIBA) && bart.estaApoyado) {
			bart.estaSaltando = true;
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && bart.estaSaltando) {
			bart.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && bart.estaSaltando) {
			bart.moverse(false);
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)&& bart.estaCayendo) {
			bart.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)&& bart.estaCayendo) {
			bart.moverse(false);
		}
		
		
		//EJECUCIÓN DE METODOS DEL JUGADOR
		
		bart.mostrar(entorno);
		bart.movVertical();
		
		//Detectar colisiones con el piso
		if(detectarApoyo(bart, p)) {
			bart.estaApoyado = true;
		}
		else {
			bart.estaApoyado = false;
		}
		
		if(detectarColision (bart, p)) {
			bart.estaSaltando = false;
			bart.contadorSalto = 0;
		}
		
		//Detectar colisiones con el enemigo
		if (numel!=null) {
			if(detectarColisionEnemigo(bart, numel)) {
				System.out.println("se golpearon");
				}
			}
		
		
		//EJECUCIÓN DE METODOS DEL ENEMIGO
		if (numel!=null) {
			numel.mostrar(entorno);
			numel.movVertical();
			}
		
		//Detectar colisiones con el piso
		if (numel!=null) {
			if(detectarApoyo(numel, p)) {
			numel.estaApoyado = true;
		}
		else {
			numel.estaApoyado = false;
		}
		}
		if (numel!=null) {detectarColision (numel, p);}
		
		//moverse
		if (numel!=null) {numel.moverse(entorno);}
		
		
		
		
		
		
		
		//Comportamieto de la bala
		if(bala != null) {
			bala.mostrar(entorno);
			bala.moverse();
			
			
			//TODAVIA NO FUNCIONA (falta hacer que el enemigo desaparezca después de que le pegue la bala)
			if(detectarColisionBala(numel, bala)) {System.out.println("me dispararon"); bala=null; numel=null;}
		}
		
		for(int i = 0; i < p.length; i++) {
			p[i].mostrar(entorno);
		}
		
		if( bala != null && (bala.x < -0.1 * entorno.ancho() 
		|| bala.x > entorno.ancho() * 1.1)) {
			bala = null;
			
		}
		
		
		
		
		
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
	
	public boolean detectarApoyo(Entidad ba, Bloque bl) {
		return Math.abs((ba.getPiso() - bl.getTecho())) < 2 && 
				(ba.getIzquierdo() < (bl.getDerecho())) &&
				(ba.getDerecho() > (bl.getIzquierdo()));		
	}

	
	public boolean detectarApoyo(Entidad ba, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarApoyo(ba, pi.bloques[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarApoyo(Entidad ba, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarApoyo(ba, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColision(Entidad ba, Bloque bl) {
		return Math.abs((ba.getTecho() - bl.getPiso())) < 3.5 && 
				(ba.getIzquierdo() < (bl.getDerecho())) &&
				(ba.getDerecho() > (bl.getIzquierdo()));		
	}
	
	public boolean detectarColision(Entidad ba, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarColision(ba, pi.bloques[i])) {
				if(pi.bloques[i].rompible) {
					pi.bloques[i] = null;
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColision(Entidad ba, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarColision(ba, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColisionEnemigo(Entidad ba, Entidad nu) {
		return Math.abs((ba.getPiso() - nu.getPiso())) < 3.5 && 
				(ba.getIzquierdo() < (nu.getDerecho())) &&
				(ba.getDerecho() > (nu.getIzquierdo()));		
	}
	
	public boolean detectarColisionBala(Entidad en, Bala b) {
		return (Math.abs(b.getDerecho()-en.getIzquierdo())<3.5 ||
				Math.abs(b.getIzquierdo()-en.getDerecho())<3.5 &&
				(b.getTecho()>en.getTecho()) &&
				(b.getPiso()<en.getPiso()));
	}
}
	
