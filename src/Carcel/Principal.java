package Carcel;

public class Principal {
	public static void main(String[] args) {
		// HORARIO DISPONIBLE DE VISITA
		String horariosVisita[] = {};
		// INSTANCIACIÓN DEL SISTEMA
		GrupoA_SistemaPPL sistemaPPL = new GrupoA_SistemaPPL("", 0, horariosVisita, "");
		// LLAMADO AL MENÚ PRINCIPAL
		sistemaPPL.mostrarMenu();
	}
}
