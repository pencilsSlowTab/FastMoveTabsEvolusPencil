import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ToolSwing {

	private JFrame frame;

	private String pencilsFilePath;
	private File previousFile;
	private JLabel styledText = new JLabel("");
	private XmlMoveTool t;
	private int selectedPage;
	final JFileChooser fc = new JFileChooser();

	JList list_1 = new JList();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToolSwing window = new ToolSwing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void loadHistoric() {
		try {
			previousFile = new File("history.txt");
			if (!previousFile.exists()) {
				previousFile.createNewFile();
			} else {
				try {
					Scanner sc = new Scanner(previousFile);
					pencilsFilePath = sc.nextLine();
					System.out.println("old pencilsFilePath " + pencilsFilePath);
					loadXmlTree(list_1);
				} catch (Exception e) {
					System.err.println("Failed to open the previous file: " + pencilsFilePath);
					e.printStackTrace();
				}
			}
			System.out.println(previousFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 */
	public ToolSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 676, 591);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		styledText.setBounds(12, 12, 633, 15);
		frame.getContentPane().add(styledText);

		list_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				selectedPage = list_1.getSelectedIndex();
				System.out.println("Page: " + selectedPage);
			}
		});

		loadHistoric();
		loadXmlTree(list_1);

		JButton btnNewButton = new JButton("Load");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileFilter filter = new FileNameExtensionFilter("Pencil", "ep");
				;
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					pencilsFilePath = file.getAbsolutePath();
					System.out.println("pencilsFilePath " + pencilsFilePath);
					loadXmlTree(list_1);
					// This is where a real application would open the file.
					// log.append("Opening: " + file.getName() + "." + newline);
				} else {
					// log.append("Open command cancelled by user." + newline);
				}
			}
		});
		btnNewButton.setBounds(279, 74, 117, 25);
		frame.getContentPane().add(btnNewButton);

		JButton btnUp = new JButton("up");
		btnUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Page: " + selectedPage);
				t.moveUp(selectedPage);
				loadXmlTree(list_1);
				selectedPage--;
				list_1.setSelectedIndex(selectedPage);
			}
		});
		btnUp.setBounds(279, 130, 117, 25);
		frame.getContentPane().add(btnUp);

		JButton btnNewButton_2 = new JButton("down");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Page: " + selectedPage);
				t.moveDown(selectedPage);
				loadXmlTree(list_1);
				selectedPage++;
				list_1.setSelectedIndex(selectedPage);
			}
		});
		btnNewButton_2.setBounds(279, 167, 117, 25);
		frame.getContentPane().add(btnNewButton_2);

		list_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		list_1.setBounds(45, 39, 211, 448);
		frame.getContentPane().add(list_1);
	}

	private void loadXmlTree(JList list) {
		try {
			list.removeAll();

			if (pencilsFilePath != null) {
				styledText.setText(pencilsFilePath);
				try {
					PrintWriter out = new PrintWriter(previousFile);
					System.out.println("pencilsFilePath " + pencilsFilePath);
					out.print(pencilsFilePath);
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			File fXmlFile = new File(pencilsFilePath);
			t = new XmlMoveTool(fXmlFile);
			String[] pages = t.list();
			final DefaultListModel model = new DefaultListModel();
			for (int i = 0; i < pages.length; i++) {
				model.addElement(pages[i]);
				System.out.println(" pages[i] " + pages[i]);
				// item.setText(pages[i]);
				// item.setData("id", i);
			}
			list.setModel(model);
		} catch (Exception e2) {
			System.err.println(e2);
		}

	}
}
