package pre_requisites;

import java.util.function.Function;

public class Differentiation extends MathOperations {
    private String method;
    public Differentiation(String method, Function<Number,Double> func){
        this.method = method;
        this.func = func;
        this.interval = 0.0001;
    }
    public double derivative(int order,double x){
        if(order ==0) return x;
        char[] coeffs = Integer.toString((int) Math.pow(11, order)).toCharArray();
        double d = 0;
        if(order == 1){
            d = (func.apply(x + interval) - func.apply(x))/interval;
            return d;
        }
        for(int i = 0;i<=order;i++){
            int pos = Integer.parseInt(String.valueOf(coeffs[order-i]));
            int sign  = (order-i)%2 == 0 ? 1 : -1;
            if(method.equalsIgnoreCase("forward difference"))
                 d += (sign * pos * func.apply(x + i*interval));
            else if(method.equalsIgnoreCase("backward difference"))
                d += (sign * pos * func.apply(x - i*interval));
        }


        d /= Math.pow(interval,order);
        return d;
    }
}
