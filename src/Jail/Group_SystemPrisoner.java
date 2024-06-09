package Jail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;//INGRESO DE DATOS POR TECLADO

public class Group_SystemPrisoner {
	// DECLARACIÓN ATRIBUTOS
	private Scanner cin;
	private int opMenu, scheduleId;
	private String VISITS_FILE_NAME, lineReader;
	private GroupA_Prisoner prisoner;
	private GroupA_Visitant visitant;
	private boolean prisonerExists, scheduleTaken, hasVisitsPendient;
	private FileWriter writer;
	private File file;
	private ArrayList<String> scheduleVisit;

	public Group_SystemPrisoner(String tramiteId, int opMenu, String[] horarios, String estadoVisita) {
		// INICIALIZACIÓN DE ATRIBUTOS
		this.opMenu = opMenu;
		this.prisoner = new GroupA_Prisoner("", "", "", '0', "", 0, 0, "", "", "", 0, 0);
		this.visitant = new GroupA_Visitant("", "", "", "", '0', "", 0, 0, "", "", prisoner);
		this.cin = new Scanner(System.in);// PARA INGRESO DE DATOS POR TECLADO
		this.VISITS_FILE_NAME = "visits.csv";
		this.scheduleId = 0;
		this.writer = null;
		this.file = null;
		this.lineReader = "";
		this.prisonerExists = false;
		this.scheduleTaken = false;
		this.hasVisitsPendient = false;
		this.scheduleVisit = new ArrayList<>();
	}

	public void loadSchedule() {
		// GENERA ESTÁTICAMENTE EL HORARIO DE VISITA USANDO ARRAYLIST
		scheduleVisit.add("ID | fecha     | hora        | lugar");
		scheduleVisit.add("1  | 20/6/2024 | 09:00 - 9:30  | Zona A");
		scheduleVisit.add("2  | 21/6/2024 | 10:00 - 10:30 | Zona B");
		scheduleVisit.add("3  | 22/6/2024 | 11:00 - 11:30 | Zona A");
		scheduleVisit.add("4  | 23/6/2024 | 15:00 - 15:30 | Zona B");
		scheduleVisit.add("5  | 24/6/2024 | 07:00 - 07:30 | Zona D");
		scheduleVisit.add("6  | 25/6/2024 | 11:00 - 11:30 | Zona C");
		scheduleVisit.add("7  | 26/6/2024 | 13:00 - 13:30 | Zona C");
		// LEE HORARIOS EN EL ARRAY LIST
		System.out.println("--------------------------------------");
		System.out.println("HORARIOS DISPONIBLES");
		for (int i = 0; i < scheduleVisit.size(); i++) {
			System.out.println(scheduleVisit.get(i));
		}
		System.out.println("--------------------------------------");
	}

	public void reserveVisit() {
		cin = new Scanner(System.in);
		prisonerExists = false;// INICIALIZA VARIABLE QUE REVISA SI EXISTE PRISIONERO
		scheduleTaken = false;// BANDERA QUE REVISA HORARIO DISPONIBLE
		hasVisitsPendient = false;// VERIFICA QUE EL VISITANTE NO TENGA VISITAS PENDIENTES
		System.out.println("--------------------------------------");
		System.out.println("DATOS VISITANTE");
		do {
			System.out.print("Ingrese su cedula (10 digitos): ");
			visitant.dni = cin.nextLine();
			// CONTROLA DIGITOS CEDULA
		} while (visitant.dni.length() != 10);
		// CONTROLA EXISTENCIA USUARIO VISITANTE
		visitant.queryVisitant(visitant.dni);
		if (!visitant.visitantExist) {
			System.out.println("--------------------------------------");
			System.out.println("La cédula ingresada no existe en el sistema");
		}
		// VERIFICA EXISTENCIA DE VISITAS PENDIENTES DEL VISITANTE
		try (BufferedReader reader = new BufferedReader(new FileReader(VISITS_FILE_NAME))) {
			while ((lineReader = reader.readLine()) != null) {
				if (lineReader.contains(visitant.dni)) {
					hasVisitsPendient = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// MENSAJE VISITA PENDIENTE
		if (hasVisitsPendient) {
			System.out.println("Visita no reservada, tienes una visita pendiente");
		} else {
			// CONTINUA PROCESO DE LLENAR DATOS
			if (visitant.visitantExist) {
				prisonerExists = prisoner.queryDataPrisoner();
			}
			if (prisonerExists) {
				// LEE HORARIOS DISPONIBLES
				loadSchedule();
				do {
					// INGRESA HORARIO ID
					do {
						try {
							System.out.print("Ingresa el ID del horario (1-7): ");
							scheduleId = cin.nextInt();
							// CONTROLA QUE EL USUARIO INGRESE VALOR NUMÉRICO
						} catch (InputMismatchException e) {
							cin = new Scanner(System.in);
						}
					} while (scheduleId < 1 || scheduleId > 7);
					// VERIFICA QUE EL HORARIO ESTÉ DISPONIBLE
					try (BufferedReader reader = new BufferedReader(new FileReader(VISITS_FILE_NAME))) {
						while ((lineReader = reader.readLine()) != null) {
							System.out.println(lineReader);
							if (lineReader.contains(scheduleId + ",")) {
								System.out.println("Horario no disponible, escoge otro");
								scheduleTaken = true;
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} while (scheduleTaken);
				// REGISTRA VISITA
				try {
					// CREA VISITAS.CSV
					writer = new FileWriter(VISITS_FILE_NAME, true); // TRUE PARA QUE ACTUALICE EL ARCHIVO
					file = new File(VISITS_FILE_NAME);
					// SI NO EXISTE, CREA VISITAS.CSV
					if (file.length() == 0) {
						writer.append("horarioId,cedula visitante, cedula ppl\n");
					}
					// AGREGA HORARIOID, CEDULA VISITANTE Y PPL
					writer.append(scheduleId + "," + visitant.dni + ", " + prisoner.dni);
					writer.write("\n");
					// CIERRA ESCRITOR
					writer.close();
					System.out.println("La visita se ha reservado correctamente");
					try (BufferedReader reader = new BufferedReader(new FileReader(VISITS_FILE_NAME))) {
						System.out.println("VISITAS CREADAS");
						while ((lineReader = reader.readLine()) != null) {
							System.out.println(lineReader);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (IOException e) {
					System.err.println("La visita no ha sido reservada");
				}
			}
		}

	}

	public void showMenu() {
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
					prisoner.queryDataPrisoner();
					break;
				}
				case 2: {
					visitant.getVisitantData();
					break;
				}
				case 3: {
					this.reserveVisit();
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
