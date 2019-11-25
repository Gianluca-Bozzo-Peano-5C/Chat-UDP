/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudpclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;



/**
 *
 * @author Bettaieb Ayoub
 */
public class ChatGUI extends JFrame implements ActionListener {
    
        JPanel pannello;
        JButton invia,ok;
        JScrollPane scroll;
        JTextArea areaMessaggi;
        JTextField messaggio,username;
        JLabel info;
        String nome;
        String indirizzoIP;
        int porta;
        String mex;
        DatagramSocket s;
        Thread a;
        
        
        ChatGUI() throws SocketException {
        this.s = new DatagramSocket();
        a=new Thread(new ricezione());
        pannello = new JPanel();
        invia = new JButton();
        scroll = new JScrollPane();
        areaMessaggi = new JTextArea();
        messaggio = new JTextField();
        username = new JTextField();
        ok = new JButton();
        info = new JLabel();
        areaMessaggi.setEditable(false);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        invia.addActionListener(this);
        ok.addActionListener(this);

        invia.setText("Send");

        areaMessaggi.setColumns(20);
        areaMessaggi.setRows(5);
        scroll.setViewportView(areaMessaggi);
  
        ok.setText("OK");

        info.setText("Username");

        javax.swing.GroupLayout pannelloLayout = new javax.swing.GroupLayout(pannello);
        pannello.setLayout(pannelloLayout);
        pannelloLayout.setHorizontalGroup(
            pannelloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pannelloLayout.createSequentialGroup()
                .addGroup(pannelloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pannelloLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(info))
                    .addGroup(pannelloLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pannelloLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(ok)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(pannelloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pannelloLayout.createSequentialGroup()
                        .addComponent(messaggio, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(invia))
                    .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pannelloLayout.setVerticalGroup(
            pannelloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pannelloLayout.createSequentialGroup()
                .addGroup(pannelloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pannelloLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(scroll))
                    .addGroup(pannelloLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(info)
                        .addGap(28, 28, 28)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(ok)
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(pannelloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messaggio, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invia))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pannello, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pannello, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        setVisible(true);
        pack();
    } 

    public static void main(String[] args) throws SocketException {

        new ChatGUI();

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource().equals(ok)){
            nome=username.getText();
            if (nome != null) {
                    areaMessaggi.append("Nome utente: " + nome + "\n");
                    a.start();
                }
        }
        if(ae.getSource().equals(invia)){
           if(nome==null){
               JOptionPane.showMessageDialog(null,"Inserisci username");
           }else{
               try {
                   invio(messaggio.getText(),nome);
               } catch (IOException ex) {
                   Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
               }
               messaggio.setText("");
               
           }
        }
    }
    
        public void invio(String mex,String nome) throws IOException {
        DatagramPacket dat;

        byte[] b;
        b = mex.getBytes("UTF-8");
        dat = new DatagramPacket(b, b.length, InetAddress.getByName(indirizzoIP), 1234);
        s.send(dat);
    }
        
    public class ricezione implements Runnable {

        public ricezione() {
        }
        
        

        @Override
        public void run() {
            DatagramPacket dat;
        String ricezione = null;
        byte[] b = new byte[100];

        dat = new DatagramPacket(b, b.length);
        while (!Thread.interrupted()) {
                try {
                    s.receive(dat);
                } catch (IOException ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ricezione = new String(dat.getData(), 0, dat.getLength(), "ISO-8859-1");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            areaMessaggi.append(nome + " > " + ricezione + "\n");
        }
        s.close();
        }
    }
}