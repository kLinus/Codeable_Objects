/*
 * Codeable Objects by Jennifer Jacobs is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * Based on a work at hero-worship.com/portfolio/codeable-objects.
 *
 * This file is part of the Codeable Objects Framework.
 *
 *     Codeable Objects is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Codeable Objects is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Codeable Objects.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ui;

import java.awt.event.MouseEvent;
import java.util.Vector;

import processing.core.PApplet;

import com.primitive2d.*;
public class ScreenManager {

	public static Vector<LineCollection> drawables = new Vector<LineCollection>();
	public static boolean drawBounding= false;
	public static PApplet parent;
	
	
	public ScreenManager(PApplet parent){
		this.parent = parent;
		this.parent.registerDraw(this);
	     this.parent.registerMouseEvent(this);
	}
	
	public static void addtoScreen(LineCollection lc){
		drawables.add(lc);
	}
	
	public static void removeFromScreen(LineCollection lc){
		drawables.remove(lc);
	}
	
	public void draw() {
	
		for(int i=0;i<drawables.size();i++){
			drawables.get(i).draw(parent, 1);
			drawables.get(i).drawOrigin(parent);
			drawables.get(i).drawBoundingBox(parent);
			if(drawables.get(i).selected){
				drawables.get(i).drawSliders();
			}
		}
				
	}
	
	public void mouseEvent(MouseEvent event) {

		 int x = event.getX();
	        int y = event.getY();

	        switch (event.getID()) {
	            case MouseEvent.MOUSE_PRESSED:
	                this.mousePressed(x,y);
	                break;
	            case MouseEvent.MOUSE_RELEASED:
	            	this.mouseReleased(x,y);
	                break;
	            case MouseEvent.MOUSE_CLICKED:
	            	this.mouseClicked(x,y);
	                break;
	            case MouseEvent.MOUSE_DRAGGED:
	                this.mouseDragged(x,y);

	                break;
	            case MouseEvent.MOUSE_MOVED:
	                // umm... forgot
	                break;
	        }
		
	}

	private void mouseClicked(int x, int y) {
		
		
	}

	private void mouseReleased(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	private void mouseDragged(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	private void mousePressed(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void print() {
		// TODO Auto-generated method stub
		
	}
	
	

}
