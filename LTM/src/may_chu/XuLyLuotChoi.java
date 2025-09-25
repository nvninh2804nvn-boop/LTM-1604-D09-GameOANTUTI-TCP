package may_chu;

/** Luật oẳn tù tì. */
public class XuLyLuotChoi {
    public static String judge(String p1, String p2) {
        if (p1 == null || p2 == null) return "Hòa";
        if (p1.equals(p2)) return "Hòa";
        if ((p1.equals("ROCK") && p2.equals("SCISSORS")) ||
            (p1.equals("SCISSORS") && p2.equals("PAPER")) ||
            (p1.equals("PAPER") && p2.equals("ROCK"))) {
            return "Thắng";
        }
        return "Thua";
    }
}
