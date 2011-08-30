
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class randomizer extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */

	static protected JButton random_b;
	static protected JButton restore_b;
	//TODO
	//Fazer Load, menus
	//Criar uma class que Ž chamada do main para me ver livre dos statics todos.
	static ArrayList<File> files = new ArrayList<File>();
	static String dir;
	static int random;

	static Hashtable<String, String> hashes = new Hashtable<String,String>();

	public static void main(String[] args) {
		init();
	}

	private static void init()
	{
		random_b = new JButton("Just Randomize");
		restore_b = new JButton("Atempt to Restore Files");

		random_b.setHorizontalTextPosition(AbstractButton.LEADING);
		restore_b.setHorizontalTextPosition(AbstractButton.LEADING);

		random_b.setActionCommand("random");
		restore_b.setActionCommand("restore");

		random_b.setToolTipText("Faster - Randomize the files inside the directory");
		restore_b.setToolTipText("Restore a previously randomized location to its original state");

		random_b.addActionListener(this);
		restore_b.addActionListener(this);
		//System.out.println(System.getProperty("os.name"));

		//I don't want it to look ugly, so I'll go for the systems Look and Field or nothing
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.out.println("Error setting native LAF: " + e);
			System.exit(1);}


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
	private static String getDir()
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


	@SuppressWarnings({ "unchecked", "unused" })
	public static Hashtable<String,String> loadFromFile()
	{
		Hashtable<String,String> ret = null;
		String path = dir+System.getProperty("file.separator")+"RESTORE_DATA_DO_NOT_DELETE.bak";
		try {

			FileInputStream fs = new FileInputStream(path);
			ObjectInputStream os = new ObjectInputStream(fs);

			//I added the supresswarnings, but I still want it to blow here
			//before it can corrupt anything else, so if something's wrong
			//it'll blow here with a ClassNotFoundException (I think)
			//and simply return an empty hashtable
			//check code found @StackOverflow, sorry but can't remember where
			ret = (Hashtable<String,String>)os.readObject();
			for (String  s : ret.keySet());
					for (String  n : ret.values());
		} catch (FileNotFoundException e) {
			ret = new Hashtable<String,String>();
		} catch (IOException e) {
			System.out.println("Cannot access disk...");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			ret = new Hashtable<String,String>();
		}
		return ret;
	}

	private static void saveToFile()
	{
		try
		{
			String path = dir+System.getProperty("file.separator")+"RESTORE_DATA_DO_NOT_DELETE.bak";
			//System.out.println(path);
			FileOutputStream fs = new FileOutputStream(path);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(hashes);
			os.flush();
			os.close();
		}catch(Exception e){
			System.out.println("cannot access disk");
			System.exit(1);
		}
	}

	private static String getRelativePath(String p)
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
	private static long crc(String file) {
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

	private static void randomizeDirectory()
	{
		//popup window to chose dir
		dir = getDir();
		hashes = loadFromFile();
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
		saveToFile();		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
