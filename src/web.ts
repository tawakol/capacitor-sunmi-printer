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
}
