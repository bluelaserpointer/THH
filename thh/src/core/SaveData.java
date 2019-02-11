package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveData implements Serializable{
	private static final long serialVersionUID = 8084358389955267688L;
	
	public static final void save(SaveData save,File file) {
		try{ //データの書き込み
			if(!file.exists())
				file.createNewFile();
			final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(new SaveHolder(save));
			oos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static final SaveData load(File file) {
		if(!file.exists()) {
			System.out.println("File doesn't exist.");
			return null;
		}
		SaveHolder saveHolder = null;
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
			saveHolder = (SaveHolder)ois.readObject();
		}catch(IOException | ClassNotFoundException e){}
		if(saveHolder == null)
			System.out.println("Load Error.");
		return saveHolder.getData();
	}
}
final class SaveHolder implements Serializable{
	private static final long serialVersionUID = 8376623130315069281L;
	private final SaveData saveData;
	SaveHolder(SaveData saveData){
		this.saveData = saveData;
	}
	SaveData getData(){
		return saveData;
	}
}
