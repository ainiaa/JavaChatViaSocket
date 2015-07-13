package javachatviasocket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javachatviasocket.client.ui.ChatClientUI;
import javachatviasocket.data.SocketConst;

public class ChatClientThread extends Thread {
    ChatClientUI ui;
    Socket client;
    BufferedReader reader;
    PrintWriter writer;

    public ChatClientThread(ChatClientUI ui) {
        this.ui = ui;
        try {
            client = new Socket(SocketConst.ip, SocketConst.port);//这里设置连接服务器端的IP的端口
            println("连接服务器成功：端口" + SocketConst.port);
            reader = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream(), true);
            // 如果为 true，则 println、printf 或 format 方法将刷新输出缓冲区
        } catch (IOException e) {
            println("连接服务器失败：端口" + SocketConst.port);
            println(e.toString());
            e.printStackTrace();
        }
        this.start();
    }

    public void run() {
        String msg = "";
        while (true) {
            try {
                msg = reader.readLine();
            } catch (IOException e) {
                println("服务器断开连接");

                break;
            }
            if (msg != null && msg.trim() != "") {
                println(">>" + msg);
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            writer.println(msg);
        } catch (Exception e) {
            println(e.toString());
        }
    }

    public void println(String s) {
        if (s != null) {
            this.ui.getMsgHistoryjTextArea().setText(this.ui.getMsgHistoryjTextArea().getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}
