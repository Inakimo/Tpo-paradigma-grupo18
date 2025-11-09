package gui;

import modelo.*;
import persistencia.PersistenciaManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

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


        crearMenu();


        tabbedPane = new JTabbedPane();


        clientesPanel = new ClientesPanel();
        vehiculosPanel = new VehiculosPanel();
        mecanicosPanel = new MecanicosPanel();
        repuestosPanel = new RepuestosPanel();
        ordenesPanel = new OrdenesPanel();
        reportesPanel = new ReportesPanel();

        vehiculosPanel.setClientesPanel(clientesPanel);
        ordenesPanel.setPaneles(vehiculosPanel, mecanicosPanel, repuestosPanel);
        reportesPanel.setPaneles(clientesPanel, vehiculosPanel, mecanicosPanel, repuestosPanel, ordenesPanel);

        tabbedPane.addTab("Clientes", clientesPanel);
        tabbedPane.addTab("Vehículos", vehiculosPanel);
        tabbedPane.addTab("Mecánicos", mecanicosPanel);
        tabbedPane.addTab("Repuestos", repuestosPanel);
        tabbedPane.addTab("Órdenes de Trabajo", ordenesPanel);
        tabbedPane.addTab("Reportes", reportesPanel);

        add(tabbedPane);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }
    
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        

        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke("control S"));
        itemGuardar.addActionListener(e -> {
            guardarTodosDatos();
            JOptionPane.showMessageDialog(this, 
                "Datos guardados correctamente", 
                "Guardar", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        

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
            

            java.util.Map<String, Integer> contadores = persistenciaManager.cargarContadores();
            clientesPanel.setSiguienteId(contadores.getOrDefault("clienteIdCounter", 1));
            vehiculosPanel.setSiguienteId(contadores.getOrDefault("vehiculoIdCounter", 1));
            ordenesPanel.setSiguienteId(contadores.getOrDefault("ordenIdCounter", 1));
            
            System.out.println("Datos cargados correctamente:");
            System.out.println("  - " + clientes.size() + " clientes");
            System.out.println("  - " + vehiculos.size() + " vehículos");
            System.out.println("  - " + mecanicos.size() + " mecánicos");
            System.out.println("  - " + repuestos.size() + " repuestos");
            System.out.println("  - " + ordenes.size() + " órdenes");
            
        } catch (Exception e) {
            System.err.println("Error al cargar datos: " + e.getMessage());
            e.printStackTrace();
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
            0,
            0,
            ordenesPanel.getSiguienteId(),
            0
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
