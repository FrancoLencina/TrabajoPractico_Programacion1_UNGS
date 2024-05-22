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
	Antizanahorias[] numeles;
	
	//Comentario de Fran
	
	
	public Juego() {
		//Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Super Duper Juego", 800, 600);
		
		//Inicialización de las Entidades
		bart = new Bartolome(300, 542);
		numeles = new Antizanahorias[8];
		int ene= 420;
		for (int i=0; i<numeles.length;i++) {
			numeles[i] = new Antizanahorias((int) (Math.random()*770) + 30 , ene);
			if (i%2==1) {
				ene-=122;
			}
		}
		
		
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
		
		if (detectarCostado (bart,p)) {
			System.out.println("toque el costado del bloque");
		}
		
		//Detectar colisiones con el enemigo
		for (int i=0; i<numeles.length;i++) {
			if (numeles[i]!=null) {
				if(detectarColisionEnemigo(bart, numeles[i])) {
					System.out.println("se golpearon");
					}
				}
		}
		
		
		//EJECUCIÓN DE METODOS DEL ENEMIGO
		for (int i=0; i<numeles.length;i++) {
			if (numeles[i]!=null) {
				numeles[i].mostrar(entorno);
				numeles[i].movVertical();
				}
			}
		
		//Detectar colisiones con el piso
		for (int i=0; i<numeles.length;i++) {
			if (numeles[i]!=null) {
				if(detectarApoyo(numeles[i], p)) {
					numeles[i].estaApoyado = true;
					}
				else {
					numeles[i].estaApoyado = false;
					}
				}
		}
		
		//if (numeles[i]!=null) {detectarColision (numel, p);}
		
		//moverse
		for (int i=0; i<numeles.length;i++) {
			if (numeles[i]!=null) {
				numeles[i].moverse(entorno);
				}
		}
		
		
		
		//Comportamieto de la bala
		if(bala != null) {
			bala.mostrar(entorno);
			bala.moverse();
			
			
			//TODAVIA NO FUNCIONA (falta hacer que el enemigo desaparezca después de que le pegue la bala)
			for (int i=0; i<numeles.length;i++) {
				if(detectarColisionBala(numeles[i], bala)) {
					/*bala=null;
					numeles[i]=null;*/}
				}
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
	
	public boolean detectarCostado(Entidad ba, Bloque bl) {
		return (Math.abs(ba.getDerecho() - bl.getIzquierdo()) < 3.5 ||
				Math.abs(ba.getIzquierdo() - bl.getDerecho()) < 3.5) &&
				bl.getTecho()<ba.getPiso()&&
				bl.getPiso()>ba.getTecho();
		}
	
	public boolean detectarCostado(Entidad ba, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarCostado(ba, pi.bloques[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarCostado(Entidad ba, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarCostado(ba, pisos[i])) {
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
	public boolean detectarColisionBala(Antizanahorias[] nu, Bala b) {
		for (int i=0; i<nu.length;i++) {
			if (detectarColisionBala(nu[i], b)) {
				return true;
			}
		}
		return false;
	}
}
	
