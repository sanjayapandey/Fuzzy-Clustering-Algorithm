package com.und.sanjaya.softcomputing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Hard C-Means Algorithm
 * 
 * @author sanjaya
 *
 */
public class HardCMeansCluster {
	static double[] d1 = new double[178];
	static double[] d2 = new double[178];
	static double[] d3 = new double[178];
	static double[] d4 = new double[178];
	static double[] d5 = new double[178];
	static double[] d6 = new double[178];
	static double[] d7 = new double[178];
	static double[] d8 = new double[178];
	static double[] d9 = new double[178];
	static double[] d10 = new double[178];
	static double[] d11 = new double[178];
	static double[] d12 = new double[178];
	static double[] d13 = new double[178];
	
	static Cluster cluster1 = new Cluster();
	static Cluster cluster2 = new Cluster();
	static Cluster cluster3 = new Cluster();
	
	static List<Point> points1 = new ArrayList<Point>();
	static List<Point> points2 = new ArrayList<Point>();
	static List<Point> points3 = new ArrayList<Point>();
	
	static double squaredError1 = 0;
	static double squaredError2 = 0;
	static double squaredError3 = 0;
	
	
	public static void main(String[] args) {
		String filePath = "/home/sanjaya/Desktop/soft_computing/assignment/wine.xls";
		excelParser(filePath);
	
		for (int i = 0; i < 10; i++) {
			/* Given that, k=3. Hence, Initially, we should pick three random point*/
			Point m1 = getRandomPoint();
			Point m2 = getRandomPoint();
			Point m3 = getRandomPoint();
			
			System.out.println("\n\n------------Initial Random point of Cluster---------------------------------");
			m1.print();
			m2.print();
			m3.print();
			
			createCluster(m1, m2, m3);
			
			//update the mean
			Point updatedM1 = calcMean(points1);
			Point updatedM2 = calcMean(points2);
			Point updatedM3 = calcMean(points3);
		
			do{
				m1 = updatedM1;
				m2 = updatedM2;
				m3 = updatedM3;
				
				createCluster(m1, m2, m3);
				
				//update the mean
				updatedM1 = calcMean(points1);
				updatedM2 = calcMean(points2);
				updatedM3 = calcMean(points3);
			}while(!(isEqual(m1, updatedM1) && isEqual(m2, updatedM2) && isEqual(m3, updatedM3)));
			
			updateSquaredError(updatedM1, updatedM2, updatedM3);
			
			System.out.println("\n------------Final cluster---------------------------------");
			
			cluster1 = new Cluster(points1, updatedM1);
			cluster2 = new Cluster(points2,updatedM2);
			cluster3 = new Cluster(points3, updatedM3);
			System.out.println("===================Cluster-1====================================");
			cluster1.print();
			System.out.println("===================Cluster-2====================================");
			cluster2.print();
			System.out.println("===================Cluster-3====================================");
			cluster3.print();
			System.out.println("\nCenter of Cluster 1: "); calcCenter(points1).print();
			System.out.println("Center of Cluster 2: ");calcCenter(points2).print();
			System.out.println("Center of Cluster 3: ");calcCenter(points3).print();
			
			System.out.println("\n\nTotal square error of Cluster:"+(squaredError1+squaredError2+squaredError3));
			System.out.println("\n");
		}
		
	}
	
