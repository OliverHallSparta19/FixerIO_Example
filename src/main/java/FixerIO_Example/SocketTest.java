package FixerIO_Example;
import java.io.IOException;
        import java.io.PrintWriter;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.util.Date;
        import java.util.EventListener;

public class SocketTest {
    public static void main(String[] args) throws IOException {
        try (ServerSocket listener = new ServerSocket(3333)) {
            System.out.println("The date server is running...");
            while (true) {
                try (Socket socket = listener.accept()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    System.out.println("TEST");
                    out.println(new Date().toString());
                }
            }
        }
    }
}
