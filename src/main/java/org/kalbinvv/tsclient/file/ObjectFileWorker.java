package org.kalbinvv.tsclient.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.kalbinvv.tscore.test.TestData;

public class ObjectFileWorker implements FileWorker{

	@Override
	public void save(Object object, File file) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public TestData loadTestData(File file) throws IOException, ClassNotFoundException{
		TestData object = null;
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		object = (TestData) objectInputStream.readObject();
		objectInputStream.close();
		return object;
	}

}
