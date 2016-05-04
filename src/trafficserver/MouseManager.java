/*
 * Copyright (C) 2016 Visakh Venugopal
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package trafficserver;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import org.graphstream.graph.Graph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

/**
 *
 * @author Visakh Venugopal
 */
public class MouseManager implements MouseInputListener 
{

    protected View view;
    protected Viewer viewer;
    protected Graph graph;
    protected GraphicElement curElement;
    private Canvas parent = null ;
    private double[] curNodeXYCords;
    
    public MouseManager(Graph graph, View view,Viewer viewer,double[] curNodeXYCords,Canvas parent) 
    {
 	this.view = view;
        this.viewer = viewer;
 	this.graph = graph;
        this.parent = parent ;
        this.curNodeXYCords = curNodeXYCords;
        
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        }

    @Override
    public void mousePressed(MouseEvent me) 
    {
        curElement = view.findNodeOrSpriteAt(me.getX(), me.getY());
        if(curElement != null)
        {
            mouseButtonPressOnElement(curElement, me);
        }
        else
        {
            Point3 pixels = viewer.getDefaultView().getCamera().transformGuToPx(me.getX(), me.getY(), 0);
            System.out.printf("\n(%.3f;%.3f)",pixels.x, pixels.y);
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        }

    @Override
    public void mouseEntered(MouseEvent me) {
        }

    @Override
    public void mouseExited(MouseEvent me) {
        }

    @Override
    public void mouseDragged(MouseEvent me) 
    {
        view.selectionGrowsAt(me.getX(), me.getY());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        }

    private void mouseButtonPressOnElement(GraphicElement curElement, MouseEvent me) 
    {
        //if(me.getButton() == 3){}
        System.out.println("Clicked on :"+curElement.getId()+":"+curElement.label);
        Object [] array = curElement.getAttribute("xyz");
        String x = array[0].toString();
        String y = array[1].toString();
        System.out.println(x+":"+y);
        curNodeXYCords[0] = Double.parseDouble(x);
        curNodeXYCords[1] = Double.parseDouble(y);
        parent.resultTextPane.setText("\n"+curElement.getId()+"\n"+curElement.label+"\nLat : "+x+"\nLon : "+y);
    }
    
}
