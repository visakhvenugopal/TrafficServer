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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.graphstream.graph.Edge;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;


public class Canvas extends javax.swing.JPanel implements ViewerListener//Canvas for drawing graph
{

    //variables
    
    static private View view = null;
    static Graph graph = null;
    static private Viewer viewer = null;
    static private SpriteManager sMan = null;
    private int nodeCount = 2;
    private int edgeCount = 2;
    private Random rnd;
    private float zoomLevel = 1.0f;
    protected boolean loop = true;
    public double curNodeXYCords[] = { 76.323 , -10.0046};
    
    
    public Canvas()
    {
        initComponents();
        this.panelDrawCanvas.setLayout(new BorderLayout());
        
        graph = new MultiGraph("RoadNetwork");
        graph.setStrict(false);
        graph.setAutoCreate( true );
        //graph.addAttribute("ui.stylesheet", "graph{ fill-color: grey; } sprite{ fill-color: yellow; } node{shape: box; size: 10px, 10px; fill-mode: plain; fill-color: red; stroke-mode: plain;stroke-color: blue;}");
        graph.addAttribute("ui.stylesheet", "graph { fill-color: grey; }");
        graph.addAttribute("ui.stylesheet", "node {\n" + 
                                            "    size: 10px;\n" +
                                            "    fill-mode : dyn-plain;\n" +
                                            "    fill-color: green, red ;\n" +
                                            "    text-mode : normal ;\n" +
                                            "    z-index: 0;\n" +
                                            "}\n" +
                                            "edge {\n" +
                                            "    shape: line;\n" +
                                            "    fill-mode : dyn-plain;\n" +
                                            "    fill-color: green, red ;\n" +
                                            "    arrow-size: 6px, 4px;\n" +
                                            "}"+
                                            "node:clicked{fill-color:black;}");
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        try {
                graph.read("D:\\Projects\\NetBeans\\TrafficServer\\src\\assets\\cochi.dgs");
                /*
                graph.setAttribute("ui.multigraph", true);
                Set<Integer> keys = GraphHandler.nodeMap.keySet();
                for(int key : keys)
                {
                RNode node = GraphHandler.nodeMap.get(key);
               
                String id = String.valueOf(node.id);
                graph.addNode(id);
                graph.getNode(id).addAttribute("ui.label",node.getNodeName());
                graph.getNode(id).addAttribute("z_level",0);
                graph.getNode(id).addAttribute("xyz",node.getLat(),node.getLon(),0);
                
                }
                keys = GraphHandler.edgeMap.keySet();
                for(int key : keys)
                {
                REdge edge = GraphHandler.edgeMap.get(key);
                graph.addEdge(String.valueOf(key),String.valueOf(edge.getFirstNode()) ,String.valueOf(edge.getSecondNode()));
                
                }  */
               //graph.display(true);
            } catch (IOException ex) {
                Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GraphParseException ex) {
                Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        catch(ElementNotFoundException e  ) 
        {
            System.out.println(e);
	    return;
        }
        
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view   = viewer.addDefaultView(false);
        
        //ViewerPipe fromViewer = viewer.newViewerPipe();
        //fromViewer.addViewerListener(this);
        //fromViewer.addSink(graph);
        view.addMouseListener(new MouseManager(graph,view,viewer,curNodeXYCords,this));
        //view.setMouseManager(new MouseMan(graph,view));
        
        sMan = new SpriteManager(graph);
        
        rnd = ThreadLocalRandom.current();
        
        renderScene();
        this.panelDrawCanvas.add((Component) view,BorderLayout.CENTER);
        this.setVisible(true);
        
        //new UpdateTimer(graph).start();
       // this.wait(5000);
        //new UpdateEdgeColor(graph).start();
        
    }
    
    
    private synchronized void renderScene()
    {        
        setOrigin();
        zoomScene();
    }
    
    private synchronized void setOrigin()
    {
       view.getCamera().setViewCenter(curNodeXYCords[0],curNodeXYCords[1], 0);//get camera 
    }
    
    private synchronized void zoomScene()
    {
        System.out.println("Zoom :"+zoomLevel);
        this.labelZoomLevel.setText(String.valueOf(zoomLevel));
        view.getCamera().setViewPercent(zoomLevel); 
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDrawCanvas = new javax.swing.JPanel();
        panelCanvasMenu = new javax.swing.JPanel();
        textFieldSearch = new javax.swing.JTextField();
        buttonSearch = new javax.swing.JButton();
        labelSearchNode = new javax.swing.JLabel();
        buttonZoomOut = new javax.swing.JButton();
        buttonZoomIn = new javax.swing.JButton();
        buttonResetZoom = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTextPane = new javax.swing.JTextPane();
        labelZoomControl = new javax.swing.JLabel();
        labelZoomLevel = new javax.swing.JLabel();
        buttonSetOrigin = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        panelDrawCanvas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout panelDrawCanvasLayout = new javax.swing.GroupLayout(panelDrawCanvas);
        panelDrawCanvas.setLayout(panelDrawCanvasLayout);
        panelDrawCanvasLayout.setHorizontalGroup(
            panelDrawCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );
        panelDrawCanvasLayout.setVerticalGroup(
            panelDrawCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        panelCanvasMenu.setBackground(new java.awt.Color(255, 255, 204));
        panelCanvasMenu.setPreferredSize(new java.awt.Dimension(160, 371));

        textFieldSearch.setBackground(new java.awt.Color(255, 204, 204));
        textFieldSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldSearchFocusGained(evt);
            }
        });

