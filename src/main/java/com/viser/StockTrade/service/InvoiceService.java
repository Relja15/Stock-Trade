package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.InvoiceDto;
import com.viser.StockTrade.entity.Purchase;
import com.viser.StockTrade.entity.PurchaseItem;
import com.viser.StockTrade.entity.Supplier;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final PurchaseService purchaseService;
    private final SupplierService supplierService;

    /**
     * Generates a PDF invoice for a given purchase and returns it in the response.
     *
     * This method retrieves the details of a purchase, including the supplier information and items,
     * and uses JasperReports to generate a PDF invoice. The generated PDF is returned as a byte array
     * in the response entity with appropriate headers to indicate it is a PDF file.
     *
     * @param id the ID of the purchase for which the invoice is to be generated (an {@link Integer})
     * @return a {@link ResponseEntity} containing the PDF file as a byte array, with headers set for
     *         inline display and content type as PDF
     * @throws JRException if there is an error while generating the PDF report
     */
    public ResponseEntity<byte[]> getPdf(int id) throws JRException {
        Purchase purchase = purchaseService.getById(id);
        Supplier supplier = supplierService.getByName(purchase.getSupplierName());
        List<InvoiceDto> invoiceDtos = getInvoiceList(purchase);
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(invoiceDtos);
        double totalAmountPrimitive = purchase.getTotalAmount();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CollectionData", itemsJRBean);
        parameters.put("date", purchase.getDate());
        parameters.put("id", purchase.getId());
        parameters.put("totalAmount", totalAmountPrimitive);
        parameters.put("supplier", purchase.getSupplierName());
        parameters.put("supplierAddress", supplier.getAddress());
        parameters.put("supplierPhone", supplier.getPhone());
        parameters.put("supplierEmail", supplier.getEmail());
        InputStream input = getClass().getResourceAsStream("/invoice.jrxml");

        JasperDesign jasperDesign = JRXmlLoader.load(input);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * Converts a list of purchase items associated with a purchase into a list of {@link InvoiceDto} objects.
     *
     * This method iterates through the purchase items, creating an {@link InvoiceDto} for each item.
     * It sets the quantity, product name, unit price, and line total for each invoice item based on
     * the details from the purchase items. The list of {@link InvoiceDto} objects is then returned.
     *
     * @param purchase the {@link Purchase} object containing the purchase items to be included in the invoice
     * @return a {@link List} of {@link InvoiceDto} objects representing the items in the invoice
     */
    private List<InvoiceDto> getInvoiceList(Purchase purchase) {
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (PurchaseItem purchaseItem : purchase.getPurchaseItems()) {
            InvoiceDto invoiceDto = new InvoiceDto();
            invoiceDto.setQuantity(purchaseItem.getQuantity());
            invoiceDto.setProduct(purchaseItem.getProductName());
            invoiceDto.setUnitPrice(purchaseItem.getPrice());
            invoiceDto.setLineTotal(purchaseItem.getQuantity() * purchaseItem.getPrice());
            invoiceDtos.add(invoiceDto);
        }
        return invoiceDtos;
    }
}
