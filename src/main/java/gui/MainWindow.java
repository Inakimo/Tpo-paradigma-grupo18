package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import modelo.Cliente;
import modelo.Mecanico;
import modelo.OrdenDeTrabajo;
import modelo.Repuesto;
import modelo.Vehiculo;
import persistencia.PersistenciaManager;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private ClientesPanel clientesPanel;
    private VehiculosPanel vehiculosPanel;
    private MecanicosPanel mecanicosPanel;
    private RepuestosPanel repuestosPanel;
    private OrdenesPanel ordenesPanel;
    private ReportesPanel reportesPanel;
    
    private PersistenciaManager persistenciaManager;

    public MainWindow() {
        persistenciaManager = new PersistenciaManager();
        
        // Agregar shutdown hook para garantizar que se guarden los datos al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Ejecutando shutdown hook - guardando datos...");
            try {
                guardarTodosDatosDirecto();
            } catch (Exception e) {
                System.err.println("Error en shutdown hook: " + e.getMessage());
            }
        }));
        
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión de Taller Mecánico");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Crear menú
        crearMenu();

        // Crear el panel de pestañas
        tabbedPane = new JTabbedPane();

        // Crear los paneles
        clientesPanel = new ClientesPanel();
        vehiculosPanel = new VehiculosPanel();
        mecanicosPanel = new MecanicosPanel();
        repuestosPanel = new RepuestosPanel();
        ordenesPanel = new OrdenesPanel();
        reportesPanel = new ReportesPanel();

        // Conectar paneles (para que puedan compartir datos)
        vehiculosPanel.setClientesPanel(clientesPanel);
        ordenesPanel.setPaneles(vehiculosPanel, mecanicosPanel, repuestosPanel);
        reportesPanel.setPaneles(clientesPanel, vehiculosPanel, mecanicosPanel, repuestosPanel, ordenesPanel);

        // Agregar pestañas con iconos (opcional)
        tabbedPane.addTab("Clientes", clientesPanel);
        tabbedPane.addTab("Vehículos", vehiculosPanel);
        tabbedPane.addTab("Mecánicos", mecanicosPanel);
        tabbedPane.addTab("Repuestos", repuestosPanel);
        tabbedPane.addTab("Órdenes de Trabajo", ordenesPanel);
        tabbedPane.addTab("Reportes", reportesPanel);

        add(tabbedPane);

        // Configurar apariencia
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Agregar WindowListener para guardar datos al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }
    
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        
        // Opción Guardar
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke("control S"));
        itemGuardar.addActionListener(e -> {
            guardarTodosDatos();
            JOptionPane.showMessageDialog(this, 
                "Datos guardados correctamente", 
                "Guardar", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Opción Salir
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        itemSalir.addActionListener(e -> cerrarAplicacion());
        
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }
    
    private void cargarDatosIniciales() {
        try {
            // Cargar en orden: clientes, vehiculos, mecanicos, repuestos, ordenes
            List<Cliente> clientes = persistenciaManager.cargarClientes();
            clientesPanel.setClientes(clientes);
            
            List<Vehiculo> vehiculos = persistenciaManager.cargarVehiculos(clientes);
            vehiculosPanel.setVehiculos(vehiculos);
            
            List<Mecanico> mecanicos = persistenciaManager.cargarMecanicos();
            mecanicosPanel.setMecanicos(mecanicos);
            
            List<Repuesto> repuestos = persistenciaManager.cargarRepuestos();
            repuestosPanel.setRepuestos(repuestos);
            
            List<OrdenDeTrabajo> ordenes = persistenciaManager.cargarOrdenes(
                vehiculos, mecanicos, repuestos
            );
            ordenesPanel.setOrdenes(ordenes);
            
            // Cargar contadores
            java.util.Map<String, Integer> contadores = persistenciaManager.cargarContadores();
            clientesPanel.setSiguienteId(contadores.getOrDefault("clienteIdCounter", 1));
            vehiculosPanel.setSiguienteId(contadores.getOrDefault("vehiculoIdCounter", 1));
            ordenesPanel.setSiguienteId(contadores.getOrDefault("ordenIdCounter", 1));
            // Nota: MecanicosPanel y RepuestosPanel también necesitarán esto si se implementan contadores
            
            System.out.println("Datos cargados correctamente:");
            System.out.println("  - " + clientes.size() + " clientes");
            System.out.println("  - " + vehiculos.size() + " vehículos");
            System.out.println("  - " + mecanicos.size() + " mecánicos");
            System.out.println("  - " + repuestos.size() + " repuestos");
            System.out.println("  - " + ordenes.size() + " órdenes");
            
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
            // No mostramos diálogo de error si es la primera vez (archivos no existen)
        }
    }
    
    private void cerrarAplicacion() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea guardar los cambios antes de salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            guardarTodosDatos();
            System.out.println("Cerrando aplicación...");
            dispose();
            System.exit(0);
        } else if (opcion == JOptionPane.NO_OPTION) {
            System.out.println("Cerrando aplicación sin guardar...");
            dispose();
            System.exit(0);
        }
        // Si es CANCEL_OPTION, no hacemos nada (la ventana permanece abierta)
    }
    
    private void guardarTodosDatos() {
        try {
            guardarTodosDatosDirecto();
            System.out.println("Datos guardados correctamente");
        } catch (Exception e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al guardar datos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void guardarTodosDatosDirecto() {
        persistenciaManager.guardarClientes(clientesPanel.getClientes());
        persistenciaManager.guardarVehiculos(vehiculosPanel.getVehiculos());
        persistenciaManager.guardarMecanicos(mecanicosPanel.getMecanicos());
        persistenciaManager.guardarRepuestos(repuestosPanel.getRepuestos());
        persistenciaManager.guardarOrdenes(ordenesPanel.getOrdenes());
        
        persistenciaManager.guardarContadores(
            clientesPanel.getSiguienteId(),
            vehiculosPanel.getSiguienteId(),
            0, // mecanicoIdCounter - por implementar
            0, // repuestoIdCounter - por implementar
            ordenesPanel.getSiguienteId(),
            0  // servicioIdCounter - por implementar
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
