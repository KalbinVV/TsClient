package org.kalbinvv.tsclient.file;

import java.io.File;
import java.io.IOException;

import org.kalbinvv.tscore.test.TestData;

public interface FileWorker {

	public void save(Object object, File file);
	public TestData loadTestData(File file) throws IOException, ClassNotFoundException;

}
