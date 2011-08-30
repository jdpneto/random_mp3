import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class menu extends JPanel
                        implements ActionListener {
	

	
	private int command;
	
	public menu()
	{
		command = 0;
		

		
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