	/**
	 * Method to create clusters using hard C-mean algorithm
	 * @param m1 
	 * 	{@link Point} type: the mean of cluster
	 * @param m2
	 * 	{@link Point} type: the mean of cluster
	 * @param m3
	 * 	{@link Point} type: the mean of cluster
	 */
	public static void createCluster(Point m1,Point m2, Point m3){
		/* calculate euclidean distance between current center and point 
		 * Distnace is: (xi-mci)
		 * */
		//flush the previous content
		points1.clear();
		points2.clear();
		points3.clear();
		//Distance with first mean
		for(int i =0;i<178;i++){
			double distance1 = Math.pow((m1.p1-d1[i]),2) + Math.pow((m1.p2-d2[i]),2) +Math.pow((m1.p3-d3[i]),2)+Math.pow((m1.p4-d4[i]),2) +
					          +Math.pow((m1.p5-d5[i]),2) + +Math.pow((m1.p6-d6[i]),2)+Math.pow((m1.p7-d7[i]),2)+Math.pow((m1.p8-d8[i]),2)+
					          +Math.pow((m1.p9-d9[i]),2)+Math.pow((m1.p10-d10[i]),2)+Math.pow((m1.p11-d11[i]),2)+Math.pow((m1.p12-d12[i]),2)
					          +Math.pow((m1.p13-d13[i]),2);
			double distance2 = Math.pow((m2.p1-d1[i]),2) + Math.pow((m2.p2-d2[i]),2) +Math.pow((m2.p3-d3[i]),2)+Math.pow((m2.p4-d4[i]),2) +
			          			+Math.pow((m2.p5-d5[i]),2) + +Math.pow((m2.p6-d6[i]),2)+Math.pow((m2.p7-d7[i]),2)+Math.pow((m2.p8-d8[i]),2)+
			          			+Math.pow((m2.p9-d9[i]),2)+Math.pow((m2.p10-d10[i]),2)+Math.pow((m2.p11-d11[i]),2)+Math.pow((m2.p12-d12[i]),2)
			          			+Math.pow((m2.p13-d13[i]),2);
			double distance3 = Math.pow((m3.p1-d1[i]),2) + Math.pow((m3.p2-d2[i]),2) +Math.pow((m3.p3-d3[i]),2)+Math.pow((m3.p4-d4[i]),2) +
			          +Math.pow((m3.p5-d5[i]),2) + +Math.pow((m3.p6-d6[i]),2)+Math.pow((m3.p7-d7[i]),2)+Math.pow((m3.p8-d8[i]),2)+
			          +Math.pow((m3.p9-d9[i]),2)+Math.pow((m3.p10-d10[i]),2)+Math.pow((m3.p11-d11[i]),2)+Math.pow((m3.p12-d12[i]),2)
			          +Math.pow((m3.p13-d13[i]),2);
			
			//choose the cluster
			if((distance1 <= distance2) && (distance1 <= distance3)){
				//Case:1 First cluster
				points1.add(new Point(i,d1[i], d2[i], d3[i], d4[i],d5[i],d6[i],d7[i],d8[i],d9[i],d10[i],d11[i],d12[i],d13[i]));
			}else if ((distance2 <= distance1) && (distance2 <= distance3)){
				//Case:2 Second cluster
				points2.add(new Point(i,d1[i], d2[i], d3[i], d4[i],d5[i],d6[i],d7[i],d8[i],d9[i],d10[i],d11[i],d12[i],d13[i]));
			}else{
				//Case:3 Third cluster
				points3.add(new Point(i,d1[i], d2[i], d3[i], d4[i],d5[i],d6[i],d7[i],d8[i],d9[i],d10[i],d11[i],d12[i],d13[i]));
			}
		}
	}
	/**
	 * Calculate the mean value
	 * @param points
	 * @return
	 */
	public static Point calcMean(List<Point> points){
		double sumOfDimension1 =0;
		double sumOfDimension2 =0;
		double sumOfDimension3 =0;
		double sumOfDimension4 =0;
		double sumOfDimension5 =0;
		double sumOfDimension6 =0;
		double sumOfDimension7 =0;
		double sumOfDimension8 =0;
		double sumOfDimension9 =0;
		double sumOfDimension10 =0;
		double sumOfDimension11 =0;
		double sumOfDimension12 =0;
		double sumOfDimension13 =0;
		
		for (Point point : points) {
			sumOfDimension1+= point.p1;
			sumOfDimension2+= point.p2;
			sumOfDimension3+= point.p3;
			sumOfDimension4+= point.p4;
			sumOfDimension5+= point.p5;
			sumOfDimension6+= point.p6;
			sumOfDimension7+= point.p7;
			sumOfDimension8+= point.p8;
			sumOfDimension9+= point.p9;
			sumOfDimension10+= point.p10;
			sumOfDimension11+= point.p11;
			sumOfDimension12+= point.p12;
			sumOfDimension13+= point.p13;
			
		}
		int size = points.size();
		//new mean 
		return new Point(0,Math.round(sumOfDimension1/size*100)/100.0d, 
				Math.round(sumOfDimension2/size*100)/100.0d,
				Math.round(sumOfDimension3/size*100)/100.0d, 
				Math.round(sumOfDimension4/size*100)/100.0d,
				Math.round(sumOfDimension5/size*100)/100.0d,
				Math.round(sumOfDimension6/size*100)/100.0d,
				Math.round(sumOfDimension7/size*100)/100.0d,
				Math.round(sumOfDimension8/size*100)/100.0d,
				Math.round(sumOfDimension9/size*100)/100.0d,
				Math.round(sumOfDimension10/size*100)/100.0d,
				Math.round(sumOfDimension11/size*100)/100.0d,
				Math.round(sumOfDimension12/size*100)/100.0d,
				Math.round(sumOfDimension13/size*100)/100.0d);
	}
	
