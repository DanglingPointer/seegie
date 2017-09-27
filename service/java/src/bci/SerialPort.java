package bci;

import java.util.HashMap;
import java.util.Map;

public class SerialPort implements AutoCloseable
{
    public static SerialPort get(String portName) {
        SerialPort manager = s_handlers.getOrDefault(portName, null);
        if (manager == null) {
            manager = new SerialPort(portName);
            s_handlers.put(portName, manager);
        }
        return manager;
    }
    public static String[] getAllNames() {
        return Native.getAllPorts();
    }
    private static Map<String, SerialPort> s_handlers = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------
    private final long    m_ptr;
    private       boolean m_valid;

    private SerialPort(String port) {
        m_ptr = Native.initialize(port);
        m_valid = true;
    }
    public void setBaudRate(int baud) {
        if (!m_valid) throw new IllegalStateException();
        Native.setBaudRate(m_ptr, baud);
    }
    public String read() {
        if (!m_valid) throw new IllegalStateException();
        return Native.read(m_ptr);
    }
    public void write(String data) {
        if (!m_valid) throw new IllegalStateException();
        Native.write(m_ptr, data);
    }
    @Override
    public void close() {
        if (m_valid) {
            Native.finalize(m_ptr);
            s_handlers.values().remove(this);
            m_valid = false;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    private static final class Native
    {
        static native String[] getAllPorts();

        static native long initialize(String port); // allocates mem and opens port
        static native void setBaudRate(long ptr, int baud);
        static native String read(long ptr);
        static native void write(long ptr, String data);
        static native void finalize(long ptr); // closes port and deallocates mem

        static {
            System.loadLibrary("SerialCom");
        }
    }
}
