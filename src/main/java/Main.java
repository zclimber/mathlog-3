import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    private PrintStream out;

    private List<String> start, start2, proofLessCycle, proofMore;

    Main(PrintStream ps) {
        out = ps;
        try {
            start = Files.readAllLines(Paths.get("res/start.txt"));
            start2 = Files.readAllLines(Paths.get("res/start2.txt"));
            proofLessCycle = Files.readAllLines(Paths.get("res/proofLessCycle.txt"));
            proofMore = Files.readAllLines(Paths.get("res/proofMore.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private String toChurch(int x) {
        assert (x >= 0);
        StringBuilder sb = new StringBuilder("0");
        for (int i = 0; i < x; i++) {
            sb.append('\'');
        }
        return sb.toString();
    }

    public void proveLess(int a, int b) {
        String astr = toChurch(a);
        String bstr = toChurch(b);
        out.println("|- ?p(A + p = B)"
                .replace("A", astr)
                .replace("B", bstr));
        for (String str : start) {
            out.println(str);
        }
        out.println("@a(a + 0 = a) -> A + 0 = A"
                .replace("A", astr));
        out.println("A + 0 = A"
                .replace("A", astr));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b - a; i++) {
            for (String str : proofLessCycle) {
                out.println(str
                        .replace("A", astr)
                        .replace("C", sb));
            }
            sb.append('\'');
        }
        out.println("(A + C) = B -> ?p(A + p) = B"
                .replace("A", astr)
                .replace("B", bstr)
                .replace("C", sb));
        out.println("?p(A + p = B)"
                .replace("A", astr)
                .replace("B", bstr));
    }

    public void proveNotLess(int a, int b) {
        String astr = toChurch(a);
        String bstr = toChurch(b);
        out.println("|- !(?p(A + p = B))"
                .replace("A", astr)
                .replace("B", bstr));
        for (String str : start) {
            out.println(str);
        }
        for (String str : start2) {
            out.println(str);
        }
        bstr = bstr.substring(1);
        String cstr = toChurch(a - b - 1).substring(1);
        for (String str : proofMore) {
            out.println(str
                    .replace("C", cstr)
                    .replace("B", bstr));
        }
    }

    public static void main(String[] args) {
        Main m;
        if (args.length >= 1) {
            try {
                m = new Main(new PrintStream(Files.newOutputStream(Paths.get(args[0]))));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            m = new Main(System.out);
        }
        int a, b;
        Scanner sc = new Scanner(System.in);
        a = sc.nextInt();
        b = sc.nextInt();
        if (a < b) {
            m.proveLess(a, b);
        } else {
            m.proveNotLess(a, b);
        }
        m.out.flush();
    }
}
