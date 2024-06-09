package Jail;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;//INGRESO DE DATOS POR TECLADO

public abstract class GroupA_Person {
	// DECLARACIÓN DE ATRIBUTOS
	protected String dni, name, lastName, nationality;
	protected char gender;
	protected int age, yearBorn;
	private Scanner cin;// PROTECTED PARA REUTILIZAR SCANNER EN CLASES HIJAS
	private LocalDate currentYear;

	public GroupA_Person(String dni, String name, String lastName, char gender, String nationality, int age,
			int yearBorn) {
		// INICIALIZACIÓN DE ATRIBUTOS
		this.dni = dni;
		this.name = name;
		this.lastName = lastName;
		this.gender = gender;
		this.nationality = nationality;
		this.age = age;
		this.yearBorn = yearBorn;
		// INICIALIZACIÓN OBJETO SCANNER PARA PEDIR DATOS POR TECLADO
		this.cin = new Scanner(System.in);
		// INICIALIAZCIÓN OBJETO LOCALDATE PARA OBTENER FECHA/HORA ACTUAL
		currentYear = LocalDate.now();
	}

	// INGRESAR DATOS GENERALES POR TELCADO
	public void getDataPerson() {
		// VERIFICA QUE NO SE HAYA INGRESADO DATOS EN LA CEDULA, SI NO SE PIEDE NUEVO
		if (dni.length() == 0) {
			// CONTROL DIGITOS CEDULA
			do {
				System.out.print("Ingrese su cedula (10 digitos): ");
				dni = cin.nextLine();
			} while (dni.length() != 10);
		}
		// DATOS GENERALES
		System.out.print("Ingrese su nombre: ");
		name = cin.nextLine();
		System.out.print("Ingrese su apellido: ");
		lastName = cin.nextLine();
		do {
			System.out.print("Ingrese su genero (M/F): ");
			gender = cin.next().toUpperCase().charAt(0);// OBTIENE CARACTER INGRESADO EN POSICIÓN
		} while ((gender != 'M' && gender != 'F') || Character.toString(gender).length() != 1);
		cin = new Scanner(System.in);// LIMPIAR BUFFER
		System.out.print("Ingrese su nacionalidad: ");
		nationality = cin.nextLine();
		// CONTROL AÑO DE NACIMIENTO
		do {
			try {
				System.out.print("Ingrese su año de nacimiento (1950-2006): ");
				yearBorn = cin.nextInt();
			} catch (InputMismatchException e) {
				cin = new Scanner(System.in);
			}
		} while (yearBorn < 1950 || yearBorn > 2006);
		// CALCULO AUTOMATICO DE EDAD
		age = currentYear.getYear() - yearBorn;
		// LIMPIAR BUFFER
		this.cin = new Scanner(System.in);
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	// DECLARACIÓN MÉTODO POLIMORFISMO
	public abstract void showData();
}
