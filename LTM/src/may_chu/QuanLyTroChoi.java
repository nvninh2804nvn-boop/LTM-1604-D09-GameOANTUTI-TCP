package may_chu;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QuanLyTroChoi {
    /** Hàng đợi cho random match */
    private final Queue<XuLyKhach> randomQueue = new ConcurrentLinkedQueue<>();
    /** Map phòng chơi: username -> đối thủ */
    private final Map<String, XuLyKhach> opponents = new ConcurrentHashMap<>();

    /** Yêu cầu ghép cặp ngẫu nhiên */
    public void requestRandomMatch(XuLyKhach player) {
        randomQueue.offer(player);
        tryPairing();
    }

    private synchronized void tryPairing() {
        while (randomQueue.size() >= 2) {
            XuLyKhach p1 = randomQueue.poll();
            XuLyKhach p2 = randomQueue.poll();
            if (p1 == null || p2 == null) break;
            createRoom(p1, p2);
        }
    }

    /** Tạo phòng giữa 2 người chơi */
    public void createRoom(XuLyKhach p1, XuLyKhach p2) {
        opponents.put(p1.getUsername(), p2);
        opponents.put(p2.getUsername(), p1);

        p1.setOpponent(p2);
        p2.setOpponent(p1);

        p1.getOut().println("START|" + p2.getUsername());
        p2.getOut().println("START|" + p1.getUsername());
    }

    /** Mời người chơi cụ thể */
    public void invite(XuLyKhach from, String targetName) {
        XuLyKhach target = MayChu.clients.get(targetName);
        if (target != null) target.getOut().println("INVITE|" + from.getUsername());
        else from.getOut().println("ERROR|Người chơi không tồn tại hoặc offline");
    }

    /** Đồng ý lời mời */
    public void acceptInvite(XuLyKhach from, String inviterName) {
        XuLyKhach inviter = MayChu.clients.get(inviterName);
        if (inviter != null) createRoom(from, inviter);
        else from.getOut().println("ERROR|Người mời không còn online");
    }

    /** Thực hiện nước đi (ĐÃ SỬA – bỏ cast sai) */
    public void handleMove(XuLyKhach player, String move) {
        XuLyKhach opp = opponents.get(player.getUsername());
        if (opp == null) {
            player.getOut().println("ERROR|Bạn chưa có đối thủ");
            return;
        }

        player.setMove(move);

        if (player.getMove() != null && opp.getMove() != null) {
            String[] res = XuLyLuotChoi.xuLyKetQua(
                player.getUsername(), player.getMove(),
                opp.getUsername(),   opp.getMove()
            );

            player.getOut().println("RESULT|" + res[0]);
            opp.getOut().println("RESULT|" + res[1]);

            // Reset để chơi tiếp ván mới
            player.setMove(null);
            opp.setMove(null);
        }
    }

    /** Người chơi thoát phòng */
    public void leave(XuLyKhach player) {
        XuLyKhach opp = opponents.remove(player.getUsername());
        if (opp != null) {
            opponents.remove(opp.getUsername());
            opp.setOpponent(null);
            opp.setMove(null);
            opp.getOut().println("BYE|Đối thủ đã thoát");
        }
        player.setOpponent(null);
        player.setMove(null);
    }
}
