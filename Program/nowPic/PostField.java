package nowPic;

import java.awt.Dimension;

import javax.swing.JTextArea;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class PostField extends JTextArea{
  private static final long serialVersionUID = 1L;
  protected Twitter twitter;
  protected static final int POST_LENGTH=80;
  protected ClientModel model;
  
  public PostField(ClientModel model){
    this.model=model;
    this.twitter=model.getTwitter();
    this.setRows(2);
    this.setLineWrap(true);
    this.addKeyListener(new Poster(this));
  }
  public void post(){
    try {
      this.twitter.updateStatus(this.getText());
    } catch (TwitterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.setText("");
  }
}
