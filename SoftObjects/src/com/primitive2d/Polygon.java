package com.primitive2d;

import java.util.Collections;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;

import com.datatype.CmpY;
import com.datatype.DCFace;
import com.datatype.Point;
import com.math.Geom;
import com.math.PolyBoolean;
import com.ornament.Pattern;
import com.ornament.Tree;

public class Polygon extends LineCollection{

public boolean fill = false;
private int fR=0;
private int fG=0;
private int fB=0;
	
	public Polygon(){
		super();
		this.r=0;
		this.g=0;
		this.b=255;
		
	}
	
	public Polygon(int sides, double length, double x, double y){
		super();
		double angle = 360.0/(double)sides;
		for(int i=0;i<sides-1;i++){
			  this.forward(length);
			  this.right(angle);
			}
		this.closePoly();
		this.setOriginUpperLeft();
		this.moveTo(x, y);
		this.resetTurtle();
		this.r=0;
		this.g=0;
		this.b=255;
		
	}
	
	
	

	//polygon add point method that automatically links up points into lines
	@Override
	public void addPoint(double x,double y){
		Point point = new Point(x,y);
		
		int numLines = this.getAllLines().size();
		int numPoints = this.getAllPoints().size();
		if(numPoints == 0){
			this.addPoint(point);
		}
		else if(numLines==0){
			Line line = new Line(this.getPointAt(0),point);
			this.addLineWithoutPoints(line);
			this.addPoint(point);
		}
		if(numLines>0){
			Point start = this.getLineAt(numLines-1).end;
			Line line = new Line(start.copy(),point.copy());
			this.addLine(line);
		}
		
		
		
	}
	
	
	
	public void closePoly(){
		int numPoints = this.getAllPoints().size();
		Line line = new Line(getPointAt(numPoints-1).copy(),getPointAt(0).copy());
		this.addLine(line);
	}
	
	
	public void clipPattern(Pattern pattern){
		System.out.println("clipping to shape with "+this.getAllPoints().size()+" verticies");
		this.orderEdges();
		Vector<Line> patternEdges = pattern.getAllLines();
		Vector<Line> patternNewEdges = new Vector<Line>();
		pattern.removeAllPoints();
		
		for(int i=patternEdges.size();i>=0;i--){
			try{
				Line newEdge = PolyBoolean.clipInBorder(patternEdges.get(i),this);
			
			if(newEdge!=null){
				patternNewEdges.add(newEdge);
				pattern.addPoint(newEdge.start);
				pattern.addPoint(newEdge.end);
			}
			}
			catch (Exception e){
				System.out.println("missed edge at:"+i);
				System.out.println(e);
			}
			
		}
		
		pattern.setAllLines(patternNewEdges);
	}
	
	
	/* public void centerOrigin(){
	    	this.orderEdges();
	    	this.origin = Geom.findCentroid(this).copy();
	    }*/
	
	 public void orderEdges(){
	    	
	    	Vector<Line> currentLines = this.getAllLines();
	    	Collections.sort(currentLines);
	    
	    	
	    	//find case based on verticies
	    	
	    	Vector<Point> verticies = Geom.removeDuplicateVerts(this.getAllPoints());
	    	//System.out.println("verticies num="+verticies.size());
	    	//System.out.println("points num="+this.getAllPoints().size());
	    	/*for(int i=0;i<verticies.size();i++){
	    		//System.out.println("vert="+verticies.get(i).getX()+","+verticies.get(i).getY());
	    	}*/
	    	
	    	/*for(int i=0;i<getAllPoints().size();i++){
	    		//System.out.println("point="+getAllPoints().get(i).getX()+","+getAllPoints().get(i).getY());
	    	}*/
	    	
	    	if(currentLines.size()==1){ //only 1 edge
	    		//System.out.println("there is only one edge");
	    		//TODO: handle this case
	    	}
	    	
	    	
	    	else if ((this.getAllPoints().size()/2.0)-verticies.size()<0){ //incomplete shape missing one edge
	    		//System.out.println("missing one edge");
	    		this.closePoly();
	    		this.orderEdges();
	    		return;
	    	}
	    	
	    	else if((this.getAllPoints().size()/2.0)-verticies.size()==0){ //complete shape
	    		//System.out.println("complete shape");
	    		
	    		sortEdges(verticies,currentLines);
	    	}
	    	
	    	else if(verticies.size()-verticies.size()==2){ //two distinct segments
	    		System.out.println("two distinct segments");
	    		//TODO: handle this case
	    	}
	    	
	    }
	 
	 
	 private void sortEdges(Vector<Point> verticies, Vector<Line> currentLines){
	    	Line highestEdge1 = null;
			Line highestEdge2 = null;
			Collections.sort(verticies,new CmpY());
			//System.out.println(verticies.size());
	    	
			for(int i=0;i<currentLines.size();i++){
	    		if (currentLines.get(i).start.compareTo(verticies.get(0))==0||currentLines.get(i).end.compareTo(verticies.get(0))==0){
	    			if(highestEdge1==null){
	    				highestEdge1=currentLines.get(i);
	    			}
	    			else if(highestEdge2==null){
	    				highestEdge2=currentLines.get(i);
	    				break;
	    			}
	    			
	    		}
	    		
	    	}
	    	
	    	
	    	double angle1 = Geom.getHighestAngle(highestEdge1,verticies.get(0));
	    	double angle2 = Geom.getHighestAngle(highestEdge2,verticies.get(0));
	    	
	    	Line selectedEdge;
	    	if(angle1>=angle2){
	    		selectedEdge = highestEdge1;
	    		
	    		
	    	}
	    	else{
	    		selectedEdge = highestEdge2;
	    		
	    	}
	    	Line cleanedEdge;
	    	if(selectedEdge.start.compareTo(verticies.get(0))==0)
	    		cleanedEdge= new Line(verticies.get(0),selectedEdge.end);
	    	else
	    		cleanedEdge= new Line(verticies.get(0),selectedEdge.start);
		
		
			Vector<Line>sortedEdges = new Vector<Line>(0);
			
				
			
			recurseSort(cleanedEdge,selectedEdge,sortedEdges, currentLines);
			
	    }
	    
	 
	 
