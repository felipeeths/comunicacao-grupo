import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class Interface extends JFrame {

	private JPanel contentPane;
    final List<String> salas = new LinkedList<String>();
    private String sala;
    JTextArea textArea;
    private JLabel label;
    TeamJavaSpeak teamjava;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Interface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblTeamJavaSpeak = new JLabel("Team Java Speak");
		lblTeamJavaSpeak.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTeamJavaSpeak, BorderLayout.NORTH);
		
		JButton btnNewButton_1 = new JButton("Criar/Entrar Sala");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sala = (String) JOptionPane.showInputDialog(contentPane,"Digite o nome da sala:");
				salas.add(sala);
				appendText(sala);
				teamjava = new TeamJavaSpeak(sala);
			}
		});
		contentPane.add(btnNewButton_1, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setRows(5);
		textArea.setTabSize(20);
		contentPane.add(textArea, BorderLayout.SOUTH);
	
	}
	private void appendText(String newline) {

	      textArea.append("Sala: " + newline);
	      textArea.append("\n");
	      textArea.setCaretPosition(textArea.getDocument().getLength());
	  }
}