        buttonSearch.setBackground(new java.awt.Color(255, 204, 0));
        buttonSearch.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonSearch.setLabel("Search");
        buttonSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSearchMouseClicked(evt);
            }
        });

        labelSearchNode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelSearchNode.setText("Search Node :");

        buttonZoomOut.setBackground(new java.awt.Color(255, 204, 0));
        buttonZoomOut.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonZoomOut.setText("-");
        buttonZoomOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonZoomOutMouseClicked(evt);
            }
        });

        buttonZoomIn.setBackground(new java.awt.Color(255, 204, 0));
        buttonZoomIn.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonZoomIn.setText("+");
        buttonZoomIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonZoomInMouseClicked(evt);
            }
        });

        buttonResetZoom.setBackground(new java.awt.Color(255, 204, 0));
        buttonResetZoom.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        buttonResetZoom.setText("RST");
        buttonResetZoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonResetZoomMouseClicked(evt);
            }
        });

        resultTextPane.setBackground(new java.awt.Color(255, 255, 204));
        resultTextPane.setBorder(null);
        resultTextPane.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        resultTextPane.setSelectionColor(new java.awt.Color(153, 153, 255));
        jScrollPane1.setViewportView(resultTextPane);

        labelZoomControl.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelZoomControl.setText("Zoom Controls :");

        labelZoomLevel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelZoomLevel.setText("**");

        buttonSetOrigin.setBackground(new java.awt.Color(255, 204, 51));
        buttonSetOrigin.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        buttonSetOrigin.setText("set orgn : crnt node");
        buttonSetOrigin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSetOriginMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelCanvasMenuLayout = new javax.swing.GroupLayout(panelCanvasMenu);
        panelCanvasMenu.setLayout(panelCanvasMenuLayout);
        panelCanvasMenuLayout.setHorizontalGroup(
            panelCanvasMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCanvasMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCanvasMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(textFieldSearch)
                    .addGroup(panelCanvasMenuLayout.createSequentialGroup()
                        .addComponent(buttonZoomOut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonZoomIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCanvasMenuLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonSearch))
                    .addComponent(labelSearchNode)
                    .addComponent(buttonSetOrigin, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addGroup(panelCanvasMenuLayout.createSequentialGroup()
                        .addComponent(labelZoomControl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelZoomLevel)
                        .addGap(12, 12, 12))
                    .addComponent(buttonResetZoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelCanvasMenuLayout.setVerticalGroup(
            panelCanvasMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCanvasMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelSearchNode)
                .addGap(3, 3, 3)
                .addComponent(textFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSetOrigin, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCanvasMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelZoomControl, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelZoomLevel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCanvasMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonZoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonResetZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelDrawCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCanvasMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDrawCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelCanvasMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textFieldSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldSearchFocusGained
        // TODO add your handling code here:
        this.resultTextPane.setEnabled(false);
    }//GEN-LAST:event_textFieldSearchFocusGained

    private void buttonZoomInMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonZoomInMouseClicked
        // TODO add your handling code here:
        if(zoomLevel > 0.05f)
        {
            zoomLevel -= 0.05;
            zoomScene();
        }
    }//GEN-LAST:event_buttonZoomInMouseClicked

    private void buttonZoomOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonZoomOutMouseClicked
        // TODO add your handling code here:
        if(zoomLevel < 2.0f)
            zoomLevel += 0.05f;
        zoomScene();
    }//GEN-LAST:event_buttonZoomOutMouseClicked

    private void buttonResetZoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonResetZoomMouseClicked
        // TODO add your handling code here:
        zoomLevel = 1.0f;
        curNodeXYCords[0] =  76.323 ;  
        curNodeXYCords[1] = -10.0046;
        renderScene();
    }//GEN-LAST:event_buttonResetZoomMouseClicked

    private void buttonSetOriginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSetOriginMouseClicked
        // TODO add your handling code here:
         setOrigin();
         System.out.println(curNodeXYCords[0]+" "+curNodeXYCords[1]);
    }//GEN-LAST:event_buttonSetOriginMouseClicked

    private void buttonSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSearchMouseClicked
        // TODO add your handling code here:
        String searchWrd = textFieldSearch.getText();
        int srchId ;
        try
        {
            srchId = Integer.valueOf(searchWrd);
            if(srchId >= 101 && srchId<=130)
            {
                Node nd = graph.getNode(searchWrd);
                resultTextPane.setText("\n"+nd.getId()+"\n"+nd.getLabel("ui.label")+"");
            }
        }
        catch(NumberFormatException e)
        {
            String label="";
            String id="";
            boolean found = false;
            for(Node node : graph)
            {
               label = String.join(node.getLabel("ui.label"),"");
               if(label.equalsIgnoreCase(searchWrd))
               {
                   id = node.getId();
                   found = true;
                   break;
               }
              
            }
            if(found)
            {
               resultTextPane.setText("\n"+id+"\n"+label+""+"\nLat :"); 
            }
        }
                
    }//GEN-LAST:event_buttonSearchMouseClicked

    
    @Override
	public void viewClosed(String id) {
		loop = false;
	}

        @Override
	public void buttonPushed(String id) {
		System.out.println("Button pushed on node "+id);
	}

        @Override
	public void buttonReleased(String id) {
		System.out.println("Button released on node "+id);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonResetZoom;
    private javax.swing.JButton buttonSearch;
    private javax.swing.JButton buttonSetOrigin;
    private javax.swing.JButton buttonZoomIn;
    private javax.swing.JButton buttonZoomOut;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelSearchNode;
    private javax.swing.JLabel labelZoomControl;
    private javax.swing.JLabel labelZoomLevel;
    private javax.swing.JPanel panelCanvasMenu;
    private javax.swing.JPanel panelDrawCanvas;
    public javax.swing.JTextPane resultTextPane;
    private javax.swing.JTextField textFieldSearch;
    // End of variables declaration//GEN-END:variables
}

class UpdateTimer extends Timer
{
    
    public UpdateTimer(Graph G)
    {
        super(6000, new ActionListener() 
                    {
                        @Override
                                public void actionPerformed(ActionEvent e)
                                {
                                    Random rnd = new Random();
                                    double lvl = 0.0;
                                    for(Edge edg : G.getEachEdge())
                                    {
                                        //double speedMax = edg.getNumber("speedMax") / 130.0;
                                        //edg.setAttribute("ui.color", speedMax);
                                        //float r = rnd.nextFloat();
                                        //lvl+=r;
                                        edg.setAttribute("ui.color",rnd.nextFloat()); 
                                    }
                                    lvl/=30;
                                    //ManagerPanel.
                                    
                                }
                    }
            );
    }  
}

class UpdateEdgeColor extends Timer
{
    public UpdateEdgeColor(Graph G)
    {
        super(3000, new ActionListener() 
                    {
                        Random rnd = new Random();
                        @Override
                                public void actionPerformed(ActionEvent e)
                                {
                                    for(Node nd : G.getEachNode())
                                    {                                           
                                        nd.setAttribute("ui.color", rnd.nextFloat());
                                       
                                    }
                                }
                    }
            );
    }  
}