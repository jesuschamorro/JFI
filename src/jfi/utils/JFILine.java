/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfi.utils;

/**
 *
 * @author Luis Suárez Lloréns
 */
public class JFILine {
    private double a;
    private double b;
    private double c;

    /**
     * Construct an empty line
     */
    public JFILine(){    
    }

    /**
     * Construct the line "a*x + b*y + c =0"
     * 
     * @param a
     * @param b
     * @param c 
     */
    public JFILine(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA(){
        return a;
    }

    public double getB(){
        return b;
    }

    public double getC(){
        return c;
    }

    public void setA(double a){
        this.a = a;
    }

    public void setB(double b){
        this.b = b;
    }

    public void setC(double c){
        this.c = c;
    }
}
