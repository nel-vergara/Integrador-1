package com.medina.fragrances.fragrance_app.service;

import com.google.common.base.Preconditions; // Google Guava
import com.medina.fragrances.fragrance_app.model.Producto;
import com.medina.fragrances.fragrance_app.repository.ProductoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils; // Apache Commons
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger; // Logback (SLF4J Facade)
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductoExcelService {

    // 1. LOGBACK: Logger para registrar la actividad del servicio
    private static final Logger log = LoggerFactory.getLogger(ProductoExcelService.class);

    @Autowired
    private ProductoRepository productoRepository;

    public void generarExcel(HttpServletResponse response) throws IOException {
        log.info("Iniciando la generación del archivo Excel de productos...");

        List<Producto> productos = productoRepository.findAll();
    
        // 2. GOOGLE GUAVA: Validar precondiciones antes de procesar
        Preconditions.checkNotNull(productos, "La lista de productos no puede ser nula");
        
        log.info("Se encontraron {} productos en la base de datos.", productos.size());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet("Catálogo de perfumes");

            String[] columnas = { "ID", "Nombre", "Marca", "Categoría", "Precio (S/)", "Stock", "Descripción" };

            Font fuente = workbook.createFont();
            fuente.setBold(true);

            CellStyle estilo = workbook.createCellStyle();
            estilo.setFont(fuente);

            // Crear encabezados
            Row encabezado = hoja.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell celda = encabezado.createCell(i);
                celda.setCellValue(columnas[i]);
                celda.setCellStyle(estilo);
            }

            // Llenar filas
            int fila = 1;
            for (Producto producto : productos) {
                Row row = hoja.createRow(fila++);

                // 3. APACHE COMMONS: Limpieza y formato seguro de textos (evita NullPointerException)
                String nombreFormateado = StringUtils.capitalize(StringUtils.defaultString(producto.getNombre(), "Sin Nombre"));
                String marcaFormateada = StringUtils.upperCase(StringUtils.defaultString(producto.getMarca(), "SIN MARCA"));
                String descripcion = StringUtils.truncate(StringUtils.defaultString(producto.getDescripcion(), "-"), 100);

                row.createCell(0).setCellValue(producto.getId() != null ? producto.getId() : 0);
                row.createCell(1).setCellValue(nombreFormateado);
                row.createCell(2).setCellValue(marcaFormateada);
                row.createCell(3).setCellValue(StringUtils.defaultString(producto.getCategoria(), "General"));
                row.createCell(4).setCellValue(producto.getPrecio() != null ? producto.getPrecio() : 0.0);
                row.createCell(5).setCellValue(producto.getStock() != null ? producto.getStock() : 0);
                row.createCell(6).setCellValue(descripcion);
            }

            for (int i = 0; i < columnas.length; i++) {
                hoja.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();

            log.info("El archivo Excel se generó y descargó correctamente.");
        } catch (Exception e) {
            log.error("Error al generar el archivo Excel de productos", e);
            throw e;
        }
    }
}