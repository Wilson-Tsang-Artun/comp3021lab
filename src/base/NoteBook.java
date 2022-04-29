package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements Serializable {
	
	private ArrayList<Folder> folders;
	private static final long serialVersionUID = 1L;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
	}
	
	public NoteBook(String file) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook) in.readObject(); // ?
			folders = n.folders;
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean insertNote(String folderName , Note note ) {
		Folder f = null;
		for(Folder f1 : folders) {
			if(f1.getName() == folderName) {
				f = f1;
			}
		}
		
		if(f == null) {
			f = new Folder(folderName);
			folders.add(f);
		}
		
		for(Note n : f.getNotes() ) {
			if(n.equals(note)) {
				System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed" );
				return false;
			}
		}
		
		f.addNote(note);
		return true;

	}
	
	public boolean createImageNote(String folderName , String title) {
		ImageNote note = new ImageNote(title);
		return insertNote(folderName, note);
	}
	
	public boolean createTextNote(String folderName , String title) {
		TextNote note = new TextNote(title);
		return insertNote(folderName, note);
	}
	
	public boolean createTextNote(String folderName , String title, String content) {
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
	
	public ArrayList<Folder> getFolders() {
		return folders;
	}
	
	public void sortFolders() {
		for (Folder folder : folders ) {
			folder.sortNotes();
		}
		Collections.sort(this.folders);
	}
	
	public List<Note> searchNotes(String keywords){
		ArrayList<Note> output = new ArrayList<Note>();
		for( Folder folder : folders )
			output.addAll(folder.searchNotes(keywords));
		return output;
	}
	
	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this); 
			out.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public boolean isFolderNameExist(String name) {
		for(Folder folder : this.folders) {
			if( folder.getName().equals(name) ) 
				return true;
		}
		return false;
	}
	
	public void addFolder(String folderName) {
	    //TODO lab8 task2  
		Folder folder = new Folder(folderName);
		this.folders.add(folder);
		//for(Folder f : folders) {
			//System.out.println(f.getName());
		//}
	}
	
}
