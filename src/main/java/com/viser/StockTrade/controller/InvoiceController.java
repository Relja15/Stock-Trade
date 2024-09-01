package com.viser.StockTrade.controller;

import com.viser.StockTrade.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewInvoice(@PathVariable("id") Integer id) throws JRException {
        return invoiceService.getPdf(id);
    }
}
