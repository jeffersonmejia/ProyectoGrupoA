package Jail;

public class Main {
	public static void main(String[] args) {
		// HORARIO DISPONIBLE DE VISITA
		String defaultSchedule[] = {};
		// INSTANCIACIÓN DEL SISTEMA
		Group_SystemPrisoner systemPrisoner = new Group_SystemPrisoner("", 0, defaultSchedule, "");
		// LLAMADO AL MENÚ PRINCIPAL
		systemPrisoner.showMenu();
	}
}
