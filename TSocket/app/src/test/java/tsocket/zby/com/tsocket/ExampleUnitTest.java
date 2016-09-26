package tsocket.zby.com.tsocket;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import tsocket.zby.com.tsocket.bean.TimerBean;
import tsocket.zby.com.tsocket.connection.agreement.CmdPackage;
import tsocket.zby.com.tsocket.connection.agreement.CmdParseImpl;
import tsocket.zby.com.tsocket.connection.agreement.CmdProcess;
import tsocket.zby.com.tsocket.utils.MyByteUtils;
import tsocket.zby.com.tsocket.utils.MyHexUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() throws Exception {
    assertEquals(4, 2 + 2);
    System.out.println("19:22:22".compareTo("19:24:22"));
    System.out.println(1<<3);
    System.out.println(1<<2);
    System.out.println(1<<1);
    TimerBean timerBean = TimerBean.getNewTimerBean();
    System.out.println(MyHexUtils.buffer2String(CmdPackage.setTimerCheck()));
  }

  @Test
  public void test () {
    TimerBean tb = TimerBean.getNewTimerBean();
    TimerBean tb2;
    try {
      tb2 = (TimerBean) tb.clone();
      tb2.setWeekValue(113);
      tb.setWeekValue(111);
      tb2.setId(11111);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public  void testEncrypt() {
    CmdParseImpl cmdParse = null;
    CmdProcess cp = new CmdProcess(cmdParse);

    String cmd1 = "05 B1 01 00 00 B4 04 b3 a1 00 16 05";
    String cmd2 = " B1 01 00 02 B6 04 B3 A7 00 10";
    String cmd3 = " 11 B2 00 00 18 29 00 19 29 00 05 00 00 05 00 7f 16 ca 11 b2";
    String cmd4 = " 01 16 39 13 16 3a 12 00 05 00 00 05 00 02 16 b7 05 b1 01 00";
    String cmd5 = " 02 b6 04 b3 a3 00 14";

    byte[] bb = MyHexUtils.hexStringToByte(cmd1);
    cp.ProcessDataCommand(bb, bb.length);

    bb = MyHexUtils.hexStringToByte(cmd2);
    cp.ProcessDataCommand(bb, bb.length);

    bb = MyHexUtils.hexStringToByte(cmd3);
    cp.ProcessDataCommand(bb, bb.length);

    bb = MyHexUtils.hexStringToByte(cmd4);
    cp.ProcessDataCommand(bb, bb.length);

    bb = MyHexUtils.hexStringToByte(cmd5);
    cp.ProcessDataCommand(bb, bb.length);

  }
}