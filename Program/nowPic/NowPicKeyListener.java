package nowPic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import twitter4j.Status;
import twitter4j.Twitter;

public class NowPicKeyListener implements KeyListener{

  private ClientModel model;

  public NowPicKeyListener(ClientModel model){
    this.model=model;
  }
  
  @Override
  public void keyPressed(KeyEvent event) {
    char key = event.getKeyChar();
    if (key == 's'){
      this.model.saveImage();
    }else if (key == 'f'){
      this.model.favorite();
    }else if (key == 'r'){
      this.model.reTweet();
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
