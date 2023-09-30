/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 */

package org.vcssl.nano.plugin.system.file;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Locale;


/**
 * A class managing file I/O from/to multiple files.
 * 
 * File I/O from/to to each file is performed by FileIOUnit.
 * This object internally has a list of instances of FileIOUnit.
 * An integer "file ID" is assigned for each file/FileIOUnit.
 * 
 * Methods of this class receives the file ID as an argument,
 * and performs the file I/O from/to the corresponding file/FileIOUnit.
 * 
 * Note that, the "file ID" we using here is different from the file ID assigned by OS.
 * Our file ID will be assigned in ascending order, and the firstly opened file has the ID "1".
 * (If a file was closed, its index will be assigned (reused) to the nextly opened file.)
 */
public class FileIOHub {

	/** The list storing all FileIOUnit instances. The index of an element in this list is: "file ID".*/
	private List<FileIOUnit> fileIOUnitList = null;

	/** The queue storing file IDs of closed files, for reusing. */
	private Deque<Integer> closedFileIdQueue = null;

	/** The locale to determine the language of error messages. */
	private Locale locale = null;

	/** The flag for switching the language of error messages to Japanese. */
	private boolean isJapanese = false;


	/**
	 * Creates a new file I/O hub.
	 */
	public FileIOHub() {
		this.setLocale(Locale.getDefault());
		this.initializeResources();
	}


	/**
	 * Sets the locale, for switching the language of error messages.
	 * 
	 * @param locale The locale for specifying the language of error messages.
	 */
	public synchronized void setLocale(Locale locale) {
		this.locale = locale;
		this.isJapanese =
			( locale.getLanguage()!=null && locale.getLanguage().equals("ja") )
			||
			( locale.getCountry()!=null && locale.getCountry().equals("JP") );
	}


	/**
	 * Initializes the resources of this instance.
	 */
	public synchronized void initializeResources() {
		this.fileIOUnitList = new ArrayList<FileIOUnit>();
		this.closedFileIdQueue = new ArrayDeque<Integer>();

		// The file ID "0" is unavailable, so put the placeholder instance at [0] in the list.
		// (For preventing the use of uninitialized ID variable in scripts.)
		FileIOUnit placeholderUnit = new FileIOUnit();
		placeholderUnit.setLocale(this.locale);
		this.fileIOUnitList.add(placeholderUnit);
	}


	/**
	 * Disposes the resources of this instance.
	 * 
	 * If opened files exist when this method has been called,
	 * they will be closed before disposing file I/O units.
	 * 
	 * After using this method, if you want to reuse this instance,
	 * call initializeResources() method again.
	 * 
	 * @throws ConnectorException Thrown when any error has occurred for file closing processes.
	 */
	public synchronized void disposeResources() throws ConnectorException {

		// The flag representing whether any error occurred for closing processes of files.
		boolean closeFailedFileExists = false;

		// Stores the paths of the files which could not be closed by errors.
		StringBuilder closeFailedFilePathBuilder = new StringBuilder();

		// Close all files.
		if (this.fileIOUnitList != null) {
			for (FileIOUnit unit: this.fileIOUnitList) {
				if (unit != null && unit.getFileIOMode() != FileIOMode.UNOPEND_OR_CLOSED) {
					try {
						unit.close();
					} catch (ConnectorException e) {
						if (closeFailedFileExists) {
							closeFailedFilePathBuilder.append(", ");
						}
						closeFailedFileExists = true;
						closeFailedFilePathBuilder.append(unit.getFile().getPath());
					}
				}
			}
		}

		// Free the list and the queue.
		this.fileIOUnitList = null;
		this.closedFileIdQueue = null;

		// If any error occurred for closing processes of files, throw an exception.
		if (closeFailedFileExists) {
			if (this.isJapanese) {
				throw new ConnectorException("ファイルを閉じられませんでした： " + closeFailedFilePathBuilder.toString());
			} else {
				throw new ConnectorException("File closing process has failed: " + closeFailedFilePathBuilder.toString());
			}
		}
	}


	/**
	 * Checks that the file ID is valid, and then gets the corresponding file I/O unit from the list.
	 * 
	 * @param fileId The file ID corresponding with the file I/O unit to get.
	 * @return The file I/O unit corresponding with the specified file ID.
	 * @throws ConnectorException throws if the specified ID is invalid.
	 */
	private FileIOUnit checkAndGetUnit(int fileId) throws ConnectorException {
		if (fileId == 0) {
			if (this.isJapanese) {
				throw new ConnectorException("ファイルID「 0 」が指定されましたが、0 番は存在しません。IDを格納する変数が、未初期化の可能性があります。");
			} else {
				throw new ConnectorException("The file ID \"0\" is specified, but 0-th file does not exist. Maybe the variable storing the ID has not been initialized yet.");
			}
		}

		if (fileId < 0) {
			if (this.isJapanese) {
				throw new ConnectorException("負のファイルIDが指定されましたが、負のIDは存在しません。指定する変数を間違っている可能性があります。");
			} else {
				throw new ConnectorException("The negative file ID is specified, but a negative ID never exists. Maybe an incorrect variable is specified for the ID.");
			}
		}

		if (this.fileIOUnitList.size() <= fileId) {
			if (this.isJapanese) {
				throw new ConnectorException("ファイルID「" + fileId + "」が指定されましたが、そのIDのはまだ割り当てられていません。");
			} else {
				throw new ConnectorException("The file ID \"" + fileId + "\" is specified, but \"" + fileId + "\" has not been assigned yet.");
			}
		}

		FileIOUnit unit = this.fileIOUnitList.get(fileId);
		if (unit == null || unit.getFileIOMode() == FileIOMode.UNOPEND_OR_CLOSED) {
			if (this.isJapanese) {
				throw new ConnectorException("ファイルID「" + fileId + "」が指定されましたが、そのIDのファイルは開かれていないか、既に閉じられています。");
			} else {
				throw new ConnectorException("The file ID \"" + fileId + "\" is specified, but \"" + fileId + "\"-th file was already closed, or has not been opened yet.");
			}
		}

		return unit;
	}


