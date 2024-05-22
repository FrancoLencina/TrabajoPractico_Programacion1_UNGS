package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	Jugador jugador;
	Piso[] p;
	Bala bala;
	Enemigo[] enemigos;
	Image background;
	
	
	public Juego() {
		//Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Juego", 800, 600);
		background = Herramientas.cargarImagen("fondo.jpg");
		
		
		//Inicialización de las Entidades
		jugador = new Jugador(300, 542);
		enemigos = new Enemigo[8];
		int ene= 420;
		for (int i=0; i<enemigos.length;i++) {
			enemigos[i] = new Enemigo((int) (Math.random()*770) + 30 , ene);
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
		//Cargar Fondo
		entorno.dibujarImagen(background, 400,300, 0);
		
		//Procesamiento de un instante de tiempo
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			jugador.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			jugador.moverse(false);
		}
		if(bala == null && entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
			bala = new Bala(jugador.x, jugador.y, jugador.dir);
		}
		if(entorno.estaPresionada(entorno.TECLA_ARRIBA) && jugador.estaApoyado) {
			jugador.estaSaltando = true;
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && jugador.estaSaltando) {
			jugador.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && jugador.estaSaltando) {
			jugador.moverse(false);
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)&& jugador.estaCayendo) {
			jugador.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)&& jugador.estaCayendo) {
			jugador.moverse(false);
		}
		if(jugador.estaMuerto) {
			jugador.x=jugador.xInicial;
			jugador.y=jugador.yInicial;
			int ene= 420;
			for (int i=0; i<enemigos.length;i++) {
				enemigos[i]= null; 		
				enemigos[i] = new Enemigo((int) (Math.random()*770) + 30 , ene);
				if (i%2==1) {
					ene-=122;
				}
				
			}
			for(int i = 0; i < p.length; i++) {
				p[i] = null;
				p[i] = new Piso(120 + i * (entorno.alto() / p.length));
			}
			jugador.estaMuerto=false;
		}
		
		//EJECUCIÓN DE METODOS DEL JUGADOR
		
		jugador.mostrar(entorno);
		jugador.movVertical();
		
		
		//Detectar colisiones con el piso
		if(detectarApoyo(jugador, p)) {
			jugador.estaApoyado = true;
		}
		else {
			jugador.estaApoyado = false;
		}
		
		if(detectarColision (jugador, p)) {
			jugador.estaSaltando = false;
			jugador.contadorSalto = 0;
		}
		
		if (detectarCostado (jugador,p)) {
			System.out.println("toque el costado del bloque");
		}
		
		//Detectar colisiones con el enemigo
		for (int i=0; i<enemigos.length;i++) {
			if (enemigos[i]!=null) {
				if(detectarColisionEnemigo(jugador, enemigos[i])) {
					System.out.println("se golpearon");
					jugador.estaMuerto = true;
					}
				}
		}
		
		
		//EJECUCIÓN DE METODOS DEL ENEMIGO
		for (int i=0; i<enemigos.length;i++) {
			if (enemigos[i]!=null) {
				
				
				//Mostrar
				enemigos[i].mostrar(entorno);
				
				//Moverse
				enemigos[i].moverse(entorno);
				
				//Caer
				enemigos[i].movVertical();
				
				//Detectar apoyo con el piso
				if(detectarApoyo(enemigos[i], p)) {
					enemigos[i].estaApoyado = true;
				}
				else {
					enemigos[i].estaApoyado = false;
				}
			}
		}
		
	
		
		
		
		
		//COMPORTAMIENTO DE LA BALA
		if(bala != null) {
			bala.mostrar(entorno);
			bala.moverse();
			
			
			//Detectar disparo
			for (int i=0; i<enemigos.length;i++) {
				if (enemigos[i] != null) {
					if (detectarColisionBala(enemigos[i], bala)) {
					/*bala=null;
					enemigos[i]=null;*/}
					}
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
	
	
	public boolean detectarApoyo(Entidad ju, Bloque bl) {
		return Math.abs((ju.getPiso() - bl.getTecho())) < 2 && 
				(ju.getIzquierdo() < (bl.getDerecho())) &&
				(ju.getDerecho() > (bl.getIzquierdo()));		
	}

	
	public boolean detectarApoyo(Entidad ju, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarApoyo(ju, pi.bloques[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarApoyo(Entidad ju, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarApoyo(ju, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColision(Entidad ju, Bloque bl) {
		return Math.abs((ju.getTecho() - bl.getPiso())) < 3.5 && 
				(ju.getIzquierdo() < (bl.getDerecho())) &&
				(ju.getDerecho() > (bl.getIzquierdo()));		
	}
	
	public boolean detectarColision(Entidad ju, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarColision(ju, pi.bloques[i])) {
				if(pi.bloques[i].rompible) {
					pi.bloques[i] = null;
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColision(Entidad ju, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarColision(ju, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarCostado(Entidad ju, Bloque bl) {
		return (Math.abs(ju.getDerecho() - bl.getIzquierdo()) < 1.5 ||
				Math.abs(ju.getIzquierdo() - bl.getDerecho()) < 1.5) &&
				bl.getTecho()<ju.getPiso()&&
				bl.getPiso()>ju.getTecho();
		}
	
	public boolean detectarCostado(Entidad ju, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarCostado(ju, pi.bloques[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarCostado(Entidad ju, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarCostado(ju, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColisionEnemigo(Entidad ju, Entidad en) {
		return Math.abs((ju.getPiso() - en.getPiso())) < 3.5 && 
				(ju.getIzquierdo() < (en.getDerecho())) &&
				(ju.getDerecho() > (en.getIzquierdo()));		
	}
	
	
	public boolean detectarColisionBala(Entidad en, Bala b) {
		return (Math.abs(b.getDerecho() - en.getIzquierdo()) < 3.5 ||
				Math.abs(b.getIzquierdo() - en.getDerecho()) < 3.5 &&
				(b.y > en.getTecho()) &&
				(b.y < en.getPiso()));
	}
	public boolean detectarColisionBala(Enemigo[] en, Bala b) {
		for (int i=0; i<en.length;i++) {
			if (detectarColisionBala(en[i], b)) {
				return true;
			}
		}
		return false;
	}
}
	