	/**
	 * Sorting the data in ascending order first and find the middle value.
	 * @param points
	 * @return
	 */
	public static Point calcCenter(List<Point> points){
		double[] dimension1 =new double[points.size()];
		double[] dimension2 =new double[points.size()];
		double[] dimension3 =new double[points.size()];
		double[] dimension4 =new double[points.size()];
		double[] dimension5 =new double[points.size()];
		double[] dimension6 =new double[points.size()];
		double[] dimension7 =new double[points.size()];
		double[] dimension8 =new double[points.size()];
		double[] dimension9 =new double[points.size()];
		double[] dimension10 =new double[points.size()];
		double[] dimension11 =new double[points.size()];
		double[] dimension12 =new double[points.size()];
		double[] dimension13 =new double[points.size()];
		
		for(int i=0;i<points.size();i++){
			dimension1[i] = points.get(i).p1;dimension2[i] = points.get(i).p2;
			dimension3[i] = points.get(i).p3;dimension4[i] = points.get(i).p4;
			dimension5[i] = points.get(i).p5;dimension6[i] = points.get(i).p6;
			dimension7[i] = points.get(i).p7;dimension8[i] = points.get(i).p8;
			dimension9[i] = points.get(i).p9;dimension10[i] = points.get(i).p10;
			dimension11[i] = points.get(i).p11;dimension12[i] = points.get(i).p12;
			dimension13[i] = points.get(i).p13;
		}
		//sort in ascending order
		Arrays.sort(dimension1);Arrays.sort(dimension2);Arrays.sort(dimension3);Arrays.sort(dimension4);
		Arrays.sort(dimension5);Arrays.sort(dimension6);Arrays.sort(dimension7);Arrays.sort(dimension8);
		Arrays.sort(dimension9);Arrays.sort(dimension10);Arrays.sort(dimension11);Arrays.sort(dimension12);
		Arrays.sort(dimension13);
		/*calculation of center
		 * 
		 * center = (upperValue + lower value)/2
		 */
		return new Point(0,(dimension1[points.size()-1]+dimension1[0])/2,(dimension2[points.size()-1]+dimension2[0])/2,
				(dimension3[points.size()-1]+dimension3[0])/2, (dimension4[points.size()-1]+dimension4[0])/2,(dimension5[points.size()-1]+dimension5[0])/2,
				(dimension6[points.size()-1]+dimension6[0])/2, (dimension7[points.size()-1]+dimension7[0])/2, (dimension8[points.size()-1]+dimension8[0])/2
				,(dimension9[points.size()-1]+dimension9[0])/2 , (dimension10[points.size()-1]+dimension10[0])/2, (dimension11[points.size()-1]+dimension11[0])/2
				, (dimension12[points.size()-1]+dimension12[0])/2, (dimension13[points.size()-1]+dimension13[0])/2);
	}	
	
	public static double getRandom(double[] array) {
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}

