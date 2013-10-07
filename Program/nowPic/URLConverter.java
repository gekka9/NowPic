package nowPic;

public class URLConverter {

  public static String URLtoName(String target){
    String result="";
    if(target.startsWith("http://pbs.twimg.com/")){
      result=target.replaceAll("http://pbs.twimg.com/media/", "");
      System.out.println("test:"+result);
      int point = result.lastIndexOf(".");
      if (point != -1) {
        result=result.substring(0, point);
      } 
    }else if(target.startsWith("http://twitpic.com/")){
      result=target.replaceAll("http://twitpic.com/", "");
    }else if(target.startsWith("http://pic.twitter.com/")){
      result=target.replaceAll("http://pic.twitter.com/", "");
    }
    return result;
  }

}
