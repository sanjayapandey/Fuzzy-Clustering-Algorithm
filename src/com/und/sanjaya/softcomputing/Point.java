package com.und.sanjaya.softcomputing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author sanjaya
 *
 */
public class Point {
	public int id;
	public double p1;
	public double p2;
	public double p3;
	public double p4;
	public double p5;
	public double p6;
	public double p7;
	public double p8;
	public double p9;
	public double p10;
	public double p11;
	public double p12;
	public double p13;
	
	public Point() {
	}
	public Point(int id, double p1, double p2, double p3, double p4, double p5, double p6, double p7
			, double p8, double p9, double p10, double p11, double p12, double p13) {
		this.id = id;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.p5 = p5;
		this.p6 = p6;
		this.p7 = p7;
		this.p8 = p8;
		this.p9 = p9;
		this.p10 = p10;
		this.p11 = p11;
		this.p12 = p12;
		this.p13 = p13;
		
	}
	public Point(int id, Double[] vertex) {
		this.id = id;
		this.p1 = vertex[0];
		this.p2 = vertex[1];
		this.p3 = vertex[2];
		this.p4 = vertex[3];
		this.p5 = vertex[4];
		this.p6 = vertex[5];
		this.p7 = vertex[6];
		this.p8 = vertex[7];
		this.p9 = vertex[8];
		this.p10 = vertex[9];
		this.p11 = vertex[10];
		this.p12 = vertex[11];
		this.p13 = vertex[12];
	}
	public Point(int id, double[] vertex) {
		this.id = id;
		this.p1 = vertex[0];
		this.p2 = vertex[1];
		this.p3 = vertex[2];
		this.p4 = vertex[3];
		this.p5 = vertex[4];
		this.p6 = vertex[5];
		this.p7 = vertex[6];
		this.p8 = vertex[7];
		this.p9 = vertex[8];
		this.p10 = vertex[9];
		this.p11 = vertex[10];
		this.p12 = vertex[11];
		this.p13 = vertex[12];
	}
	public void print(){
		System.out.println(id+"("+p1+","+p2+","+p3+","+p4+","+p5+","+p6+","+p7+","+p8+","+p9+","+p10+","+p11+","+p12+","+p13);
	}
	@Override
	public String toString() {
		return String.valueOf(id);
		//return id+"("+p1+","+p2+","+p3+","+p4+","+p5+","+p6+","+p7+","+p8+","+p9+","+p10+","+p11+","+p12+","+p13+")";
	}

}
	
}
