package com.paywithspare.capacitorsunmiprinter;

import android.os.RemoteException;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;

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
