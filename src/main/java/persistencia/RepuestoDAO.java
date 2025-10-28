package persistencia;

import modelo.Repuesto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class RepuestoDAO implements IRepositorio<Repuesto> {
    private static final String ARCHIVO_REPUESOS = "repuestos.csv";

    @Override
    public void guardar(Repuesto repuesto) {
        // Implementación de escritura en archivo
    }

    @Override
    public List<Repuesto> listarTodos() {
        List<Repuesto> repuestos = new ArrayList<>();
        // Uso obligatorio de try-with-resources (Requisito B.4 y C.1)
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_REPUESOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // repuestos.add(ConvertidorCSV.aRepuesto(linea));
            }
        } catch (IOException e) {
            // Manejo de excepción de E/S
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
        return repuestos;
    }

    // ... otros métodos
}