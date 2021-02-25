package tsocket.zby.com.tsocket;

/**
 * Created by zhuj on 2016/9/19 14:15.
 */
public class AppConstants {

  public final static boolean isDemo = false;

  /**
   * 英语
   */
  public final static int language_en = 1;
  public final static int language_zh = 0;
  public final static int language_default = language_zh;

  public static final long DELAY_TIME = 1000;
  public final static String charset = "utf-8";
  public final static int connecting_count = 5;
  /**
   * 发送协议间隔时间
   */
  public static final long SEND_TIME_DEALY = 600;
  public static long scan_time = 10000;

  /**
   * 循环最低时间 单位秒
   */
  public static int DELAY_TIME_MIN = 5;
}
