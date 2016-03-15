/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;
import java.awt.Robot;
import java.awt.AWTException; 
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Visakh Venugopal
 */
public class TrafficServer extends javax.swing.JFrame 
{

    //App Variables
    
    Canvas painter;
    private int currentMenuId = 0;
    private javax.swing.JButton currentMenu = null;
    private long mask = InputEvent.MOUSE_EVENT_MASK;
    Robot bot = null;
    /**
     * Creates new form TrafficServer
     */
    public TrafficServer() 
    {
        //LoginForm LF= new LoginForm();
        
        initComponents();
        try {
            bot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(TrafficServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentMenu = graphViewMenu;
        painter = new Canvas();
       
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pnl_Main = new javax.swing.JPanel();
        Pnl_Head = new javax.swing.JPanel();
        Lbl_Name = new javax.swing.JLabel();
        Lbl_NameDef = new javax.swing.JLabel();
        Pnl_Nav = new javax.swing.JPanel();
        graphViewMenu = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        Pnl_Body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));
        setMinimumSize(new java.awt.Dimension(800, 600));

        Pnl_Main.setBackground(new java.awt.Color(51, 51, 51));

        Pnl_Head.setBackground(new java.awt.Color(102, 102, 102));

        Lbl_Name.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 48)); // NOI18N
        Lbl_Name.setForeground(new java.awt.Color(255, 153, 0));
        Lbl_Name.setText("ITcaS");

        Lbl_NameDef.setForeground(new java.awt.Color(255, 255, 255));
        Lbl_NameDef.setText(" Intelligent Traffic Congestion Avoidence System");

        javax.swing.GroupLayout Pnl_HeadLayout = new javax.swing.GroupLayout(Pnl_Head);
        Pnl_Head.setLayout(Pnl_HeadLayout);
        Pnl_HeadLayout.setHorizontalGroup(
            Pnl_HeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_HeadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_HeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Lbl_NameDef, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lbl_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(433, Short.MAX_VALUE))
        );
        Pnl_HeadLayout.setVerticalGroup(
            Pnl_HeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Pnl_HeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Lbl_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Lbl_NameDef)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Pnl_Nav.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 0)));
        Pnl_Nav.setOpaque(false);
        Pnl_Nav.setLayout(new java.awt.GridLayout(6, 1, 2, 2));

        graphViewMenu.setText("Map View");
        graphViewMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                graphViewMenuMouseClicked(evt);
            }
        });
        graphViewMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphViewMenuActionPerformed(evt);
            }
        });
        Pnl_Nav.add(graphViewMenu);

        jButton2.setText("jButton2");
        Pnl_Nav.add(jButton2);

        jButton3.setText("jButton3");
        Pnl_Nav.add(jButton3);

        jButton4.setText("jButton4");
        Pnl_Nav.add(jButton4);

        jButton5.setText("jButton5");
        Pnl_Nav.add(jButton5);

        jButton6.setText("jButton6");
        Pnl_Nav.add(jButton6);

        Pnl_Body.setBackground(new java.awt.Color(153, 153, 153));
        Pnl_Body.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 51)));
        Pnl_Body.setLayout(new java.awt.GridLayout(1, 1));

        javax.swing.GroupLayout Pnl_MainLayout = new javax.swing.GroupLayout(Pnl_Main);
        Pnl_Main.setLayout(Pnl_MainLayout);
        Pnl_MainLayout.setHorizontalGroup(
            Pnl_MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_MainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Pnl_MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pnl_Head, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Pnl_MainLayout.createSequentialGroup()
                        .addComponent(Pnl_Nav, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Pnl_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        Pnl_MainLayout.setVerticalGroup(
            Pnl_MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pnl_MainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pnl_Head, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Pnl_MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Pnl_MainLayout.createSequentialGroup()
                        .addComponent(Pnl_Nav, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(Pnl_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        Pnl_Nav.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pnl_Main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void graphViewMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphViewMenuMouseClicked
        // TODO add your handling code here:
        updateGraphView();
    }//GEN-LAST:event_graphViewMenuMouseClicked

    private void graphViewMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphViewMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_graphViewMenuActionPerformed

    private void updateGraphView()
    {
        Pnl_Body.removeAll();
        Pnl_Body.add(painter);
        painter.setVisible(true);
        Pnl_Body.updateUI();
       
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrafficServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrafficServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrafficServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrafficServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Lbl_Name;
    private javax.swing.JLabel Lbl_NameDef;
    private javax.swing.JPanel Pnl_Body;
    private javax.swing.JPanel Pnl_Head;
    private javax.swing.JPanel Pnl_Main;
    private javax.swing.JPanel Pnl_Nav;
    private javax.swing.JButton graphViewMenu;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    // End of variables declaration//GEN-END:variables
}
