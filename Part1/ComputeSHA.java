import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.security.*;
//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class ComputeSHA
{
	public static void main(String []args) throws Exception
	{
		if (args.length > 0)
		{
			String filename = args[0];
			//System.out.println(filename);
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			int bytePos =0;
			byte []buffer= new byte[1024];
			FileInputStream in = new FileInputStream(filename);

			while((bytePos = in.read(buffer))!= -1)
				messageDigest.update(buffer, 0, bytePos);

			byte []digested = messageDigest.digest();
			StringBuffer stringBuf = new StringBuffer("");	

			for (int i = 0; i < digested.length; i++) {
            	stringBuf.append(Integer.toString((digested[i] & 0xff) + 0x100, 16).substring(1));
        	}

			System.out.println(stringBuf.toString());
			in.close();
		}
		else 
			System.out.println("Usage: java ComputeSHA <filename>");
	}
}

			/*try{
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line = null;
	 			while ((line = br.readLine()) != null) {
	   				System.out.println(line);
	 			}
	 		}	 			
	 		catch (Exception e){
			    System.err.println(e.getMessage()); // handle exception
			}*/