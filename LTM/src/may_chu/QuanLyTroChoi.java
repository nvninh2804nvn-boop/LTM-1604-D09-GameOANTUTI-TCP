// 📁 GameManager.java
// ==============================
package may_chu;


import java.util.HashMap;


public class QuanLyTroChoi {
private final HashMap<String, String> playerMoves = new HashMap<>();


public void setMove(String player, String move) {
playerMoves.put(player, move);
}


public boolean hasBothMoves(String p1, String p2) {
return playerMoves.containsKey(p1) && playerMoves.containsKey(p2);
}


public String getResult(String p1, String p2) {
String move1 = playerMoves.get(p1);
String move2 = playerMoves.get(p2);


if (move1.equals(move2)) return "Hòa";
if ((move1.equals("ROCK") && move2.equals("SCISSORS")) ||
(move1.equals("SCISSORS") && move2.equals("PAPER")) ||
(move1.equals("PAPER") && move2.equals("ROCK"))) {
return p1 + " thắng";
} else {
return p2 + " thắng";
}
}


public void clearMoves(String p1, String p2) {
playerMoves.remove(p1);
playerMoves.remove(p2);
}
}