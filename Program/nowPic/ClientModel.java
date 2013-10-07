package nowPic;

import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.imageio.ImageIO;
import javax.security.auth.login.Configuration;
import javax.swing.ImageIcon;

public class ClientModel {
  private Twitter twitter;
  private HashMap<User,ImageIcon> iconMap=new HashMap<User, ImageIcon>();
  private Properties configuration = new Properties();
  private File propertyFile;
  private MainFrame frame;
  private static int SIZE=48;
  private Mode mode=Mode.NORMAL;
  private boolean enablePostField=false;
  enum Mode{NORMAL,ONLY_MENTION,CUT_MENTION}

  public ClientModel(Twitter twitter){
    this.twitter=twitter;
    this.propertyFile = new File("nowTwitter.properties");
    if(!this.propertyFile.exists()){
      try {
        this.propertyFile.createNewFile();
        this.initializePropaties();
        this.configuration.store(new FileOutputStream(this.propertyFile), "");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      InputStream inputStream = new FileInputStream(this.propertyFile);
      configuration.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(this.configuration.getProperty("PostField").equals("Enable")){
      this.enablePostField=true;
    }else{
      this.enablePostField=false;
    }
    if(this.configuration.getProperty("Mention").equals("Normal")){
      this.mode=Mode.NORMAL;
    }else if(this.configuration.getProperty("Mention").equals("OnlyMention")){
      this.mode=Mode.ONLY_MENTION;
    }else if(this.configuration.getProperty("Mention").equals("CutMention")){
      this.mode=Mode.CUT_MENTION;
    }
  }
  
  public void setMode(Mode mode){
    this.mode=mode;
    try {
      switch(mode){
      case NORMAL:{
        this.configuration.setProperty("Mention", "Normal");
        break;
      }
      case ONLY_MENTION:{
        this.configuration.setProperty("Mention", "OnlyMention");
        break;
      }
      case CUT_MENTION:{
        this.configuration.setProperty("Mention", "CutMention");
        break;
      }
    }
      this.configuration.store(new FileOutputStream(this.propertyFile), "");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void initializePropaties(){
    this.addPropatiy("PostField", "Disable");
    this.addPropatiy("Mention", "Disable");
  }
  
  private void addPropatiy(String key, String value){
    if(this.configuration.containsKey(key))
      System.err.println("Key already exists: " + key);
    else {
      this.configuration.setProperty(key, value);
    }
  }
  
  public Twitter getTwitter() {
    return twitter;
  }

  public void setTwitter(Twitter twitter) {
    this.twitter = twitter;
  }
  
  public void setFrame(MainFrame frame) {
    this.frame=frame;
  }
  
  public void viewPost(Status status){
    this.putIcon(status);
    this.frame.setValue(status);
  }
  
  private void putIcon(Status status){
    if(this.iconMap.containsKey(status.getUser())){
    }else{
      this.iconMap.put(status.getUser(), this.createIcon(status.getUser().getProfileImageURL()));
    }
  }
  
  private ImageIcon createIcon(String urlString){
    Image image;
    try {
      image = ImageIO.read(new URL(urlString));
      if(image.getWidth(null)!=SIZE && image.getHeight(null) !=SIZE){
        image = image.getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT);
      }
      ImageIcon icon = new ImageIcon(image);
      return icon;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public HashMap<User,ImageIcon> getIconMap() {
    return this.iconMap;
  }
  
  public void enablePostField(){
    this.frame.enablePostField();
    this.configuration.setProperty("PostField", "Enable");
    try {
      this.configuration.store(new FileOutputStream(this.propertyFile), "");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public void disablePostField(){
    this.frame.disablePostField();
    this.configuration.setProperty("PostField", "Disable");
    try {
      this.configuration.store(new FileOutputStream(this.propertyFile), "");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public boolean isEnablePostField() {
    return enablePostField;
  }

  public MainFrame getFrame() {
    // TODO Auto-generated method stub
    return this.frame;
  }
}
