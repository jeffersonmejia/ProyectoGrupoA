package Carcel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//HEREDA CLASE PERSONA
public class GrupoA_Visitante extends GrupoA_Persona {
	// DECLARACIÓN ATRIBUTO
	private String visitanteId, relacionPreso, motivoVisita, duracionVisita, fechaVisita, VISITANTES_FILE_NAME,
			HORARIOS_FILE_NAME, lineReader;;
	private JSONObject visitanteJSONObject;
	private JSONArray visitanteJSONArray;
	private JSONParser parser;
	private boolean existeVisitante;
	private Object objectParser;
	private GrupoA_PPL ppl;
	private int horarioId;

	public GrupoA_Visitante(String visitanteId, String cedula, String nombre, String apellido, char genero,
			String nacionalidad, int edad, int anioNacimiento, String relacionPreso, String motivoVisita,
			String duracionVisita, String fechaVisita, GrupoA_PPL ppl) {
		// ASIGNACIÓN VALORES DE ATRIBUTOS HEREDADOS
		super(cedula, nombre, apellido, genero, nacionalidad, edad, anioNacimiento);
		this.visitanteId = visitanteId;
		this.relacionPreso = relacionPreso;
		this.motivoVisita = motivoVisita;
		this.duracionVisita = duracionVisita;
		this.fechaVisita = fechaVisita;
		this.ppl = ppl;
		this.visitanteJSONObject = new JSONObject();
		this.visitanteJSONArray = new JSONArray();
		this.parser = new JSONParser();
		this.existeVisitante = false;
		this.VISITANTES_FILE_NAME = "visitantes.json";
		this.HORARIOS_FILE_NAME = "horarios.csv";
		this.objectParser = null;
		this.lineReader = "";
		horarioId = 0;

	}

	private void consultarVisitante(String cedula) {
		// REINICIO ATRIBUTO QUE VERIFICA SI EXISTE VISITANTE
		existeVisitante = false;
		try (FileReader reader = new FileReader(VISITANTES_FILE_NAME)) {
			// PARSEAR .JSON A OBJETO JAVA
			objectParser = parser.parse(reader);
			visitanteJSONArray = (JSONArray) objectParser;
			// BUSCAR SI YA EXISTE UN VISITANTE CON LA MISMA CÉDULA
			for (Object object : visitanteJSONArray) {
				visitanteJSONObject = (JSONObject) object;
				if (visitanteJSONObject.get("cedula").equals(cedula)) {
					existeVisitante = true;
					break;
				} else {
					existeVisitante = false;
				}
			}
		} catch (Exception e) {
			existeVisitante = false;
		}
	}

	public void ingresarDatosVisitante() {
		// PEDIDO DATOS GENERALES
		System.out.println("--------------------------------------");
		System.out.println("MENÚ > REGISRO VISITANTE");
		do {
			do {
				System.out.print("Ingrese su cedula (10 digitos): ");
				cedula = cin.nextLine();
				// CONTROL DIGITOS CEDULA
			} while (cedula.length() != 10);
			// CONTROL EXISTENCIA USUARIO VISITANTE
			consultarVisitante(cedula);
			if (existeVisitante) {
				System.out.println("--------------------------------------");
				System.out.println("El visitante con cédula " + cedula + " ya existe en el sistema");
			}
		} while (existeVisitante);
		// INGRESO DE DATOS GENERALES
		this.ingresarDatosPersona();
		// PEDIDO DATOS VISITANTE
		System.out.print("Relacion con PPL: ");
		relacionPreso = cin.nextLine();
		System.out.print("Motivo de la Visita: ");
		motivoVisita = cin.nextLine();
		System.out.print("Duracion de su visita: ");
		duracionVisita = cin.nextLine();
		fechaVisita = LocalDate.now().toString();
		// GENERACIÓN ID ÚNICO (NOMBRE & 4 ÚLTIMOS DIGITOS CEDULA)
		visitanteId = nombre.split("")[0] + cedula.substring(cedula.length() - 4);
		// GUARDADO DE DATOS EN .JSON
		this.guardarDatosVisitante();
	}

