import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;


public class randomizer {

	/**
	 * @param args
	 */
	static ArrayList<File> files = new ArrayList<File>();
	static String dir = "O:\\";
	public static void main(String[] args) {
		getAllFiles(dir);
		int[] numbers = new int[files.size()];
		for(int i = 0; i< files.size(); i++)
		{
			//System.out.println(files.get(i).getPath());
			numbers[i]=i;
		}
		int random = (int) (numbers.length*Math.random());
		shuffle(numbers);
		String[] final_names = new String[files.size()];
		for(int i = 0; i< files.size(); i++)
		{
			final_names[i]=String.format("%010d", numbers[i])+".mp3";
			final_names[i] = random+final_names[i];
			//System.out.println(final_names[i]);
		}
		File whereto = new File(dir);
		for(int i = 0; i<files.size();i++)
		{
			files.get(i).renameTo(new File(whereto,final_names[i]));
		}
	}
		
	public static void getAllFiles(String d)
	{
		FileFilter fileFilter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.isDirectory() || file.getPath().endsWith(".mp3");
		    }
		};
		File dir = new File(d);
		File[] fs = dir.listFiles(fileFilter);
		for(int i = 0; i<fs.length;i++)
		{
			if(fs[i].isDirectory())
				getAllFiles(fs[i].getPath());
			else
			{
				files.add(fs[i]);
			}
				
		}
	}
	
	public static void shuffle(int[] array) {
		Random r = new Random();
		int n = array.length;

		while (n > 1) {
			int k = r.nextInt(n);
			n--;
			int tmp = array[n];
			array[n]=array[k];
			array[k]=tmp;
		}
	}
	

}
