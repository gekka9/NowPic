package nowPic;


import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;

import nowPic.ClientModel.Mode;

class MyMenu extends MenuBar implements ActionListener, ItemListener {
  private static final long serialVersionUID = 1L;
  private static final String CLEAR = "Clear Authentication";
  private static final String TWEET = "Enable Tweet Form";
  private static final String ONLY_MENTION = "View ONLY Mention";
  private static final String CUT_MENTION = "NO View Mention";
  private CheckboxMenuItem mention;
  private CheckboxMenuItem tweet;
  private CheckboxMenuItem cutMention;
  private ClientModel model;
  private PostWindow postWindow;
  
  public MyMenu(ClientModel model){
    this.model=model;
    Menu menuFile = new Menu("Utility");
    menuFile.addActionListener(this);
    this.add(menuFile);
    MenuItem clearAuthentication = new MenuItem(CLEAR);
    menuFile.add(clearAuthentication);    
        
    this.tweet = new CheckboxMenuItem(TWEET);
    tweet.addItemListener(this);
    menuFile.add(tweet);
        
    this.mention = new CheckboxMenuItem(ONLY_MENTION);
    mention.addItemListener(this);
    menuFile.add(mention);
    
    this.cutMention = new CheckboxMenuItem(CUT_MENTION);
    cutMention.addItemListener(this);
    menuFile.add(cutMention);
  }
  public void actionPerformed(ActionEvent e) {
      if(e.getActionCommand().equals("Clear Authentication")){
       
      }
    }
  public void itemStateChanged(ItemEvent e) {
    CheckboxMenuItem menu = (CheckboxMenuItem)e.getSource();
    if (menu.getState()) {
      String label = menu.getLabel();
      if(label.equals(CLEAR)){
      }else if(label.equals(TWEET)){
        this.model.enablePostField();
      }else if(label.equals(ONLY_MENTION)){
        this.cutMention.setEnabled(false);
        this.model.setMode(Mode.ONLY_MENTION);
      }else if(label.equals(CUT_MENTION)){
        this.mention.setEnabled(false);
        this.model.setMode(Mode.CUT_MENTION);
      }
      System.out.println(menu.getLabel() + " SELECTED");
    } else {
      String label = menu.getLabel();
      if(label.equals(CLEAR)){
        
      }else if(label.equals(TWEET)){
        this.model.disablePostField();
      }else if(label.equals(ONLY_MENTION)){
        this.cutMention.setEnabled(true);
        this.model.setMode(Mode.NORMAL);
      }else if(label.equals(CUT_MENTION)){
        this.mention.setEnabled(true);
        this.model.setMode(Mode.NORMAL);
      }
      System.out.println(menu.getLabel() + " SELECTED");
    }
  }
  
  public void setPostField(boolean bool){
    this.tweet.setState(bool);
  }
}