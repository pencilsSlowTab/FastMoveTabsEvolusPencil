import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.custom.StyledText;

public class Tools {

	private Shell shell;
	private StyledText styledText;
	private XmlMoveTool t;

	private String pencilsFilePath;

	private File previousFile;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Tools window = new Tools();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void loadHistoric(Tree tree) {
		try {
			previousFile = new File("history.txt");
			if (!previousFile.exists()) {
				previousFile.createNewFile();
			} else {
				try {
					Scanner sc = new Scanner(previousFile);
					pencilsFilePath = sc.next();
					loadXmlTree(tree);
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

	TreeItem selectedItem;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1112, 736);
		shell.setText("SWT Application");
		
		styledText = new StyledText(shell, SWT.BORDER);
		styledText.setBounds(10, 1, 683, 19);

		Button btnNewButton = new Button(shell, SWT.NONE);
		final Tree tree = new Tree(shell, SWT.BORDER);

		loadHistoric(tree);
		styledText.setText(pencilsFilePath);

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println(selectedItem.getText());
				Integer position = (Integer) selectedItem.getData("id");
				System.out.println(position);
				t.moveUp(position);

				loadXmlTree(tree);

				TreeItem item = tree.getItem(position - 1);
				tree.select(item);
				selectedItem = item;
				//
			}
		});
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(316, 50, 91, 29);
		btnNewButton.setText("Up");

		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Integer position = (Integer) selectedItem.getData("id");
				t.moveDown(position);
				loadXmlTree(tree);

				TreeItem item = tree.getItem(position + 1);
				tree.select(item);
				selectedItem = item;
			}
		});
		btnNewButton_1.setBounds(316, 96, 91, 29);
		btnNewButton_1.setText("Down");

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// selected element from the tree
				Tree item = (Tree) e.getSource();
				TreeItem treeItem = item.getSelection()[0];
				selectedItem = treeItem;
				System.out.println(selectedItem.getText());
				System.out.println(selectedItem.getData("id"));
			}
		});
		tree.setBounds(10, 26, 300, 670);

		Button btnChooseFile = formToolkit.createButton(shell, "Choose file", SWT.NONE);
		btnChooseFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.ep" });
				// dialog.setFilterPath("c:\\temp");
				pencilsFilePath = dialog.open();
//				styledText.setText(pencilsFilePath);
				loadXmlTree(tree);
			}

		});
		btnChooseFile.setBounds(558, 189, 91, 29);
		
		
		formToolkit.adapt(styledText);
		formToolkit.paintBordersFor(styledText);

	}

	private void loadXmlTree(Tree tree) {
		try {
			tree.removeAll();

			if (pencilsFilePath != null) {
					styledText.setText(pencilsFilePath);
				try {
					PrintWriter out = new PrintWriter(previousFile);
					out.print(pencilsFilePath);
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			File fXmlFile = new File(pencilsFilePath);
			t = new XmlMoveTool(fXmlFile);
			String[] pages = t.list();
			for (int i = 0; i < pages.length; i++) {
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText(pages[i]);
				item.setData("id", i);
			}
		} catch (Exception e2) {
			System.err.println(e2);
		}

	}
}
