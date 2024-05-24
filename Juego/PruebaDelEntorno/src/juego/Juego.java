package juego;

//import java.awt.Color;
import java.awt.Image;
//import java.awt.Rectangle;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	Jugador jugador;
	Piso[] p;
	Bala bala;
	Bala bomba;
	Enemigo[] enemigos;
	Image background;
	
	
	public Juego() {
		//Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Juego", 900, 650);
		background = Herramientas.cargarImagen("fondo.jpg");
		
		
		//Generación de pisos
				p = new Piso[5];
				
				for(int i = 0; i < p.length; i++) {
					p[i] = new Piso(entorno.alto()/p.length + i * (entorno.alto() / p.length), entorno);
				}
				
		//Inicialización de las Entidades
		jugador = new Jugador(entorno.ancho()/2, entorno.alto()-(entorno.alto()/10));
		enemigos = new Enemigo[(p.length-1)*2];
		double ene= entorno.alto()-(entorno.alto()/10)-entorno.alto()/p.length;
		for (int i=0; i<enemigos.length;i++) {
			enemigos[i] = new Enemigo((int) (Math.random()*entorno.ancho()*0.95) + entorno.ancho()*0.04 , ene);
			if (i%2==1) {
				ene-=entorno.alto()/p.length;
			}
		}
		
		//Inicia el juego!
		this.entorno.iniciar();
	}

	public void tick() {
		//Cargar Fondo
		entorno.dibujarImagen(background, 400,300, 0);
		
		//Procesamiento de un instante de tiempo
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			jugador.moverse(true,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			jugador.moverse(false,entorno);
		}
		if(bala == null && entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
			bala = new Bala(jugador.x, jugador.y, jugador.dir);
		}
		if(entorno.estaPresionada(entorno.TECLA_ARRIBA) && jugador.estaApoyado) {
			jugador.estaSaltando = true;
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && jugador.estaSaltando) {
			jugador.moverse(true,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && jugador.estaSaltando) {
			jugador.moverse(false,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)&& jugador.estaCayendo) {
			jugador.moverse(true,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)&& jugador.estaCayendo) {
			jugador.moverse(false,entorno);
		}
		if(jugador.estaMuerto) {
			bala=null;
			jugador.x=jugador.xInicial;
			jugador.y=jugador.yInicial;
			double ene= entorno.alto()-entorno.alto()*0.3;
			for (int i=0; i<enemigos.length;i++) {
				enemigos[i] = new Enemigo((int) (Math.random()*entorno.ancho()*0.95) + entorno.ancho()*0.04 , ene);
				if (i%2==1) {
					ene-=entorno.alto()/p.length;
				}
			}
			for(int i = 0; i < p.length; i++) {
				p[i] = null;
				p[i] = new Piso(entorno.alto()/p.length + i * (entorno.alto() / p.length), entorno);
			}
			jugador.estaMuerto=false;
		}
		
		//Disparo del enemigo
		/*for (int i = 0 ; i < enemigos.length ; i++) {
			if(bomba == null && enemigos[i].getPiso() == jugador.getPiso()) {
				if (enemigos[i].dir && enemigos[i].x<jugador.x){
					bomba = new Bala(enemigos[i].x, enemigos[i].y, enemigos[i].dir);
					System.out.println("pium pium! (derecha)");
				} 
				
				if (!enemigos[i].dir && enemigos[i].x>jugador.x){
					bomba = new Bala(enemigos[i].x, enemigos[i].y, !enemigos[i].dir);
					System.out.println("pium pium! (izquierda)");
				}
			}
		}*/
		//EJECUCIÓN DE METODOS DEL JUGADOR
		
		jugador.mostrar(entorno);
		jugador.movVertical(entorno, p);
		
		
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
					bala=null;
					enemigos[i]=null;}
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
		if (b!=null) {
			return ((Math.abs(b.getDerecho() - en.getIzquierdo()) < 3.5 ||
				Math.abs(b.getIzquierdo() - en.getDerecho()) < 3.5 )&&
				(b.y > en.getTecho()) &&
				(b.y < en.getPiso()));
		}
		return false;
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
	
