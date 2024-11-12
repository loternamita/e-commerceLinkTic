package com.linktic.application.Shared;

import com.linktic.core.Domain.Order.Order;
import com.linktic.core.Domain.Order.OrderProduct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.io.ByteArrayOutputStream;

@Component
public class OrderExcelGenerator {
    public byte[] generateOrderExcel(List<Order> orders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Orders");

            // Crear encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Order ID");
            headerRow.createCell(1).setCellValue("Customer ID");
            headerRow.createCell(2).setCellValue("Order Date");
            headerRow.createCell(3).setCellValue("Total Amount");
            headerRow.createCell(4).setCellValue("Product Name");
            headerRow.createCell(5).setCellValue("Quantity");
            headerRow.createCell(6).setCellValue("Price");

            int rowNum = 1;
            for (Order order : orders) {
                // Crear fila para la orden
                Row orderRow = sheet.createRow(rowNum++);
                orderRow.createCell(0).setCellValue(order.getId());
                orderRow.createCell(1).setCellValue(order.getCustomerId());
                orderRow.createCell(2).setCellValue(order.getOrderDate().toString());
                orderRow.createCell(3).setCellValue(order.getTotalAmount());

                // Agregar productos relacionados
                if (!order.getProducts().isEmpty()) {
                    // Obtener el primer producto y rellenar en la misma fila que la orden
                    OrderProduct firstProduct = order.getProducts().get(0);
                    orderRow.createCell(4).setCellValue(firstProduct.getNameProduct());
                    orderRow.createCell(5).setCellValue(firstProduct.getQuantity());
                    orderRow.createCell(6).setCellValue(firstProduct.getPrice());

                    // Si hay más de un producto, crear nuevas filas para cada uno
                    for (int i = 1; i < order.getProducts().size(); i++) {
                        OrderProduct product = order.getProducts().get(i);
                        Row productRow = sheet.createRow(rowNum++);
                        productRow.createCell(4).setCellValue(product.getNameProduct());
                        productRow.createCell(5).setCellValue(product.getQuantity());
                        productRow.createCell(6).setCellValue(product.getPrice());
                    }
                }
            }

            // Autoajustar columnas
            for (int i = 0; i <= 6; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
