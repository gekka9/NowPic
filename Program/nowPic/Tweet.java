package nowPic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;

public class Tweet extends JPanel{
  private static final long serialVersionUID = 1L;
  private ClientModel model;
  private JTextArea name;
  private JTextArea text;
  private JTextArea footer;
  private JLabel image;
  private JButton rtButton;
  private JButton reButton;
  private JButton favButton;
  public static final int BUTTON_SIZE=32;
  public static final int ICON_SIZE=60;
  public static final int CONSOLE_SIZE=80;
  
  public Tweet(ClientModel model){
    super();
    this.model=model;
    this.setFocusable(false);
    this.setEnabled(false);
    //this.setPreferredSize(new Dimension(400,120));
    BoxLayout layout=new BoxLayout(this,BoxLayout.Y_AXIS);
    this.setLayout(layout);
    //this.setLayout(new GridLayout(4, 1));
    //this.setBackground(new Color(0x00000000,true));
    
    //名前
    name = new JTextArea();
    name.setEditable(false);
    name.setLineWrap(false);
    name.setMaximumSize(new Dimension(Short.MAX_VALUE,12));
    name.setForeground(Color.black);
    name.setBackground(Color.green);
    this.add(name);
    name.setFocusable(false);
    
    //画像
    image = new JLabel();
    image.setForeground(Color.black);
    image.setBackground(Color.red);
    JPanel imagePanel = new JPanel();
    imagePanel.add(image);
    this.add(imagePanel);
    
    //本文
    text = new JTextArea();
    text.setEditable(false);
    text.setLineWrap(true);
    text.setForeground(Color.white);
    text.setBackground(Color.DARK_GRAY);
    this.add(text);
    text.setFocusable(false);
    
    //フッタ
    footer = new JTextArea();
    footer.setEditable(false);
    footer.setLineWrap(true);
    footer.setMaximumSize(new Dimension(Short.MAX_VALUE,12));
    footer.setForeground(Color.black);
    footer.setBackground(Color.green);
    this.add(footer);
    footer.setFocusable(false);
    
    rtButton = new JButton(new ImageIcon("./gui/rt.png") );
    rtButton.addActionListener(new ReTweet());

    reButton = new JButton(new ImageIcon("./gui/re.png") );
    reButton.addActionListener(new Reply(this.model));
    

    favButton = new JButton(new ImageIcon("./gui/fav.png") );
    favButton.addActionListener(new Favorite());
  }
  public void setValue(Status status){
    String text=status.getText();
    URLEntity[] entities = status.getURLEntities();//entity取得
    MediaEntity[] m_entities = status.getMediaEntities();
    
    if(m_entities.length>0){
      System.out.println("ねむい");
      String ex_url = m_entities[0].getMediaURL();//展開後のURL
      String tco = m_entities[0].getURL();//t.co
      //System.out.println("メディア展開: "+ex_url);
      this.image.setIcon(new ImageIcon(ProcessImage.resize(ProcessImage.URLtoImage(ex_url), 300)));
      Pattern p = Pattern.compile(tco);
      Matcher m = p.matcher(text);
      text = m.replaceAll(ex_url);//置換
    }else if(entities.length>0){
      String ex_url = entities[0].getExpandedURL();//展開後のURL
      String tco = entities[0].getURL();//t.co
      //System.out.println("展開: "+ex_url);
      this.image.setIcon(new ImageIcon(ProcessImage.resize(ProcessImage.URLtoImage(ex_url), 300)));
      Pattern p = Pattern.compile(tco);
      Matcher m = p.matcher(text);
      text = m.replaceAll(ex_url);//置換
    }
    this.name.setText(status.getUser().getName());
    this.text.setText(this.omitLongTweet(text));
    //this.text.setText(status.getText());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.JAPAN);
    this.footer.setText(" at:"+sdf.format(status.getCreatedAt()));
    ActionListener[] listeners= this.reButton.getActionListeners();
    
    Reply reply = (Reply) listeners[0];
    reply.setStatus(status);
    
    listeners= this.rtButton.getActionListeners();
    ReTweet reTweet = (ReTweet) listeners[0];
    reTweet.setStatus(status, model.getTwitter());
    
    listeners= this.favButton.getActionListeners();
    Favorite favorite = (Favorite) listeners[0];
    favorite.setStatus(status, model.getTwitter());
    //全部解析パターン
    /*
    for (URLEntity entity : entities) {
        String ex_url = entity.getExpandedURL();//展開後のURL
        String tco = entity.getURL();//t.co
        Pattern p = Pattern.compile(tco);
        String tweet=null;
        Matcher m = p.matcher(tweet);
        tweet = m.replaceAll(ex_url);//置換
    }
    */
    
    this.repaint();
    this.model.getFrame().pack();
    this.repaint();
  }
  
  private String omitLongTweet(String target){
    char[] targetArray = target.toCharArray();
    int count=0;
    boolean isOmitted=false;
    for(char aChar : targetArray){
      if(aChar == '\n'){
        count++;
      }
    }
    if(count > 3){
      target=target.replaceAll("\n", "");
      isOmitted=true;
    }
    if(target.length()>70){
      target = target.substring(0, 69);
      target = target + "...";
      isOmitted=true;
    }
    if(isOmitted){
      target = target+" [omitted]";
    }
    return target;
  }

  public JButton getReplyButton(){
    return this.reButton;
  }
  public JButton getFavoriteButton(){
    return this.favButton;
  }
  public JButton getRetweetButton(){
    return this.rtButton;
  }
  
  public ClientModel getClientModel() {
    return this.model;
  }
  class Reply implements ActionListener{
    private JFrame replyFrame;
    private ReplyField replyField;
    private Status status;
    public Reply(ClientModel model){
      super();
      this.replyFrame=new JFrame();
      this.replyFrame.setVisible(false);
      this.replyFrame.setMinimumSize(new Dimension(200,100));
      this.replyFrame.setLocation(50,50);
      this.replyFrame.setAlwaysOnTop(true);
      this.replyField = new ReplyField(model,replyFrame);
      this.replyFrame.add(this.replyField);
    }
    public void setStatus(Status status){
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      this.replyFrame.setTitle("reply to @"+status.getUser().getScreenName());
      this.replyField.setValue(this.status);
      this.replyFrame.setVisible(true);
    }
  }
  
  class ReTweet implements ActionListener{
    private Status status;
    private Twitter twitter;
    public ReTweet(){
      super();
    }
    public void setStatus(Status status,Twitter twitter){
      this.twitter=twitter;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      if(!this.status.isRetweetedByMe()){
        try {
          this.twitter.retweetStatus(this.status.getId());
        } catch (TwitterException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
  class Favorite implements ActionListener{
    private Status status;
    private Twitter twitter;
    
    public Favorite(){
      super();
    }
    
    public void setStatus(Status status,Twitter twitter){
      this.twitter=twitter;
      this.status=status;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
      if(!this.status.isFavorited()){
        try {
          this.twitter.createFavorite(this.status.getId());
        } catch (TwitterException e) {
          // TODO Auto-generated catch block
          try {
            this.twitter.destroyFavorite(this.status.getId());
          } catch (TwitterException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          e.printStackTrace();
        }
      }
    }
  }

}
