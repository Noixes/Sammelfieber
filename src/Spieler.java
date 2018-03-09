import java.awt.*;

public class Spieler
{
    public String id;
    public Color farbe;
    public int x, y;
    public int coins;
    public Spieler(String id, int x, int y, Color farbe)
    {
        this.coins = 0;
        this.id = id;
        this.farbe = farbe;
        this.x = x;
        this.y = y;
    }
    
    public void neuePosition(int x, int y){
        this.x = x;
        this.y = y;
    }
}
