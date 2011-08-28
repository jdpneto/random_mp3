
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class randomizer {

	/**
	 * @param args
	 */
	
	//TODO
	//Ver como funcionam as paths, tentar construir paths a partir da dir original ate ao fim e depois
	//construir sempre dessa forma quando se vai fazer load
	static ArrayList<File> files = new ArrayList<File>();
	static String dir;
	static int random;
	
	static Hashtable<String, String> hashes = new Hashtable<String,String>();
	
	public static void main(String[] args) {
		
		//System.out.println(System.getProperty("os.name"));

		//I don't want it to look ugly, so I'll go for the systems Look and Field or nothing
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.out.println("Error setting native LAF: " + e);
			System.exit(1);}

		//popup window to chose dir
		dir = getDir();

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
			//files.get(i).renameTo(new File(whereto,final_names[i]));
		}
	}
	//Simple and dumb recursive method to allow
	//That fetches all mp3 files present on
	//the chosen directory
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
				//hashes.put(crc(fs[i].getPath())+""+fs[i].length(),fs[i].getAbsolutePath());
//				System.out.println(fs[i].getAbsolutePath());
//				try {
//					System.out.println(fs[i].getCanonicalPath());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println(fs[i].getPath());
				//System.out.println(getRelativePath(fs[i].getAbsolutePath()));
				addToHash(crc(fs[i].getPath())+""+fs[i].length(),getRelativePath(fs[i].getAbsolutePath()));
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

	//Brute force method to get a random file
	//if you chose one with only a file it might
	//take a bit to get there, depending on how
	//lukcy you are
	private static int getRandomFiles()
	{
		int ret = (int) ((files.size()+1)*Math.random());
		File test = new File(dir,String.format("%010d", 0)+".mp3");
		if(test.exists())
			ret = getRandomFiles();
		return ret;
	}

	//just common java.swing stuff 
	//not my strong suit, just got something together
	//that appears to me working well enough
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
	
	
	public static void loadFromFile()
	{
		
	}
	
	public static void saveToFile()
	{
		try
		{
			FileOutputStream fs = new FileOutputStream(dir+"RESTORE_DATA_DO_NOT_DELETE.bak");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(hashes);
			os.flush();
			os.close();
		}catch(Exception e){
			System.out.println("cannot access disk");
			System.exit(1);
		}
	}
	
	public static String getRelativePath(String p)
	{
		return p.substring(dir.length()+1, p.length());
	}
	
	//to have only the original dir in the recovery file
	public static void addToHash(String hash, String location)
	{
		if(!hashes.contains(hash))
			hashes.put(hash, location);
	}

	//Get a checksum for the first 1024 bytes of a file
	//that plus the size will give me mostly unique hashes
	//an be fast enough to be usable
	//some files might be too big to do a complete hash
	public static long crc(String file) {
		Checksum ret = new CRC32();
		try
		{
			FileInputStream fs = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fs);
			byte[] bytes = new byte[1024];
			
			int length = bis.read(bytes);
			ret.update(bytes, 0, length);
			
			bis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(ret.getValue());
		return ret.getValue();
	}

}