	/**
	 * Opens the specified file, and assign a new file index to the file.
	 * 
	 * @param filePath The file to be opened.
	 * @param modeSpecifier The string for specifying the file I/O mode (e.g.: "w" for WRITE, "r" for READ, and so on).
	 * @param encodingName The name of the character encoding of the file.
	 * @param lineFeedCode The line-feed code of the file.
	 * @return The file index assigned to the opened file.
	 * @throws ConnectorException Thrown when it has failed to open the file.
	 */
	public synchronized int open(String filePath, String modeSpecifier, String encoding, String lineFeedCode) throws ConnectorException {

		// Create a new File I/O unit, and open the specified file.
		FileIOUnit unit = new FileIOUnit();
		unit.setLocale(this.locale);
		unit.open(filePath, modeSpecifier, encoding, lineFeedCode);

		// Store the file I/O unit in the list as an element, and return its index.
		if (1 <= this.closedFileIdQueue.size()) {
			int fileId = this.closedFileIdQueue.poll();
			this.fileIOUnitList.set(fileId, unit);
			return fileId;
		} else {
			int fileId = this.fileIOUnitList.size();
			this.fileIOUnitList.add(unit);
			return fileId;
		}
	}


	/**
	 * Close the file.
	 * 
	 * @param fileId The file ID of the file to be closed.
	 * @throws ConnectorException Thrown when any error has occurred for the closing process.
	 */
	public synchronized void close(int fileId) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		unit.close();

		this.fileIOUnitList.set(fileId, null);
		this.closedFileIdQueue.add(fileId);
	}


	/**
	 * Writes the specified contents to the file.
	 * 
	 * If the mode of this instance is WRITE, all elements in "contents" array will be written in the file without being delimited.
	 * If the mode is WRITE_CSV, the elements will be written with delimited by "," (comma).
	 * If the mode is WRITE_TSV, the elements will be written with delimited by "\t" (tab space).
	 * 
	 * @param fileId The file ID of the file to be closed.
	 * @param contents The contents to be written to the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized void write(int fileId, String[] contents) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		unit.write(contents);
	}


	/**
	 * Writes the specified contents to the file, and go to the next line.
	 * 
	 * If the mode of this instance is WRITE, all elements in "contents" array will be written in the file without being delimited.
	 * If the mode is WRITE_CSV, the elements will be written with delimited by "," (comma).
	 * If the mode is WRITE_TSV, the elements will be written with delimited by "\t" (tab space).
	 * 
	 * @param fileId The file ID of the file to be closed.
	 * @param contents The contents to be written to the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized void writeln(int fileId, String[] contents) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		unit.writeln(contents);
	}


	/**
	 * Flushes the currently stored contents in the writing buffer.
	 * 
	 * @param fileId The file ID of the file to be flushed.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized void flush(int fileId) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		unit.flush();
	}


	/**
	 * Reads contents of one line from to the file.
	 * 
	 * If the mode of this instance is READ, whole the read line will be returned as an array of which length is 1.
	 * If the mode is READ_CSV, text of a line will be splitted by "," (comma), and they will be returned as an array.
	 * If the mode is READ_TSV, text of a line will be splitted by "\t" (tab), and they will be returned as an array.
	 * If the mode is READ_STSV, text of a line will be splitted by (may be sequential) tabs or spaces, 
	 * and they will be returned as an array.
	 * 
	 * @param fileId The file ID of the file to be flushed.
	 * @return The (delimited) contents of a line read from the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized String[] readln(int fileId) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		return unit.readln();
	}


	/**
	 * Reads all contents from to the file.
	 * 
	 * If the mode of this instance is READ, whole the contnts in the file will be returned as an array of which length is 1.
	 * If the mode is READ_CSV, text of all lines will be splitted by "," (comma), and they will be returned as an array.
	 * If the mode is READ_TSV, text of all lines line will be splitted by "\t" (tab), and they will be returned as an array.
	 * If the mode is READ_STSV, text of all lines line will be splitted by (may be sequential) tabs or spaces, 
	 * and they will be returned as an array.
	 * 
	 * @param fileId The file ID of the file to be flushed.
	 * @return The (delimited) contents of all contents read from the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized String[] read(int fileId) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		return unit.read();
	}


	/**
	 * Reads all contents from to the file.
	 * 
	 * If the mode of this instance is READ, whole the contnts in the file will be returned as an array of which length is 1.
	 * If the mode is READ_CSV, text of all lines will be splitted by "," (comma), and they will be returned as an array.
	 * If the mode is READ_TSV, text of all lines line will be splitted by "\t" (tab), and they will be returned as an array.
	 * If the mode is READ_STSV, text of all lines line will be splitted by (may be sequential) tabs or spaces, 
	 * and they will be returned as an array.
	 * 
	 * @param fileId The file ID of the file to be flushed.
	 * @return The (delimited) contents of all contents read from the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized int countln(int fileId) throws ConnectorException {
		FileIOUnit unit = this.checkAndGetUnit(fileId);
		return unit.countln();
	}
}
