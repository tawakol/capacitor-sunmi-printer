export interface PrinterPlugin {
  getPrinterPaperWidth: () => Promise<{width: string}>;
  getPrinterModel: () => Promise<{printerModel: string}>;
  initPrinter: () => Promise<void>;
  openCashBox: () => Promise<void>;
  getPrinterStatus: () => Promise<{status: number}>;
  printText: (options: {textToPrint: string}) => Promise<void>;
  setPrinterAlignment: (options: {alignment: 'left' | 'center' | 'right'}) => Promise<void>;
  setFontSize: (options: {fontSize: number}) => Promise<void>;
  /**
   * Send raw ESC/POS bytes (encoded as a Base64 string) to the printer.
   *
   * Must be wrapped in a buffer transaction to actually print:
   *   enterPrinterBuffer -> sendRAWData -> exitPrinterBuffer.
   */
  sendRAWData: (options: {base64Data: string}) => Promise<void>;
  /** Open the printer buffer. Pass clean=true (default) to discard leftover content. */
  enterPrinterBuffer: (options?: {clean?: boolean}) => Promise<void>;
  /** Close the printer buffer. Pass commit=true (default) to flush queued commands. */
  exitPrinterBuffer: (options?: {commit?: boolean}) => Promise<void>;
  /** Flush queued commands without closing the buffer. */
  commitPrinterBuffer: () => Promise<void>;
  /** Cut the paper (devices with a cutter only). */
  cutPaper: () => Promise<void>;
  /** Feed the given number of blank lines. */
  lineWrap: (options: {lines: number}) => Promise<void>;
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