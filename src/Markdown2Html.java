import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class Markdown2Html extends JFrame {
	/**
	 * @param args
	 */
	private int port;
	private String host;
	private JButton connect = new JButton("CSCW");
	private JButton reset = new JButton("Normal");
	private JSplitPane splitPane;
	private Thread exec;
	private boolean isconnected = false;
	public MarkdownInput input;
	public HtmlView view;
	public Markdown2Html(){
		view = new HtmlView();
		input = new MarkdownInput();
		input.setconnect("",0,false);
		input.addObserver(view);
		splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane( input.getpane() ),
				new JScrollPane( view.getpane() ) );
		splitPane.setDividerLocation( 240 );
		add(splitPane);
		JMenuBar jmbar = new JMenuBar();
		jmbar.add(reset);
		jmbar.add(connect);
		setJMenuBar(jmbar);
	    exec = new Thread(){
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				while(isconnected)
				Execute();
			}		    	
	    };
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// TODO Auto-generated method stub
				isconnected = false;
				input.setconnect("",0,false);
			
			}
    	});
		connect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// TODO Auto-generated method stub
				if(isconnected){
					JOptionPane.showMessageDialog(null, "Please press the 'Normal' button to stop the current connection");
				}
				else{
				String Ipadd = JOptionPane.showInputDialog("Enter the IP address");
				host = Ipadd;
				input.setconnect(host,8384,true);
			    exec.start();
			    isconnected = true;
				}
	
			}
    	});
		
		
		
	}
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Markdown2Html frame = new Markdown2Html();
        frame.setTitle("Markdown2Html");
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setport(8384);


	}
	public void setport(int port){
		this.port = port;
	}
		
	public void Execute(){
		try {
			ServerSocket serverSocket =  new  ServerSocket(port);
			while(true){
				Socket socket = serverSocket.accept();
				if(!isconnected)
					break;
				HandleAData task = new HandleAData(socket,input.getarea());
				new Thread(task).start();
				
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}

class HandleAData implements Runnable{
	private Socket socket;
	private JTextArea jtarea;
	public HandleAData(Socket socket,JTextArea jtarea){
		this.socket = socket;
		this.jtarea = jtarea;
	}
	public void run(){
		try {
			final DataInputStream in = new DataInputStream(socket.getInputStream());
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					final StringBuffer text = new StringBuffer();
					try {
						text.delete(0, text.length());
						text.append(in.readUTF());
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							jtarea.setText(text.toString());	
						}
					});
					
				}
			});
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
 
}


