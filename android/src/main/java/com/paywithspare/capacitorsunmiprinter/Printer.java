package com.paywithspare.capacitorsunmiprinter;

import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import com.sunmi.peripheral.printer.ExceptionConst;
import com.sunmi.peripheral.printer.InnerLcdCallback;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.WoyouConsts;

import android.util.Log;

public class Printer {

    public static int NoSunmiPrinter = 0x00000000;
    public static int CheckSunmiPrinter = 0x00000001;
    public static int FoundSunmiPrinter = 0x00000002;
    public static int LostSunmiPrinter = 0x00000003;

    /**
     * sunmiPrinter means checking the printer connection status
     */
    public int sunmiPrinter = CheckSunmiPrinter;
    /**
     * SunmiPrinterService for API
     */
    private SunmiPrinterService sunmiPrinterService;

    private static Printer helper = new Printer();
     private void Printer() {}
     public static Printer getInstance() {
         return helper;
     }

    public enum Alignment {
        LEFT(0),
        CENTER(1),
        RIGHT(2);

        private final int code;

        Alignment(int code) {
            this.code = code;
        }

        /**
         * Returns the numeric code (0, 1, or 2).
         */
        public int getCode() {
            return code;
        }

        /**
         * Convert from String ("left", "center", "right") to the enum. You can
         * call this if you still need to accept a String at runtime.
         */
        public static Integer fromString(String s) {
            if (s == null) {
                throw new IllegalArgumentException("Alignment cannot be null");
            }
            switch (s.toLowerCase()) {
                case "left":
                    return LEFT.code;
                case "center":
                    return CENTER.code;
                case "right":
                    return RIGHT.code;
                default:
                    throw new IllegalArgumentException("Invalid alignment: " + s
                            + ". Must be \"left\", \"center\", or \"right\".");
            }
        }
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            Log.i("Printer", "onConnected");
            sunmiPrinterService = service;
            checkSunmiPrinterService(service);
        }

