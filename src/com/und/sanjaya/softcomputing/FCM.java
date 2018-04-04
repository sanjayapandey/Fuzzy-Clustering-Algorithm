package com.und.sanjaya.softcomputing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * This class consists of 
 * 1. FCM
 * 2. PCM
 * 3. GK-FCM
 * 
 * @author sanjaya
 *
 */
public class FCM {
	/*
	 * Define global variable
	 */
	static final int DATA_SIZE=178;
	static final int CLUSTER_SIZE=3;
	static final int DATA_DIMENSION=13;
	static Double[][] DATA  = new Double[DATA_SIZE][DATA_DIMENSION];
	static Double MEMBERSHIP_MATRIX[][] = new Double[DATA_SIZE][DATA_DIMENSION];
	static final Double CLUSTER_CENTER[][] = new Double[CLUSTER_SIZE][DATA_DIMENSION];
	static final Double PENALTY_TERMS[] = new Double[CLUSTER_SIZE];
	static final Double COV_MATRIX[][] = new Double[DATA_DIMENSION][DATA_DIMENSION];
	static final double ERROR=0.005;
	static final double FUZZINESS = 2.0;
	
	static Cluster cluster1 = new Cluster();
	static Cluster cluster2 = new Cluster();
	static Cluster cluster3 = new Cluster();
	
	static List<Point> points1 = new ArrayList<Point>();
	static List<Point> points2 = new ArrayList<Point>();
	static List<Point> points3 = new ArrayList<Point>();
	
	
	
	/*
	 * Step-1: Initialization of membership matrix
	 */
	
	public static void initializeMembershipMatix(){
		for(int i=0;i<DATA_SIZE;i++) {
			//for cluster-1
			MEMBERSHIP_MATRIX[i][0] = 0.1;
			//for cluster-2
			MEMBERSHIP_MATRIX[i][1] = 0.3;
			//for cluster-3
			MEMBERSHIP_MATRIX[i][2] = 0.7;
		}
	}
	
	/*
	 * Step-2: Calculation of cluster center vector
	 */
	public static void calculateCenterVector() {
		double numerator, denominator;
		/*
		 * Compute the powers of degree-of-membership
		 */
		Double t[][] = new Double[DATA_SIZE][CLUSTER_SIZE];
		for(int i=0;i<DATA_SIZE;i++) {
			for(int j=0;j<CLUSTER_SIZE;j++) {
				t[i][j] = Math.pow(MEMBERSHIP_MATRIX[i][j], FUZZINESS);
			}
		}
		/*
		 * Calculate the central vector
		 */
		for(int j=0;j<CLUSTER_SIZE;j++) {
			for(int k=0;k<DATA_DIMENSION;k++) {
				numerator = 0;
				denominator=0;
				for(int i=0;i<DATA_SIZE;i++) {
					numerator+= t[i][j] * DATA[i][k];
					denominator+= t[i][j];
				}
				CLUSTER_CENTER[j][k] = numerator/denominator;
			}
		}
	}
	
	/*
	 * Step-3: Calculation of norm
	 */
	
	public static double getNorm(int i, int j) {
		
		double sum=0.0;
		for(int k=0;k<DATA_DIMENSION;k++) {
			sum+= Math.pow(DATA[i][k] - CLUSTER_CENTER[j][k],2);
		}
		return Math.sqrt(sum);
	}
	
	/*
	 * Step-4: Update degree of membership value
	 */
	
	public static double getNewMembership(int i,int j) {
		double t, p, sum;
		sum=0.0;
		p=2.0/(FUZZINESS-1);
		for(int k=0;k<CLUSTER_SIZE;k++) {
			if(getNorm(i, k)!=0) {
				t = getNorm(i, j)/getNorm(i, k);
				t = Math.pow(t,p);
				sum+=t;
			}
		}
		if(sum==0) return 0;
		return 1.0/sum;
	}
	
	public static double updateDegreeOfMembership() {
		double newUij=0.0;
		double maxDiff = 0.0, diff;
		for(int j=0;j<CLUSTER_SIZE;j++) {
			for(int i=0;i<DATA_SIZE;i++) {
				newUij = getNewMembership(i, j);
				diff = newUij - MEMBERSHIP_MATRIX[i][j];
				if(diff>maxDiff) maxDiff = diff;
				MEMBERSHIP_MATRIX[i][j] = newUij;
			}
		}
		return maxDiff;
	}
	
	
	/**
	 * ================================================================================
	 * Possibilistic Fuzzy c-means algorithm (PCM)
	 * ================================================================================
	 */
	
