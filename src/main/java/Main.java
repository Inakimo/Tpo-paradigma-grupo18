import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Vehiculo> vehiculos = new ArrayList<>();
    private static List<Factura> facturas = new ArrayList<>();
    private static List<Servicio> servicios = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n==== MENÚ PRINCIPAL ====");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Vehículos");
            System.out.println("3. Gestionar Servicios");
            System.out.println("4. Ver Reportes");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    gestionarClientes(scanner);
                    break;
                case 2:
                    gestionarVehiculos(scanner);
                    break;
                case 3:
                    gestionarServicios(scanner);
                    break;
                case 4:
                    verReportes();
                    break;
                case 5:
                    System.out.println("Saliendo del sistema...");
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

        scanner.close();
    }

    private static void gestionarClientes(Scanner scanner) {
        System.out.println("\n=== Gestión de Clientes ===");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Ver Clientes");
        System.out.println("3. Regresar al Menú Principal");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                scanner.nextLine(); // Limpiar el buffer
                System.out.print("Ingrese nombre del cliente: ");
                String nombre = scanner.nextLine();
                System.out.print("Ingrese ID del cliente: ");
                int idCliente = scanner.nextInt();

                Cliente cliente = new Cliente(idCliente, nombre);
                clientes.add(cliente);
                System.out.println("Cliente agregado correctamente.");
                break;
            case 2:
                System.out.println("\nLista de Clientes:");
                for (Cliente c : clientes) {
                    System.out.println("ID: " + c.getId() + ", Nombre: " + c.getNombre());
                }
                break;
            case 3:
                System.out.println("Regresando al menú principal...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private static void gestionarVehiculos(Scanner scanner) {
        System.out.println("\n=== Gestión de Vehículos ===");
        System.out.println("1. Registrar Vehículo");
        System.out.println("2. Ver Vehículos");
        System.out.println("3. Regresar al Menú Principal");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                scanner.nextLine();
                System.out.print("Ingrese modelo del vehículo: ");
                String modelo = scanner.nextLine();
                System.out.print("Ingrese marca del vehículo: ");
                String marca = scanner.nextLine();
                System.out.print("Ingrese año: ");
                int anio = scanner.nextInt();
                Vehiculo vehiculo = new Vehiculo(modelo, marca, anio);
                vehiculos.add(vehiculo);
                System.out.println("Vehículo registrado correctamente.");
                break;
            case 2:
                System.out.println("\nLista de Vehículos:");
                for (Vehiculo v : vehiculos) {
                    System.out.println("Modelo: " + v.getModelo() + ", Marca: " + v.getMarca() + ", Año: " + v.getAnio());
                }
                break;
            case 3:
                System.out.println("Regresando al menú principal...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private static void gestionarServicios(Scanner scanner) {
        System.out.println("\n=== Gestión de Servicios ===");
        System.out.println("1. Registrar Servicio");
        System.out.println("2. Ver Servicios");
        System.out.println("3. Regresar al Menú Principal");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                scanner.nextLine();
                System.out.print("Ingrese descripción del servicio: ");
                String descripcion = scanner.nextLine();
                System.out.print("Ingrese costo base del servicio: ");
                double costoBase = scanner.nextDouble();
                Servicio servicio = new Servicio(descripcion, costoBase);
                servicios.add(servicio);
                System.out.println("Servicio registrado correctamente.");
                break;
            case 2:
                System.out.println("\nLista de Servicios:");
                for (Servicio s : servicios) {
                    System.out.println("Descripción: " + s.getDescripcion() + ", Costo Base: " + s.getCostoBase());
                }
                break;
            case 3:
                System.out.println("Regresando al menú principal...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private static void verReportes() {
        System.out.println("\n=== Reportes ===");
        System.out.println("Total de Clientes: " + clientes.size());
        System.out.println("Total de Vehículos: " + vehiculos.size());
        System.out.println("Total de Servicios: " + servicios.size());
        System.out.println("Total de Facturas Generadas: " + facturas.size());
    }
}