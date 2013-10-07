package nowPic;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Example
{
  public static final String KEY = "WCH9LZtxMe2l7Zkjs12IQ";
  public static final String SECRET = "kHHUWuSDdbPdQ7HUICmqJ03srsX628kecx0OJCQhXRg";
  public static final String ACCESS_FILENAME="resource/access.obj";
  
  public static void main(String[] args) throws Exception{
    if(args.length>0){
      if(args[0].equals("-h") || args[0].equals("-help")){
        showHelp();
        return;
      }
    }
    
    File newdir = new File("./resource");
    newdir.mkdir();
    Example stream = new Example();
    stream.startUserStream();
  }
  
  /*
   * UserStreamの開始
   */
  public String startUserStream()throws Exception
  {
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.setOAuthConsumerKey(KEY);
    builder.setOAuthConsumerSecret(SECRET);
    AccessToken accessToken=null;
    Configuration conf = builder.build();
    TwitterFactory factory = new TwitterFactory(conf);
    Twitter twitter= factory.getInstance(); 
    try{
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCESS_FILENAME));
      accessToken = (AccessToken) ois.readObject();
      ois.close();
    }catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      RequestToken requestToken = twitter.getOAuthRequestToken();
      JFrame frame = new JFrame();
      JOptionPane.showMessageDialog(frame, "OKボタンを押すと、ブラウザで認証ページを開きます。\n認証後に表示されたPINコードを入力してください。");
      Desktop desktop = Desktop.getDesktop();
      String uriString = requestToken.getAuthorizationURL();
      try {
        URI uri = new URI(uriString);
        desktop.browse(uri);
      } catch (URISyntaxException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      String pin = JOptionPane.showInputDialog("PINコードを入力してください");
      if (pin == null){
        System.exit(1);
      }
      try{
         if(pin.length() > 0){
           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
         }else{
           accessToken = twitter.getOAuthAccessToken();
         }
         //将来の参照用に accessToken を永続化する
         storeAccessToken(accessToken);
      } catch (TwitterException te) {
        if(401 == te.getStatusCode()){
          System.out.println("Unable to get the access token.");
        }else{
          te.printStackTrace();
        }
      }
    }
    //twitter.setOAuthConsumer(KEY, SECRET);
    twitter.setOAuthAccessToken(accessToken);
    ClientFactory clientFactory= new ClientFactory(twitter);
    ClientModel model = clientFactory.getClientModel();
    TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(conf);
    TwitterStream twitterStream = twitterStreamFactory.getInstance();
    twitterStream.addListener(new MyUserStreamAdapter(model));
    twitterStream.setOAuthAccessToken(accessToken);
    twitterStream.user();
    
    return null;
  }
  
  private static void storeAccessToken(AccessToken accessToken){
    //accessToken.getToken() を保存
    //accessToken.getTokenSecret() を保存
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(ACCESS_FILENAME));
      oos.writeObject(accessToken);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private static void showHelp(){
    /*
    String ln = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    sb.append("java -jar enigma.jar [OPTIONS] <TARGETS...>").append(ln);
    sb.append(ln);
    sb.append("OPTIONS").append(ln);
    sb.append("  -n, -noenceypt:        NO encrypt and NO deceypt.").append(ln);
    sb.append("  -l, -l:                view ONLY posts tweeted from this client.").append(ln);
    sb.append("  -h, --help:            show this message.").append(ln);
    sb.append("  no input:              ececute in normal mode.").append(ln);
    System.out.println(sb.toString());
    */
  }
}

/**
 * UserStreamAdapter
 */
class MyUserStreamAdapter extends UserStreamAdapter
{

  private ClientModel model;
  
  public MyUserStreamAdapter(ClientModel model){
    this.model=model;
  }
  
  /*
   * ツイートに対する反応
   */
  @Override
  public void onStatus(Status status)
  {
    super.onStatus(status);
    //logger.info(status.getText() + " : " + status.getUser().getScreenName());
    URLEntity[] urlEntity = status.getURLEntities();
    MediaEntity[] mediaEntity = status.getMediaEntities();
    if(mediaEntity.length>0){
      if(CheckURL.check(mediaEntity[0].getMediaURL()) ){
        this.model.viewPost(status);
        System.out.println(status.getText() + " : " + status.getUser().getScreenName()+" "+status.getUser().getName());
      }
    }else if(urlEntity.length>0){
      if(CheckURL.check(urlEntity[0].getExpandedURL()) ){
        this.model.viewPost(status);
        System.out.println(status.getText() + " : " + status.getUser().getScreenName()+" "+status.getUser().getName());
      }
    }
    //System.out.println(count);
    //count++;
  }
}