	/**
	 * Method to parse the given excell into array of double.
	 *@param{ String filePath}
	 **/
	public static void excelParser(String filePath){
		File inputWorkbook = new File(filePath);
        Workbook w;
        try 
        {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            
            int count = 0;
            for (int i = 1; i < sheet.getRows() && count<178; i++) 
            {	// loop over att1
                Cell cell1 = sheet.getCell(1, i);
                d1[count] = Double.valueOf(cell1.getContents());
                // loop over att2
                Cell cell2 = sheet.getCell(2, i);
                d2[count] = Double.valueOf(cell2.getContents());
                // loop over att3
                Cell cell3 = sheet.getCell(3, i);
                d3[count] = Double.valueOf(cell3.getContents());
                // loop over att4
                Cell cell4 = sheet.getCell(4, i);
                d4[count] = Double.valueOf(cell4.getContents());
                // loop over att5
                Cell cell5 = sheet.getCell(5, i);
                d5[count] = Double.valueOf(cell5.getContents());
                // loop over att6
                Cell cell6 = sheet.getCell(6, i);
                d6[count] = Double.valueOf(cell6.getContents());
                // loop over att7
                Cell cell7 = sheet.getCell(7, i);
                d7[count] = Double.valueOf(cell7.getContents());
                // loop over att8
                Cell cell8 = sheet.getCell(8, i);
                d8[count] = Double.valueOf(cell8.getContents());
                // loop over att9
                Cell cell9 = sheet.getCell(9, i);
                d9[count] = Double.valueOf(cell9.getContents());
                // loop over att10
                Cell cell10 = sheet.getCell(10, i);
                d10[count] = Double.valueOf(cell10.getContents());
                // loop over att11
                Cell cell11 = sheet.getCell(11, i);
                d11[count] = Double.valueOf(cell11.getContents());
                // loop over att12
                Cell cell12 = sheet.getCell(12, i);
                d12[count] = Double.valueOf(cell12.getContents());
                // loop over att11
                Cell cell13 = sheet.getCell(13, i);
                d13[count] = Double.valueOf(cell13.getContents());
                count ++;
            }
        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * To check if two points are equals or not.
	 * 
	 * @param point1
	 * @param point2
	 * @return <code>true</code> if two points are equal
	 */
	public static boolean isEqual(Point point1, Point point2){
		return (point1.p1 == point2.p1) && (point1.p2 == point2.p2) && (point1.p3 == point2.p3) && (point1.p4 == point2.p4)
				&& (point1.p5 == point2.p5) && (point1.p6 == point2.p6) && (point1.p7 == point2.p7) && (point1.p8 == point2.p8)
				&& (point1.p9 == point2.p9) && (point1.p10 == point2.p10) && (point1.p11 == point2.p11) && (point1.p12 == point2.p12)&& (point1.p13 == point2.p13) ;
	}
	public static Point getRandomPoint() {
		int randNum = new Random().nextInt(178);
		return new Point(randNum, d1[randNum], d2[randNum],d3[randNum],d4[randNum],d5[randNum],d6[randNum],d7[randNum],
				d8[randNum],d9[randNum],d10[randNum],d11[randNum],d12[randNum],d13[randNum]);
	}
	
	public static void updateSquaredError(Point m1,Point m2, Point m3){
		squaredError1=0;
		squaredError2=0;
		squaredError3=0;
		
		for(Point point:points1){
			squaredError1+= Math.pow((m1.p1-point.p1),2) + Math.pow((m1.p2-point.p2),2) +Math.pow((m1.p3-point.p3),2)+Math.pow((m1.p4-point.p4),2)+
							Math.pow((m1.p5-point.p5),2) + Math.pow((m1.p6-point.p6),2) +Math.pow((m1.p7-point.p7),2)+Math.pow((m1.p8-point.p8),2) +
							Math.pow((m1.p9-point.p9),2) + Math.pow((m1.p10-point.p10),2) +Math.pow((m1.p11-point.p11),2)+Math.pow((m1.p12-point.p12),2) 
							+ Math.pow((m1.p13 - point.p13), 2);
		}
		for(Point point:points2){
			squaredError2+= Math.pow((m2.p1-point.p1),2) + Math.pow((m2.p2-point.p2),2) +Math.pow((m2.p3-point.p3),2)+Math.pow((m2.p4-point.p4),2)+
					Math.pow((m2.p5-point.p5),2) + Math.pow((m2.p6-point.p6),2) +Math.pow((m2.p7-point.p7),2)+Math.pow((m2.p8-point.p8),2) +
					Math.pow((m2.p9-point.p9),2) + Math.pow((m2.p10-point.p10),2) +Math.pow((m2.p11-point.p11),2)+Math.pow((m2.p12-point.p12),2) 
					+ Math.pow((m2.p13 - point.p13), 2);
		}
		for(Point point:points3){
			squaredError3+= Math.pow((m3.p1-point.p1),2) + Math.pow((m3.p2-point.p2),2) +Math.pow((m3.p3-point.p3),2)+Math.pow((m3.p4-point.p4),2)+
					Math.pow((m3.p5-point.p5),2) + Math.pow((m3.p6-point.p6),2) +Math.pow((m3.p7-point.p7),2)+Math.pow((m3.p8-point.p8),2) +
					Math.pow((m3.p9-point.p9),2) + Math.pow((m3.p10-point.p10),2) +Math.pow((m3.p11-point.p11),2)+Math.pow((m3.p12-point.p12),2) 
					+ Math.pow((m3.p13 - point.p13), 2);
		}
	}
	
	
}