package com.pasaporte.backend.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.pasaporte.backend.entity.Componente;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private final QrService qrService;

    public PdfService(QrService qrService) {
        this.qrService = qrService;
    }

    public byte[] generarPasaportePdf(Componente componente) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 11);

            Paragraph titulo = new Paragraph("PASAPORTE DIGITAL DE HARDWARE", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Datos Técnicos del Componente", fontSubtitulo));
            document.add(new Paragraph("Número de Serie: " + componente.getNumSerie(), fontTexto));
            document.add(new Paragraph("Fabricante: " + componente.getFabricante(), fontTexto));
            document.add(new Paragraph("Modelo: " + componente.getModelo(), fontTexto));
            document.add(new Paragraph("Tipo: " + componente.getTipoComponente(), fontTexto));
            document.add(new Paragraph("Estado actual: " + componente.getEstado(), fontTexto));
            document.add(new Paragraph(" "));

            if (componente.getFechaRegistro() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime finGarantia = componente.getFechaRegistro().plusYears(3);
                boolean enGarantia = LocalDateTime.now().isBefore(finGarantia);

                document.add(new Paragraph("Estado de Garantía", fontSubtitulo));
                document.add(new Paragraph("Fecha de registro: " + componente.getFechaRegistro().format(formatter), fontTexto));
                document.add(new Paragraph("Fin de garantía: " + finGarantia.format(formatter) + (enGarantia ? " (ACTIVA)" : " (EXPIRADA)"), fontTexto));
                document.add(new Paragraph(" "));
            }

            String urlPasaporte = "http://localhost:8080/api/pasaporte/" + componente.getNumSerie();
            byte[] qrBytes = qrService.generarCodigoQr(urlPasaporte, 200, 200);
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);

            Paragraph pie = new Paragraph("Escanea este código QR para verificar la autenticidad y garantía en línea.", fontTexto);
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el certificado PDF del componente", e);
        }
    }
}