	private void guardarDatosVisitante() {
		// INICIALIZACIÓN OBJETO
		visitanteJSONObject = new JSONObject();
		// GUARDADO FORMATO JSON
		visitanteJSONObject.put("visitante ID", visitanteId);
		visitanteJSONObject.put("cedula", cedula);
		visitanteJSONObject.put("nombre", nombre);
		visitanteJSONObject.put("apellido", apellido);
		visitanteJSONObject.put("genero", Character.toString(genero));
		visitanteJSONObject.put("nacionalidad", nacionalidad);
		visitanteJSONObject.put("edad", edad);
		visitanteJSONObject.put("año nacimiento", anioNacimiento);
		visitanteJSONObject.put("relacion", relacionPreso);
		visitanteJSONObject.put("motivo visita", motivoVisita);
		visitanteJSONObject.put("Duracion Visita", duracionVisita);
		visitanteJSONObject.put("fecha visita", fechaVisita);
		visitanteJSONArray.add(visitanteJSONObject);

		// VERIFICAR SI YA EXISTEN DATOS EN EL .JSON
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(VISITANTES_FILE_NAME)) {
			// PARSEAR .JSON A OBJETO JAVA
			Object obj = jsonParser.parse(reader);
			visitanteJSONArray = (JSONArray) obj;
			// AGREGAR DATOS ANTERIORES
			visitanteJSONArray.add(visitanteJSONObject);
		} catch (IOException | ParseException e) {
			// SI NO EXISTE EL ARCHIVO INICIALIZAR JSON ARRAY NUEVAMENTE
			visitanteJSONArray = new JSONArray();
		}

		// GUARDA DATOS DEL .JSON
		try (FileWriter file = new FileWriter(VISITANTES_FILE_NAME)) {
			// ESCRITURA JSON
			file.write(visitanteJSONArray.toJSONString());
			// LIMPIAR BUFFER ARCHIVO
			file.flush();
			mostrarDatos();
		} catch (Exception e) { //
			// IMPRIME ERRORES SI NO GUARDA EL ARCHIVO
			System.out.println("El archivo no existe, se creara uno nuevo");
		}
	}

	public void reservarVisita() {
		System.out.println("--------------------------------------");
		System.out.println("DATOS VISITANTE");
		do {
			System.out.print("Ingrese su cedula (10 digitos): ");
			cedula = cin.nextLine();
			// CONTROLA DIGITOS CEDULA
		} while (cedula.length() != 10);
		// CONTROLA EXISTENCIA USUARIO VISITANTE
		consultarVisitante(cedula);
		if (!existeVisitante) {
			System.out.println("--------------------------------------");
			System.out.println("La cédula ingresada no existe en el sistema");
			// SI EXISTE, SELECCIONA HORARIO
		} else if (ppl.consultarDatosPPL()) {

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
			// crear .csv "visitas.csv"
		}
	}

	// IMPLEMENTACIÓN MÉTODO POLIMORFISMO DE CLASE PADRE PERSONA
	@Override
	public void mostrarDatos() {
		System.out.println("--------------------------------------");
		System.out.println("REGISTRO ÉXITOSO - DATOS");
		System.out.println("Cédula: " + cedula);
		System.out.println("Nombre: " + nombre);
		System.out.println("Apellido: " + apellido);
		System.out.println("Género: " + genero);
		System.out.println("Nacionalidad: " + nacionalidad);
		System.out.println("Edad: " + edad + " años");
		System.out.println("Año nacimiento: " + anioNacimiento);
		System.out.println("Relación: " + relacionPreso);
		System.out.println("Motivo preso: " + motivoVisita);
		System.out.println("Duración visita: " + duracionVisita);
		System.out.println("Fecha visita: " + fechaVisita);
	}
}
