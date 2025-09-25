package may_chu;

import java.util.concurrent.ConcurrentLinkedQueue;

/** Tổ chức ghép cặp + xử lý ván chơi. */
public class QuanLyTroChoi {
    /** Hàng đợi ghép ngẫu nhiên (server-wide). */
    private static final ConcurrentLinkedQueue<XuLyKhach> waiting = new ConcurrentLinkedQueue<>();

    /** Tạo phòng giữa 2 người. */
    public static void createRoom(XuLyKhach p1, XuLyKhach p2) {
        if (p1 == null || p2 == null) return;
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        p1.getOut().println("START|" + p2.getUsername());
        p2.getOut().println("START|" + p1.getUsername());
    }

    /** Mời người chơi cụ thể. */
    public static void invite(XuLyKhach from, String targetName) {
        XuLyKhach target = MayChu.clients.get(targetName);
        if (target != null && target.getOpponent() == null && from.getOpponent() == null) {
            target.getOut().println("INVITE|" + from.getUsername());
            from.getOut().println("INFO|Đã gửi lời mời tới " + targetName);
        } else {
            from.getOut().println("ERROR|Người chơi không sẵn sàng");
        }
    }

    /** Đồng ý lời mời. */
    public static void acceptInvite(XuLyKhach from, String inviterName) {
        XuLyKhach inviter = MayChu.clients.get(inviterName);
        if (inviter != null && inviter.getOpponent() == null && from.getOpponent() == null) {
            createRoom(inviter, from);
        } else {
            from.getOut().println("ERROR|Người mời đã bận hoặc offline");
        }
    }

    /** Ghép ngẫu nhiên. */
    public static void randomMatch(XuLyKhach me) {
        if (me.getOpponent() != null) {
            me.getOut().println("ERROR|Bạn đang bận");
            return;
        }
        // thử lấy 1 người đang chờ
        XuLyKhach other;
        while ((other = waiting.poll()) != null) {
            if (other != me && other.getOpponent() == null) {
                createRoom(me, other);
                return;
            }
        }
        // nếu chưa có ai -> vào hàng chờ
        waiting.offer(me);
        me.getOut().println("INFO|Đang chờ ghép ngẫu nhiên...");
    }

    /** Nhận nước đi; khi đủ 2 bên thì chấm điểm và trả kết quả + lưu kép. */
    public static void handleMove(XuLyKhach player, String move) {
        XuLyKhach opp = player.getOpponent();
        if (opp == null) { player.getOut().println("ERROR|Bạn chưa có đối thủ"); return; }

        player.setMove(move);

        if (player.getMove() != null && opp.getMove() != null) {
            String r1 = XuLyLuotChoi.judge(player.getMove(), opp.getMove());
            String r2 = XuLyLuotChoi.judge(opp.getMove(), player.getMove());

            player.getOut().println("RESULT|Bạn: " + player.getMove()
                    + ", Đối thủ: " + opp.getMove() + " → " + r1);
            opp.getOut().println   ("RESULT|Bạn: " + opp.getMove()
                    + ", Đối thủ: " + player.getMove() + " → " + r2);

            // ✅ lưu kép (SQLite + CSV)
            QuanLyLichSu.saveHistory(player.getUsername(), opp.getUsername(), r1);
            QuanLyLichSu.saveHistory(opp.getUsername(), player.getUsername(), r2);

            // reset ván
            player.setMove(null);
            opp.setMove(null);
        }
    }

    /** Khi người chơi rời phòng/hủy ghép, loại bỏ khỏi hàng chờ. */
    public static void removeFromQueue(XuLyKhach player) {
        waiting.remove(player);
    }
}
