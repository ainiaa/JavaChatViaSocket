package javachatviasocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javachatviasocket.data.SocketConst;
import javachatviasocket.server.ui.ChatServerUI;
import javax.swing.DefaultListModel;

public class ChatServer extends Thread {

    ChatServerUI serverUI;
    ServerSocket serverSocket;
    BufferedReader reader;
    PrintWriter writer;

    public ChatServer(ChatServerUI ui) {
        this.serverUI = ui;
        this.start();
    }

    public void run() {
        try {

            serverSocket = new ServerSocket(SocketConst.port);
            ChatServerUI.clients = new ArrayList<>();
            println("启动服务器成功：端口:" + SocketConst.port);

            while (true) {
                println("等待客户端");
                Socket client = serverSocket.accept();
                serverUI.clients.add(client);
                println("连接成功" + client.toString());

                //更新列表
                updateClientList(client);

                new ListenerClient(serverUI, client);
            }
        } catch (IOException e) {
            println("启动服务器失败：端口:" + SocketConst.port);
            println(e.toString());
            e.printStackTrace();
        }

    }

    public void updateClientList(Socket client) {
        DefaultListModel listModel;
        if (serverUI.getClientjList().getModel() == null) {
            listModel = new DefaultListModel();
        } else {
            listModel = (DefaultListModel) (serverUI.getClientjList().getModel());
        }
        listModel.addElement(client.toString() + listModel.getSize());
        serverUI.getClientjList().setModel(listModel);
    }

    public void println(String s) {
        if (s != null) {
            this.serverUI.getMsgListjTextArea().setText(this.serverUI.getMsgListjTextArea().getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}
