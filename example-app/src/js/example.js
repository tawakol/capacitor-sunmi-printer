import { Printer } from 'capacitor-sunmi-printer';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    Printer.echo({ value: inputValue })
}
