package ba.bitcamp.homework21.tasks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class is used as server, that will communicate with client using sockets
 * ports and ip addresses. It simulates GUI chat application
 * 
 * @author Mladen13
 *
 */
public class Server extends JFrame {

	private static final long serialVersionUID = 6849303102794682753L;

	// GUI components
	private JButton send = new JButton("Send");
	private JTextArea area = new JTextArea();
	private JTextField field = new JTextField();
	private JPanel panel = new JPanel();
	private JPanel panel2 = new JPanel();
	// Client server components
	private BufferedWriter writer = null;
	private BufferedReader reader = null;
	private ServerSocket server = null;
	private Socket client = null;
	private String s = "";

	/**
	 * Default constructor that creates server and gui application, adds gui
	 * components to frame
	 * 
	 * @throws IOException
	 */
	public Server() throws IOException {
		// Adding action listeners from ButtonHandler class
		field.addActionListener(new ButtonHandler());
		send.addActionListener(new ButtonHandler());

		setTitle("Server");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// Creating server and waiting for client
		server = new ServerSocket(8888);
		client = server.accept();

		// Adding gui components to frame
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		add(panel2, BorderLayout.SOUTH);
		add(field, BorderLayout.NORTH);

		// adding text area to panel
		panel.setLayout(new GridLayout(1, 1));
		panel.add(area);

		// setting text area look
		area.setEnabled(false);
		area.setBackground(Color.GRAY);
		area.setOpaque(true);
		area.setFont(new Font("Monospaced", Font.BOLD, 15));
		// adding components to second panel
		panel2.setLayout(new GridLayout(1, 1));
		panel2.add(send);

		setVisible(true);
		// Creating buffered writer and reader.
		writer = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		// Reading lines from client and appending them to text area
		while ((s = reader.readLine()) != null) {
			if (s.length() > 0) {

				checkLine(s);

			}

		}

	}

	/**
	 * This method checks if our line contains some of wanted inputs
	 * 
	 * @param s
	 *            - represents one line
	 * @throws IOException
	 */
	public void checkLine(String s) throws IOException {
		if (s.startsWith("/web ")) {
			try {
				Desktop.getDesktop().browse(
						new URI("http://"
								+ s.substring(s.indexOf(" ") + 1, s.length())));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else if (s.startsWith("/open ")) {
			String path = s.substring(s.indexOf(" ") + 1, s.length());
			Desktop.getDesktop().open(new File(path));

		} else if (s.startsWith("/delete ")) {
			String path = s.substring(s.indexOf(" ") + 1, s.length());
			File f = new File(path);
			f.delete();

		} else if (s.startsWith("/list ")) {
			String path = s.substring(s.indexOf(" ") + 1, s.length());
			File f = new File(path);
			String[] arr = f.list();
			for (int i = 0; i < arr.length; i++) {
				area.append(arr[i] + "\n");
			}

		} else {
			area.append("Client: " + s + "\n");
		}
	}

	/**
	 * This class is used for making actions that will help client and server to
	 * communicate when clicking on button
	 * 
	 * @author Mladen13
	 *
	 */
	public class ButtonHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {

				writer.write(field.getText());
				writer.newLine();
				writer.flush();
				if (field.getText().length() > 0) {
					area.append("Server: " + field.getText() + "\n");
					field.setText("");
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		try {

			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
