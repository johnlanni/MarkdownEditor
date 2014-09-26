import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public final class MarkdownInput extends Observable {
	private final JScrollPane jspane = new JScrollPane();
	private final JTextArea jtarea = new JTextArea();
	private String host;
	private int port;
	private Socket socket;
	private DataOutputStream out;
	private boolean isconnected;
	public MarkdownInput(){
		jspane.getViewport().add(jtarea, null);
		jtarea.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				setChanged();
				notifyObservers(jtarea.getText());
				if(isconnected)
				SendData();
			}
			public void keyPressed(KeyEvent e) {
			}
		});

    }
	public void setconnect(String host, int port,boolean isconnected){
		this.host=host;
		this.port=port;
		this.isconnected = isconnected;
	}
	public void SendData() {
		try{
		socket = new Socket(host,port);
		out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(jtarea.getText());
		out.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public JScrollPane getpane(){
		return jspane;
	}
	public JTextArea getarea(){
		return jtarea;
	}
}
