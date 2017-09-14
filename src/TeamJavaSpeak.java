import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
 
public class TeamJavaSpeak extends ReceiverAdapter {
 
    final List<String> history = new LinkedList<String>();
 
    private JChannel channel;
    private String userName = System.getProperty("user.name");
    private JTextField textField;
    private JTextArea textArea;

    public static void main(String[] args) throws Exception {
        new TeamJavaSpeak().start();
    }
 
    public void viewAccepted(View newView) {
        appendText("Na sala: " + newView + "\n");
    }
 
    public void receive(Message msg) {
        String line = msg.getObject().toString();
        synchronized(history) {
            history.add(line);
            appendText(line);
        }
    }
 
    public void getState(OutputStream output) throws Exception {
        synchronized(history) {
            Util.objectToStream(history, new DataOutputStream(output));
        }
    }
 
    public void setState(InputStream input) throws Exception {
        List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
        synchronized(history) {
            history.clear();
            history.addAll(list);
        }
        appendText(list.size() + " Mensagens recebidas:\n");
        for(String str: list) appendText(str);
    }
 
    private void start() throws Exception {
        final JPanel panel = createPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Opções");
        channel = new JChannel();
        channel.setReceiver(this);
        channel.connect("ChatCluster");
        channel.getState(null, 10000);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("TeamJavaSpeak");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(550,550);
                frame.setJMenuBar(menuBar);
                frame.getContentPane().add(panel);
                //frame.pack();
                frame.setVisible(true);
                JMenuItem mntmNewMenuItem_1 = new JMenuItem("Novo Cluster");
                fileMenu.add(mntmNewMenuItem_1);
                mntmNewMenuItem_1.addActionListener(new ActionListener() {
        			@Override
        			public void actionPerformed(ActionEvent e) {
        				// TODO Auto-generated method stub
        			     try {
							start();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

        			}
                
                });
                JMenu mnNewMenu = new JMenu("Usuario");
                menuBar.add(mnNewMenu);
                
                JMenuItem mntmNewMenuItem = new JMenuItem("Trocar Nome");
                mnNewMenu.add(mntmNewMenuItem);
                mntmNewMenuItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						userName = (String) JOptionPane.showInputDialog(frame,"Digite o nome:");
					}
                	
                });
                
                menuBar.add(fileMenu);
               
            }
        });
    }
 
    private JPanel createPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridwidth = GridBagConstraints.REMAINDER;
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = grid.weighty = 1.0;
        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea),grid);
 
        grid.weightx = grid.weighty = 0.0;
        grid.fill = GridBagConstraints.HORIZONTAL;
      
       
        JPanel messageContainer = new JPanel(new FlowLayout());
        messageContainer.add(new JLabel("Mensagem:"));
        textField = new JTextField(40);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String line="[" + userName + "] > " + textField.getText() + "\n";
                try {
                    channel.send(new Message(null, line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        messageContainer.add(textField);
        panel.add(messageContainer);
       
        return panel;
    }
 
    private void appendText(String newline) {
        textArea.append(newline);
        textField.setText("");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
 
}