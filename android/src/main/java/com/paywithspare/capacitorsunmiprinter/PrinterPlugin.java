package com.paywithspare.capacitorsunmiprinter;

import android.content.Context;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    public void sendRAWData(PluginCall call) {
        String base64Data = call.getString("base64Data", "");
        try {
            implementation.sendRAWData(base64Data);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to send raw printer data", e);
        }
    }

    @PluginMethod
    public void enterPrinterBuffer(PluginCall call) {
        boolean clean = Boolean.TRUE.equals(call.getBoolean("clean", true));
        try {
            implementation.enterPrinterBuffer(clean);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to enter printer buffer", e);
        }
    }

    @PluginMethod
    public void exitPrinterBuffer(PluginCall call) {
        boolean commit = Boolean.TRUE.equals(call.getBoolean("commit", true));
        try {
            implementation.exitPrinterBuffer(commit);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to exit printer buffer", e);
        }
    }

    @PluginMethod
    public void commitPrinterBuffer(PluginCall call) {
        try {
            implementation.commitPrinterBuffer();
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to commit printer buffer", e);
        }
    }

    @PluginMethod
    public void cutPaper(PluginCall call) {
        try {
            implementation.cutPaper();
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to cut paper", e);
        }
    }

    @PluginMethod
    public void lineWrap(PluginCall call) {
        int lines = call.getInt("lines", 1);
        try {
            implementation.lineWrap(lines);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to feed lines", e);
        }
    }

    @PluginMethod
    public void printColumnsString(PluginCall call) {
        JSArray columns = call.getArray("columns");
        if (columns == null) {
            call.reject("columns is required");
            return;
        }
        try {
            List<JSONObject> cols = columns.toList();
            int n = cols.size();
            String[] texts = new String[n];
            int[] widths = new int[n];
            int[] aligns = new int[n];
            for (int i = 0; i < n; i++) {
                JSONObject col = cols.get(i);
                texts[i] = col.optString("text", "");
                widths[i] = col.optInt("width", 1);
                aligns[i] = col.optInt("align", 0);
            }
            implementation.printColumnsString(texts, widths, aligns);
            call.resolve();
        } catch (JSONException e) {
            call.reject("Invalid columns payload", e);
        } catch (Exception e) {
            call.reject("Failed to print columns", e);
        }
    }

    @PluginMethod
    public void printBitmap(PluginCall call) {
        String base64Image = call.getString("base64Image", "");
        try {
            implementation.printBitmap(base64Image);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to print bitmap", e);
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
