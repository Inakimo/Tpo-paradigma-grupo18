package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import modelo.Cliente;

public class ClientesPanel extends JPanel {
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private List<Cliente> clientes;
    private int siguienteId = 1;

    // Componentes del formulario
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    private JTextField txtTelefono;

    public ClientesPanel() {
        clientes = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);

        // Panel central con tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agregar Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        // Email
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Dirección
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtDireccion = new JTextField(20);
        panel.add(txtDireccion, gbc);

        // Teléfono
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtTelefono = new JTextField(20);
        panel.add(txtTelefono, gbc);

        // Botón Agregar
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        JButton btnAgregar = new JButton("Agregar Cliente");
        btnAgregar.addActionListener(e -> agregarCliente());
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        String[] columnas = {"ID", "Nombre", "Email", "Dirección", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.addActionListener(e -> verDetalleCliente());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarCliente());

        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panel.add(btnVerDetalle);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void agregarCliente() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(siguienteId++, nombre, email, direccion, telefono);
        clientes.add(cliente);

        Object[] fila = {cliente.getId(), cliente.getNombre(), cliente.getEmail(), 
                        cliente.getDireccion(), cliente.getTelefono()};
        modeloTabla.addRow(fila);

        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Cliente agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void verDetalleCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = clientes.get(filaSeleccionada);
        String detalle = String.format(
            "ID: %d\nNombre: %s\nEmail: %s\nDirección: %s\nTeléfono: %s",
            cliente.getId(), cliente.getNombre(), cliente.getEmail(),
            cliente.getDireccion(), cliente.getTelefono()
        );

        JOptionPane.showMessageDialog(this, detalle, "Detalle del Cliente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este cliente?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            clientes.remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Cliente eliminado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtNombre.requestFocus();
    }

    private void actualizarTabla() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        // Recargar todos los clientes
        for (Cliente c : clientes) {
            Object[] fila = {
                c.getId(),
                c.getNombre(),
                c.getEmail(),
                c.getDireccion(),
                c.getTelefono()
            };
            modeloTabla.addRow(fila);
        }
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes.clear();
        this.clientes.addAll(clientes);
        actualizarTabla();
        // Actualizar el siguiente ID
        if (!clientes.isEmpty()) {
            int maxId = 0;
            for (Cliente c : clientes) {
                if (c.getId() > maxId) {
                    maxId = c.getId();
                }
            }
            this.siguienteId = maxId + 1;
        }
    }

    public int getSiguienteId() {
        return siguienteId;
    }

    public void setSiguienteId(int siguienteId) {
        this.siguienteId = siguienteId;
    }
}
