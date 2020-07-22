package pre_requisites;

import java.util.Scanner;
import java.util.function.Function;

public class ZeroPoint extends MathOperations {
    private String method;
    private double s_p;
    private Differentiation diff;
    private double thres = 1E-4;


    public ZeroPoint(String Method,Function<Number,Double> func){
        this.func = func;
        s_p = 0.0;
        this.method = Method;
        diff = new Differentiation("forward difference",func);
    }
    public ZeroPoint(String Method,Function<Number,Double> func,double s_p){
        this.func = func;
        this.s_p = s_p;
        this.method = Method;
        diff = new Differentiation("forward difference",func);
    }
    public ZeroPoint(String Method,double s_p){
        this.s_p = s_p;
        this.method = Method;
        diff = new Differentiation("forward difference");
    }
    public void setFunc(Function<Number,Double> func){
        super.setFunc(func);
        diff.setFunc(func);
    }
    public void setThres(double t){thres = t;}
    public double solve(){
        double x = 0.0;

        if(method.equalsIgnoreCase("Bisection")) x = Bisection();
        else if(method.equalsIgnoreCase("Newton Raphson")) x = Newton_Raphson();
        else if(method. equalsIgnoreCase("Principal falsi")) x = Principal_false();

        if(Math.abs(x) < 1E-4)
            x = 0.0;
        return x;
    }

    private double Bisection() {
        double a = s_p;
        System.out.println("Enter the next part of the interval:");
        double b = new Scanner(System.in).nextDouble();

        /*if(func.apply(a)/func.apply(b) > 0) {
            System.out.println("The given interval ends are not of opposite signs. Method failure imminent");
            return Double.MIN_VALUE;
        }*/
        double x = 0.0;
        double err = Double.MAX_VALUE;
        int iter = 0;
        while(err > thres && iter < 1000){
            x = (a+b)/2;
            if(func.apply(x) > 0) a = x;
            else if(func.apply(x) == 0) return x;
            else b = x;
            err = Math.abs(func.apply(x));
            iter++;
        }
        return x;
    }
    private double Newton_Raphson(){
        double x = s_p;
        int iter = 0;

        while(func.apply(x) > thres || iter < 1000){
            x -= (func.apply(x)/diff.derivative(1,x));
            iter ++;
        }
        return x;
    }
    private double Principal_false(){
        double x = s_p;
        return x;
    }
}
