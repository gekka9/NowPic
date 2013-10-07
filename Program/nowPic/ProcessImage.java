package nowPic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ProcessImage {

 public static BufferedImage URLtoImage(String URL){
   BufferedImage image=null;
   try {
     if(URL.startsWith("http://twitpic.com/")){
       URL = URL.replaceAll("http://twitpic.com/", "http://twitpic.com/show/full/");
     }
     System.out.println(URL);
     image = ImageIO.read(new URL(URL));
   } catch (MalformedURLException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
   } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
   }
   return image;
 }
 
 public static BufferedImage resize(BufferedImage target,int width,int height){
   BufferedImage resizeImage = new BufferedImage(width, height, target.getType());
   resizeImage.getGraphics().drawImage(target.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
  return resizeImage;
 }
 
 public static BufferedImage resize(BufferedImage target,int size){
   if(target.getWidth()<target.getHeight()){
     double bunbo = (double)size/(double)target.getHeight();
     double weight = target.getWidth()*bunbo;
     return resize(target,(int)weight,size);
   }else{
     double bunbo = (double)size/(double)target.getWidth();
     double height = target.getHeight()*bunbo;
     return resize(target,size,(int)height);
   }

 }

}
