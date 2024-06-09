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
public class GrupoA_Visitant extends GrupoA_Person {
	// DECLARACIÓN ATRIBUTO
	private String visitantId, relationPrisoner, reasonVisita, VISITANTS_FILE_NAME;
	private JSONObject visitanteJSONObject;
	private JSONArray visitanteJSONArray;
	private JSONArray pplJSONArray;
	private JSONParser parser;
	public boolean visitantExist, isDniPrisoner;
	private Object objectParser;
	private GrupoA_Prisoner prisoner;
	private Scanner cin;

	public GrupoA_Visitant(String visitantId, String dni, String name, String lastName, char gender,
			String nationality, int age, int yearBorn, String relationPrisoner, String reasonVisita,
			GrupoA_Prisoner prisoner) {
		// ASIGNACIÓN VALORES DE ATRIBUTOS HEREDADOS
		super(dni, name, lastName, gender, nationality, age, yearBorn);
		// ASIGNACIÓN VALORES ATRIBUTOS PROPIOS
		this.visitantId = visitantId;
		this.relationPrisoner = relationPrisoner;
		this.reasonVisita = reasonVisita;
		this.prisoner = prisoner;
		// LECTURA & ESCRITURA .JSON
		this.visitanteJSONObject = new JSONObject();
		this.visitanteJSONArray = new JSONArray();
		this.parser = new JSONParser();
		this.objectParser = null;
		this.visitantExist = false;
		this.isDniPrisoner = false;
		// NOMENCLATURA ARCHIVOS
		this.VISITANTS_FILE_NAME = "visitants.json";
		this.pplJSONArray = new JSONArray();
		cin = new Scanner(System.in);
	}

	public void consultarVisitante(String cedula) {
		// REINICIO ATRIBUTO QUE VERIFICA SI EXISTE VISITANTE
		visitantExist = false;
		try (FileReader reader = new FileReader(VISITANTS_FILE_NAME)) {
			// PARSEAR .JSON A OBJETO JAVA
			objectParser = parser.parse(reader);
			visitanteJSONArray = (JSONArray) objectParser;
			// BUSCAR SI YA EXISTE UN VISITANTE CON LA MISMA CÉDULA
			for (Object object : visitanteJSONArray) {
				visitanteJSONObject = (JSONObject) object;
				if (visitanteJSONObject.get("cedula").equals(cedula)) {
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

	public void ingresarDatosVisitante() {
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
			consultarVisitante(dni);
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
					pplJSONArray.add((JSONObject) objectParser);
				} else if (objectParser instanceof JSONArray) {
					pplJSONArray = (JSONArray) objectParser;
				}
				// LEE JSON
				for (Object obj : pplJSONArray) {
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
		reasonVisita = cin.nextLine();

		// GENERACIÓN ID ÚNICO (NOMBRE & 4 ÚLTIMOS DIGITOS CEDULA)
		visitantId = name.split("")[0] + dni.substring(dni.length() - 4);
		// GUARDADO DE DATOS EN .JSON
		this.guardarDatosVisitante();
	}

	private void guardarDatosVisitante() {
		// INICIALIZACIÓN OBJETO
		visitanteJSONObject = new JSONObject();
		// GUARDADO FORMATO JSON
		visitanteJSONObject.put("visitante ID", visitantId);
		visitanteJSONObject.put("cedula", dni);
		visitanteJSONObject.put("nombre", name);
		visitanteJSONObject.put("apellido", lastName);
		visitanteJSONObject.put("genero", Character.toString(gender));
		visitanteJSONObject.put("nacionalidad", nationality);
		visitanteJSONObject.put("edad", age);
		visitanteJSONObject.put("año nacimiento", yearBorn);
		visitanteJSONObject.put("relacion", relationPrisoner);
		visitanteJSONObject.put("motivo visita", reasonVisita);

		visitanteJSONArray.add(visitanteJSONObject);

		// VERIFICAR SI YA EXISTEN DATOS EN EL .JSON
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(VISITANTS_FILE_NAME)) {
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
		try (FileWriter file = new FileWriter(VISITANTS_FILE_NAME)) {
			// ESCRITURA JSON
			file.write(visitanteJSONArray.toJSONString());
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
		System.out.println("Motivo preso: " + reasonVisita);
	}
}