        @Override
        protected void onDisconnected() {
            Log.i("Printer", "onDisconnected");
            sunmiPrinterService = null;
            sunmiPrinter = LostSunmiPrinter;
        }
    };

    private InnerResultCallback innerPrinterResultCallback = new InnerResultCallback() {
        @Override
        public void onRunResult(boolean isSuccess) {
            if (!isSuccess) {
                Log.e("Printer", "Failed to run command");
            }
        }

        @Override
        public void onReturnString(String value) {
            // Handle any returned string if necessary
        }

        @Override
        public void onRaiseException(int code, String msg) {
            Log.e("Printer", "Exception occurred: " + msg);
        }

        @Override
        public void onPrintResult(int code, String msg) {
            Log.i("Printer", "Print result: " + msg);
        }
    };

    /**
     * init sunmi print service
     */
    public void initSunmiPrinterService(Context context) {
        try {
            boolean ret = InnerPrinterManager.getInstance().bindService(context,
                    innerPrinterCallback);
            if (!ret) {
                sunmiPrinter = NoSunmiPrinter;
            }

            Log.i("Printer", "initSunmiPrinterService called");
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }

    private void checkSunmiPrinterService(SunmiPrinterService service) {
        boolean ret = false;
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
        sunmiPrinter = ret ? FoundSunmiPrinter : NoSunmiPrinter;
    }

    /**
     * Get printer paper size This method can be used on Sunmi devices with a
     * printer interface If the printer is not yet initialized or the call
     * fails, an empty string will be returned
     *
     * Reference to
     * https://docs.sunmi.com/general-function-modules/external-device-debug/printer-driver/
     */
    public String getPrinterPaper() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return "";
        }
        try {
            return sunmiPrinterService.getPrinterPaper() == 1 ? "58mm" : "80mm";
        } catch (RemoteException e) {
            handleRemoteException(e);
            return "";
        }
    }

    /**
     * Get printer model This method can be used on Sunmi devices with a printer
     * interface If the printer is not yet initialized or the call fails, an
     * empty string will be returned
     *
     * Reference to
     * https://docs.sunmi.com/general-function-modules/external-device-debug/printer-driver/
     */
    public String getPrinterModel() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return "";
        }
        try {
            return sunmiPrinterService.getPrinterModal();
        } catch (RemoteException e) {
            handleRemoteException(e);
            return "";
        }
    }

    /**
     * Get printer status This method can be used on Sunmi devices with a
     * printer interface If the printer is not yet initialized or the call
     * fails, an empty string will be returned
     *
     * Reference to
     * https://docs.sunmi.com/general-function-modules/external-device-debug/printer-driver/
     */
    public Integer getPrinterStatus() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return 0;
        }
        try {
            return sunmiPrinterService.updatePrinterState();
        } catch (RemoteException e) {
            handleRemoteException(e);
            return 0;
        }
    }

    /**
     * Print text This method can be used on Sunmi devices with a printer
     * interface If the printer is not yet initialized or the call fails, an
     * exception will be thrown
     *
     * Reference to
     * https://docs.sunmi.com/general-function-modules/external-device-debug/printer-driver/
     */
    public void printText(String text) {
        Log.i("Printer", "printText called with text: " + text);
        if (sunmiPrinterService == null) {
            Log.e("Printer", "SunmiPrinterService is not initialized or disconnected");
            //TODO Service disconnection processing
            return;
        }

        if (text == null || text.isEmpty()) {
            Log.e("Printer", "Text cannot be empty");
            return;
        }

        try {
            sunmiPrinterService.printText(text, innerPrinterResultCallback);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Send raw ESC/POS bytes (passed as a Base64 string) straight to the printer.
     *
     * IMPORTANT: On Sunmi devices, raw data is only committed to the print head
     * when it is wrapped in a printer buffer transaction:
     *
     *   enterPrinterBuffer(true)  -> sendRAWData(...) -> exitPrinterBuffer(true)
     *
     * Calling sendRAWData on its own (outside a buffer) is accepted by the AIDL
     * service but silently discarded, which is why raw printing appears blank.
     *
     * Reference to
     * https://docs.sunmi.com/general-function-modules/external-device-debug/printer-driver/
     */
    public void sendRAWData(String base64Data) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        if (base64Data == null || base64Data.isEmpty()) {
            Log.e("Printer", "Raw printer data cannot be empty");
            return;
        }

        try {
            byte[] data = Base64.decode(base64Data, Base64.DEFAULT);
            sunmiPrinterService.sendRAWData(data, innerPrinterResultCallback);
        } catch (IllegalArgumentException e) {
            Log.e("Printer", "Invalid raw printer data: " + e.getMessage());
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Open the printer buffer. While the buffer is open, subsequent print
     * commands are queued instead of printed immediately. Pass clean=true to
     * discard any content left over from a previous, uncommitted transaction.
     */
    public void enterPrinterBuffer(boolean clean) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.enterPrinterBuffer(clean);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Close the printer buffer. Pass commit=true to flush all queued commands
     * to the print head; commit=false discards the buffered content.
     */
    public void exitPrinterBuffer(boolean commit) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.exitPrinterBuffer(commit);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Flush the queued commands to the print head without closing the buffer,
     * allowing more commands to be appended to the same transaction afterwards.
     */
    public void commitPrinterBuffer() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.commitPrinterBuffer();
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Cut the paper (only effective on devices with a cutter).
     */
    public void cutPaper() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.cutPaper(innerPrinterResultCallback);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Feed paper by printing the given number of blank lines.
     */
    public void lineWrap(int count) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.lineWrap(count, innerPrinterResultCallback);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Print a table row using the device's native column layout.
     *
     * Column widths are *proportional weights* (not character counts), so the
     * printer computes each column's pixel width from the weights — this keeps
     * columns aligned even with the proportional Arabic font.
     *
     * @param colsTextArr  text for each column
     * @param colsWidthArr proportional weight of each column
     * @param colsAlign    alignment per column: 0 left, 1 center, 2 right
     */
    public void printColumnsString(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.printColumnsString(colsTextArr, colsWidthArr, colsAlign, innerPrinterResultCallback);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Print a bitmap (decoded from a Base64-encoded PNG/JPEG). Lets the caller
     * render text/layout pixel-perfectly (any font, exact RTL/alignment) in JS
     * and hand the printer a finished image.
     */
    public void printBitmap(String base64Image) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        if (base64Image == null || base64Image.isEmpty()) {
            Log.e("Printer", "Bitmap data cannot be empty");
            return;
        }

        try {
            byte[] data = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Log.e("Printer", "Failed to decode bitmap data");
                return;
            }
            sunmiPrinterService.printBitmap(bitmap, innerPrinterResultCallback);
        } catch (IllegalArgumentException e) {
            Log.e("Printer", "Invalid bitmap data: " + e.getMessage());
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    public void setPrinterAlignment(String alignment) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.setAlignment(Alignment.fromString(alignment), innerPrinterResultCallback);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    public void setFontSize(float fontSize) {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.setFontSize(fontSize, innerPrinterResultCallback);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }
    /**
     * Open cash box This method can be used on Sunmi devices with a cash drawer
     * interface If there is no cash box (such as V1、P1) or the call fails, an
     * exception will be thrown
     *
     * Reference to
     * https://docs.sunmi.com/general-function-modules/external-device-debug/cash-box-driver/}
     */
    public void openCashBox() {
        if (sunmiPrinterService == null) {
            //TODO Service disconnection processing
            return;
        }

        try {
            sunmiPrinterService.openDrawer(null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    private void handleRemoteException(RemoteException e) {
        //TODO process when get one exception
        Log.e("Printer", "RemoteException occurred: " + e.getMessage());
    }
}
