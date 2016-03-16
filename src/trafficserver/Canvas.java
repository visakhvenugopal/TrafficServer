/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;
/**
 *
 * @author Visakh Venugopal
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;


public class Canvas extends javax.swing.JPanel //Canvas for drawing graph
{

    //variables
    
    private View view = null;
    private Graph graph = null;
    private Viewer viewer = null;
    private SpriteManager sMan = null;
    private int nodeCount = 2;
    private int edgeCount = 2;
    private Random rnd;
    private float zoomLevel = 1.0f;
    
        
    public Canvas() 
    {
        initComponents();
        this.drawPanel.setLayout(new BorderLayout());
        
        graph = new MultiGraph("RoadNetwork");
        graph.setStrict(false);
        graph.setAutoCreate( true );
        //graph.addAttribute("ui.stylesheet", "graph{ fill-color: grey; } sprite{ fill-color: yellow; } node{shape: box; size: 10px, 10px; fill-mode: plain; fill-color: red; stroke-mode: plain;stroke-color: blue;}");
        graph.addAttribute("ui.stylesheet", "graph { fill-color: grey; }");
        graph.addAttribute("ui.stylesheet", "node {\n" + 
                                            "    size: 3px;\n" +
                                            "    fill-color: red;\n" +
                                            "    text-mode: hidden;\n" +
                                            "    z-index: 0;\n" +
                                            "}\n" +
                                            "edge {\n" +
                                            "    shape: line;\n" +
                                            "    fill-color: #222;\n" +
                                            "    arrow-size: 3px, 2px;\n" +
                                            "}");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        try {
		graph.read("D:\\Projects\\NetBeans\\TrafficServer\\src\\assets\\LeHavre.dgs");
                //graph.read(null, null);
	} 
        catch(IOException | GraphParseException | ElementNotFoundException e) 
        {
            e.printStackTrace();
	    return;
        }
        
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        view   = viewer.addDefaultView(false);  
        
        sMan = new SpriteManager(graph);
        
        rnd = ThreadLocalRandom.current();
        
        
        renderScene();
        //zoomScene();
        
        //this.add((Component) view,BorderLayout.CENTER);
        this.drawPanel.add((Component) view,BorderLayout.CENTER);
        this.setVisible(true);
        
    }
    
    
    private synchronized void renderScene()
    {
        //should read from local graph data structures 
        /*for(int i=1; i<=10 ;i++)
        {
            Node node = graph.addNode(String.valueOf(i));
            node.setAttribute("xyz",i,i,0);
            node.setAttribute("ui.label",node.getId());
        }*/
        /*
        graph.addNode("A");
        Node node = graph.getNode("A");
        node.setAttribute("xyz",1,1,0);
        String a = "Ambadi";
        node.setAttribute("ui.label",a);
        
        graph.addNode("B");
        Node nodeB = graph.getNode("B");
        nodeB.setAttribute("xyz",2,2,0);
        nodeB.setAttribute("ui.label", "Balikeramala");
        
        graph.addNode("C");
        Node nodeC = graph.getNode("C");
        nodeC.setAttribute("xyz",2,3,0);
        nodeC.setAttribute("ui.label", "Balikeramala");
        */
        //Sprite s = sMan.addSprite("S1");
        //s.setPosition(2, 1, 0);
        //s.attachToNode("5");
       zoomScene();
    }
    
    private synchronized void zoomScene()
    {
        System.out.println("Zoom :"+zoomLevel);
        view.getCamera().setViewCenter(444000,2505000, 0);//get camera
        view.getCamera().setViewPercent(zoomLevel); 
    }
    
    
    /*
    @Override
    public void paintComponent(java.awt.Graphics g)
    {
        
        //this.setBackground(null);
         super.paintComponent(g);
         g.fillRoundRect(50, 50, 8, 8, 0, 0);
         g.drawLine(50,50,100,100);
         g.fillRoundRect(100, 100, 8, 8, 0, 0);
         g.drawLine(100,100,150,250);
         g.fillRoundRect(150, 250, 8, 8, 0, 0);
    }
    */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        nodeSearchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        labelSearchNode = new javax.swing.JLabel();
        zoomOutButton = new javax.swing.JButton();
        zoomInButton = new javax.swing.JButton();
        rstZoomButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTextPane = new javax.swing.JTextPane();
        labelZoomControl = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        drawPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout drawPanelLayout = new javax.swing.GroupLayout(drawPanel);
        drawPanel.setLayout(drawPanelLayout);
        drawPanelLayout.setHorizontalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );
        drawPanelLayout.setVerticalGroup(
            drawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        menuPanel.setBackground(new java.awt.Color(255, 255, 204));
        menuPanel.setPreferredSize(new java.awt.Dimension(160, 371));

        nodeSearchField.setBackground(new java.awt.Color(255, 204, 204));
        nodeSearchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nodeSearchFieldFocusGained(evt);
            }
        });

        searchButton.setBackground(new java.awt.Color(255, 204, 0));
        searchButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        searchButton.setLabel("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        labelSearchNode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelSearchNode.setText("Search Node :");

        zoomOutButton.setBackground(new java.awt.Color(255, 204, 0));
        zoomOutButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        zoomOutButton.setText("-");
        zoomOutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoomOutButtonMouseClicked(evt);
            }
        });

        zoomInButton.setBackground(new java.awt.Color(255, 204, 0));
        zoomInButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        zoomInButton.setText("+");
        zoomInButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoomInButtonMouseClicked(evt);
            }
        });

        rstZoomButton.setBackground(new java.awt.Color(255, 204, 0));
        rstZoomButton.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        rstZoomButton.setText("RST");
        rstZoomButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rstZoomButtonMouseClicked(evt);
            }
        });

        resultTextPane.setBackground(new java.awt.Color(255, 255, 204));
        resultTextPane.setBorder(null);
        resultTextPane.setSelectionColor(new java.awt.Color(153, 153, 255));
        jScrollPane1.setViewportView(resultTextPane);

        labelZoomControl.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelZoomControl.setText("Zomm Controls :");

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(rstZoomButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createSequentialGroup()
                        .addGap(0, 77, Short.MAX_VALUE)
                        .addComponent(searchButton))
                    .addComponent(nodeSearchField)
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addComponent(zoomOutButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zoomInButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelSearchNode)
                            .addComponent(labelZoomControl))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelSearchNode)
                .addGap(3, 3, 3)
                .addComponent(nodeSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelZoomControl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zoomOutButton)
                    .addComponent(zoomInButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rstZoomButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(drawPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(drawPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        this.resultTextPane.setEnabled(true);
    }//GEN-LAST:event_searchButtonActionPerformed

    private void nodeSearchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nodeSearchFieldFocusGained
        // TODO add your handling code here:
        this.resultTextPane.setEnabled(false);
    }//GEN-LAST:event_nodeSearchFieldFocusGained

    private void zoomInButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomInButtonMouseClicked
        // TODO add your handling code here:
        if(zoomLevel > 0.05f)
        {
            zoomLevel -= 0.05;
            zoomScene();
        }
    }//GEN-LAST:event_zoomInButtonMouseClicked

    private void zoomOutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomOutButtonMouseClicked
        // TODO add your handling code here:
        if(zoomLevel < 2.0f)
            zoomLevel += 0.05f;
        zoomScene();
    }//GEN-LAST:event_zoomOutButtonMouseClicked

    private void rstZoomButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rstZoomButtonMouseClicked
        // TODO add your handling code here:
        zoomLevel = 1.0f;
        zoomScene();
    }//GEN-LAST:event_rstZoomButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel drawPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelSearchNode;
    private javax.swing.JLabel labelZoomControl;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTextField nodeSearchField;
    private javax.swing.JTextPane resultTextPane;
    private javax.swing.JButton rstZoomButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton zoomInButton;
    private javax.swing.JButton zoomOutButton;
    // End of variables declaration//GEN-END:variables
}
