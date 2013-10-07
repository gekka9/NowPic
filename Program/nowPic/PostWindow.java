package nowPic;

import java.awt.Dimension;

import javax.swing.JFrame;


public class PostWindow extends JFrame{

  public PostWindow(ClientModel model){
    this.setVisible(false);
    this.setMinimumSize(new Dimension(120, 100));
    this.setPreferredSize(new Dimension(200, 150));
    this.setLocation(100,50);
    this.add(new PostField(model));
  }
}
