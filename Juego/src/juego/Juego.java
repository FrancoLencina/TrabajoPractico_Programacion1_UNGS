package juego;
import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El Entorno
	private Entorno entorno;
	//Entidades
	Jugador[] jugadores;
	Enemigo[] enemigos;
	//Municiones
	Bala[] balas;
	Bala[] bombas;
	//Pisos
	Piso[] p;
	Piso lava;
	//Imagenes
	Image titulo;
	Image background;
	Image pinkLava;
	Image gato;
	//Variables Globales
	boolean inicioJuego = false;
	boolean multijugador = false;
	int puntaje = 0;
	int muertos = 0;
	int contadorEnemigos = 0;
	double lavaVel = 0;
	double posInicial;
	String insertarJugador = "INSERT para Jugador 2";
	
	
	
	public Juego() {
		//Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Juego", 800,600);
		
		posInicial = entorno.ancho()/2-50;
		
		//Se Cargan las imagenes correspondientes
		titulo = Herramientas.cargarImagen("titulo.png");
		background = Herramientas.cargarImagen("fondo.jpg");
		gato = Herramientas.cargarImagen("StrawberryCat.png");
		pinkLava = Herramientas.cargarImagen("pink lava.png");
		
		//Generación de pisos
				p = new Piso[5];
				for(int i = 0; i < p.length; i++) {
					p[i] = new Piso(entorno.alto()/p.length + i * (entorno.alto() / p.length), entorno);
				}
				
		//-------------------------------------------INICIALIZACIÓN DE ENTIDADES-------------------------------------------------------------//
				
		jugadores = new Jugador[2]; //El array ya tiene espacio para dos jugadores
		jugadores[0] = new Jugador(posInicial, entorno.alto()-(entorno.alto()/10));
		jugadores[1] = new Jugador(-200, entorno.alto()-(entorno.alto()/10));
		enemigos = new Enemigo[(p.length-1)*2]; 
		double yInicial= entorno.alto()-(entorno.alto()/10)-entorno.alto()/p.length;
		double xInicial= Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04;
		//Generamos 2 enemigos por piso
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

	//Procesamiento de un instante de tiempo
	public void tick() {
		
		//Cargar Fondo
		entorno.dibujarImagen(background, entorno.ancho()/2,entorno.alto()/2, 0,1); //revisar el tema de la escala
		
		//Dibujamos el texto para insertar el 2do jugador (lo hacemos primero porque el tamaño de la fuente es distinto a los demás textos)
		if(!multijugador) {
	       	entorno.cambiarFont("Serif", 20, Color.WHITE);
	       	entorno.escribirTexto(insertarJugador, 10 , entorno.alto()- 60);
       }
		
		//Dibujamos Puntaje y Enemigos eliminados
        entorno.cambiarFont("Serif", 40, Color.WHITE);
        String texto = "Puntos: " + puntaje; 
        String texto2 = "Enemigos eliminados: " + muertos;
        entorno.escribirTexto(texto, 10, entorno.alto()/20);
        entorno.escribirTexto(texto2, entorno.ancho()-410, entorno.alto()/20); //410 es aprox el tamaño del texto2
        
        //Dibujamos al gato (meta)
		entorno.dibujarImagen(gato, entorno.ancho()/2,78,0,0.1);
	 
		//Dibujamos los pisos
		for(int i = 0; i < p.length; i++) {
			p[i].mostrar(entorno);
		}
		
		//--------------------------------------------------------CONTROLES------------------------------------------------------------------//
		
		

		//CONTROLES JUGADOR 1
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
		
		//CONTROLES JUGADOR 2
		if (jugadores.length>1 && multijugador){
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
		
		
		//----------------------------------------------------REINCIO DEL JUEGO--------------------------------------------------------------//
		
		//Chequea si los jugadores estan muertos
		int c=0;
		for (int j = 0; j<jugadores.length;j++) {
			if (jugadores[j]!=null) {
				if (jugadores[j].estaMuerto) {c+=1;}
			}
		}
		
		if( c==jugadores.length|| entorno.estaPresionada(entorno.TECLA_CTRL)) { //c=jugadores.lenght significa que los dos jugadores murieron
			for (int j=0; j<jugadores.length;j++) {
				//Si hay dos jugadores, se reinicia el juego con ambos en pantalla. Si no, solo el jugador 1.
				if (multijugador) {
					jugadores[j] = null;
					jugadores[0] = new Jugador(posInicial, entorno.alto()-(entorno.alto()/10));
					jugadores[1] = new Jugador(posInicial+100, entorno.alto()-(entorno.alto()/10));
				}
				else {
					jugadores[0] = null;
					jugadores[0] = new Jugador(posInicial, entorno.alto()-(entorno.alto()/10));
				}
			}
			
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
			lavaVel=0;
			lava.y=entorno.alto()*2;
			
			for (int j = 0; j< jugadores.length; j++) {
				jugadores[j].estaMuerto = false;
			}
			
		}

		//------------------------------------------------------------LAVA-------------------------------------------------------------------//
		
		lava = new Piso(entorno.alto()*2-lavaVel, entorno); 
		lava.mostrar(entorno); 
		
		//Mover la lava hacia arriba
		if(inicioJuego) {
			lavaVel+=1;
		}
		
		entorno.dibujarImagen(pinkLava, entorno.ancho()/2, lava.y+430, 0);
		
		//------------------------------------------------METODOS DE JUGADORES---------------------------------------------------------------//
			
		//Insertar un nuevo jugador
		if (lava.y > jugadores[1].y) {
			if(entorno.estaPresionada(entorno.TECLA_INSERT) && !multijugador) {
				jugadores[1].x = posInicial+100;
				multijugador = true;	
			}
		}
		
		//Cargar el sprite del segundo jugador
		if(jugadores.length > 1) {
			if (jugadores[1]!=null){
				jugadores[1].spriteDer = Herramientas.cargarImagen("BlueberryPiRight.png");
				jugadores[1].spriteIzq = Herramientas.cargarImagen("BlueberryPiLeft.png");
			}
		}
		
		//Cargar el sprite de las balas del segundo jugador
		if(balas.length > 1) {
			if (balas[1]!=null){
				balas[1].spriteDer = Herramientas.cargarImagen("arandanoDer.png");
				balas[1].spriteIzq = Herramientas.cargarImagen("arandanoIzq.png");
			}
		}
		
		
		for (int j=0; j<jugadores.length; j++) {
			if (jugadores[j]!=null) {
				
				//mostrar el jugador
				jugadores[j].mostrar(entorno);
				
				//Quitar al jugador de la pantalla al morir, mientras haya un jugador vivo
				if (jugadores[j].estaMuerto) { 
					jugadores[j].y= entorno.alto()+100;
				}
				
				//Detectar colisiones con el piso
				if(detectarApoyo(jugadores[j], p)) {
					jugadores[j].estaApoyado = true;
				}
				else {
					jugadores[j].estaApoyado = false;
				}
			
				if(detectarColision (jugadores[j], p)) {
					jugadores[j].estaSaltando = false;
					jugadores[j].contadorSalto = 0;
				}
			
				if (chocoDer(jugadores[j], p) && (jugadores[j].estaSaltando||jugadores[j].estaCayendo) && entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
					jugadores[j].x+=2;
					jugadores[j].estaSaltando=false;
					jugadores[j].estaCayendo=true;
					jugadores[j].estaApoyado=false;
				}
			
				if (chocoIzq(jugadores[j], p)  && (jugadores[j].estaSaltando||jugadores[j].estaCayendo) && entorno.estaPresionada(entorno.TECLA_DERECHA)) {
					jugadores[j].x-=2;
					jugadores[j].estaSaltando=false;
					jugadores[j].estaCayendo=true;
					jugadores[j].estaApoyado=false;
				}
				
			
				//Detectar colisiones con el enemigo
				for (int i=0; i<enemigos.length;i++) {
					if (enemigos[i]!=null && jugadores[j]!=null) {
						if(detectarColisionEnemigo(jugadores[j], enemigos[i])) {
						jugadores[j].estaMuerto = true;
						}
					}
				}
				
				//Detectar colisiones con la bomba
				for (int i = 0; i < bombas.length; i++) {
					if (detectarColisionBala(jugadores[j], bombas[i])) {
						jugadores[j].estaMuerto = true;
					}
				}
				
				//Detectar colisión con la lava
				if (lava.y<jugadores[j].getPiso()+48) {
					jugadores[j].estaMuerto=true;	
				}
			}
		}
		
		if (!multijugador) {
			if (jugadores[0].estaMuerto) {jugadores[1].estaMuerto = true;}
		}
		
		//Activar la gravedad para cada jugador
		jugadores[0].movVertical(entorno, p);
		if (multijugador && !jugadores[1].estaMuerto) {
			jugadores[1].movVertical(entorno, p);
		}
		
		//-------------------------------------------------METODOS DEL ENEMIGO---------------------------------------------------------------//
		
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
			
			//Al quedar solo un enemigo se genera uno nuevo
			if (contadorEnemigos<2 && enemigos[i]== null) {
				enemigos[i] = new Enemigo(Math.random()*entorno.ancho()*0.95 + entorno.ancho()*0.04, -30);
				contadorEnemigos+=1;
			}
			
			if (lava.y<enemigos[i].getPiso()+50) {
				enemigos[i]=null;
			}
		}
		
		//-------------------------------------------------COMPORTAMIENTO DE LAS BALAS-------------------------------------------------------//
		
		//DEL JUGADOR
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
		
		//DEL ENEMIGO
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
					if( bombas[i] == null && Math.abs(enemigos[i].y-jugadores[j].y) < 5){
						
						//Si mira para la derecha y el jugador está a la derecha
						if (enemigos[i].dir && enemigos[i].x<jugadores[j].x){
							bombas[i] = new Bala(enemigos[i].x, enemigos[i].y, enemigos[i].dir);
						} 
						//Si mira para la izquierda y el jugador está a la izquierda
						if (!enemigos[i].dir && enemigos[i].x>jugadores[j].x){
						bombas[i] = new Bala(enemigos[i].x, enemigos[i].y, enemigos[i].dir);
						}
					}
				}
			}
		}
		
		//Eliminar la bomba perteneciente a un enemigo específico cuando éste muere
		for (int i = 0; i < bombas.length; i++) {
			if (enemigos[i]==null) {
				bombas[i] = null;
			}
		}
		
		//---------------------------------------------------GANAR EL JUEGO------------------------------------------------------------------//
		
		//Llamamos a estos metodos al final para poder dibujar una imagen por encima de la pantalla principal
		for (int j=0; j<jugadores.length; j++) {
			if (jugadores[j]!=null) {
				if((jugadores[j].x <= entorno.ancho()/2 + 15 && jugadores[j].x >= entorno.ancho()/2 - 15) && jugadores[j].y <= 90){
					entorno.dibujarImagen(background, entorno.ancho()/2,entorno.alto()/2, 0,1);
					jugadores[j].velocidad = 0;
					lavaVel=0;
				}
			}
		}

		//--------------------------------------------------PANTALLA DE INICIO---------------------------------------------------------------//
		
		//Llamamos a estos metodos al final para poder dibujar una imagen por encima de la pantalla principal
		if(titulo != null) {
			entorno.dibujarImagen(titulo, entorno.ancho()/2,entorno.alto()/2, 0,1);
		}
		
		if(entorno.estaPresionada(entorno.TECLA_ESPACIO)){
			inicioJuego = true;
			titulo=null;
		}
	}
	
	//INICIO DEL OBJETO JUEGO
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
	
	
	//METODOS DE COLISIÓN DE LAS ENTIDADES CON EL PISO
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
	
	//METODO DE COLISIÓN DEL JUGADOR CON EL ENEMIGO
	public boolean detectarColisionEnemigo(Entidad ju, Entidad en) {
		return Math.abs((ju.getPiso() - en.getPiso())) < 3.5 && 
				(ju.getIzquierdo() < (en.getDerecho())) &&
				(ju.getDerecho() > (en.getIzquierdo()));		
	}
	
	//METODO DE COLISIÓN DE LAS ENTIDADES CON LAS BALAS
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
	
	//METODO DE COLISIÓN DE LA BALA CON LA BOMBA
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