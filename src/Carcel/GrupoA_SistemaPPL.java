package Carcel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;//INGRESO DE DATOS POR TECLADO

public class GrupoA_SistemaPPL {
	// DECLARACIÓN ATRIBUTOS
	Scanner cin;
	private int opMenu, horarioId;
	private String horarios[];
	private String estadoVisita, tramiteId, lineReader, HORARIOS_FILE_NAME, VISITAS_FILE_NAME;
	private GrupoA_PPL ppl;
	private GrupoA_Visitante visitante;
	private boolean existePPL;
	private FileWriter writer;
	private File file;

	public GrupoA_SistemaPPL(String tramiteId, int opMenu, String[] horarios, String estadoVisita) {
		// INICIALIZACIÓN DE ATRIBUTOS
		this.tramiteId = tramiteId;
		this.opMenu = opMenu;
		this.horarios = horarios;
		this.estadoVisita = estadoVisita;
		this.ppl = new GrupoA_PPL("", "", "", "", '0', "", 0, 0, 0, 0, "", "");
		visitante = new GrupoA_Visitante("", "", "", "", '0', "", 0, 0, "", "", "", "", ppl);
		cin = new Scanner(System.in);// PARA INGRESO DE DATOS POR TECLADO
		lineReader = "";
		this.HORARIOS_FILE_NAME = "horarios.csv";
		this.VISITAS_FILE_NAME = "visitas.csv";
		this.horarioId = 0;
		writer = null;
		file = null;
		existePPL = false;
	}

	public void reservarVisita() {
		cin = new Scanner(System.in);
		existePPL = false;
		System.out.println("--------------------------------------");
		System.out.println("DATOS VISITANTE");
		do {
			System.out.print("Ingrese su cedula (10 digitos): ");
			visitante.cedula = cin.nextLine();
			// CONTROLA DIGITOS CEDULA
		} while (visitante.cedula.length() != 10);
		// CONTROLA EXISTENCIA USUARIO VISITANTE
		visitante.consultarVisitante(visitante.cedula);
		if (!visitante.existeVisitante) {
			System.out.println("--------------------------------------");
			System.out.println("La cédula ingresada no existe en el sistema");
			// SI EXISTE, SELECCIONA HORARIO
		}
		existePPL = ppl.consultarDatosPPL();
		if (existePPL) {
			// LEE HORARIOS.CSV
			try (BufferedReader reader = new BufferedReader(new FileReader(HORARIOS_FILE_NAME))) {
				System.out.println("--------------------------------------");
				System.out.println("HORARIOS DISPONIBLES:");
				// LEE LÍNEA A LÍNEA ARCHIVO
				while ((lineReader = reader.readLine()) != null) {
					// SEPARA ELEMENTOS ","
					String[] values = lineReader.split(",");
					// FORMATEA VALORES CSV
					System.out.println(String.join(" | ", values));
				}
				// SI NO EXISTEN HORARIO DISPONIBLES, REGRESA MENÚ PRINCIPAL
			} catch (IOException e) {
				System.err.println("Lo sentimos, no existen horarios disponibles");
			}
			// INGRESA HORARIO ID
			do {
				try {
					System.out.println("--------------------------------------");
					System.out.println("Ingresa el ID del horario (1-7): ");
					horarioId = cin.nextInt();
					// CONTROLA QUE EL USUARIO INGRESE VALOR NUMÉRICO
				} catch (InputMismatchException e) {
					cin = new Scanner(System.in);
				}
			} while (horarioId < 1 || horarioId > 7);
			// REGISTRA VISITA
			try {
				// CREA VISITAS.CSV
				writer = new FileWriter(VISITAS_FILE_NAME, true); // TRUE PARA QUE ACTUALICE EL ARCHIVO
				file = new File(VISITAS_FILE_NAME);
				// SI NO EXISTE, CREA VISITAS.CSV
				if (file.length() == 0) {
					writer.append("horarioId,cedula visitante, cedula ppl\n");
				}
				// AGREGA HORARIOID, CEDULA VISITANTE Y PPL
				writer.append(horarioId + "," + visitante.cedula + ", " + ppl.cedula);
				writer.write("\n");
				// CIERRA ESCRITOR
				writer.close();
				System.out.println("La visita se ha reservado correctamente");
			} catch (IOException e) {
				System.err.println("La visita no ha sido reservada");
			}
		}
	}

	public void mostrarMenu() {
		// BUCLE QUE CIERRA SOLO CON OPCIÓN 4 (SALIR)
		do {
			try {
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
					this.reservarVisita();
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
				// CONTROL SI USUARIO INGRESA CADENA EN LUGAR DE VALOR NUMERICO
			} catch (InputMismatchException e) {
				cin = new Scanner(System.in);
			}
		} while (opMenu != 4);
		// CERRAR SCANNER AL FINALIZAR PARA QUE NO CONSUMA RECURSOS
		cin.close();
	}

}
