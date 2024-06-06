package Carcel;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//HEREDA CLASE PERSONA
public class GrupoA_Visitante extends GrupoA_Persona {
	// DECLARACIÓN ATRIBUTO
	private String visitanteId, relacionPreso, motivoVisita, VISITANTES_FILE_NAME;
	private JSONObject visitanteJSONObject;
	private JSONArray visitanteJSONArray;
	private JSONArray pplJSONArray;
	private JSONParser parser;
	public boolean existeVisitante, esCedulaPPL;
	private Object objectParser;

	private GrupoA_PPL ppl;

	public GrupoA_Visitante(String visitanteId, String cedula, String nombre, String apellido, char genero,
			String nacionalidad, int edad, int anioNacimiento, String relacionPreso, String motivoVisita,
			String duracionVisita, String fechaVisita, GrupoA_PPL ppl) {
		// ASIGNACIÓN VALORES DE ATRIBUTOS HEREDADOS
		super(cedula, nombre, apellido, genero, nacionalidad, edad, anioNacimiento);
		// ASIGNACIÓN VALORES ATRIBUTOS PROPIOS
		this.visitanteId = visitanteId;
		this.relacionPreso = relacionPreso;
		this.motivoVisita = motivoVisita;

		this.ppl = ppl;
		// LECTURA & ESCRITURA .JSON
		this.visitanteJSONObject = new JSONObject();
		this.visitanteJSONArray = new JSONArray();
		this.parser = new JSONParser();
		this.objectParser = null;
		this.existeVisitante = false;
		this.esCedulaPPL = false;
		// NOMENCLATURA ARCHIVOS
		this.VISITANTES_FILE_NAME = "visitantes.json";

		this.pplJSONArray = new JSONArray();
	}

	public void consultarVisitante(String cedula) {
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
		esCedulaPPL = false;
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
			// VERIFICA QUE CÉDULA NO ESTÉ REGISTRADA COMO PPL
			try (FileReader reader = new FileReader("PPL.json")) {
				// OBTIENE DATOS DEL JSON
				Object objectParser = parser.parse(reader);
				// PARSEA JSON A OBJETO
				if (objectParser instanceof JSONObject) {
					pplJSONArray.add((JSONObject) objectParser);
				} else if (objectParser instanceof JSONArray) {
					pplJSONArray = (JSONArray) objectParser;
				}
				// LEE JSON
				for (Object obj : pplJSONArray) {
					JSONObject jsonObj = (JSONObject) obj;
					ppl.cedula = (String) jsonObj.get("cedula");
					if (cedula.equals(ppl.cedula)) {
						System.out.println("La cédula ingresada está registrada como PPL");
						esCedulaPPL = true;
						break;
					}
				}
			} catch (IOException | ParseException e) {
				System.out.println("No se ha podido acceder a los datos de PPLs");
			}
			// REPITE PROCESO SI EXISTE VISITANTE
		} while (existeVisitante || esCedulaPPL);
		// INGRESO DE DATOS GENERALES
		this.ingresarDatosPersona();
		// PEDIDO DATOS VISITANTE
		System.out.print("Relacion con PPL: ");
		relacionPreso = cin.nextLine();
		System.out.print("Motivo de la Visita: ");
		motivoVisita = cin.nextLine();

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
	}
}
