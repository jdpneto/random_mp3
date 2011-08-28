import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class menu extends JPanel
                        implements ActionListener {
	
	protected JButton random;
	protected JButton save;
	protected JButton restore;
	
	private int command;
	
	public menu()
	{
		command = 0;
		
		random = new JButton("Just Randomize");
		save = new JButton("Randomize w/ Backup");
		restore = new JButton("Restore Files");
		
		random.setHorizontalTextPosition(AbstractButton.LEADING);
		save.setHorizontalTextPosition(AbstractButton.LEADING);
		restore.setHorizontalTextPosition(AbstractButton.LEADING);
		
		random.setActionCommand("random");
		save.setActionCommand("save");
		restore.setActionCommand("restore");
		
		random.setToolTipText("Faster - Randomize the files inside the directory without the possibility of reversing the process");
		save.setToolTipText("Randomize the files inside the directory while keeping a backup of their location");
		restore.setToolTipText("Restore a previously randomized location to their original directories");
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public int getCommand()
	{
		return command;
	}
}