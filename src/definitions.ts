export interface PrinterPlugin {
  getPrinterPaperWidth: () => Promise<{width: string}>;
  getPrinterModel: () => Promise<{printerModel: string}>;
  initPrinter: () => Promise<void>;
  openCashBox: () => Promise<void>;
  getPrinterStatus: () => Promise<{status: number}>;
  printText: (options: {textToPrint: string}) => Promise<void>;
  setPrinterAlignment: (options: {alignment: 'left' | 'center' | 'right'}) => Promise<void>;
  setFontSize: (options: {fontSize: number}) => Promise<void>;
}

export const PrinterStatusTypes = {
  1: "Printer is under normal operation",
  2: "Printer is under preparation",
  3: "Communication is abnormal",
  4: "Printer is out of paper",
  5: "Printer is overheating",
  6: "Cover is open",
  7: "Cutter Error",
  8: "Cutter recovered",
  9: "Black mark not detected",
  505: "Printer is not detected",
  507: "Printer firmware update failed"
};