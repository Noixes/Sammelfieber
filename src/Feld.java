import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener; 
import java.util.Random;


public class Feld extends JApplet implements KeyListener
{

	private static final long serialVersionUID = -3997705229534458132L;
	Spieler spieler1, spieler2;
    Random r;
    public FeldObjekt[][] foFelder = new FeldObjekt[32][32];
    int maxPoints;
    public Feld(){
        this.setFocusable(true);
        
        for(int x = 0; x < 32; x++){
            for(int y = 0; y < 32; y++){
                foFelder[x][y] = new FeldObjekt(x, y, FeldStatus.NICHTS);
            }
        }
        
        r = new Random(System.currentTimeMillis());
        foFelder[0][0].feldStatus = FeldStatus.SPIELER;
        foFelder[31][31].feldStatus = FeldStatus.SPIELER;
        
        spawnCoin();
        spawnCoin();
        
        spieler1 = new Spieler("Rot", 0, 0, new Color(255, 0, 0));
        spieler2 = new Spieler("Blau", 31, 31, new Color(0, 0, 255));
        
        for(int i = 0; i < 40; i++){
            spawnWall();
        }
        
        this.addKeyListener(this);
        while(true){
            String eingabe = JOptionPane.showInputDialog(null, "Welche Punktzahl soll erreicht werden?", "Punktzahl?", JOptionPane.PLAIN_MESSAGE);
            try{
                maxPoints = Integer.parseInt(eingabe);
                return;
            }catch(Exception e){
            }
        }
    }
    public void paint(Graphics g){
        this.setSize(new Dimension(1280, 970));
        g.setColor(new Color(255,255,255));
        g.fillRect(0,0,1280,960);
        for(int x = 0; x < 32; x++){
            for(int y = 0; y < 32; y++){
                switch(foFelder[x][y].feldStatus){
                    case NICHTS:
                    g.setColor(new Color(0,0,0));
                    g.drawRect((x*30),(y*30),30,30);
                    break;
                    case SPIELER:
                    g.setColor(new Color(0,0,0));
                    g.drawRect((x*30),(y*30),30,30);
                    if(spieler1.x == x && spieler1.y == y){
                        g.setColor(spieler1.farbe);
                        g.fillRect((x*30)+5,(y*30)+5,20,20);
                    }else{
                        g.setColor(spieler2.farbe);
                        g.fillRect((x*30)+5,(y*30)+5,20,20);
                    }
                    break;
                    case COIN:
                    g.setColor(new Color(255, 215, 0));
                    g.fillOval(x*30+7, y*30+2, 16, 27);
                    break;
                    case WALL:
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(x*30, y*30, 30, 30);
                    break;
                }
            }
        }
        g.setColor(new Color(0,0,0));
        g.drawString("Rot:\t" + spieler1.coins, 1000, 20);
        g.drawString("Blau:\t" + spieler2.coins, 1000, 40);
    }
    public void keyTyped(KeyEvent e) { 
        
    } 

    public void keyPressed(KeyEvent e) { 
                
    } 

    public void keyReleased(KeyEvent e) { 
        // Spieler 1
        if(e.getKeyCode() == KeyEvent.VK_W){
            if( spieler1.y != 0){
                if(performStep(spieler1, spieler1.x, spieler1.y - 1)){
                    foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.NICHTS;
                    spieler1.neuePosition(spieler1.x, --spieler1.y);
                    foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.SPIELER;
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            if( spieler1.x != 0 ){
                if(performStep(spieler1, spieler1.x - 1, spieler1.y)){
                foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.NICHTS;
                spieler1.neuePosition(--spieler1.x, spieler1.y);
                foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            if( spieler1.y != 31 ){
                if(performStep(spieler1, spieler1.x, spieler1.y + 1)){
                foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.NICHTS;
                spieler1.neuePosition(spieler1.x, spieler1.y + 1);
                foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            if( spieler1.x != 31 ){
                if(performStep(spieler1, spieler1.x + 1, spieler1.y)){
                foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.NICHTS;
                spieler1.neuePosition(++spieler1.x, spieler1.y);
                foFelder[spieler1.x][spieler1.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        
        // Spieler 2
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if( spieler2.y != 0 ){
                if(performStep(spieler2, spieler2.x, spieler2.y - 1)){
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.NICHTS;
                spieler2.neuePosition(spieler2.x, --spieler2.y);
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if( spieler2.x != 0 ){
                if(performStep(spieler2, spieler2.x - 1, spieler2.y)){
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.NICHTS;
                spieler2.neuePosition(--spieler2.x, spieler2.y);
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if( spieler2.y != 31 ){
                if(performStep(spieler2, spieler2.x, spieler2.y + 1)){
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.NICHTS;
                spieler2.neuePosition(spieler2.x, ++spieler2.y);
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if( spieler2.x != 31 ){
                if(performStep(spieler2, spieler2.x + 1, spieler2.y)){
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.NICHTS;
                spieler2.neuePosition(++spieler2.x, spieler2.y);
                foFelder[spieler2.x][spieler2.y].feldStatus = FeldStatus.SPIELER;
            }
        }
        }
        repaint();
    }
    boolean performStep(Spieler spieler, int x, int y){
        if(foFelder[x][y].feldStatus == FeldStatus.COIN){
            spieler.coins++;
            spawnCoin();
            if(spieler.coins == maxPoints){
                
                JOptionPane.showMessageDialog(null,
                            "Spieler " + spieler.id + " hat gewonnen!",
                            "Gewonnen",
                            JOptionPane.INFORMATION_MESSAGE);
                
                            
                for(int x_ = 0; x_ < 32; x_++){
                    for(int y_ = 0; y_ < 32; y_++){
                        foFelder[x_][y_] = new FeldObjekt(x_, y_, FeldStatus.NICHTS);
                    }
                }
                spieler1 = new Spieler("Rot", 0, 0, new Color(255, 0, 0));
                spieler2 = new Spieler("Blau", 31, 31, new Color(0, 0, 255));
                foFelder[0][0].feldStatus = FeldStatus.SPIELER;
                foFelder[31][31].feldStatus = FeldStatus.SPIELER;
                for(int i = 0; i < 40; i++){
                    spawnWall();
                }
                spawnCoin();
                spawnCoin();
                return false;
            }
            
        }
        if(foFelder[x][y].feldStatus == FeldStatus.SPIELER || foFelder[x][y].feldStatus == FeldStatus.WALL){
            return false;
        }
        return true;
    }
    void spawnCoin(){
        int x = r.nextInt(32);
        int y = r.nextInt(32);
        if(foFelder[x][y].feldStatus == FeldStatus.NICHTS){
            foFelder[x][y].feldStatus = FeldStatus.COIN;
        }else{
            spawnCoin();
        }
    }
    void spawnWall(){
        int x = r.nextInt(32);
        int y = r.nextInt(32);
        if(foFelder[x][y].feldStatus == FeldStatus.NICHTS){
            foFelder[x][y].feldStatus = FeldStatus.WALL;
        }else{
            spawnWall();
        }
    }
}