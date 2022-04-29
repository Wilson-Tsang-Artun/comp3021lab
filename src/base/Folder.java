package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>, Serializable {
	private ArrayList<Note> notes;
	private String name;

	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}

	public void addNote(Note title) {
		notes.add(title);
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Note> getNotes() {
		return this.notes;
	}
	
	public boolean removeNote(String title) {
		// TODO
		// Given the title of the note, delete it from the folder.
		// Return true if it is deleted successfully, otherwise return false. 
		int noteIndex = 0;
		for(Note note : this.notes) {
			if(note.getTitle().equals(title)) {
				this.notes.remove(noteIndex);
				return true;
			}
			noteIndex++;
		}
		return false;
	}

	public String toString() {
		int nText = 0;
		int nImage = 0;

		for (Note note : notes) {
			if (note instanceof TextNote)
				nText++;
			else if (note instanceof ImageNote)
				nImage++;
		}

		return name + ":" + nText + ":" + nImage;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else
			return false;
	}

	@Override
	public int compareTo(Folder o) {
		return this.name.compareTo(o.name);
	}

	public void sortNotes() {
		Collections.sort(this.notes);
	}
	
	public boolean isContainingTextNote() {
		for(Note note : notes) {
			if(note instanceof TextNote) {
				return true;
			}
		}
		return false;
	}
	

	public List<Note> searchNotes(String keywords) {

		ArrayList<ArrayList<Note>> listOfList = new ArrayList<ArrayList<Note>>(); // list of list
		// java or lab attendance or session
		// java shit oh
		// java or lab   >>  add   ,  shit  >> retainAll, 
		// java attendance  >> retainAll
		// java or lab  >> add
		// java or lab session
		// list of [ ( java + lab ) ,  session list ] 
		// retainAll list of list
		
		String[] splitKey = keywords.toLowerCase().split(" ");

		ArrayList<Integer> orKeywordIndex = new ArrayList<>();
		ArrayList<Integer> indexOfOr = new ArrayList<>();
		for (int i = 0; i < splitKey.length; i++) {
			if (splitKey[i].equals("or")) {
				indexOfOr.add(i);
				orKeywordIndex.add(i);
				orKeywordIndex.add(i - 1);
				orKeywordIndex.add(i + 1);
			}
		}
		
		// its like list of tuples 
		// lets say  "A or B C"  >> this gives u (A,B).
		ArrayList<String[]> orKeyList = new ArrayList<>();
		for (int i = 0; i < indexOfOr.size(); i++) {
			int index = indexOfOr.get(i);
			String key1 = splitKey[index - 1];
			String key2 = splitKey[index + 1];
			String[] orKey = new String[] { key1, key2 };
			orKeyList.add(orKey);
		}

		// OR stuff!!!!!!
		for (int i = 0; i < orKeyList.size(); i++) {

			//orKeyList.get(0);   k1,k2
			String head = orKeyList.get(i)[0];
			String tail = orKeyList.get(i)[1];

			ArrayList<Note> tempOutputOrSet = new ArrayList<>();
			for (int j = 0; j < notes.size(); j++) {
				
				if (notes.get(j) instanceof TextNote ) {
					if ( ((TextNote) notes.get(j)).getTitle().toLowerCase().contains(head) 
							|| ((TextNote) notes.get(j)).getContent().toLowerCase().contains(head) 
							|| ((TextNote) notes.get(j)).getTitle().toLowerCase().contains(tail) 
							|| ((TextNote) notes.get(j)).getContent().toLowerCase().contains(tail) ) 
					{	
						tempOutputOrSet.add(notes.get(j));
					}
				}else if(notes.get(j) instanceof ImageNote ) {
					if ( ((ImageNote) notes.get(j)).getTitle().toLowerCase().contains(head) 
							|| ((ImageNote) notes.get(j)).getTitle().toLowerCase().contains(tail) )
					{
						tempOutputOrSet.add(notes.get(j));
					}
				}
				
				
			}
			listOfList.add(tempOutputOrSet);
		}
		
		//OR end

		for (int i = 0; i < splitKey.length; i++) {
			if( !orKeywordIndex.contains(i) ) {
				ArrayList<Note> tempOutput = new ArrayList<Note>();
				for (Note note : notes) {

					if(note instanceof TextNote ) {
						if ( ((TextNote) note).getTitle().toLowerCase().contains(splitKey[i]) 
								|| ((TextNote) note).getContent().toLowerCase().contains(splitKey[i]) ) 
							tempOutput.add(note);
					}else if(note instanceof ImageNote ) {
						if (((ImageNote) note).getTitle().toLowerCase().contains(splitKey[i])) 
							tempOutput.add(note);
					}
					
				}
				listOfList.add(tempOutput);
			}
		}

		for (int i = 0; i < listOfList.size(); i++) {
			listOfList.get(0).retainAll(listOfList.get(i));
		}

		return listOfList.get(0);

	}

}
