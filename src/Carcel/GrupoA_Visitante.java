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
	private String visitanteId, relacionPreso, motivoVisita, duracionVisita, fechaVisita, VISITANTES_FILE_NAME;
	private JSONObject visitanteJSONObject;
	private JSONArray visitanteJSONArray;
	private JSONParser parser;

	public GrupoA_Visitante(String visitanteId, String cedula, String nombre, String apellido, String genero,
			String nacionalidad, int edad, int anioNacimiento, String relacionPreso, String motivoVisita,
			String duracionVisita, String fechaVisita) {
		// ASIGNACIÓN VALORES DE ATRIBUTOS HEREDADOS
		super(cedula, nombre, apellido, genero, nacionalidad, edad, anioNacimiento);
		this.visitanteId = visitanteId;
		this.relacionPreso = relacionPreso;
		this.motivoVisita = motivoVisita;
		this.duracionVisita = duracionVisita;
		this.fechaVisita = fechaVisita;
		this.visitanteJSONObject = new JSONObject();
		this.visitanteJSONArray = new JSONArray();
		parser = new JSONParser();
		this.VISITANTES_FILE_NAME = "visitantes.json";
	}

	public void ingresarDatosVisitante() {
		// PEDIDO DATOS GENERALES

		this.ingresarDatosPersona();

		// PEDIDO DATOS VISITANTE
		System.out.print("Ingrese su Relacion con el PPL: ");
		relacionPreso = cin.nextLine();
		System.out.print("Ingrese su Motivo de la Visita: ");
		motivoVisita = cin.nextLine();
		System.out.print("Ingrese su Duracion de su visita: ");
		duracionVisita = cin.nextLine();
		System.out.print("Ingrese su Fecha de la visita: ");
		fechaVisita = cin.nextLine();
		// GENERACIÓN ID ÚNICO
		visitanteId = nombre.toUpperCase() + "-" + fechaVisita.split("/")[0];
		// GUARDADO DE DATOS EN .JSON
		guardarDatosVisitante();
	}

	public void guardarDatosVisitante() {
		// INICIALIZACIÓN OBJETO
		visitanteJSONObject = new JSONObject();
		// GUARDADO FORMATO JSON
		visitanteJSONObject.put("cedula", cedula);
		visitanteJSONObject.put("visitante ID", visitanteId);
		visitanteJSONObject.put("nombre", nombre);
		visitanteJSONObject.put("apellido", apellido);
		visitanteJSONObject.put("genero", genero);
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
			System.out.println("Datos guardados con éxito en " + VISITANTES_FILE_NAME);
			// readData();
		} catch (Exception e) { //
			// IMPRIME ERRORES SI NO GUARDA EL ARCHIVO
			System.out.println("El archivo no existe, se creara uno nuevo");
		}
	}

	public void mostrarDatosVisitante() {

	}
}
