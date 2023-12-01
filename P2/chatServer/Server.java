import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    private Map<String, SocketChannel> clients = new HashMap<>();
    private Map<String, String> clientsNickname = new HashMap<>();
    private InetSocketAddress socketAddress;

    private void doDisconnection(SocketChannel clientSocketChannel, SelectionKey cur) throws IOException {
        String uuid, nickName = "";
        for (Map.Entry<String, SocketChannel> client : clients.entrySet()) {
            if (client.getValue().equals(clientSocketChannel)) {
                uuid = client.getKey();
                nickName = clientsNickname.get(uuid);
                clients.remove(client.getKey());
                clientsNickname.remove(uuid);
                break;
            }
        }

        System.out.println("[server] client " + nickName + " disconnects");
        String data = "[global] client " + nickName + " leaves the chat\n";

        for (Map.Entry<String, SocketChannel> client : clients.entrySet()) {
            // broadcasting
            ByteBuffer broadcastMsg = ByteBuffer.allocate(1024);
            broadcastMsg.put(Charset.forName("GBK").encode(data));
            broadcastMsg.flip();
            client.getValue().write(broadcastMsg);
        }
        clientSocketChannel.close();
        cur.cancel();
    }

    public Server(String ip, int port) {
        socketAddress = new InetSocketAddress(ip, port);
    }

    public void start() throws IOException {
        Selector selector = Selector.open(); // create selector
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // create channel
        serverSocketChannel.configureBlocking(false); // set channel to non-blocking
        serverSocketChannel.bind(socketAddress); // bind channel to a port

        /*
         * register channel to selector, and start listen to connection from clients
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("[server] server starts at " + socketAddress.toString());

        ByteBuffer readBuf = ByteBuffer.allocate(1024);

        while (true) {
            selector.select(); // block until there is at least 1 available channel

            // iterate all available channels and process according to type
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            SocketChannel clientSocketChannel;

            while (iterator.hasNext()) {
                SelectionKey cur = iterator.next();
                // accept is ready
                if (cur.isAcceptable()) {
                    clientSocketChannel = serverSocketChannel.accept();
                    clientSocketChannel.configureBlocking(false); // non blocking
                    clientSocketChannel.register(selector, SelectionKey.OP_READ);
                    clients.put(String.valueOf(UUID.randomUUID()), clientSocketChannel);
                }
                // read is ready
                else if (cur.isValid() && cur.isReadable()) {
                    clientSocketChannel = (SocketChannel) cur.channel();
                    readBuf.clear();

                    int bytesRead = -1;

                    try {
                        bytesRead = clientSocketChannel.read(readBuf);
                    } catch (IOException e) {
                        doDisconnection(clientSocketChannel, cur);
                    }

                    if (bytesRead > 0) {

                        String uuid = "";
                        for (Map.Entry<String, SocketChannel> client : clients.entrySet()) {
                            if (client.getValue().equals(clientSocketChannel)) {
                                uuid = client.getKey();
                                break;
                            }
                        }

                        readBuf.flip(); // reset `position`
                        String data = String.valueOf(Charset.forName("GBK").decode(readBuf));

                        if (data.matches("(\\\\testing connection, this is \\[([\\s\\S]+)\\])")) {

                            Matcher matcher = Pattern.compile("\\[([\\s\\S]+)\\]").matcher(data);
                            matcher.find();
                            String clientName = matcher.group(1);
                            clientsNickname.put(uuid, clientName);

                            System.out.println("[server] client " + clientName + ": " + data);

                            data = "[global] client " + clientName + " joins the chat\n";
                            ByteBuffer broadcastMsg = ByteBuffer.allocate(1024);
                            for (Map.Entry<String, SocketChannel> client : clients.entrySet()) {
                                // broadcasting
                                broadcastMsg.clear();
                                broadcastMsg.put(Charset.forName("GBK").encode(data));
                                broadcastMsg.flip();
                                client.getValue().write(broadcastMsg);
                            }
                            cur.interestOps(SelectionKey.OP_READ);
                        } else if (data.matches("\\\\exit")) {
                            ByteBuffer msg = ByteBuffer.allocate(1024);
                            msg.put(Charset.forName("GBK").encode("\\exit"));
                            clientSocketChannel.write(msg);
                            doDisconnection(clientSocketChannel, cur);
                        } else {
                            cur.attach(data);
                            cur.interestOps(SelectionKey.OP_WRITE);
                        }
                    }
                } else if (cur.isValid() && cur.isWritable()) {
                    clientSocketChannel = (SocketChannel) cur.channel();
                    ByteBuffer broadcastMsg = ByteBuffer.allocate(1024);

                    String uuid = "";
                    for (Map.Entry<String, SocketChannel> client : clients.entrySet()) {
                        if (client.getValue().equals(clientSocketChannel)) {
                            uuid = client.getKey();
                            break;
                        }
                    }

                    String data = (String) cur.attachment();

                    System.out.println("[server] client " + clientsNickname.get(uuid) + ": " + data);
                    for (Map.Entry<String, SocketChannel> client : clients.entrySet()) {
                        // broadcasting
                        broadcastMsg.clear();
                        broadcastMsg.put(Charset.forName("GBK")
                                .encode(clientsNickname.get(uuid) + ": " + data + "\n"));
//                        broadcastMsg.put(Charset.forName("GBK")
//                                .encode(uuid + ": " + data + "\n"));
                        broadcastMsg.flip();
                        client.getValue().write(broadcastMsg);
                    }
                    cur.interestOps(SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server("127.0.0.1", 5708);
        server.start();
    }
}