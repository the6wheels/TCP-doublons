package TP_01;



//TP2 Réseaux et Systèmes Répartis 2021-2022

//Nom:HADJAZI
//Prenom: Mohammed Hisham
//Specialite:   RSSI      Groupe: 01



import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class SERVER {

	private JFrame frmServerTp;
	private JTextField input;
	private PrintWriter out;
	private BufferedReader in;
	private Socket client;
	private ServerSocket listner;
	static JTextArea txtout;
	private static int clientCTR = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SERVER window = new SERVER();
					window.frmServerTp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SERVER() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServerTp = new JFrame();
		frmServerTp.setBackground(new Color(64, 224, 208));
		frmServerTp.getContentPane().setBackground(UIManager.getColor("activeCaption"));
		frmServerTp.setTitle("Server TP_01");
		frmServerTp.setBounds(100, 100, 406, 459);
		frmServerTp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServerTp.getContentPane().setLayout(null);

		input = new JTextField();
		input.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 26));
		input.setHorizontalAlignment(SwingConstants.CENTER);
		input.setBounds(202, 48, 160, 50);
		frmServerTp.getContentPane().add(input);
		input.setColumns(10);

		JLabel lblEnterPortNumber = new JLabel("Enter PORT number  :");
		lblEnterPortNumber.setBounds(31, 41, 178, 40);
		frmServerTp.getContentPane().add(lblEnterPortNumber);

		JLabel lblTo = new JLabel("49152 to 65535");
		lblTo.setBounds(54, 68, 130, 40);
		frmServerTp.getContentPane().add(lblTo);

		JLabel lblServerStatus = new JLabel("Server Status :");
		lblServerStatus.setBounds(34, 208, 135, 15);
		frmServerTp.getContentPane().add(lblServerStatus);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				JButton btnStart = new JButton("Start Server");
				btnStart.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

//				getting server port from user

						int PORT = Integer.parseInt(input.getText());

//				 creating list to add clients to it

						ArrayList<Handler> clients = new ArrayList<>();

//			     creating a limited pool of clients of 10

						ExecutorService pool = Executors.newFixedThreadPool(10);

						new SwingWorker() {

							@Override
							protected Object doInBackground() throws Exception {

								try {

//					 		creating server socket

									listner = new ServerSocket(PORT);
									txtout.setFont(new Font("Dialog", Font.BOLD, 9));
									txtout.setForeground(new Color(0, 255, 0));
									txtout.append("[SERVER] : Waiting for client connection....\n");
									System.out.println("[SERVER] : Waiting for client connection....");

									while (true) {

//								waiting for clients

										client = listner.accept();

//								Increase number of clients

										setClientCTR(getClientCTR() + 1);

										txtout.setFont(new Font("Dialog", Font.BOLD, 9));
										txtout.setForeground(new Color(0, 255, 0));
										txtout.append("[SERVER] : Connected to new Client, Number of clients : "
												+ getClientCTR() + "\n");

//								Creating new thread and adding it to clients and the pool the executing the pool

										Handler clientThread = new Handler(client);
										clients.add(clientThread);
										pool.execute(clientThread);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								return null;
							}

						}.execute();

					}
				});
				btnStart.setBackground(new Color(0, 255, 127));
				btnStart.setBounds(54, 137, 130, 25);
				frmServerTp.getContentPane().add(btnStart);

			}
		});

		JButton btnStop = new JButton("Stop Server");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Close socket input/output stream and client/server socket

				try {
					out.close();
					in.close();
					client.close();
					listner.close();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});
		btnStop.setBackground(new Color(255, 0, 0));
		btnStop.setBounds(205, 137, 130, 25);
		frmServerTp.getContentPane().add(btnStop);
		
		JLabel lblCreatedByHadjazi = new JLabel("Created by HADJAZI + AMOUR , G_01 RSSI");
		lblCreatedByHadjazi.setBounds(77, 401, 293, 15);
		frmServerTp.getContentPane().add(lblCreatedByHadjazi);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 235, 342, 143);
		frmServerTp.getContentPane().add(scrollPane);
		
				txtout = new JTextArea();
				scrollPane.setViewportView(txtout);
				txtout.setForeground(Color.GREEN);
				txtout.setBackground(Color.GRAY);

	}

	public static int getClientCTR() {
		return clientCTR;
	}

	public static void setClientCTR(int clientCTR) {
		SERVER.clientCTR = clientCTR;
	}
}

class Handler implements Runnable {

	private Socket client;
	private BufferedReader in;
	private PrintWriter out;

//	constructor for the handler class

	public Handler(Socket clientSocket) throws IOException {

//		creation of the socket object with the in and out objects
		this.client = clientSocket;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

	}

	@SuppressWarnings("null")
	@Override
	public void run() {

//		new client address
		SocketAddress addrC = client.getRemoteSocketAddress();
		SERVER.txtout.append("[SERVER] : A new client connected with adress : " + addrC + "\n");
		System.out.println("[SERVER] : A new client connected with adress : " + addrC);

//		Sending current Number of clients to the new client

		out.println("Current Number of Clients = " + SERVER.getClientCTR() + "\n");

		try {
			while (true) {

//			getting a table from client and checking for connection at the same time

				String request = in.readLine();
				if (request.equals("bye") || request.equals(null)) {
					SERVER.setClientCTR(SERVER.getClientCTR() - 1);
					SERVER.txtout.append("[SERVER] : A client Discconnected\n");
					System.out.println("[SERVER] : A client Discconnected");

				} else {
					out.println("Recieved new Table\n" + request + "\n");
					SERVER.txtout.append("[SERVER] : recived a new table " + request + "\n");
					System.out.println("[SERVER] : recived a new table " + request);

//				Processing table from client

					SERVER.txtout.append("[SERVER] : Processing table .... \n");

					String[] parts = request.split(",");
					int[] array = new int[parts.length];
					for (int i = 0; i < parts.length; i++) {
						array[i] = Integer.parseInt(parts[i]);
					}

//				Sorting the array and finding repeated elements

					Arrays.sort(array);
					ArrayList<String> record = new ArrayList<String>();

					for (int i = 0; i < array.length - 1; i++) {

						if (array[i] == array[i + 1]) {
							System.out.println("duplicate item [" + array[i + 1] + "]");
							record.add(String.valueOf(array[i]));

						}

					}

//				Checking Results and sending them to client

					if (record.isEmpty()) {
						out.println("votre tableau ne contient aucun doublon\n");
						System.out.println("votre tableau ne contient aucun doublon");
						SERVER.txtout.append("[SERVER] : Finished with NO Repeated elements \n");
					} else {
						SERVER.txtout.append("[SERVER] : Finished. Found repeated " + record + "\n");
						SERVER.txtout.append("[SERVER] : Sending Results to Client\n");
						out.println("votre tableau contient un doublon\n duplicate items are : " + record + "\n");
						System.out.println("votre tableau ne contient aucun doublon" + record);

					}

				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				client.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

}
