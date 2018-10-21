import org.anhcraft.spaciouslib.utils.Table;

public class Test4 {
    public static void main(String[] args){
        /*
        4 9 7 0 2 8
        6 5 2 1 3 9
        5 6 0 7 2 1
         */
        Table<Integer> table = new Table<>(6, 3);
        table.insert(
                4, 9, 7, 0, 2, 8,
                       6, 5, 2, 1, 3, 9,
                       5, 6, 0, 7, 2, 1
        );
        table.set(0, 3);
        table.clone(0, 5);
        table.copy(0, 12, 3);
        table.copyColumns(5, 0, 1);

        table.forEachRow(i -> System.out.print(i+" "), 0);
        System.out.println();
        table.forEachRow(i -> System.out.print(i+" "), 1);
        System.out.println();
        table.forEachRow(i -> System.out.print(i+" "), 2);
    }
}
