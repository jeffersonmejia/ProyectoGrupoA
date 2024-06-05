package Carcel;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;//MANEJO ERRORES JSON

public class GrupoA_PPL extends GrupoA_Persona {
	private int duracionCondena, tiempoCumplido;
	private String presoId;
	private String delito, fechaIngreso;
	private JSONArray pplJSONArray;
	private JSONObject pplJSONObject;
	private JSONParser parser;
	private Object objectParser;
	private boolean existeEnJson;
	Scanner cin;

	public GrupoA_PPL(String presoId, String cedula, String nombre, String apellido, char genero, String nacionalidad,
			int edad, int anioNacimiento, int duracionCondena, int tiempoCumplido, String delito, String fechaIngreso) {
		// ASIGNACIÓN DE VALORES ATRIBUTOS HEREDADOS
		super(cedula, nombre, apellido, genero, nacionalidad, edad, anioNacimiento);
		this.presoId = presoId;
		this.duracionCondena = duracionCondena;
		this.tiempoCumplido = tiempoCumplido;
		this.delito = delito;
		this.fechaIngreso = fechaIngreso;
		// INICIALIZACIÓN OBJETO SCANNER PARA PEDIR DATOS POR TECLADO
		cin = new Scanner(System.in);
		// INICIALIZACIÓN ATRIBUTOS PARA MANIPULACIÓN DE JSON
		pplJSONArray = new JSONArray();
		pplJSONObject = new JSONObject();
		parser = new JSONParser();// PARSE OBJETO JAVA -> JSON, O VICEVERSA
		existeEnJson = false;
	}

	// CONSULTA DATOS PPL POR CÉDULA
	public boolean consultarDatosPPL() {
		pplJSONArray = new JSONArray();
		existeEnJson = false;
		System.out.println("--------------------------------------");
		System.out.println("DATOS PPL");
		do {
			System.out.print("Ingrese la cedula del PPL (10 dígitos): ");
			cedula = cin.nextLine();
		} while (cedula.length() != 10);
		// LÓGICA LECTURA PPL.JSON
		try (FileReader reader = new FileReader("PPL.json")) {
			parser = new JSONParser();
			objectParser = parser.parse(reader);
			// CONVERSION JSON A OBJETO/ARRAY PARA JAVA
			if (objectParser instanceof JSONObject) {
				pplJSONArray.add((JSONObject) objectParser);
			} else if (objectParser instanceof JSONArray) {
				// CONVERSIÓN JSON A ARRAY
				pplJSONArray = (JSONArray) objectParser;
			}
			System.out.println("--------------------------------------");
			System.out.println("DATOS PPL");
			// CONVERSIÓN JSON A ARRAY
			mostrarDatos();
			if (!existeEnJson) {
				System.out.println("El PPL con cédula " + cedula + " no está en el sistema");
			}
			return existeEnJson;
			// MANEJO DE ERRORES EN CASO QUE NO EXISTAA JSON
		} catch (IOException | ParseException e) {
			System.out.println("El PPL con cédula " + cedula + " no existe");
		}
		return false;
	}

	// IMPLEMENTACIÓN MÉTODO POLIMORFISMO DE CLASE PADRE PERSONA
	@Override
	public void mostrarDatos() {
		for (Object pplJSONFile : pplJSONArray) {
			pplJSONObject = (JSONObject) pplJSONFile;
			// VERIFICA EXISTENCIA POR NOMBRE
			if (cedula.equals(pplJSONObject.get("cedula"))) {
				System.out.println("Fecha ingreso: " + pplJSONObject.get("fechaIngreso"));
				System.out.println("ID: " + pplJSONObject.get("presoId"));
				System.out.println("Cédula: " + pplJSONObject.get("cedula"));
				System.out.println("Nombre: " + pplJSONObject.get("nombre"));
				System.out.println("Apellido: " + pplJSONObject.get("apellido"));
				System.out.println("Genero: " + pplJSONObject.get("genero"));
				System.out.println("Nacionalidad: " + pplJSONObject.get("nacionalidad"));
				System.out.println("Edad: " + pplJSONObject.get("edad") + " años");
				System.out.println("Año nacimiento: " + pplJSONObject.get("anioNacimiento"));
				System.out.println("Duracion Condena: " + pplJSONObject.get("duracionCondena") + " años");
				System.out.println("Tiempo cumplido condena: " + pplJSONObject.get("tiempoCumplido") + " años");
				System.out.println("Delito: " + pplJSONObject.get("delito"));
				existeEnJson = true;
			}
		}

	}

}
