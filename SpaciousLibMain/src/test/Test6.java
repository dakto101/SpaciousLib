import org.anhcraft.spaciouslib.builders.ArrayBuilder;
import org.anhcraft.spaciouslib.utils.*;

import java.util.Arrays;

public class Test6 {
    public static void main(String[] args){
        System.out.println(CommonUtils.compare(
                new ArrayBuilder(int.class).append(3).build(),
                new ArrayBuilder(int.class).append(3).build()
        ));
        Integer[] x = new Integer[]{0,1,2,3,4,5,6,7,8,9,10};
        Paginator<Integer> p = new Paginator<>(x, 3);
        System.out.println(Arrays.toString(p.get()));
        System.out.println(Arrays.toString(p.next().get()));
        System.out.println(Arrays.toString(p.next().get()));
        System.out.println(Arrays.toString(p.next().get()));
        System.out.println(new Returner<String>() {
            @Override
            public String handle() {
                StringBuilder sb = new StringBuilder();
                Repeater.until(0, 1, new Repeater() {
                    @Override
                    public void run(int current) {
                        sb.append(p.go(current).get()[0]);
                    }

                    @Override
                    public boolean check(int current) {
                        return current > p.max();
                    }
                });
                return sb.toString();
            }
        }.run());
        System.out.println(StringUtils.repeat("-", 5));
        System.out.println(StringUtils.removeNumericChars("ac1547979+0a"));
        System.out.println(StringUtils.removeAlphabetChars("0a5detw1Z59Arrt4t2B6rtZ"));
        System.out.println(StringUtils.reverse("Hello World"));
    }
}