	 private void recurseSort(Line currentEdge,Line edgeToRemove, Vector<Line>sortedEdges,Vector<Line>currentLines){
	    	Line currentEdgeNew=null;
	    	Line edgeToRemoveNew=null;
	    	currentLines.remove(edgeToRemove);
			sortedEdges.add(currentEdge);
	    	if(currentLines.size()!=0){
	    		
	    		for(int i=0;i<currentLines.size();i++){
	    			Line checkEdge = currentLines.get(i);
	    			if(checkEdge.start.compareTo(currentEdge.end)==0){
	    			//	System.out.println("found edge at start");
	    				currentEdgeNew = checkEdge;
	    				edgeToRemoveNew = checkEdge;
	    				
	    				break;
	    				
	    			}
	    			
	    			if(checkEdge.end.compareTo(currentEdge.end)==0){
	    				//System.out.println("found edge at end");
	    				
	    				currentEdgeNew = new Line(checkEdge.end.copy(),checkEdge.start.copy());
	    				edgeToRemoveNew = checkEdge;
	    				
	    				break;
	    				
	    			}
	    		}
	    		recurseSort(currentEdgeNew,edgeToRemoveNew,sortedEdges,currentLines);
	    	}
	    	else{
	    		this.setAllLines(sortedEdges);
	    		Vector <Point> newPoints = new Vector<Point>();
	    		for(int i=0;i<sortedEdges.size();i++){
	    			newPoints.add(sortedEdges.get(i).start);
	    			newPoints.add(sortedEdges.get(i).end);
	    		}
	    		this.setAllPoints(newPoints);
	    	}
	    	
	    }
	
	  //=============================DRAW AND PRINT METHODS==================================//
	
	
	 @Override 
	public void draw(PApplet parent, float strokeWeight){
		if(fill){
			parent.fill(fR,fG,fB);
		}
		else{
			parent.noFill();
		}
		parent.stroke(r,g,b);
		parent.beginShape();
		this.orderEdges();
		
		for(int i=0;i<this.getAllLines().size();i++ ){
			
			parent.vertex((float)this.getLineAt(i).start.getX(),(float)this.getLineAt(i).start.getY());
		}
		
		
		 parent.endShape(PConstants.CLOSE);
		 parent.noFill();
	 }
	 
	 
	  public void setFillColor(int r, int g, int b){
	    	this.fR=r;
	    	this.fG=g;
	    	this.fB=b;
	    }
	@Override
	public Polygon copy(){
		Polygon poly = new Polygon();
		
		Vector<Line> lines = this.getAllLines();
		for(int i=0;i<lines.size();i++){
			Line line = lines.get(i).copy();
			poly.addLine(line);
		}
		poly.setOrigin(this.origin.copy());
		return poly;
	}
	
	public void addCopy(Curve c){
		Vector<Line> curveLines = c.copyAllLines();
		for(int i=0;i<curveLines.size();i++){
			this.addPoint(curveLines.get(i).start.getX(),curveLines.get(i).start.getY());
		}
		
	}
	
	public void addCopy(Curve c, boolean reverse){
		Vector<Line> curveLines = c.copyAllLines();
		for(int i=curveLines.size()-1;i>=0;i--){
			this.addPoint(curveLines.get(i).start.getX(),curveLines.get(i).start.getY());
		}
		
	}

}


