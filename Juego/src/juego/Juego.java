package juego;

import java.awt.Color;
import java.awt.Image;
//import java.awt.Rectangle;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	Jugador[] jugadores;
	Piso[] p;
	Bala[] balas;
	Bala[] bombas;
	Enemigo[] enemigos;
	Image background;
	Bloque gato;
	Image michi;
	int puntaje = 0;
	int muertos = 0;
	int contadorEnemigos =0;
	
	
	
	public Juego() {
		//Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Juego", 800,600);
		background = Herramientas.cargarImagen("fondo.jpg");
		
		gato = new Bloque(entorno.ancho()/2,78);
		
		michi = Herramientas.cargarImagen("StrawberryCat.png");
		
		//Generación de pisos
				p = new Piso[5];
				for(int i = 0; i < p.length; i++) {
					p[i] = new Piso(entorno.alto()/p.length + i * (entorno.alto() / p.length), entorno);
				}
				
				
		//Inicialización de las Entidades
		jugadores = new Jugador[1];
		for (int j=0; j<jugadores.length;j++) {
			jugadores[j] = new Jugador(entorno.ancho()/2, entorno.alto()-(entorno.alto()/10));
		}
		enemigos = new Enemigo[1];//(p.length-1)*2
		double yInicial= entorno.alto()-(entorno.alto()/10)-entorno.alto()/p.length;
		double xInicial= Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04;
		for (int i=0; i<enemigos.length;i++) {
			enemigos[i] = new Enemigo((int) (xInicial), yInicial);
			contadorEnemigos += 1;
			if (i%2==0) {
				xInicial += entorno.ancho()/3;
				if (xInicial > entorno.ancho()) {
					xInicial -= entorno.ancho();
				}
			}
			if (i%2==1) {
				xInicial=Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04;
				yInicial-=entorno.alto()/p.length;
			}
		}
		//Inicialización del array de bombas y balas
		
		balas = new Bala[jugadores.length];
		bombas = new Bala[enemigos.length];
		
		//Inicia el juego!
		this.entorno.iniciar();
	}

	public void tick() {
		
		//Cargar Fondo
		entorno.dibujarImagen(background, entorno.ancho()/2,entorno.alto()/2, 0,1); //revisar el tema de la escala
        entorno.cambiarFont("Serif", 40, Color.WHITE);
        String texto = "Puntos: " + puntaje; 
        String texto2 = "Enemigos eliminados: " + muertos;
        entorno.escribirTexto(texto, 10, entorno.alto()/20);
        entorno.escribirTexto(texto2, entorno.ancho()-410, entorno.alto()/20); //410 es aprox el tamaño del texto2
        //Dibujar la meta
		entorno.dibujarImagen(michi, entorno.ancho()/2,78,0,0.1);
        //gato.mostrar(entorno);

		
		//Procesamiento de un instante de tiempo
		for(int i = 0; i < p.length; i++) {
			p[i].mostrar(entorno);
		}
		
		//controles jugador 1
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			jugadores[0].moverse(true,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			jugadores[0].moverse(false,entorno);
		}
		if(balas[0] == null && entorno.estaPresionada(entorno.TECLA_ENTER)) {
			balas[0] = new Bala(jugadores[0].x, jugadores[0].y, jugadores[0].dir);
		}
		if(entorno.estaPresionada(entorno.TECLA_ARRIBA) && jugadores[0].estaApoyado) {
			jugadores[0].estaSaltando = true;
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && jugadores[0].estaSaltando) {
			jugadores[0].moverse(true,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && jugadores[0].estaSaltando) {
			jugadores[0].moverse(false,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)&& jugadores[0].estaCayendo) {
			jugadores[0].moverse(true,entorno);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)&& jugadores[0].estaCayendo) {
			jugadores[0].moverse(false,entorno);
		}
		
		//controles jugador 2
		if (jugadores.length>1 && jugadores[1]!=null){
			if(entorno.estaPresionada('D')) {
				jugadores[1].moverse(true,entorno);
			}
			if(entorno.estaPresionada('A')) {
				jugadores[1].moverse(false,entorno);
			}
			if(balas[1] == null && entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
				balas[1] = new Bala(jugadores[1].x, jugadores[1].y, jugadores[1].dir);
			}
			if(entorno.estaPresionada('W') && jugadores[1].estaApoyado) {
				jugadores[1].estaSaltando = true;
			}
			if(entorno.estaPresionada('D') && jugadores[1].estaSaltando) {
				jugadores[1].moverse(true,entorno);
			}
			if(entorno.estaPresionada('A') && jugadores[1].estaSaltando) {
				jugadores[1].moverse(false,entorno);
			}
			if(entorno.estaPresionada('D')&& jugadores[1].estaCayendo) {
				jugadores[1].moverse(true,entorno);
			}
			if(entorno.estaPresionada('A')&& jugadores[1].estaCayendo) {
				jugadores[1].moverse(false,entorno);
			}
		}
			int c=0;
		for (int j = 0; j<jugadores.length;j++) {
			if (jugadores[j]!=null) {
			if (jugadores[j].estaMuerto) {c+=1;}}
		}
		
		if( c==jugadores.length|| entorno.estaPresionada(entorno.TECLA_CTRL)) { //c=jugadores.lenght significa que los dos jugadores murieron
			for (int j=0; j<jugadores.length;j++) {
				jugadores[j]=null;
			}
			jugadores[0] = new Jugador(entorno.ancho()/2, entorno.alto()-(entorno.alto()/10));
			//jugadores[1] = new Jugador(entorno.ancho()/2+50, entorno.alto()-(entorno.alto()/10));
			puntaje=0;
			muertos=0;
			contadorEnemigos=0;
			for(int i = 0; i < bombas.length; i++) {
				bombas[i] = null;
			}
			for (int b=0; b<balas.length; b++) {
				balas[b]=null;
			}
			double yInicial= entorno.alto()-(entorno.alto()/10)-entorno.alto()/p.length;
			double xInicial= Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04;
			for (int i=0; i<enemigos.length;i++) {
				enemigos[i] = new Enemigo((int) (xInicial), yInicial);
				contadorEnemigos += 1;
				if (i%2==0) {
					xInicial += entorno.ancho()/3;
					if (xInicial > entorno.ancho()) {
						xInicial -= entorno.ancho();
					}
				}
				if (i%2==1) {
					xInicial=Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04;
					yInicial-=entorno.alto()/p.length;
				}
			}
			for(int i = 0; i < p.length; i++) {
				p[i] = null;
				p[i] = new Piso(entorno.alto()/p.length + i * (entorno.alto() / p.length), entorno);
			}
			for (int j=0; j<jugadores.length;j++) {
				jugadores[0].estaMuerto=false;
			}
		}
		
		//COMPORTAMIENTO DE LA BOMBA
		for(int i = 0; i < bombas.length; i++) {
			if(bombas[i] != null) {
				bombas[i].spriteDer = Herramientas.cargarImagen("bomba.png");
				bombas[i].spriteIzq = Herramientas.cargarImagen("bomba.png");
				bombas[i].mostrar(entorno);
				bombas[i].moverse();
			}
		//que la bomba desaparezca al tocar el borde de la pantalla
			
			if(bombas[i] != null && (bombas[i].x < -0.1 * entorno.ancho() 
				|| bombas[i].x > entorno.ancho() * 1.1)) {
				bombas[i] = null;
					
				}
		
		//que desaparezca una bomba al tocar la bala
			for (int b=0; b<balas.length; b++) {
				if(bombas[i] != null && balas[b]!=null) {
					if(balaContraBomba(balas[b], bombas[i])) {
						bombas[i]=null;
						puntaje+=1;
					}
				}
			}
		}
		
		//Disparo del enemigo
		
					
		for (int i = 0 ; i < enemigos.length ; i++) {
			for (int j = 0 ; j < jugadores.length ; j++) {
				if (enemigos[i] != null && jugadores[j]!=null) {
					if( bombas[i] == null && Math.abs(enemigos[i].y-jugadores[j].y) < 5) {
						if (enemigos[i].dir && enemigos[i].x<jugadores[j].x){
							bombas[i] = new Bala(enemigos[i].x, enemigos[i].y, enemigos[i].dir);
						} 
				
						if (!enemigos[i].dir && enemigos[i].x>jugadores[0].x){
						bombas[i] = new Bala(enemigos[i].x, enemigos[i].y, enemigos[i].dir);
						}
					}
				}
			}
		}
		
		//Morir al tocar una bomba
		
		for (int i = 0; i < bombas.length; i++) {
			for (int j = 0; j<jugadores.length ; j++) {
				if (detectarColisionBala(jugadores[j], bombas[i])) {
					jugadores[j].estaMuerto = true;
				}
			}
		}
		
		//Eliminar la bomba perteneciente a un enemigo específico cuando éste muere
		for (int i = 0; i < bombas.length; i++) {
			if (enemigos[i]==null) {
				bombas[i] = null;
			}
		}
		
	
		//EJECUCIÓN DE METODOS DE LOS JUGADORES
		if(jugadores.length > 1) {
			if (jugadores[1]!=null){
				jugadores[1].spriteDer = Herramientas.cargarImagen("bomba.png");
				jugadores[1].spriteIzq = Herramientas.cargarImagen("bomba.png");
			}
		}
		
		
		for (int i=0; i<jugadores.length; i++) {
			
			//jugadores[i].mostrar(entorno);
			//jugadores[i].movVertical(entorno, p);
			
			if (jugadores[i]!=null) {
				jugadores[i].mostrar(entorno);
				jugadores[i].movVertical(entorno, p);
				if (jugadores[i].estaMuerto) { 
					jugadores[i].x=entorno.ancho()+100;
					
					}
			}
		
		
		//Detectar colisiones con el piso
			if (jugadores[i]!=null) {
			if(detectarApoyo(jugadores[i], p)) {
				jugadores[i].estaApoyado = true;
			}
		
			else {
			jugadores[i].estaApoyado = false;
			}
		
			if(detectarColision (jugadores[i], p)) {
			jugadores[i].estaSaltando = false;
			jugadores[i].contadorSalto = 0;
			}
		
			if (chocoDer(jugadores[i], p) && (jugadores[i].estaSaltando||jugadores[i].estaCayendo) && entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			jugadores[i].x+=2;
			jugadores[i].estaSaltando=false;
			jugadores[i].estaCayendo=true;
			jugadores[i].estaApoyado=false;
			}
		
			if (chocoIzq(jugadores[i], p)  && (jugadores[0].estaSaltando||jugadores[0].estaCayendo) && entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			jugadores[i].x-=2;
			jugadores[i].estaSaltando=false;
			jugadores[i].estaCayendo=true;
			jugadores[i].estaApoyado=false;
			}
			
		
		//Detectar colisiones con el enemigo
			for (int j=0; j<enemigos.length;j++) {
				if (enemigos[j]!=null && jugadores[i]!=null) {
					if(detectarColisionEnemigo(jugadores[i], enemigos[j])) {
					jugadores[i].estaMuerto = true;
					}
				}
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
		
		//Al quedar solo un enemigo se genera uno nuevo
		
		for (int i=0; i<enemigos.length; i++) {
			if (contadorEnemigos<2 && enemigos[i]== null) {
				enemigos[i] = new Enemigo(Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04, -30);
				contadorEnemigos+=1;
			}
		}
		
	
		
		
		
		
		//COMPORTAMIENTO DE LA BALA
		
		for (int b=0; b<balas.length; b++) {
			if(balas[b] != null) {
				balas[b].mostrar(entorno);
				balas[b].moverse();
			}
		
		
			
			
			//Detectar disparo
			for (int i=0; i<enemigos.length;i++) {
				if (enemigos[i] != null) {
					if (detectarColisionBala(enemigos[i], balas[b])) {
						balas[b]=null;
						enemigos[i]=null;
						contadorEnemigos-=1;
						muertos+=1;
						puntaje +=2;
					}
				}
			}
		}
		
		//Hacer que la bala desaparezca al tocar el borde de la pantalla
		
		for (int b=0; b<balas.length; b++) {
			if( balas[b] != null && (balas[b].x < -0.1 * entorno.ancho() 
					|| balas[b].x > entorno.ancho() * 1.1)) {
					balas[b] = null;	
			}
		}
		
		
		//Ganar el juego
		if((jugadores[0].x <= entorno.ancho()/2 + 15 && jugadores[0].x >= entorno.ancho()/2 - 15) && jugadores[0].y <= 90){
			
			entorno.dibujarImagen(background, entorno.ancho()/2,entorno.alto()/2, 0,1); //revisar el tema de la escala
			jugadores[0].velocidad =0;

		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
	
	
	
	//Metodos de colision las entodades con el bloque
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
		boolean verificar = Math.abs((ju.getTecho() - bl.getPiso())) < 3.5 && 
				(ju.getIzquierdo() < (bl.getDerecho())) &&
				(ju.getDerecho() > (bl.getIzquierdo()));
		if (verificar && bl.rompible) {puntaje+= 2;} //ganar puntos por romper bloques
		return 	verificar;	
	}
	
	public boolean detectarColision(Jugador ju, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarColision(ju, pi.bloques[i])) {
				if(pi.bloques[i].rompible && !ju.estaMuerto) {
					pi.bloques[i] = null;
				}
				return true;
			}
		}
		
		return false;
	}
	
	public boolean detectarColision(Jugador ju, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarColision(ju, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	
	//Metodo de colision de los costados del jugador con el bloque, para que no los transpase al saltar en diagonal
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
			if(detectarCostado(ju, pisos[i]) && !ju.estaApoyado) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean chocoDer(Entidad ju, Piso[] pi) {
		for (int i=0; i<p.length; i++) {
			for (int j=0; j<pi[i].bloques.length; j++) {
				if (pi[i].bloques[j]!=null) {
					if (detectarCostado(ju, pi) && pi[i].bloques[j].getDerecho() < ju.getDerecho()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean chocoIzq(Entidad ju, Piso[] pi) {
		for (int i=0; i<p.length; i++) {
			for (int j=0; j<pi[i].bloques.length; j++) {
				if (pi[i].bloques[j]!=null) {
					if (detectarCostado(ju, pi) && pi[i].bloques[j].getIzquierdo() > ju.getIzquierdo()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	//Metodo de detectar la colison del jugador con el enemigo
	public boolean detectarColisionEnemigo(Entidad ju, Entidad en) {
		return Math.abs((ju.getPiso() - en.getPiso())) < 3.5 && 
				(ju.getIzquierdo() < (en.getDerecho())) &&
				(ju.getDerecho() > (en.getIzquierdo()));		
	}
	
	//Metodo de detectar la colison del jugador con la bala
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
	
	
	//Metodo de detectar la colison de la bala y la bomba
	public boolean balaContraBomba (Bala bom, Bala bal) {
		if (bal!=null) {
			return ((Math.abs(bal.getDerecho() - bom.getIzquierdo()) < 3.5 ||
				Math.abs(bal.getIzquierdo() - bom.getDerecho()) < 3.5 )&&
				(bal.y > bom.getTecho()) &&
				(bal.y < bom.getPiso()));
		}
		return false;
	}
	
	
}