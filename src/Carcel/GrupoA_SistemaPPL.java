package Carcel;

import java.util.Scanner;//INGRESO DE DATOS POR TECLADO

public class GrupoA_SistemaPPL {
	// DECLARACIÓN ATRIBUTOS
	Scanner cin;
	private int opMenu;
	private String horarios[];
	private String estadoVisita, tramiteId;
	GrupoA_PPL ppl;
	GrupoA_Visitante visitante;

	public GrupoA_SistemaPPL(String tramiteId, int opMenu, String[] horarios, String estadoVisita) {
		// INICIALIZACIÓN DE ATRIBUTOS
		this.tramiteId = tramiteId;
		this.opMenu = opMenu;
		this.horarios = horarios;
		this.estadoVisita = estadoVisita;
		visitante = new GrupoA_Visitante("", "", "", "", "", "", 0, 0, "", "", "", "");
		ppl = new GrupoA_PPL("", "", "", "", "", "", 0, 0, 0, 0, "", "");
		cin = new Scanner(System.in);// PARA INGRESO DE DATOS POR TECLADO
	}

	public void validarVisita() {

	}

	public void actualizarEstadoVisita() {

	}

	public void mostrarMenu() {
		// BUCLE QUE CIERRA SOLO CON OPCIÓN 4 (SALIR)
		do {
			System.out.println("--------------------------------------");
			System.out.println("SISTEMA CARCELARIO (PPL)");
			System.out.println("1. Consultar PPL");
			System.out.println("2. Registrar visita");
			System.out.println("3. Reservar Visita");
			System.out.println("4. Salir");
			System.out.print("Ingresa una opción (1-4): ");
			opMenu = cin.nextInt();

			switch (opMenu) {
			case 1: {
				ppl.consultarDatosPPL();
				break;
			}
			case 2: {
				visitante.ingresarDatosVisitante();
				break;
			}
			case 3: {
				break;
			}
			case 4: {
				System.out.println("Vuelve pronto, saliste del sistema");
				break;
			}
			default: {
				System.out.println("Opción inválida (1-4)");
				break;
			}
			}
		} while (opMenu != 4);
		cin.close();// CERRAR SCANNER AL FINALIZAR PARA QUE NO CONSUMA RECURSOS
	}

}
