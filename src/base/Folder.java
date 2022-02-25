package base;

import java.util.ArrayList;

public class Folder {
	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}
	
	public void addNote(Note title){
		notes.add(title);
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Note> getNotes(){
		return this.notes;
	}
	
	public String toString() {
		int nText = 0;
		int nImage = 0;
		
		for( Note note : notes ){
			if (note instanceof TextNote ) 
				nText++;
			else if (note instanceof ImageNote ) 
				nImage++;
		}
		
		return name + ":" + nText + ":" + nImage;
	}
	
	public boolean equals(Object obj) {
		if (this == obj ) 
			return true;
		else
			return false;
	}
}
