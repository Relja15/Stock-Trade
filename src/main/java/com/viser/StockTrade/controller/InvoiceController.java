package com.viser.StockTrade.controller;

import com.viser.StockTrade.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    /**
     * Retrieves and returns a PDF invoice for the specified ID.
     *
     * This method fetches the PDF invoice associated with the given ID from the invoice service.
     * It returns the PDF content as a byte array wrapped in a {@link ResponseEntity} object, allowing the client to
     * download or view the invoice.
     *
     * @param id the ID of the invoice to be retrieved
     * @return a {@link ResponseEntity} containing the PDF invoice as a byte array
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewInvoice(@PathVariable("id") Integer id) throws JRException {
        return invoiceService.getPdf(id);
    }
}
