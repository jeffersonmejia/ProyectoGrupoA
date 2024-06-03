package Carcel;

import java.time.LocalDate;
import java.util.Scanner;//INGRESO DE DATOS POR TECLADO

public class GrupoA_Persona {
	// DECLARACIÓN DE ATRIBUTOS
	protected String cedula, nombre, apellido, genero, nacionalidad;
	protected int edad, anioNacimiento;
	protected Scanner cin;// PROTECTED PARA REUTILIZAR SCANNER EN CLASES HIJAS
	LocalDate fechaActual;

	public GrupoA_Persona(String cedula, String nombre, String apellido, String genero, String nacionalidad, int edad,
			int anioNacimiento) {
		// INICIALIZACIÓN DE ATRIBUTOS
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;
		this.nacionalidad = nacionalidad;
		this.edad = edad;
		this.anioNacimiento = anioNacimiento;
		this.cin = new Scanner(System.in);
		fechaActual = LocalDate.now();
	}

	// INGRESAR DATOS GENERALES POR TELCADO
	public void ingresarDatosPersona() {
		// CONTROL DIGITOS CEDULA
		do {
			System.out.print("Ingrese su cedula (10 digitos): ");
			cedula = cin.nextLine();
		} while (cedula.length() != 10);

		System.out.print("Ingrese su nombre: ");
		nombre = cin.nextLine();
		System.out.print("Ingrese su apellido: ");
		apellido = cin.nextLine();

		System.out.print("Ingrese su genero: ");
		genero = cin.nextLine();
		System.out.print("Ingrese su nacionalidad: ");
		nacionalidad = cin.nextLine();
		// CONTROL AÑO DE NACIMIENTO
		do {
			System.out.print("Ingrese su año de nacimiento (1950-2006): ");
			anioNacimiento = cin.nextInt();
		} while (anioNacimiento < 1950 || anioNacimiento > 2006);
		// CALCULO AUTOMATICO DE EDAD
		edad = fechaActual.getYear() - anioNacimiento;

		// LIMPIAR BUFFER
		this.cin = new Scanner(System.in);

	}

}
