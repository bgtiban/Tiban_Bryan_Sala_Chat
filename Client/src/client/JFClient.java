package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class JFClient extends JFrame{

	private static final long serialVersionUID = 1L;
	private Client client;
	private JButton btnEnviar;
	private JTextField tfNombre;
	private JTextField tfEnviar;
	private JTextArea textArea;

	public JFClient(Client client) {
		setBackground(Color.DARK_GRAY);
		setForeground(Color.ORANGE);
		setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 15));

		this.client = client;

		getContentPane().setLayout(null);
		setSize(572, 658);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(473, 568, 73, 33);
		getContentPane().add(btnEnviar);

		tfNombre = new JTextField();
		tfNombre.setText("Pepe");
		tfNombre.setBounds(12, 569, 116, 33);
		getContentPane().add(tfNombre);
		tfNombre.setColumns(10);

		tfEnviar = new JTextField();
		tfEnviar.setColumns(10);
		tfEnviar.setBounds(140, 569, 321, 33);
		getContentPane().add(tfEnviar);


		buttons();


		tfEnviar.addKeyListener(newKeyListener());
		this.setVisible(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 13, 530, 542);
		getContentPane().add(scrollPane);

		 textArea = new JTextArea();
		 textArea.setWrapStyleWord(true);
		 textArea.setLineWrap(true);
		 scrollPane.setViewportView(textArea);
		 client.receiverMessages(textArea);

	}

	private KeyListener newKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10)
				{
					try {
						client.sendMessage(tfNombre.getText().toUpperCase() + ": " + tfEnviar.getText());
						tfEnviar.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		};
	}

	private void buttons()
	{
		btnEnviar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					client.sendMessage(tfNombre.getText().toUpperCase() + ": " + tfEnviar.getText());
				} catch (IOException e) {

					e.printStackTrace();
				}
				tfEnviar.setText("");

			}
		});
	}
}
