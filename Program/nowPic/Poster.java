package nowPic;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Poster implements KeyListener{

  private PostField field;
  
  public Poster(PostField field){
    this.field = field;
  } 
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    int mod = e.getModifiersEx();
    if ((mod & InputEvent.META_DOWN_MASK) != 0 && key == KeyEvent.VK_ENTER){
      this.field.post();
    }
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

}
