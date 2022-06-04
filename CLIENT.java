package TP_01;

//TP2 Réseaux et Systèmes Répartis 2021-2022

//Nom:HADJAZI
//Prenom: Mohammed Hisham
//Specialite:   RSSI      Groupe: 02



import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class CLIENT {

	private JFrame frmClientTp;
	private JTextField input1;
	private JTextField input2;
	private JTextField input3;
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;
	static JTextArea txtout;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CLIENT window = new CLIENT();
					window.frmClientTp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CLIENT() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClientTp = new JFrame();
		frmClientTp.getContentPane().setBackground(new Color(240, 255, 240));
		frmClientTp.setBackground(new Color(102, 205, 170));
		frmClientTp.setTitle("Client TP_01");
		frmClientTp.setBounds(100, 100, 451, 491);
		frmClientTp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClientTp.getContentPane().setLayout(null);

		input1 = new JTextField();
		input1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 26));
		input1.setHorizontalAlignment(SwingConstants.CENTER);
		input1.setBounds(215, 33, 208, 38);
		frmClientTp.getContentPane().add(input1);
		input1.setColumns(10);

		input2 = new JTextField();
		input2.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 26));
		input2.setHorizontalAlignment(SwingConstants.CENTER);
		input2.setColumns(10);
		input2.setBounds(215, 80, 208, 38);
		frmClientTp.getContentPane().add(input2);

		JLabel lblNewLabel = new JLabel("Enter server address :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(35, 33, 159, 29);
		frmClientTp.getContentPane().add(lblNewLabel);

		JLabel lblEnterServerPort = new JLabel("Enter server port :");
		lblEnterServerPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterServerPort.setBounds(35, 80, 159, 29);
		frmClientTp.getContentPane().add(lblEnterServerPort);

		JLabel label = new JLabel("");
		label.setBounds(35, 130, 208, 40);
		frmClientTp.getContentPane().add(label);

		JButton btnStart = new JButton("Cennect");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				new SwingWorker() {

					@Override
					protected Object doInBackground() throws Exception {

						try {

//			 	getting user input for server address and port

							int SERVER_PORT = Integer.parseInt(input2.getText());
							String SERVER_IP = input1.getText();

//			     Establishing socket

							socket = new Socket(SERVER_IP, SERVER_PORT);
							out = new PrintWriter(socket.getOutputStream(), true);
							in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//				 getting Sever and client address and port

							InetAddress addrS = socket.getInetAddress();
							InetAddress addrC = socket.getLocalAddress();
							int portS = socket.getPort();
							int portC = socket.getLocalPort();

							txtout.setFont(new Font("Dialog", Font.BOLD, 9));
							txtout.setForeground(new Color(0, 255, 0));
							txtout.append("[CLIENT] : My address is " + addrC + " port " + portC + "\n");
							System.out.println("[CLIENT] : My address is " + addrC + " port " + portC);
							txtout.append("[CLIENT] : The server address is " + addrS + " port " + portS + "\n");
							System.out.println("[CLIENT] : The server address is " + addrS + " port " + portS);

							label.setFont(new Font("Dialog", Font.BOLD, 14));
							label.setForeground(new Color(0, 255, 0));
							label.setText("Successful Connection");
						} catch (Exception e) {
							label.setFont(new Font("Dialog", Font.BOLD, 12));
							label.setForeground(new Color(255, 0, 0));
							label.setText("ERROR" + e);
						}

						while (true) {

							String serverResponse = in.readLine();
							if (serverResponse.isEmpty()) {

							} else {
								System.out.println("[SERVER] : " + serverResponse);
								txtout.setFont(new Font("Dialog", Font.BOLD, 9));
								txtout.setForeground(new Color(0, 255, 0));
								txtout.append("[SERVER] : " + serverResponse + "\n");
							}

						}

					}

				}.execute();

			}
		});
		btnStart.setBackground(new Color(175, 238, 238));
		btnStart.setBounds(303, 128, 120, 29);
		frmClientTp.getContentPane().add(btnStart);

		JLabel lblResult = new JLabel("Result :");
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setBounds(25, 273, 114, 29);
		frmClientTp.getContentPane().add(lblResult);

		input3 = new JTextField();
		input3.setFont(new Font("Dialog", Font.BOLD, 20));
		input3.setHorizontalAlignment(SwingConstants.CENTER);
		input3.setBounds(35, 209, 388, 52);
		frmClientTp.getContentPane().add(input3);
		input3.setColumns(10);

		JLabel lblEnterNumbers = new JLabel("Enter numbers : ex 1,2,3,3,4");
		lblEnterNumbers.setBounds(35, 182, 388, 15);
		frmClientTp.getContentPane().add(lblEnterNumbers);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

//				getting input from user
				String array = input3.getText();

//				sending input to server
				out.println(array);

//				printing table		
				txtout.append("[CLIENT] : the array you provided is : " + array + "\n");

				new SwingWorker() {

					@Override
					protected Object doInBackground() throws Exception {

//						Receiving solution from server

						try {

							while (true) {

								String serverResponse = in.readLine();
								if (serverResponse.isEmpty()) {
									// empty response
								} else {
									System.out.println("[SERVER] : " + serverResponse);
									txtout.setFont(new Font("Dialog", Font.BOLD, 9));
									txtout.setForeground(new Color(0, 255, 0));
									txtout.append("[SERVER] :" + serverResponse + "\n");
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}

				}.execute();

			}
		});
		btnSend.setBackground(new Color(175, 238, 238));
		btnSend.setBounds(303, 273, 120, 29);
		frmClientTp.getContentPane().add(btnSend);

		JButton btnStop = new JButton("Disconnect");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				out.println("bye");

				try {
					socket.close();
				} catch (IOException e) {
				}

				label.setFont(new Font("Dialog", Font.BOLD, 16));
				label.setForeground(new Color(255, 0, 0));
				label.setText("Disconnected");

			}
		});
		btnStop.setBackground(new Color(175, 238, 238));
		btnStop.setBounds(303, 168, 120, 29);
		frmClientTp.getContentPane().add(btnStop);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 312, 388, 124);
		frmClientTp.getContentPane().add(scrollPane);
		
				txtout = new JTextArea();
				scrollPane.setViewportView(txtout);
				txtout.setForeground(Color.GREEN);
				txtout.setBackground(Color.GRAY);

	}
}
