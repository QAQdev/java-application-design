import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    private InetSocketAddress socketAddress;

    private String nickname;

    public Client(String ip, int port, String nickname) {
        socketAddress = new InetSocketAddress(ip, port);
        this.nickname = nickname;
    }

    public void start() {

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(socketAddress);
            AtomicBoolean sendExitMsg = new AtomicBoolean(false);

            ByteBuffer sendBuf = ByteBuffer.allocate(1024);
            ByteBuffer readBuf = ByteBuffer.allocate(1024);

            while (true) {
                if (sendExitMsg.get()) {
                    break;
                }
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    if (sendExitMsg.get()) {
                        break;
                    }

                    SelectionKey cur = iterator.next();
                    SocketChannel clientSocketChannel;

                    // connection is ready
                    if (cur.isConnectable()) {
                        clientSocketChannel = (SocketChannel) cur.channel();
                        sendBuf.clear();

                        if (clientSocketChannel.isConnectionPending()) {
                            System.out.println("[client] connecting...");
                            if (clientSocketChannel.finishConnect()) {
                                System.out.println("[client] you connect to " + socketAddress.toString());

                                // send a test message
                                ByteBuffer encodedMsg = Charset.forName("GBK")
                                        .encode("\\testing connection, this is [" + this.nickname + "]");
                                sendBuf.put(encodedMsg);
                                sendBuf.flip();
                                clientSocketChannel.write(sendBuf);
                            }

                            clientSocketChannel.register(selector, SelectionKey.OP_READ);

                            new Thread(() -> {
                                while (true) {
                                    try {
                                        sendBuf.clear();
                                        BufferedReader bufReader = new BufferedReader(new
                                                InputStreamReader(System.in));
                                        ByteBuffer encodedMsg = Charset.forName("GBK").encode(bufReader.readLine());

//                                        ByteBuffer encodedMsg = Charset.forName("GBK").encode("test");
//                                        TimeUnit.MILLISECONDS.sleep(100);
                                        sendBuf.put(encodedMsg);
                                        sendBuf.flip();
                                        clientSocketChannel.write(sendBuf);
                                        encodedMsg.flip();
                                        if (String.valueOf(Charset.forName("GBK").decode(encodedMsg)).equals("\\exit")) {
                                            sendExitMsg.set(true);
                                            break;
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
//                                    catch (InterruptedException e) {
//                                        throw new RuntimeException(e);
//                                    }
                                }
                            }).start();
                        }
                    }
                    // read is ready
                    else if (cur.isValid() && cur.isReadable()) {

                        if (sendExitMsg.get()) {
                            break;
                        }

                        clientSocketChannel = (SocketChannel) cur.channel();

                        readBuf.clear();
                        int bytesRead = clientSocketChannel.read(readBuf);
                        if (bytesRead > 0) {
                            readBuf.flip();
                            String msg = String.valueOf(Charset.forName("GBK").decode(readBuf));
                            System.out.println(msg);
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("[client] \"\\exit\" means disconnection");

        System.out.println("[client] give yourself a fancy name!");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Client client = new Client("127.0.0.1", 5708, name);

//        Client client = new Client("127.0.0.1", 5708, "tester");

        client.start();
    }
}