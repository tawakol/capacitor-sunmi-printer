import { WebPlugin } from '@capacitor/core';

import type { PrinterPlugin } from './definitions';

export class PrinterWeb extends WebPlugin implements PrinterPlugin {

  async initPrinter(): Promise<void> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async getPrinterPaperWidth(): Promise<{ width: string }> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async getPrinterModel(): Promise<{ printerModel: string }> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async getPrinterStatus(): Promise<{ status: number }> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }


  async openCashBox(): Promise<void> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async printText(options: { textToPrint: string }): Promise<void> {
    console.log('Printing text:', options.textToPrint);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async setPrinterAlignment(options: { alignment: 'left' | 'center' | 'right' }): Promise<void> {
    console.log('Setting printer alignment:', options.alignment);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async setFontSize(options: { fontSize: number }): Promise<void> {
    console.log('Setting font size:', options.fontSize);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async sendRAWData(options: { base64Data: string }): Promise<void> {
    console.log('Sending raw data, length:', options.base64Data?.length ?? 0);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async enterPrinterBuffer(options?: { clean?: boolean }): Promise<void> {
    console.log('Entering printer buffer, clean:', options?.clean ?? true);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async exitPrinterBuffer(options?: { commit?: boolean }): Promise<void> {
    console.log('Exiting printer buffer, commit:', options?.commit ?? true);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async commitPrinterBuffer(): Promise<void> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async cutPaper(): Promise<void> {
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async lineWrap(options: { lines: number }): Promise<void> {
    console.log('Line wrap, lines:', options.lines);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async printColumnsString(options: {
    columns: { text: string; width: number; align: 0 | 1 | 2 }[];
  }): Promise<void> {
    console.log('Print columns:', options.columns?.length ?? 0);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }

  async printBitmap(options: { base64Image: string }): Promise<void> {
    console.log('Print bitmap, length:', options.base64Image?.length ?? 0);
    throw new Error('No web implementation, please run the application on a Sunmi Android device');
  }
}
