package may_chu;

public class XuLyLuotChoi {
    public static String judge(String p1, String p2) {
        if (p1.equals(p2)) return "Hòa";
        if ((p1.equals("ROCK") && p2.equals("SCISSORS")) ||
            (p1.equals("SCISSORS") && p2.equals("PAPER")) ||
            (p1.equals("PAPER") && p2.equals("ROCK")))
            return "Thắng";
        return "Thua";
    }

    /** Trả về thông báo cho cả hai người và tự lưu DB + leaderboard */
    public static String[] xuLyKetQua(String player1, String choice1,
                                      String player2, String choice2) {
        String r1 = judge(choice1, choice2);
        String r2 = judge(choice2, choice1);

        String winner = null;
        if ("Thắng".equals(r1)) winner = player1;
        else if ("Thắng".equals(r2)) winner = player2;

        Database.saveMatchHistory(player1, player2, winner);
        if (winner != null) {
            if (winner.equals(player1)) {
                Database.updateLeaderboard(player1, true);
                Database.updateLeaderboard(player2, false);
            } else {
                Database.updateLeaderboard(player2, true);
                Database.updateLeaderboard(player1, false);
            }
        }

        String msg1 = "Bạn: " + choice1 + " - Đối thủ: " + choice2 + " → " + r1;
        String msg2 = "Bạn: " + choice2 + " - Đối thủ: " + choice1 + " → " + r2;
        return new String[]{ msg1, msg2 };
    }
}
