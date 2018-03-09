 

public class FeldObjekt
{
   public int x;
   public int y;
   public FeldStatus feldStatus;
   public FeldObjekt(int x, int y, FeldStatus feldStatus){
       this.x = x;
       this.y = y;
       this.feldStatus = feldStatus;
   }
}
