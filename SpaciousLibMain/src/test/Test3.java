import org.anhcraft.spaciouslib.builders.ArrayBuilder;
import org.anhcraft.spaciouslib.utils.MathUtils;
import org.anhcraft.spaciouslib.utils.RandomUtils;
import org.anhcraft.spaciouslib.utils.TimeUnit;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Test3 {
    public static void main(String[] args){
        System.out.println(Arrays.toString((Object[]) new ArrayBuilder(Object.class)
                .append(3)
                .append(5)
                .append(true)
                .append(7).build()));
        int n = 0;
        while(n < 100) {
            if(MathUtils.isPrime(n)) {
                System.out.print(n);
                System.out.print(' ');
            }
            n++;
        }
        System.out.println();

        TimeUnit unit = TimeUnit.HOUR;
        int i = 0;
        while(i < 3) {
            int value = RandomUtils.randomInt(10, 100000);
            System.out.println("=== "+value+" "+unit.name()+" ===");
            TreeMap<TimeUnit, Integer> a = TimeUnit.format(unit, value, TimeUnit.SECOND, TimeUnit.MILLENNIUM, TimeUnit.MONTH_28, TimeUnit.MONTH_29, TimeUnit.MONTH_30);
            int t = 0;
            for(Map.Entry<TimeUnit, Integer> x : a.entrySet()) {
                t += x.getValue()*x.getKey().getSeconds();
                if(x.getValue() == 0){
                    continue;
                }
                System.out.println(x.getValue() + " " + x.getKey().name() + " (" + x.getKey().getSeconds() + " x " + x.getValue() + ")");
            }
            System.out.println("Recheck: "+ (t == unit.getSeconds()*value ? "SUCCESS" : "FAILED"));
            System.out.println();
            i++;
        }
    }
}
