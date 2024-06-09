package Jail;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;//MANEJO ERRORES JSON

public class GroupA_Prisoner extends GroupA_Person {
	private int durationJail, timeRemaining;
	private String prisonerId, penality, dateEntry, PPLdni;
	private JSONArray prisonerJSONArray;// AGREGA OBJETOS JSON A UN ARRAY
	private JSONObject prisonerJSONObject;// AGREGA CADA DATO CLAVE/VALOR AL OBJETO
	private JSONParser jsonParser;// CONVIERTE JSON A OBJETO
	private Object objectParser;// OBTIENE VALOR DEL JSON PARSER HECHO OBJETO
	private boolean jsonExist;// VERIFICA EXISTENCIA DEL .JSON
	private Scanner cin;// OBTENER DATOS POR TECLADO
	private GroupA_Prisoner prisoner;
	private ArrayList<GroupA_Prisoner> prisoners;// RECURSIVIDAD PARA GUARDAR DATOS DEL PRISIONERO EN ARRAY LIST

	public GroupA_Prisoner(String dni, String name, String lastName, char gender, String nationality, int age,
			int yearBorn, String prisonerId, String penality, String dateEntry, int durationJail, int timeRemaining) {
		// ASIGNACIÓN DE VALORES ATRIBUTOS HEREDADOS
		super(dni, name, lastName, gender, nationality, age, yearBorn);
		// ASIGNACIÓN ATRIBUTOS PROPIOS DE LA CLASE
		this.prisonerId = prisonerId;
		this.penality = penality;
		this.dateEntry = dateEntry;
		this.durationJail = durationJail;
		this.timeRemaining = timeRemaining;
		// INICIALIZACIÓN OBJETO SCANNER PARA PEDIR DATOS POR TECLADO
		cin = new Scanner(System.in);
		// INICIALIZACIÓN ATRIBUTOS PARA MANIPULACIÓN DE JSON
		prisonerJSONArray = new JSONArray();
		prisonerJSONObject = new JSONObject();
		jsonParser = new JSONParser();// PARSE OBJETO JAVA -> JSON, O VICEVERSA
		jsonExist = false;
	}

	// CONSULTA DATOS PPL POR CÉDULA
	public boolean consultarDatosPPL() {
		prisonerJSONArray = new JSONArray();
		jsonExist = false;
		System.out.println("--------------------------------------");
		System.out.println("DATOS PPL");
		do {
			System.out.print("Ingrese la cedula del PPL (10 dígitos): ");
			PPLdni = cin.nextLine();
			this.setDni(PPLdni);
		} while (this.getDni().length() != 10);
		// LÓGICA LECTURA PPL.JSON
		try (FileReader reader = new FileReader("prisoners.json")) {
			jsonParser = new JSONParser();
			objectParser = jsonParser.parse(reader);
			// CONVERSION JSON A OBJETO/ARRAY PARA JAVA
			if (objectParser instanceof JSONObject) {
				prisonerJSONArray.add((JSONObject) objectParser);
			} else if (objectParser instanceof JSONArray) {
				// CONVERSIÓN JSON A ARRAY
				prisonerJSONArray = (JSONArray) objectParser;
			}
			System.out.println("--------------------------------------");
			System.out.println("DATOS PPL");
			// CONVERSIÓN JSON A ARRAY
			showData();
			if (!jsonExist) {
				System.out.println("El PPL con cédula " + this.getDni() + " no está en el sistema");
			}
			return jsonExist;
			// MANEJO DE ERRORES EN CASO QUE NO EXISTAA JSON
		} catch (IOException | ParseException e) {
			System.out.println("El PPL con cédula " + this.getDni() + " no existe");
		}
		return false;
	}

	// IMPLEMENTACIÓN MÉTODO POLIMORFISMO DE CLASE PADRE PERSONA
	@Override
	public void showData() {
		prisoner = new GroupA_Prisoner("", "", "", '0', "", 0, 0, "", "", "", 0, 0);
		prisoners = new ArrayList<>();
		for (Object pplJSONFile : prisonerJSONArray) {
			prisonerJSONObject = (JSONObject) pplJSONFile;
			// VERIFICA EXISTENCIA POR CÉDULA
			if (this.getDni().equals(prisonerJSONObject.get("dni"))) {
				// CONVIERTE DESDE JSON A STRING/ENTERO
				prisoner.prisonerId = prisonerJSONObject.get("prisonerId").toString();
				prisoner.setDni(prisonerJSONObject.get("dni").toString());
				prisoner.name = prisonerJSONObject.get("name").toString();
				prisoner.lastName = prisonerJSONObject.get("lastName").toString();
				prisoner.gender = prisonerJSONObject.get("gender").toString().toCharArray()[0];
				prisoner.nationality = prisonerJSONObject.get("nationality").toString();
				prisoner.age = Integer.parseInt(prisonerJSONObject.get("age").toString());
				prisoner.penality = prisonerJSONObject.get("penality").toString();
				prisoner.yearBorn = Integer.parseInt(prisonerJSONObject.get("yearBorn").toString());
				prisoner.dateEntry = prisonerJSONObject.get("dateEntry").toString();
				prisoner.durationJail = Integer.parseInt(prisonerJSONObject.get("durationJail").toString());
				prisoner.timeRemaining = Integer.parseInt(prisonerJSONObject.get("timeRemaining").toString());
				// AGREGA AL ARRAY LIST
				prisoners.add(prisoner);
				jsonExist = true;
			}
		}
		// LEE ARRAY LIST
		for (int i = 0; i < prisoners.size(); i++) {
			System.out.println("ID: " + prisoners.get(i).prisonerId);
			System.out.println("Cédula: " + prisoners.get(i).getDni());
			System.out.println("Nombre: " + prisoners.get(i).name);
			System.out.println("Apellido: " + prisoners.get(i).lastName);
			System.out.println("Género: " + prisoners.get(i).gender);
			System.out.println("Nacionalidad: " + prisoners.get(i).nationality);
			System.out.println("Edad: " + prisoners.get(i).age);
			System.out.println("Año nacimiento: " + prisoners.get(i).yearBorn);
			System.out.println("Delito: " + prisoners.get(i).penality);
			System.out.println("Fecha ingreso: " + prisoners.get(i).dateEntry);
			System.out.println("Años condena: " + prisoners.get(i).durationJail);
			System.out.println("Años restantes: " + prisoners.get(i).timeRemaining);
		}
	}
}
