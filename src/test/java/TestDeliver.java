//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Scanner;
//
//import org.junit.Test;
//
//import com.tigerjoys.clear.config.Config;
//import com.tigerjoys.clear.job.Cleaner;
//
//
//
//public class TestDeliver {
//	
//	
//	
//	
//	@Test
//	public void testFileSplitLine() throws IOException {
//		FileReader reader = new FileReader("/Users/guoliufang/Downloads/combine2015-05-20.log");
////		FileReader reader = new FileReader("/Users/guoliufang/Downloads/test.log");
//		BufferedReader br = new BufferedReader(reader);
//		 String line = null;
//		 int i = 0;
//		 Cleaner dl = new Cleaner();
//         while((line = br.readLine()) != null) {
//        	 
//        	System.out.println("这是" + (++i));
//     		String[] result = dl.clean(line).split(Config.INFO);
//     		for (String string : result) {
//     			System.out.println(string);
//     		}
//              
//         }
//        
//         br.close();
//         reader.close();
//		
//
//	}
//	
//	@Test
//	public void testSplitLine() throws IOException {
//		Scanner stdin = new Scanner(System.in);
//		System.out.println("请输入数据：");
//		String line = stdin.nextLine();
//		Cleaner dl = new Cleaner();
//		String[] result = dl.clean(line).split(Config.INFO);
//		for (String string : result) {
//			System.out.println(string);
//		}
//		
//
//	}
//
//}
