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
import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;

/**
 *
 * @author Visakh Venugopal
 */
public class MouseMan implements org.graphstream.ui.view.util.MouseManager
{
    Graph graph;
    View view;
    protected GraphicElement curElement;
    
    MouseMan(Graph graph, View view)
    {
        this.graph = graph;
        this.view = view;
    }

    @Override
    public void init(GraphicGraph graph, View view) 
    {
        
    }

    @Override
    public void release()
    {
    
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
    
    }

    @Override
    public void mousePressed(MouseEvent me)
    {
       curElement = view.findNodeOrSpriteAt(me.getX(), me.getY());
        if(curElement != null)
        {
            mouseButtonPressOnElement(curElement, me);
        } 
    }

    @Override
    public void mouseReleased(MouseEvent me) 
    {
    
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
    
    }

    @Override
    public void mouseExited(MouseEvent me) 
    {
    
    }

    @Override
    public void mouseDragged(MouseEvent me)
    {
    
    }

    @Override
    public void mouseMoved(MouseEvent me)
    {
        
    }
    
    
    //--------------utility functions--------------
    
    private void mouseButtonPressOnElement(GraphicElement curElement, MouseEvent me) 
    {
        //if(me.getButton() == 3){}
        System.out.println("Clicked on :"+curElement.label);
        //textPane.setText("\n Node: "+curElement.label);
        
    }
}
