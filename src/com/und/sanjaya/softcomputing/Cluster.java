package com.und.sanjaya.softcomputing;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author sanjaya
 *
 */
public class Cluster {
	private List<Point> points = new ArrayList<Point>();
	private Point mean = new Point();
	public Cluster() {
	}
	public Cluster(List<Point> points, Point mean) {
		this.points = points;
		this.mean = mean;
	}
	
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	public Point getMean() {
		return mean;
	}
	public void setMean(Point mean) {
		this.mean = mean;
	}
	
	public void print(){
		System.out.println("Points: Size of Cluster is ("+points.size()+"). Points are: \n "+points.toString());
	}	
}
