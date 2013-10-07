package nowPic;

public class CheckURL {

  public static boolean check(String target){
    if(target.startsWith("http://twitpic.com")){
      return true;
    }else if (target.startsWith("http://pbs.twimg.com")){
      System.out.println("::"+target);
      return true;
    }else if (target.startsWith("https://pbs.twimg.com")){
      return true;
    }else if (target.startsWith("http://pic.twitter.com")){
      return true;
    }
    return false;
  }

}
