package com.paywithspare.capacitorsunmiprinter;

import android.content.Context;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Printer")
public class PrinterPlugin extends Plugin {

    private Printer implementation = new Printer();

    @PluginMethod
    public void initPrinter(PluginCall call) {
        try {
            Context applicationContext = this.getActivity().getApplicationContext();
            implementation.initSunmiPrinterService(applicationContext);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to initialize printer", e);
        }
    }

    @PluginMethod
    public void getPrinterPaperWidth(PluginCall call) {
        String paperSize = implementation.getPrinterPaper();
        call.resolve(new JSObject().put("paperSize", paperSize));
    }

    @PluginMethod
    public void getPrinterModel(PluginCall call) {
        String printerModel = implementation.getPrinterModel();
        call.resolve(new JSObject().put("printerModel", printerModel));
    }

    @PluginMethod
    public void getPrinterStatus(PluginCall call) {
        try {
            int status = implementation.getPrinterStatus();
            JSObject result = new JSObject();
            result.put("status", status);
            call.resolve(result);
        } catch (Exception e) {
            call.reject("Failed to get printer status", e);
        }
    }

    @PluginMethod
    public void printText(PluginCall call) {
        String text = call.getString("textToPrint", "Default text to print");
        try {
            implementation.printText(text);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to print text", e);
        }
    }

    @PluginMethod
    public void setPrinterAlignment(PluginCall call) {
        String alignment = call.getString("alignment", "left");
        try {
            implementation.setPrinterAlignment(alignment);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to set printer alignment", e);
        }
    }

    @PluginMethod
    public void setFontSize(PluginCall call) {
        float fontSize = call.getFloat("fontSize", 12.0f);
        try {
            implementation.setFontSize(fontSize);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to set font size", e);
        }
    }

    @PluginMethod
    public void openCashBox(PluginCall call) {
        try {
            implementation.openCashBox();
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to open cash box", e);
        }
    }

}
