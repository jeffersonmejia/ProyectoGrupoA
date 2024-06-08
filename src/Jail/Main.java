package Jail;

public class Main {
	public static void main(String[] args) {
		// HORARIO DISPONIBLE DE VISITA
		String horariosVisita[] = {};
		// INSTANCIACIÓN DEL SISTEMA
		GrupoA_SystemPrisoner sistemaPPL = new GrupoA_SystemPrisoner("", 0, horariosVisita, "");
		// LLAMADO AL MENÚ PRINCIPAL
		sistemaPPL.mostrarMenu();
	}
}
