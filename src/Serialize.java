import java.io.*;

public class Serialize<T> {

	public Serialize() {

	}

	public void save(T obj, String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public T load(String path) {
		T obj;
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = (T) in.readObject();
			in.close();
			fileIn.close();
		} catch (EOFException e) {
			obj = null;
		} catch (IOException i) {
			i.printStackTrace();
			obj = null;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			obj = null;
		}
		return obj;
	}
}