	/*
	 * Step-2: Calculation of penalty terms
	 */
	public static void calculatePenaltyTerm() {
		double numerator, denominator;
		/*
		 * Compute the powers of degree-of-membership
		 */
		Double t[][] = new Double[DATA_SIZE][CLUSTER_SIZE];
		for(int i=0;i<DATA_SIZE;i++) {
			for(int j=0;j<CLUSTER_SIZE;j++) {
				t[i][j] = Math.pow(MEMBERSHIP_MATRIX[i][j], FUZZINESS);
			}
		}
		/*
		 * Calculate the penalty terms for each cluster
		 */
		for(int j=0;j<CLUSTER_SIZE;j++) {
			numerator = 0;
			denominator=0;
			for(int i=0;i<DATA_SIZE;i++) {
				numerator+= t[i][j] * getNorm(i, j)*getNorm(i, j);
				denominator+= t[i][j];
			}
			PENALTY_TERMS[j] = numerator/denominator;
		}
	}
	/*
	 * Step-3: Update degree of membership value
	 */
	public static double updateDegreeOfMembershipPCM() {
		double newUij=0.0, t, p, sum;
		double maxDiff = 0.0, diff;
		p=1.0/(FUZZINESS-1);
		for(int j=0;j<CLUSTER_SIZE;j++) {
			for(int i=0;i<DATA_SIZE;i++) {
				t = Math.pow(getNorm(i, j), 2)/PENALTY_TERMS[j];
				sum = 1.0 + Math.pow(t, p);
				newUij = 1.0/sum;
				diff = newUij - MEMBERSHIP_MATRIX[i][j];
				if(diff>maxDiff) maxDiff = diff;
				MEMBERSHIP_MATRIX[i][j] = newUij;
			}
		}
		return maxDiff;
	}

	/*
	 * ==========================================================================================
	 * FCM with Gustafson-kessel algorithm
	 * ==========================================================================================
	 */

	/**
	 * Get the difference vector of data i with cluster center j
	 * @param i
	 * @param j
	 * @return
	 */
	public static double[] getDiffVector(int i, int j){
		double[] diffVector = new double[DATA_DIMENSION];
		for(int k=0;k<DATA_DIMENSION;k++) {
			diffVector[k]= DATA[i][k] - CLUSTER_CENTER[j][k];
		}
		return diffVector;
	}
	/**
	 * Calculate the product of (Xi-Cj)*Transpose-of(Xi-Cj)
	 * @param i
	 * @param j
	 * @return
	 */
	public static double[][] getProduct(int i, int j) {
		double[][] productMatrix = new double[DATA_DIMENSION][DATA_DIMENSION];
		
		double[] diffVector = getDiffVector(i, j);
		for(int k=0;k<DATA_DIMENSION;k++) {
			for(int l=0;l<DATA_DIMENSION;l++) {
				productMatrix[k][l] = diffVector[k]*diffVector[l];
			}
		}
		return productMatrix;
	}
	
	/**
	 * Compute the covariance matrix of cluster k
	 * @param k
	 * @return
	 */
	public static double[][] calculateCovMatrix(int k) {
		double denominator;
		double[][] covMatrix = new double[DATA_DIMENSION][DATA_DIMENSION];
		
		//Compute the powers of degree-of-membership
		Double t[][] = new Double[DATA_SIZE][CLUSTER_SIZE];
		for(int i=0;i<DATA_SIZE;i++) {
			for(int j=0;j<CLUSTER_SIZE;j++) {
				t[i][j] = Math.pow(MEMBERSHIP_MATRIX[i][j], FUZZINESS);
			}
		}
		//Calculate the fuzzy covariance matrix*
		denominator=0;
		for(int i=0;i<DATA_SIZE;i++) {
			double[][] productMatrix = getProduct(i, k);
			//multiply t[i][j] with product matrix
			for(int p=0;p<DATA_DIMENSION;p++) {
				for(int q=0;q<DATA_DIMENSION;q++) {
					covMatrix[p][q]+=t[i][k] * productMatrix[p][q];
					
				}
			}
			denominator+= t[i][k];
		}

		//covariance matrix* 
		for(int p=0;p<DATA_DIMENSION;p++) {
			for(int q=0;q<DATA_DIMENSION;q++) {
				covMatrix[p][q]=covMatrix[p][q]/denominator;
				
			}
		}
	
		return covMatrix;
	}
	/**
	 * Calculate the inverse matrix
	 * @param matrix
	 * @return
	 */
	public double[][] calcInverseMatrix(double[][] matrix){
		try{
		RealMatrix mx = MatrixUtils.createRealMatrix(matrix);
		RealMatrix Rinv = new LUDecomposition(mx).getSolver().getInverse();
		return Rinv.getData();
		}catch (Exception e){
			return new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		}
}
	
