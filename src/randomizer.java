import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;


public class randomizer {

	/**
	 * @param args
	 */
	static ArrayList<File> files = new ArrayList<File>();
	static String dir = "O:\\";
	static int random;
	public static void main(String[] args) {
		
		//I don't want it to look ugly, so I'll go for the systems Look and Field or nothing
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.out.println("Error setting native LAF: " + e);
			System.exit(1);}
		
		//popup window to chose dir
		getDir();
		
		//Get all mp3 files on that directory or any above it
		getAllFiles(dir);
		//shuffling numbers
		int[] numbers = new int[files.size()];
		for(int i = 0; i< files.size(); i++)
		{
			numbers[i]=i;
		}
		//avoiding possible colisions if you're randomizing a directory that was already random
		random = getRandomFiles();
		shuffle(numbers);
		String[] final_names = new String[files.size()];
		//generate random filenames
		for(int i = 0; i< files.size(); i++)
		{
			final_names[i]=String.format("%010d", numbers[i])+".mp3";
			final_names[i] = random+final_names[i];
		}
		//rename and / or move them to the root of that directory with their new unique names
		File whereto = new File(dir);
		for(int i = 0; i<files.size();i++)
		{
			files.get(i).renameTo(new File(whereto,final_names[i]));
		}
	}

	private static void getAllFiles(String d)
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

	private static void shuffle(int[] array) {
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

	private static int getRandomFiles()
	{
		int ret = (int) (files.size()*Math.random());
		File test = new File(dir,String.format("%010d", 0)+".mp3");
		if(test.exists())
			ret = getRandomFiles();
		return ret;
	}

	public static String getDir()
	{
		JFileChooser c;

		c = new JFileChooser();
		c.setCurrentDirectory(null);
		c.setDialogTitle("Chose folder to randomize");
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		c.setAcceptAllFileFilterUsed(false);

		if(c.showOpenDialog(new JFileChooser()) == JFileChooser.APPROVE_OPTION){

			return c.getSelectedFile().getPath();
		}
		else
			System.exit(1);
		return "why does he make me have a return string here if it'll exit before reaching this?";
	}

}
