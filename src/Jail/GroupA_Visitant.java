package Jail;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//HEREDA CLASE PERSONA
public class GroupA_Visitant extends GroupA_Person {
	// DECLARACIÓN ATRIBUTO
	private String visitantId, relationPrisoner, reasonVisit, VISITANTS_FILE_NAME;
	private JSONObject visitantJSONObject;
	private JSONArray visitantJSONArray;
	private JSONArray prisonerJSONArray;
	private JSONParser parser;
	public boolean visitantExist, isDniPrisoner;
	private Object objectParser;
	private GroupA_Prisoner prisoner;
	private Scanner cin;

	public GroupA_Visitant(String visitantId, String dni, String name, String lastName, char gender, String nationality,
			int age, int yearBorn, String relationPrisoner, String reasonVisit, GroupA_Prisoner prisoner) {
		// ASIGNACIÓN VALORES DE ATRIBUTOS HEREDADOS
		super(dni, name, lastName, gender, nationality, age, yearBorn);
		// ASIGNACIÓN VALORES ATRIBUTOS PROPIOS
		this.visitantId = visitantId;
		this.relationPrisoner = relationPrisoner;
		this.reasonVisit = reasonVisit;
		this.prisoner = prisoner;
		// LECTURA & ESCRITURA .JSON
		this.visitantJSONObject = new JSONObject();
		this.visitantJSONArray = new JSONArray();
		this.parser = new JSONParser();
		this.objectParser = null;
		this.visitantExist = false;
		this.isDniPrisoner = false;
		// NOMENCLATURA ARCHIVOS
		this.VISITANTS_FILE_NAME = "visitants.json";
		this.prisonerJSONArray = new JSONArray();
		cin = new Scanner(System.in);
	}

	public void queryVisitant(String dni) {
		// REINICIO ATRIBUTO QUE VERIFICA SI EXISTE VISITANTE
		visitantExist = false;
		try (FileReader reader = new FileReader(VISITANTS_FILE_NAME)) {
			// PARSEAR .JSON A OBJETO JAVA
			objectParser = parser.parse(reader);
			visitantJSONArray = (JSONArray) objectParser;
			// BUSCAR SI YA EXISTE UN VISITANTE CON LA MISMA CÉDULA
			for (Object object : visitantJSONArray) {
				visitantJSONObject = (JSONObject) object;
				if (visitantJSONObject.get("cedula").equals(dni)) {
					visitantExist = true;
					break;
				} else {
					visitantExist = false;
				}
			}
		} catch (Exception e) {
			visitantExist = false;
		}
	}

	public void getVisitantData() {
		isDniPrisoner = false;
		// PEDIDO DATOS GENERALES
		System.out.println("--------------------------------------");
		System.out.println("MENÚ > REGISRO VISITANTE");
		do {
			do {
				System.out.print("Ingrese su cedula (10 digitos): ");
				dni = cin.nextLine();
				// CONTROL DIGITOS CEDULA
			} while (dni.length() != 10);
			// CONTROL EXISTENCIA USUARIO VISITANTE
			queryVisitant(dni);
			if (visitantExist) {
				System.out.println("--------------------------------------");
				System.out.println("El visitante con cédula " + dni + " ya existe en el sistema");
			}
			// VERIFICA QUE CÉDULA NO ESTÉ REGISTRADA COMO PPL
			try (FileReader reader = new FileReader("prisoners.json")) {
				// OBTIENE DATOS DEL JSON
				Object objectParser = parser.parse(reader);
				// PARSEA JSON A OBJETO
				if (objectParser instanceof JSONObject) {
					prisonerJSONArray.add((JSONObject) objectParser);
				} else if (objectParser instanceof JSONArray) {
					prisonerJSONArray = (JSONArray) objectParser;
				}
				// LEE JSON
				for (Object obj : prisonerJSONArray) {
					JSONObject jsonObj = (JSONObject) obj;
					prisoner.dni = (String) jsonObj.get("dni");
					if (dni.equals(prisoner.dni)) {
						System.out.println("La cédula ingresada está registrada como PPL");
						isDniPrisoner = true;
						break;
					}
				}
			} catch (IOException | ParseException e) {
				System.out.println("No se ha podido acceder a los datos de PPLs");
			}
			// REPITE PROCESO SI EXISTE VISITANTE
		} while (visitantExist || isDniPrisoner);
		// INGRESO DE DATOS GENERALES
		this.getDataPerson();
		// PEDIDO DATOS VISITANTE
		System.out.print("Relacion con PPL: ");
		relationPrisoner = cin.nextLine();
		System.out.print("Motivo de la Visita: ");
		reasonVisit = cin.nextLine();

		// GENERACIÓN ID ÚNICO (NOMBRE & 4 ÚLTIMOS DIGITOS CEDULA)
		visitantId = name.split("")[0] + dni.substring(dni.length() - 4);
		// GUARDADO DE DATOS EN .JSON
		this.saveData();
	}

	private void saveData() {
		// INICIALIZACIÓN OBJETO
		visitantJSONObject = new JSONObject();
		// GUARDADO FORMATO JSON
		visitantJSONObject.put("visitante ID", visitantId);
		visitantJSONObject.put("cedula", dni);
		visitantJSONObject.put("nombre", name);
		visitantJSONObject.put("apellido", lastName);
		visitantJSONObject.put("genero", Character.toString(gender));
		visitantJSONObject.put("nacionalidad", nationality);
		visitantJSONObject.put("edad", age);
		visitantJSONObject.put("año nacimiento", yearBorn);
		visitantJSONObject.put("relacion", relationPrisoner);
		visitantJSONObject.put("motivo visita", reasonVisit);

		visitantJSONArray.add(visitantJSONObject);

		// VERIFICAR SI YA EXISTEN DATOS EN EL .JSON
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(VISITANTS_FILE_NAME)) {
			// PARSEAR .JSON A OBJETO JAVA
			Object obj = jsonParser.parse(reader);
			visitantJSONArray = (JSONArray) obj;
			// AGREGAR DATOS ANTERIORES
			visitantJSONArray.add(visitantJSONObject);
		} catch (IOException | ParseException e) {
			// SI NO EXISTE EL ARCHIVO INICIALIZAR JSON ARRAY NUEVAMENTE
			visitantJSONArray = new JSONArray();
		}

		// GUARDA DATOS DEL .JSON
		try (FileWriter file = new FileWriter(VISITANTS_FILE_NAME)) {
			// ESCRITURA JSON
			file.write(visitantJSONArray.toJSONString());
			// LIMPIAR BUFFER ARCHIVO
			file.flush();
			showData();
		} catch (Exception e) { //
			// IMPRIME ERRORES SI NO GUARDA EL ARCHIVO
			System.out.println("El archivo no existe, se creara uno nuevo");
		}
	}

	// IMPLEMENTACIÓN MÉTODO POLIMORFISMO DE CLASE PADRE PERSONA
	@Override
	public void showData() {
		System.out.println("--------------------------------------");
		System.out.println("REGISTRO ÉXITOSO - DATOS");
		System.out.println("Cédula: " + dni);
		System.out.println("Nombre: " + name);
		System.out.println("Apellido: " + lastName);
		System.out.println("Género: " + gender);
		System.out.println("Nacionalidad: " + nationality);
		System.out.println("Edad: " + age + " años");
		System.out.println("Año nacimiento: " + yearBorn);
		System.out.println("Relación: " + relationPrisoner);
		System.out.println("Motivo preso: " + reasonVisit);
	}
}