	/**
	 * Calculation of distance using Gustafson-kessel ,  returns the distance between ith data point to jth cluster center.
	 * @param i
	 * @param j
	 * @return
	 */
	public static double getGKDistance(int i, int j) {
		double sum=0.0;
		double[][] covMatrix = calculateCovMatrix(j);
		double[] diffVector = getDiffVector(i, j);
		double[] newMatrix = new double[DATA_DIMENSION];
		for(int k=0;k<DATA_DIMENSION;k++) {
			for(int l=0;l<DATA_DIMENSION;l++) {
				newMatrix[k]+= diffVector[k]*covMatrix[l][k];
			}
			sum+= newMatrix[k]*diffVector[k];
		}
		return sum;
	}
	public static double getNewMembershipGK(int i,int j) {
		double t, p, sum;
		sum=0.0;
		p=1.0/(FUZZINESS-1);
		for(int k=0;k<CLUSTER_SIZE;k++) {
			if(getGKDistance(i, k)!=0) {
				t = getGKDistance(i, j)/getGKDistance(i, k);
				t = Math.pow(t,p);
				sum+=t;
			}
		}
		if(sum==0) return 0;
		return 1.0/sum;
	}
	public static double updateDegreeOfMembershipGK() {
		double newUij=0.0;
		double maxDiff = 0.0, diff;
		for(int j=0;j<CLUSTER_SIZE;j++) {
			for(int i=0;i<DATA_SIZE;i++) {
				newUij = getNewMembershipGK(i, j);
				diff = newUij - MEMBERSHIP_MATRIX[i][j];
				if(diff>maxDiff) maxDiff = diff;
				MEMBERSHIP_MATRIX[i][j] = newUij;
			}
		}
		return maxDiff;
	}
		
	
	/**
	 *==========================================================================================
	 *Excel file extraction
	 *==========================================================================================
	 * Method to parse the given excel into array of double.
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
                DATA[i-1][0] = Double.valueOf(cell1.getContents());
                // loop over att2
                Cell cell2 = sheet.getCell(2, i);
                DATA[i-1][1] = Double.valueOf(cell2.getContents());
                // loop over att3
                Cell cell3 = sheet.getCell(3, i);
                DATA[i-1][2] = Double.valueOf(cell3.getContents());
                // loop over att4
                Cell cell4 = sheet.getCell(4, i);
                DATA[i-1][3] = Double.valueOf(cell4.getContents());
                // loop over att5
                Cell cell5 = sheet.getCell(5, i);
                DATA[i-1][4] = Double.valueOf(cell5.getContents());
                // loop over att6
                Cell cell6 = sheet.getCell(6, i);
                DATA[i-1][5] = Double.valueOf(cell6.getContents());
                // loop over att7
                Cell cell7 = sheet.getCell(7, i);
                DATA[i-1][6] = Double.valueOf(cell7.getContents());
                // loop over att8
                Cell cell8 = sheet.getCell(8, i);
                DATA[i-1][7]= Double.valueOf(cell8.getContents());
                // loop over att9
                Cell cell9 = sheet.getCell(9, i);
                DATA[i-1][8] = Double.valueOf(cell9.getContents());
                // loop over att10
                Cell cell10 = sheet.getCell(10, i);
                DATA[i-1][9] = Double.valueOf(cell10.getContents());
                // loop over att11
                Cell cell11 = sheet.getCell(11, i);
                DATA[i-1][10]= Double.valueOf(cell11.getContents());
                // loop over att12
                Cell cell12 = sheet.getCell(12, i);
                DATA[i-1][11] = Double.valueOf(cell12.getContents());
                // loop over att11
                Cell cell13 = sheet.getCell(13, i);
                DATA[i-1][12] = Double.valueOf(cell13.getContents());
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
	
	public static void createCluster() {
		// flush the previous content
		points1.clear();
		points2.clear();
		points3.clear();
		for(int i=0;i<DATA_SIZE;i++) {
			//for cluster-1
			if((MEMBERSHIP_MATRIX[i][0] > MEMBERSHIP_MATRIX[i][1]) && (MEMBERSHIP_MATRIX[i][0] > MEMBERSHIP_MATRIX[i][2])) {
				points1.add(new Point(i, DATA[i]));
			}else if(MEMBERSHIP_MATRIX[i][1] > MEMBERSHIP_MATRIX[i][2]) {
				points2.add(new Point(i, DATA[i]));
			}else {
				points3.add(new Point(i, DATA[i]));
			}
		}
		
		System.out.println("==========================Print Cluster======================================");
		
		cluster1 = new Cluster(points1, new Point(0, CLUSTER_CENTER[0]));
		cluster2 = new Cluster(points2,new Point(0, CLUSTER_CENTER[1]));
		cluster3 = new Cluster(points3, new Point(0, CLUSTER_CENTER[2]));
		System.out.println("============================Cluster-1===========================");
		cluster1.print();
		System.out.println("============================Cluster-2===========================");
		cluster2.print();
		System.out.println("============================Cluster-3============================");
		cluster3.print();

		System.out.println("============================Center of cluster-1==================");
		for(int i=0;i<DATA_DIMENSION;i++) {
			System.out.print(CLUSTER_CENTER[0][i]+", ");
		}
		System.out.println("============================Center of cluster-2==================");
		for(int i=0;i<DATA_DIMENSION;i++) {
			System.out.print(CLUSTER_CENTER[1][i]+", ");
		}
		System.out.println("============================Center of cluster-3==================");
		for(int i=0;i<DATA_DIMENSION;i++) {
			System.out.print(CLUSTER_CENTER[2][i]+", ");
		}
	}
	
	public static void main(String[] args) {
		
		String filePath = "/home/sanjaya/Desktop/soft_computing/assignment/wine.xls";
		excelParser(filePath);
		/**
		 * FCM
		 */
		initializeMembershipMatix();
		int num = 20;
		CLUSTER_CENTER[0] = DATA[45];
		CLUSTER_CENTER[1] = DATA[165];
		CLUSTER_CENTER[2] = DATA[151];
		double maxDiff;
		int FCMIteration=0;
		do {
			calculateCenterVector();
			maxDiff = updateDegreeOfMembership();
			FCMIteration++;
		}while(maxDiff>ERROR);
		for(int i=0;i<DATA_SIZE;i++) {
			//for cluster-1
			System.out.println(MEMBERSHIP_MATRIX[i][0]+", "+ MEMBERSHIP_MATRIX[i][1] + ", " + MEMBERSHIP_MATRIX[i][2]);
		}
		createCluster();
		System.out.println("FCMIteration -------------------------"+FCMIteration);
		/**
		 * PCM
		 */
		
