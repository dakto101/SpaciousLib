import org.anhcraft.spaciouslib.annotations.DataField;
import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.annotations.Serializable;
import org.anhcraft.spaciouslib.utils.Group;
import org.bukkit.Material;

public class Test1 {
    @Serializable
    public static class a {
        @DataField
        public Material a = Material.LAVA;
    }

    public static void main(String[] args){
        a a = new a();

        long write = 0;
        long read = 0;
        long last;

        int i = 0;
        while(i < 5) {
            last = System.currentTimeMillis();
            Group<byte[], String> res = DataSerialization.serialize(Test1.a.class, a);
            long took = System.currentTimeMillis()-last;
            System.out.println("Log: "+res.getB());
            System.out.println("Write ("+i+") = "+took+" ms");
            write = i == 0 ? took : (took+write)/2;
            last = System.currentTimeMillis();
            DataSerialization.deserialize(a.class, res.getA());
            took = System.currentTimeMillis()-last;
            System.out.println("Read ("+i+") = "+took+" ms");
            read = i == 0 ? took : (took+read)/2;
            i++;
        }

        System.out.println("Write => "+write+" ms");
        System.out.println("Read => "+read+" ms");
    }
}
