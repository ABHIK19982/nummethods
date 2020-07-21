package pre_requisites;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.*;

public class Integration extends MathOperations {
     double lower;double upper;


     private long count;

     public Integration(){
         upper = 0.0;lower = 0.0;
     }

     public Integration (Object l,Object u){
         upper = Double.parseDouble(u.toString());
         lower = Double.parseDouble(l.toString());

         computePoints();
     }

     public void getPoints(String str) throws IOException {
         File f;
         PrintWriter pwt;
         f = new File(str);
         boolean b;
         if(!f.exists()) b = f.createNewFile();
         pwt = new PrintWriter(f);
         for(int i = 0;i<count;i++){pwt.println(lower + (interval * i));}
     }

     private void computePoints(){
         count = Math.round((upper - lower)/interval) + 1;
     }

     public void setFunc(Function<Number,Double> func){
         try {
             this.func =  func;
         }
         catch (ClassCastException e){
             System.out.println("Improper type applied in function . Function should be double");
         }
     }

     public double Integrate(String type){
         double sum = 0;

         for(int i = 0;i<count;i++){
             if(type.equalsIgnoreCase("Trapezoidal"))
                  sum += (func.apply(lower + (i * interval))  * 2);
             else if(type.equalsIgnoreCase("simpsons 3/8th"))
                 sum += (i%3 == 0)? func.apply(lower+(i* interval)) * 2 :
                         func.apply(lower + (i*interval)) * 3;
             else if(type.equalsIgnoreCase("simpsons 1/3th"))
                 sum += (i%2 == 0)? func.apply(lower + (i*interval)) * 2 :
                         func.apply(lower + (i*interval)) * 4;
             else if(type.equalsIgnoreCase("Outside rectangular")||
                     type.equalsIgnoreCase("Inside rectangular"))
                 sum += func.apply(lower + (i*interval));
         }
         if(type.equalsIgnoreCase("trapezoidal"))
         {sum -=(func.apply(lower) + func.apply(upper));sum *= (interval/2);}
         else if(type.equalsIgnoreCase("simpsons 3/8th")) {
             if ((count - 1) % 3 == 0) sum -= (func.apply(lower) + func.apply(upper));
             else sum -= (func.apply(lower) + func.apply(upper) * 2);
             sum *= (3 * interval / 8);
         }
         else if(type.equalsIgnoreCase("simpsons 1/3th")) {
             if ((count - 1) % 2 == 0) sum -= (func.apply(lower) + func.apply(upper));
             else sum -= (func.apply(lower) + (func.apply(upper) * 3));
             sum *= (interval / 3);
         }
         else if(type.equalsIgnoreCase("Outside rectangular")){
             sum-=func.apply(lower);
             sum*=interval;
         }
         else if(type.equalsIgnoreCase("inside rectangular")){
             sum-=func.apply(upper);
             sum *= interval;
         }

         return sum;
     }
}