		System.out.println("\n\n=============================PCM========================================\n\n");
		maxDiff=0;
		do {
			calculateCenterVector();
			calculatePenaltyTerm();
			maxDiff = updateDegreeOfMembershipPCM();
			
		}while(maxDiff>ERROR);
		for(int i=0;i<DATA_SIZE;i++) {
			//for cluster-1
			System.out.println(MEMBERSHIP_MATRIX[i][0]+", "+ MEMBERSHIP_MATRIX[i][1] + ", " + MEMBERSHIP_MATRIX[i][2]);
			
		}
		createCluster();
		/**
		 * Gustafson-kessel (GK-FCM) algorithm
		 */
		System.out.println("\n\n=============================GK-FCM========================================\n\n");
		
		maxDiff=0.0;
		int GKIteration = 0;
		do {
			calculateCenterVector();
			maxDiff = updateDegreeOfMembershipGK();
			GKIteration++;
		}while(maxDiff>ERROR);
		for(int i=0;i<DATA_SIZE;i++) {
			System.out.println(MEMBERSHIP_MATRIX[i][0]+", "+ MEMBERSHIP_MATRIX[i][1] + ", " + MEMBERSHIP_MATRIX[i][2]);
			
		}
		createCluster();
		System.out.println("GKIteration------------"+GKIteration);
		
		
		// calculation of radius of axis
		for(int i=0;i<CLUSTER_SIZE;i++) {
			//get the covariance matrix
			double[][] covMatrix = calculateCovMatrix(i);
			//calculate the eigen vector
			Matrix A = new Matrix(covMatrix); 
			// compute the spectral decomposition
		    EigenvalueDecomposition e = A.eig();
		    Matrix V = e.getV();
		    Matrix D = e.getD();
		    //TODO
		}
		
	}
}
