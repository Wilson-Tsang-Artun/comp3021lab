package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	
	Stage stage;
	
	String selectedFileName = "";
	
	String currentNote = "";
	
	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) throws FileNotFoundException {
		loadNoteBook();
		this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes
		
		//lab 8 task 1 ------------------------------------------------------------
		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Obejct!");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				File file = fileChooser.showOpenDialog(stage);   // file is the directory of the selected file 
						
				if(file != null) {
					loadNoteBook(file);
				}
			}
		});
		
		
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//do sth
				if(selectedFileName != "") {
					noteBook.save(selectedFileName);
				}
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Successfully saved");
				alert.setContentText("Your file has been saved to file " + selectedFileName);
				alert.showAndWait().ifPresent(rs -> {
				    if (rs == ButtonType.OK) {
				        System.out.println("Pressed OK.");
				    }
				});

			}
		});
		
		//lab7 task 5 ------------------------------------------------------------
		Label labelSearch = new Label("Search : ");
		TextField textInput = new TextField();
		Button buttonSearch = new Button("Search");
		buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
		        currentSearch = textInput.getText();
		        List<Note> notes = noteBook.searchNotes(currentSearch);
				ArrayList<String> searchList = new ArrayList<String>();
				ArrayList<String> folderList = new ArrayList<String>();
				// For loop that collect searchList
				for(Note note : notes) {
					if( note instanceof TextNote  ) {
						searchList.add(note.getTitle());
					}
				}
				// For loop that collect folderList
				for(Folder folder : noteBook.getFolders()) {
					if(  currentFolder.toString().equals(folder.getName())  ) {
						for(Note note : folder.getNotes()) {
							if( note instanceof TextNote  ) {
								folderList.add(note.getTitle());
							}
						}
					}
				}
				//combine two search results
				searchList.retainAll(folderList);
				
				ObservableList<String> combox2 = FXCollections.observableArrayList(searchList);
				titleslistView.setItems(combox2);
				textAreaNote.setText("");
			}
		});
		Button buttonClear = new Button("Clear Search");
		buttonClear.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
		        currentSearch = "";
		        textInput.setText("");
		        textAreaNote.setText("");
		        updateListView();
			}
		});
		//lab7 task 5 Ended ------------------------------------------------------------
		hbox.getChildren().addAll(buttonLoad, buttonSave, labelSearch, textInput, buttonSearch, buttonClear);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		//foldersComboBox.getItems().addAll(noteBook.getFolders().get(0).getName(),noteBook.getFolders().get(1).getName(),noteBook.getFolders().get(2).getName());
		ArrayList<String> foldersNames = new ArrayList<String>();
		for(Folder folder : noteBook.getFolders()) {
			foldersNames.add(folder.getName());
		}
		//lab8 Task 2: new H box of combo box & Add-a-Folder button
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes
		
		foldersComboBox.getItems().clear();
		foldersComboBox.getItems().addAll(foldersNames);
		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				updateListView();
				currentNote = "";
			}
		});

		foldersComboBox.setValue("-----");
		
		Button addFolder = new Button("Add a Folder");
		addFolder.setPrefSize(100, 20);
		addFolder.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//lab8 task 2 
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
			    dialog.setTitle("Input");
			    dialog.setHeaderText("Add a new folder for your notebook:");
			    dialog.setContentText("Please enter the name you want to create:");

			    // Traditional way to get the response value.
			    Optional<String> result = dialog.showAndWait();
			    if (result.isPresent()){
			        // TODO 
			    	if(result.get() == "") {
			    		Alert alert = new Alert(AlertType.INFORMATION);
			    		alert.setTitle("Warning");
			    		alert.setHeaderText("Warning");
			    		alert.setContentText("Please input an valid folder name" );
			    		alert.showAndWait().ifPresent(rs -> {
			    		    if (rs == ButtonType.OK) {
			    		        System.out.println("Pressed OK.");
			    		    }
			    		});

			    	}else if(noteBook.isFolderNameExist(result.get())) {
			    		Alert alert = new Alert(AlertType.INFORMATION);
			    		alert.setTitle("Warning");
			    		alert.setHeaderText("Warning");
			    		alert.setContentText("You already have a folder named with Books");
			    		alert.showAndWait().ifPresent(rs -> {
			    		    if (rs == ButtonType.OK) {
			    		        System.out.println("Pressed OK.");
			    		    }
			    		});

			    	}else {
			    		noteBook.addFolder(result.get());
			    		foldersComboBox.setValue(result.get());
			    		currentFolder = result.get();
			    		updateListView();
			    		updateComboBox();
			    	}
			    }



			}
		});
		
		hbox.getChildren().addAll(foldersComboBox, addFolder);

		
		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				currentNote = t1.toString();
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in textAreNote
				String content = "";
				for(Folder folder : noteBook.getFolders()) {
					for(Note note : folder.getNotes()) {
						if(title.toString().equals( note.getTitle().toString() )) {
							content = ( (TextNote) note).getContent();
						}
					}
				}
				
				textAreaNote.setText(content);

			}
		});
		
		Button addNote = new Button("Add a Note");
		addNote.setPrefSize(100, 20);
		addNote.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//lab8 task 2 ( add a Note )
				if ( currentFolder == "-----" ) {
					Alert alert = new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Warning");
		    		alert.setHeaderText("Warning");
		    		alert.setContentText("Please choose a folder first!");
		    		alert.showAndWait().ifPresent(rs -> {
		    		    if (rs == ButtonType.OK) {
		    		        System.out.println("Pressed OK.");
		    		    }
		    		});
				}else {
					TextInputDialog dialog = new TextInputDialog("Add a Note");
				    dialog.setTitle("Input");
				    dialog.setHeaderText("Add a new note to current folder");
				    dialog.setContentText("Please enter the name of your note:");
				    Optional<String> addedNewNote = dialog.showAndWait();
				    noteBook.createTextNote(currentFolder, addedNewNote.get());
				    updateListView();
				    
				    Alert alert = new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Successful!");
		    		alert.setHeaderText("Message");
		    		alert.setContentText("Insert note " + addedNewNote.get() + " to folder Lab successfully!");
		    		alert.showAndWait().ifPresent(rs -> {
		    		    if (rs == ButtonType.OK) {
		    		        System.out.println("Pressed OK.");
		    		    }
		    		});
				}
			}
		});
		
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNote);

		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the currentFolder
		for(Folder folder : noteBook.getFolders()) {
			if(  currentFolder.toString().equals(folder.getName())  ) {
				for(Note note : folder.getNotes()) {
					if( note instanceof TextNote  ) {
						list.add(note.getTitle());
					}
				}
			}
		}
		
		
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private VBox addGridPane() throws FileNotFoundException {
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes
		
		FileInputStream inputSaveIcon = new FileInputStream("save.png");
		Image saveIcon = new Image(inputSaveIcon);
		ImageView saveIconView = new ImageView(saveIcon);
		saveIconView.setFitHeight(18);
		saveIconView.setFitWidth(18);
		saveIconView.setPreserveRatio(true);

		
		FileInputStream inputDeleteIcon = new FileInputStream("delete.png");
		Image deleteIcon = new Image(inputDeleteIcon);
		ImageView deleteIconView = new ImageView(deleteIcon);
		deleteIconView.setFitHeight(18);
		deleteIconView.setFitWidth(18);
		deleteIconView.setPreserveRatio(true);

		
		Button saveNote = new Button("Save Note");
		saveNote.setPrefSize(100, 20);
		saveNote.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//do sth
				if(currentFolder == "-----" || currentNote == "" ) {
					Alert alert = new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Warning");
		    		alert.setHeaderText("Warning");
		    		alert.setContentText("Please select a folder and a note");
		    		alert.showAndWait().ifPresent(rs -> {
		    		    if (rs == ButtonType.OK) {
		    		        System.out.println("Pressed OK.");
		    		    }
		    		});
				}else {
					for(Folder folder : noteBook.getFolders()) {
						if(folder.getName().equals(currentFolder)) {
							for(Note note : folder.getNotes()) {
								if(note.getTitle().equals(currentNote) )
								((TextNote) note).setContent(textAreaNote.getText());
							}
						}
					}
				}
			}
		});
		
		Button deleteNote = new Button("Delete Note");
		deleteNote.setPrefSize(100, 20);
		deleteNote.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				//do sth
				if(currentFolder == "-----" || currentNote == "" ) {
					Alert alert = new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Warning");
		    		alert.setHeaderText("Warning");
		    		alert.setContentText("Please select a folder and a note");
		    		alert.showAndWait().ifPresent(rs -> {
		    		    if (rs == ButtonType.OK) {
		    		        System.out.println("Pressed OK.");
		    		    }
		    		});
				}else {
					for(Folder folder : noteBook.getFolders()) {
						if(folder.getName().equals(currentFolder)) {
							folder.removeNote(currentNote);
							Alert alert = new Alert(AlertType.INFORMATION);
				    		alert.setTitle("Succeed!");
				    		alert.setHeaderText("Comfirmation");
				    		alert.setContentText("Your note has been successfully removed");
				    		alert.showAndWait().ifPresent(rs -> {
				    		    if (rs == ButtonType.OK) {
				    		        System.out.println("Pressed OK.");
				    		    }
				    		});
				    		updateListView();
				    		textAreaNote.clear();
				    		//
						}
					}
				}
				//write sth
			}
		});
		
		hbox.getChildren().addAll(saveIconView, saveNote, deleteIconView, deleteNote );
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		grid.add(textAreaNote, 0, 0);

		
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(grid);
		return vbox;
	}
	
	private void loadNoteBook(File file) {
		//TODO Lab 8 Task 1
		selectedFileName = file.getName();
		NoteBook nb = new NoteBook(selectedFileName);
		noteBook = nb;
		updateListView();
		ArrayList<String> foldersNames = new ArrayList<String>();
		for(Folder folder : noteBook.getFolders()) {
			if(folder.isContainingTextNote())
				foldersNames.add(folder.getName());
		}
		foldersComboBox.getItems().clear();
		foldersComboBox.getItems().addAll(foldersNames);
		foldersComboBox.setValue("-----");
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called ¡§the most shocking play in NFL history¡¨ and the Washington Redskins dubbed the ¡§Throwback Special¡¨: the November 1985 play in which the Redskins¡¦ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award¡Vwinning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything¡Xuntil it wasn¡¦t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant¡Xa part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether¡¦s Daddy Was a Number Runner and Dorothy Allison¡¦s Bastard Out of Carolina, Jacqueline Woodson¡¦s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood¡Xthe promise and peril of growing up¡Xand exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}

	private void updateComboBox() {
		ArrayList<String> foldersNames = new ArrayList<String>();
		for(Folder folder : noteBook.getFolders()) {
			if(folder.isContainingTextNote() || folder.getNotes().isEmpty()  ) {	
				foldersNames.add(folder.getName());
			}
		}
		foldersComboBox.getItems().clear();
		foldersComboBox.getItems().addAll(foldersNames);
		foldersComboBox.setValue(currentFolder);
	}
}
