import java.util.Scanner;

import org.junit.Test;

import com.tigerjoys.clear.config.Config;
import com.tigerjoys.clear.job.Cleaner;



public class TestDeliver {
	
	
	
	
	@Test
	public void testSplitLine() {
		Scanner stdin = new Scanner(System.in);
		System.out.println("请输入数据：");
		String line = stdin.nextLine();
		Cleaner dl = new Cleaner();
		
		System.out.println(dl.cleanInType(line));
		
//		String[] result = dl.splitLine(line,Config.OUT_COLON);
//		for (String string : result) {
//			System.out.println(string);
//		}
//		
	}

}
