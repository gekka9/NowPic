package nowPic;

import twitter4j.Twitter;

public class ClientFactory {

  private ClientModel model;
  private MainFrame frame;
  
  public ClientFactory(Twitter twitter){
    this.model=new ClientModel(twitter);
    PostField postField = new PostField(this.model);
    this.frame = new MainFrame(new Tweet(this.model),postField);
    this.model.setFrame(this.frame);

    this.frame.addKeyListener(new NowPicKeyListener(this.model));
    MyMenu menu = new MyMenu(this.model);
    this.frame.setMenuBar(menu);
    if(this.model.isEnablePostField()){
      this.model.enablePostField();
      menu.setPostField(true);
    }else{
      this.model.disablePostField();
      menu.setPostField(false);
    }
  }
  
  public ClientModel getClientModel(){
    return this.model;
  }

}
