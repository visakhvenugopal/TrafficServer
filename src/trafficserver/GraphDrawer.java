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

import org.graphstream.ui.view.ViewerListener;

/**
 *
 * @author Visakh Venugopal
 */
public class GraphDrawer implements ViewerListener
{
    
    
    protected boolean loop = true;
    

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
}
