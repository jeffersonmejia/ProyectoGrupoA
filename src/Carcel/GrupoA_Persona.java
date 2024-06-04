package Carcel;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;//INGRESO DE DATOS POR TECLADO

public abstract class GrupoA_Persona {
	// DECLARACIÓN DE ATRIBUTOS
	protected String cedula, nombre, apellido, nacionalidad;
	protected char genero;
	protected int edad, anioNacimiento;
	protected Scanner cin;// PROTECTED PARA REUTILIZAR SCANNER EN CLASES HIJAS
	LocalDate fechaActual;

	public GrupoA_Persona(String cedula, String nombre, String apellido, char genero, String nacionalidad, int edad,
			int anioNacimiento) {
		// INICIALIZACIÓN DE ATRIBUTOS
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;
		this.nacionalidad = nacionalidad;
		this.edad = edad;
		this.anioNacimiento = anioNacimiento;
		// INICIALIZACIÓN OBJETO SCANNER PARA PEDIR DATOS POR TECLADO
		this.cin = new Scanner(System.in);
		// INICIALIAZCIÓN OBJETO LOCALDATE PARA OBTENER FECHA/HORA ACTUAL
		fechaActual = LocalDate.now();
	}

	// INGRESAR DATOS GENERALES POR TELCADO
	public void ingresarDatosPersona() {
		// VERIFICA QUE NO SE HAYA INGRESADO DATOS EN LA CEDULA, SI NO SE PIEDE NUEVO
		if (cedula.length() == 0) {
			// CONTROL DIGITOS CEDULA
			do {
				System.out.print("Ingrese su cedula (10 digitos): ");
				cedula = cin.nextLine();
			} while (cedula.length() != 10);
		}
		// DATOS GENERALES
		System.out.print("Ingrese su nombre: ");
		nombre = cin.nextLine();
		System.out.print("Ingrese su apellido: ");
		apellido = cin.nextLine();
		do {
			System.out.print("Ingrese su genero (M/F): ");
			genero = cin.next().toUpperCase().charAt(0);// OBTIENE CARACTER INGRESADO EN POSICIÓN
		} while ((genero != 'M' && genero != 'F') || Character.toString(genero).length() != 1);
		cin = new Scanner(System.in);// LIMPIAR BUFFER
		System.out.print("Ingrese su nacionalidad: ");
		nacionalidad = cin.nextLine();
		// CONTROL AÑO DE NACIMIENTO
		do {
			try {
				System.out.print("Ingrese su año de nacimiento (1950-2006): ");
				anioNacimiento = cin.nextInt();
			} catch (InputMismatchException e) {
				cin = new Scanner(System.in);
			}
		} while (anioNacimiento < 1950 || anioNacimiento > 2006);
		// CALCULO AUTOMATICO DE EDAD
		edad = fechaActual.getYear() - anioNacimiento;
		// LIMPIAR BUFFER
		this.cin = new Scanner(System.in);
	}

	// DECLARACIÓN MÉTODO POLIMORFISMO
	public abstract void mostrarDatos();
}
