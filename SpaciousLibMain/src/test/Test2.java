import org.anhcraft.spaciouslib.serialization.DataField;
import org.anhcraft.spaciouslib.serialization.DataSerialization;
import org.anhcraft.spaciouslib.serialization.Serializable;
import org.anhcraft.spaciouslib.utils.*;

import java.util.*;

public class Test2 {
    @Serializable
    public static class a {
        @DataField
        public List<String> a = new ArrayList<>();
        @DataField
        public Set<Integer> b = new HashSet<>();
        @DataField
        public Map<String, Short> c = new HashMap<>();
        @DataField
        public TimedList<Long> d = new TimedList<>();
        @DataField
        public TimedSet<Double> e = new TimedSet<>();
        @DataField
        public TimedMap<UUID, Integer> f = new TimedMap<>();
        @DataField
        public int[] g = new int[]{1, 2, 3};
        @DataField
        public String[] h = new String[]{"abc"};
        @DataField
        public Group<UUID, Cooldown>[] i = new Group[]{
                new Group<>(UUID.randomUUID(), new Cooldown()),
                new Group<>(UUID.randomUUID(), new Cooldown()),
                new Group<>(UUID.randomUUID(), new Cooldown())
        };
        @DataField
        public List<Group<Set<UUID>, Double[]>> j = new ArrayList<>();
        @DataField
        public Map<String[], Group<Byte[], Long>[]> k = new HashMap<>();
        @DataField
        public List<Object> l = new ArrayList<>();
    }

    @Serializable
    public static class b {
        @DataField
        public TimedSet<Double> e = new TimedSet<>();
        @DataField
        public List<String> a = new ArrayList<>();
        @DataField
        public Set<Integer> b = new HashSet<>();
    }

    public static void main(String[] args){
        a a = new a();
        a.a.add("a");
        a.a.add("b");
        a.a.add("c");
        a.b.add(5);
        a.b.add(10);
        a.c.put("d", (short) 50);
        a.d.add(10L, 100);
        a.d.add(100L, 100);
        a.d.add(1000L, 100);
        a.e.add(600d, 100);
        a.f.put(UUID.randomUUID(), 10, 100);
        a.j.add(new Group<>(CommonUtils.toSet(new UUID[]{
                UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID()
        }), new Double[]{0d,1d,2d,3d,4d,5d}));
        a.j.add(new Group<>(CommonUtils.toSet(new UUID[]{
                UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID()
        }), new Double[]{0d,1d,2d,3d,4d,5d}));
        a.j.add(new Group<>(CommonUtils.toSet(new UUID[]{
                UUID.randomUUID(),UUID.randomUUID(),UUID.randomUUID()
        }), new Double[]{0d,1d,2d,3d,4d,5d}));
        a.k.put(new String[]{"abc", "xyz"}, new Group[]{
                new Group(new Byte[]{
                        1, 2, 3
                }, new Long[]{
                        4L, 5L, 6L
                })
        });
        a.l.add("aaa");
        a.l.add(10);
        a.l.add(new Cooldown());

        long write = 0;
        long read = 0;
        long last;

        int i = 0;
        while(i < 5) {
            last = System.currentTimeMillis();
            Group<byte[], String> res = DataSerialization.serialize(Test2.a.class, a);
            long took = System.currentTimeMillis()-last;
            System.out.println("Log: "+res.getB());
            System.out.println("Write ("+i+") = "+took+" ms");
            write = i == 0 ? took : (took+write)/2;
            last = System.currentTimeMillis();
            DataSerialization.deserialize(a.class, res.getA());
            DataSerialization.deserialize(b.class, res.getA());
            took = System.currentTimeMillis()-last;
            System.out.println("Read ("+i+") = "+took+" ms");
            read = i == 0 ? took : (took+read)/2;
            i++;
        }

        System.out.println("Write => "+write+" ms");
        System.out.println("Read => "+read+" ms");
    }
}
