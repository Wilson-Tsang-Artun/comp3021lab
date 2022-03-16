package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TextNote extends Note implements Serializable{
	private String content;
	
	public TextNote(String title) {
		super(title);
	}
	
	public TextNote(String title, String content) {
		super(title);
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}

	private String getTextFromFile(String absolutePath) {
		String result = "";
		
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(absolutePath);
			System.out.println("Im handsome 1");
			in = new ObjectInputStream(fis);
			System.out.println("Im handsome 2");
			result = (String) in.readObject();
			System.out.println("Im handsome 3");
			in.close();
			System.out.println("Im handsome 4");
		}catch(Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public void exportTextToFile(String pathFolder) {

		if(pathFolder == "") {
			pathFolder = ".";
		}
		File file = new File( pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		}catch(Exception e){}
	}
	

	
	
}
