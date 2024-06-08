package Jail;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;//MANEJO ERRORES JSON

public class GrupoA_Prisoner extends GrupoA_Person {
	private JSONArray pplJSONArray;
	private JSONObject pplJSONObject;
	private JSONParser parser;
	private Object objectParser;
	private boolean jsonExist;
	private Scanner cin;

	public GrupoA_Prisoner(String dni, String name, String lastName, char gender, String nationality,
			int age, int yearBorn) {
		// ASIGNACIÓN DE VALORES ATRIBUTOS HEREDADOS
		super(dni, name, lastName, gender, nationality, age, yearBorn);
		// INICIALIZACIÓN OBJETO SCANNER PARA PEDIR DATOS POR TECLADO
		cin = new Scanner(System.in);
		// INICIALIZACIÓN ATRIBUTOS PARA MANIPULACIÓN DE JSON
		pplJSONArray = new JSONArray();
		pplJSONObject = new JSONObject();
		parser = new JSONParser();// PARSE OBJETO JAVA -> JSON, O VICEVERSA
		jsonExist = false;
	}

	// CONSULTA DATOS PPL POR CÉDULA
	public boolean consultarDatosPPL() {
		pplJSONArray = new JSONArray();
		jsonExist = false;
		System.out.println("--------------------------------------");
		System.out.println("DATOS PPL");
		do {
			System.out.print("Ingrese la cedula del PPL (10 dígitos): ");
			dni = cin.nextLine();
		} while (dni.length() != 10);
		// LÓGICA LECTURA PPL.JSON
		try (FileReader reader = new FileReader("prisoners.json")) {
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
			showData();
			if (!jsonExist) {
				System.out.println("El PPL con cédula " + dni + " no está en el sistema");
			}
			return jsonExist;
			// MANEJO DE ERRORES EN CASO QUE NO EXISTAA JSON
		} catch (IOException | ParseException e) {
			System.out.println("El PPL con cédula " + dni + " no existe");
		}
		return false;
	}

	// IMPLEMENTACIÓN MÉTODO POLIMORFISMO DE CLASE PADRE PERSONA
	@Override
	public void showData() {
		for (Object pplJSONFile : pplJSONArray) {
			pplJSONObject = (JSONObject) pplJSONFile;
			// VERIFICA EXISTENCIA POR NOMBRE
			if (dni.equals(pplJSONObject.get("dni"))) {
				System.out.println("Fecha ingreso: " + pplJSONObject.get("dateEntry"));
				System.out.println("ID: " + pplJSONObject.get("prisonerId"));
				System.out.println("Cédula: " + pplJSONObject.get("dni"));
				System.out.println("Nombre: " + pplJSONObject.get("name"));
				System.out.println("Apellido: " + pplJSONObject.get("lastName"));
				System.out.println("Genero: " + pplJSONObject.get("gender"));
				System.out.println("Nacionalidad: " + pplJSONObject.get("nationality"));
				System.out.println("Edad: " + pplJSONObject.get("age") + " años");
				System.out.println("Año nacimiento: " + pplJSONObject.get("yearBorn"));
				System.out.println("Duracion Condena: " + pplJSONObject.get("durationJail") + " años");
				System.out.println("Tiempo cumplido condena: " + pplJSONObject.get("timeRemaining") + " años");
				System.out.println("Delito: " + pplJSONObject.get("penality"));
				jsonExist = true;
			}
		}

	}

}
