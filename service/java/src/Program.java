import bci.SerialPort;

public class Program
{
    public static void main(String[] args) {
        try {
            System.out.println("Hello from seegie!");

            try (SerialPort p = SerialPort.get("COM0")) {
                p.setBaudRate(100);
            }

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
