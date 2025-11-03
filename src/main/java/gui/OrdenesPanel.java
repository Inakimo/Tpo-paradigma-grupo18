package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import enums.EstadoOrden;
import modelo.Mecanico;
import modelo.OrdenDeTrabajo;
import modelo.Repuesto;
import modelo.Servicio;
import modelo.Vehiculo;

public class OrdenesPanel extends JPanel {
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private List<OrdenDeTrabajo> ordenes;
    private int siguienteId = 1;

    // Referencias a otros paneles
    private VehiculosPanel vehiculosPanel;
    private MecanicosPanel mecanicosPanel;
    private RepuestosPanel repuestosPanel;

    public OrdenesPanel() {
        ordenes = new ArrayList<>();
        initComponents();
    }

    public void setPaneles(VehiculosPanel vehiculosPanel, MecanicosPanel mecanicosPanel, RepuestosPanel repuestosPanel) {
        this.vehiculosPanel = vehiculosPanel;
        this.mecanicosPanel = mecanicosPanel;
        this.repuestosPanel = repuestosPanel;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel central con tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Órdenes de Trabajo"));

        String[] columnas = {"ID", "Vehículo", "Cliente", "Mecánico", "Estado", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaOrdenes = new JTable(modeloTabla);
        tablaOrdenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaOrdenes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaOrdenes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnNuevaOrden = new JButton("Nueva Orden");
        btnNuevaOrden.addActionListener(e -> crearNuevaOrden());

        JButton btnAsignarMecanico = new JButton("Asignar Mecánico");
        btnAsignarMecanico.addActionListener(e -> asignarMecanico());

        JButton btnAgregarServicio = new JButton("Agregar Servicio");
        btnAgregarServicio.addActionListener(e -> agregarServicio());

        JButton btnAgregarRepuesto = new JButton("Agregar Repuesto");
        btnAgregarRepuesto.addActionListener(e -> agregarRepuesto());

        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        btnCambiarEstado.addActionListener(e -> cambiarEstado());

        JButton btnVerDetalle = new JButton("Ver Detalle/Factura");
        btnVerDetalle.addActionListener(e -> verDetalle());

        panel.add(btnNuevaOrden);
        panel.add(btnAsignarMecanico);
        panel.add(btnAgregarServicio);
        panel.add(btnAgregarRepuesto);
        panel.add(btnCambiarEstado);
        panel.add(btnVerDetalle);

        return panel;
    }

    private void crearNuevaOrden() {
        if (vehiculosPanel == null || vehiculosPanel.getVehiculos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe registrar al menos un vehículo primero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Diálogo para seleccionar vehículo
        List<Vehiculo> vehiculos = vehiculosPanel.getVehiculos();
        String[] opcionesVehiculos = new String[vehiculos.size()];
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            opcionesVehiculos[i] = v.getPlaca() + " - " + v.getMarca() + " " + v.getModelo() + 
                                   " (" + v.getPropietario().getNombre() + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el vehículo:",
            "Nueva Orden de Trabajo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesVehiculos,
            opcionesVehiculos[0]);

        if (seleccion == null) {
            return;
        }

        int indiceVehiculo = -1;
        for (int i = 0; i < opcionesVehiculos.length; i++) {
            if (opcionesVehiculos[i].equals(seleccion)) {
                indiceVehiculo = i;
                break;
            }
        }

        String observaciones = JOptionPane.showInputDialog(this, "Observaciones (opcional):");

        Vehiculo vehiculo = vehiculos.get(indiceVehiculo);
        OrdenDeTrabajo orden = new OrdenDeTrabajo(siguienteId++, vehiculo, observaciones != null ? observaciones : "");
        ordenes.add(orden);

        agregarFilaTabla(orden);
        JOptionPane.showMessageDialog(this, "Orden creada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void asignarMecanico() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (mecanicosPanel == null || mecanicosPanel.getMecanicosDisponibles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mecánicos disponibles", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);

        List<Mecanico> disponibles = mecanicosPanel.getMecanicosDisponibles();
        String[] opcionesMecanicos = new String[disponibles.size()];
        for (int i = 0; i < disponibles.size(); i++) {
            Mecanico m = disponibles.get(i);
            opcionesMecanicos[i] = m.getIdMecanico() + " - " + m.getNombre() + " (" + m.getEspecialidad() + ")";
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el mecánico:",
            "Asignar Mecánico",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesMecanicos,
            opcionesMecanicos[0]);

        if (seleccion == null) {
            return;
        }

        int indiceMecanico = -1;
        for (int i = 0; i < opcionesMecanicos.length; i++) {
            if (opcionesMecanicos[i].equals(seleccion)) {
                indiceMecanico = i;
                break;
            }
        }

        Mecanico mecanico = disponibles.get(indiceMecanico);
        
        // Liberar mecánico anterior si existe
        if (orden.getMecanicoAsignado() != null) {
            orden.getMecanicoAsignado().setDisponible(true);
        }
        
        orden.setMecanicoAsignado(mecanico);
        mecanico.setDisponible(false);

        actualizarFilaTabla(filaSeleccionada, orden);
        JOptionPane.showMessageDialog(this, "Mecánico asignado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarServicio() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField txtDescripcion = new JTextField();
        JTextField txtCosto = new JTextField();
        
        panel.add(new JLabel("Descripción del servicio:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel("Costo base:"));
        panel.add(txtCosto);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Agregar Servicio", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String descripcion = txtDescripcion.getText().trim();
                double costo = Double.parseDouble(txtCosto.getText().trim());
                
                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La descripción es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Servicio servicio = new Servicio(descripcion, costo);
                orden.agregarServicio(servicio);
                
                actualizarFilaTabla(filaSeleccionada, orden);
                JOptionPane.showMessageDialog(this, "Servicio agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El costo debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarRepuesto() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (repuestosPanel == null || repuestosPanel.getRepuestosConStock().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay repuestos con stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);

        List<Repuesto> conStock = repuestosPanel.getRepuestosConStock();
        String[] opcionesRepuestos = new String[conStock.size()];
        for (int i = 0; i < conStock.size(); i++) {
            Repuesto r = conStock.get(i);
            opcionesRepuestos[i] = r.getCodigo() + " - " + r.getNombre() + 
                                   " ($" + String.format("%.2f", r.getPrecioUnitario()) + 
                                   ") Stock: " + r.getStockDisponible();
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el repuesto:",
            "Agregar Repuesto",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesRepuestos,
            opcionesRepuestos[0]);

        if (seleccion == null) {
            return;
        }

        int indiceRepuesto = -1;
        for (int i = 0; i < opcionesRepuestos.length; i++) {
            if (opcionesRepuestos[i].equals(seleccion)) {
                indiceRepuesto = i;
                break;
            }
        }

        Repuesto repuesto = conStock.get(indiceRepuesto);

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad (Stock disponible: " + repuesto.getStockDisponible() + "):");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr.trim());
            if (orden.agregarRepuesto(repuesto, cantidad)) {
                actualizarFilaTabla(filaSeleccionada, orden);
                JOptionPane.showMessageDialog(this, "Repuesto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No hay stock suficiente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstado() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);

        EstadoOrden[] estados = EstadoOrden.values();
        String[] opcionesEstados = new String[estados.length];
        for (int i = 0; i < estados.length; i++) {
            opcionesEstados[i] = estados[i].name();
        }

        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el nuevo estado:",
            "Cambiar Estado",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcionesEstados,
            orden.getEstado().name());

        if (seleccion == null) {
            return;
        }

        try {
            EstadoOrden nuevoEstado = EstadoOrden.valueOf(seleccion);
            orden.setEstado(nuevoEstado);
            actualizarFilaTabla(filaSeleccionada, orden);
            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalle() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenDeTrabajo orden = ordenes.get(filaSeleccionada);
        String factura = orden.generarFactura();

        JTextArea textArea = new JTextArea(factura);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Detalle de Orden #" + orden.getIdOrden(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarFilaTabla(OrdenDeTrabajo orden) {
        String vehiculoInfo = orden.getVehiculo().getPlaca() + " - " + orden.getVehiculo().getMarca() + " " + orden.getVehiculo().getModelo();
        String clienteInfo = orden.getCliente().getNombre();
        String mecanicoInfo = orden.getMecanicoAsignado() != null ? orden.getMecanicoAsignado().getNombre() : "Sin asignar";
        String total = String.format("$%.2f", orden.calcularTotalConIVA());

        Object[] fila = {orden.getIdOrden(), vehiculoInfo, clienteInfo, mecanicoInfo, orden.getEstado().name(), total};
        modeloTabla.addRow(fila);
    }

    private void actualizarFilaTabla(int fila, OrdenDeTrabajo orden) {
        String mecanicoInfo = orden.getMecanicoAsignado() != null ? orden.getMecanicoAsignado().getNombre() : "Sin asignar";
        String total = String.format("$%.2f", orden.calcularTotalConIVA());

        modeloTabla.setValueAt(mecanicoInfo, fila, 3);
        modeloTabla.setValueAt(orden.getEstado().name(), fila, 4);
        modeloTabla.setValueAt(total, fila, 5);
    }

    public List<OrdenDeTrabajo> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenDeTrabajo> ordenes) {
        this.ordenes.clear();
        this.ordenes.addAll(ordenes);
        
        // Actualizar el siguiente ID basado en el máximo ID existente
        int maxId = 0;
        for (OrdenDeTrabajo o : ordenes) {
            if (o.getIdOrden() > maxId) {
                maxId = o.getIdOrden();
            }
        }
        if (maxId > 0) {
            this.siguienteId = maxId + 1;
        }
        
        actualizarTabla();
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    public void setSiguienteId(int siguienteId) {
        this.siguienteId = siguienteId;
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (OrdenDeTrabajo orden : ordenes) {
            String mecanicoInfo = orden.getMecanicoAsignado() != null ? 
                orden.getMecanicoAsignado().getNombre() : "Sin asignar";
            Object[] fila = {
                orden.getIdOrden(),
                orden.getVehiculo().getPlaca(),
                orden.getVehiculo().getPropietario().getNombre(),
                mecanicoInfo,
                orden.getEstado().name(),
                String.format("$%.2f", orden.calcularTotalConIVA())
            };
            modeloTabla.addRow(fila);
        }
    }
}
