/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 */

package org.vcssl.nano.plugin.system.file;

import org.vcssl.connect.ConnectorException;
import org.vcssl.connect.ConnectorFatalException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;


/**
 * A class performing file I/O from/to a file.
 */
public final class FileIOUnit {

	/** The file I/O mode. */
	private FileIOMode mode = FileIOMode.UNOPEND_OR_CLOSED;

	/** The string representing the mode (e.g.: "r" for READ). */
	private String modeSpecifier = null;

	/** The file from/to which this unit performs file I/O. */
	private File file = null;

	/** The name of the character encoding of the file. */
	private String encodingName = null;

	/** The line-feed code. */
	private String lineFeedCode = null;

	/** The input stream for inputting the content of the file. */
	private FileInputStream fileInputStream = null;

	/** Mediates between the fileInputStream and the bufferedReader. */
	private InputStreamReader inputStreamReader = null;

	/** The reader for reading contents from the file. */
	private BufferedReader bufferedReader = null;

	/** The input stream for outputting the content of the file. */
	private FileOutputStream fileOutputStream = null;

	/** Mediates between the fileOutputStream and the bufferedWriter. */
	private OutputStreamWriter outputStreamWriter = null;

	/** The writter for writing contents to the file. */
	private BufferedWriter bufferedWriter = null;

	/** The locale to determine the language of error messages. */
	private Locale locale = null;

	/** The flag for switching the language of error messages to Japanese. */
	private boolean isJapanese = false;

	/** The length of the reading buffer array. */
	private static final int READ_BUFFER_LENGTH = 4096;


	/**
	 * Creates a new file I/O unit.
	 */
	public FileIOUnit() {
		this.setLocale(Locale.getDefault());
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
	 * Gets the file I/O mode, specified when this instance opened the file.
	 * 
	 * @return The file I/O mode.
	 */
	public synchronized FileIOMode getFileIOMode() {
		return this.mode;
	}


	/**
	 * Gets the File instance, specified when this instance opened the file.
	 * 
	 * @return The File instance.
	 */
	public synchronized File getFile() {
		return this.file;
	}


	/**
	 * Opens the specified file.
	 * 
	 * @param filePath The file to be opened.
	 * @param modeSpecifier The string for specifying the file I/O mode (e.g.: "w" for WRITE, "r" for READ, and so on).
	 * @param encodingName The name of the character encoding of the file.
	 * @param lineFeedCode The line-feed code of the file.
	 * @throws ConnectorException Thrown when it has failed to open the file.
	 */
	public synchronized void open(String filePath, String modeSpecifier, String encodingName, String lineFeedCode)
			throws ConnectorException {

		if (!FileIOMode.SPECIFIER_ENUM_MAP.containsKey(modeSpecifier)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルI/Oモードはサポートされていません： " + modeSpecifier);
			} else {
				throw new ConnectorException("The specified file I/O mode is unsupported: " + modeSpecifier);
			}
		}
		this.mode = FileIOMode.SPECIFIER_ENUM_MAP.get(modeSpecifier);
		this.modeSpecifier = modeSpecifier;
		this.file = new File(filePath);
		this.encodingName = encodingName;
		this.lineFeedCode = lineFeedCode;

		// The file must already exists, excluding when writing modes are specified.
		// (Writing modes create a new file if it does not exist.)
		if (!file.exists() && !FileIOMode.WRITE_MODE_SET.contains(mode)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルが見つかりません： " + filePath);
			} else {
				throw new ConnectorException("The specified file is not found: " + filePath);
			}
		}

		// We can't perform file I/O from/to a directory.
		if (file.isDirectory()) {
			if (this.isJapanese) {
				throw new ConnectorException("ファイルを指定する必要がある引数に、フォルダが指定されました： " + filePath);
			} else {
				throw new ConnectorException("A file must be specified, but a directory (folder) is specified: " + filePath);
			}
		}

		// Open the specified file with the specified mode/encoding.
		if (FileIOMode.READ_MODE_SET.contains(mode)) {
			this.openForReading();

		} else if (FileIOMode.WRITE_MODE_SET.contains(mode)) {
			boolean appendsToCurrentContent = false;
			this.openForWriting(appendsToCurrentContent);
			
		} else if (FileIOMode.APPEND_MODE_SET.contains(mode)) {
			boolean appendsToCurrentContent = true;
			this.openForWriting(appendsToCurrentContent);
			
		} else {
			throw new ConnectorFatalException("Unexpected file I/O mode: " + mode);
		}
	}


	/**
	 * Opens the file for reading.
	 * 
	 * @throws ConnectorException Thrown when it has failed to open the file.
	 */
	private synchronized void openForReading() throws ConnectorException {
		try {
			this.fileInputStream = new FileInputStream(file);
		} catch (IOException ioe) {
			this.mode = FileIOMode.UNOPEND_OR_CLOSED;
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルを開く際に、エラーが発生しました： " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An error has occurred when opening the specified file: " + this.file.getPath(), ioe);
			}
		}

		try {
			this.inputStreamReader = new InputStreamReader(this.fileInputStream, this.encodingName);
		} catch (UnsupportedEncodingException uee) {
			this.mode = FileIOMode.UNOPEND_OR_CLOSED;
			try {
				this.fileInputStream.close();
			} catch (IOException ioe2) {
				if (this.isJapanese) {
					throw new ConnectorException("指定されたファイルを閉じる際に、エラーが発生しました： " + this.file.getPath(), ioe2);
				} else {
					throw new ConnectorException("An error has occurred when closing the specified file: " + this.file.getPath(), ioe2);
				}
			}
			if (this.isJapanese) {
				throw new ConnectorException("指定された文字コード「 " + this.encodingName + " 」はサポートされていません。");
			} else {
				throw new ConnectorException("The specified caracter encoding \"" + this.encodingName + "\" is unsupported.");
			}
		}

		this.bufferedReader = new BufferedReader(this.inputStreamReader);
	}

	
	/**
	 * Opens the file for writing.
	 * 
	 * @param appendsToCurrentContent Specify true for appending contents to the end of the current contents of the file.
	 * @throws ConnectorException Thrown when it has failed to open the file.
	 */
	private synchronized void openForWriting(boolean appendsToCurrentContent) throws ConnectorException {
		try {
			this.fileOutputStream = new FileOutputStream(this.file, appendsToCurrentContent);
		} catch (IOException ioe) {
			this.mode = FileIOMode.UNOPEND_OR_CLOSED;
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルを開く際に、エラーが発生しました： " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An error has occurred when opening the specified file: " + this.file.getPath(), ioe);
			}
		}

		try {
			this.outputStreamWriter = new OutputStreamWriter(this.fileOutputStream, this.encodingName);
		} catch (UnsupportedEncodingException uee) {
			this.mode = FileIOMode.UNOPEND_OR_CLOSED;
			try {
				this.fileOutputStream.close();
			} catch (IOException ioe2) {
				if (this.isJapanese) {
					throw new ConnectorException("指定されたファイルを閉じる際に、エラーが発生しました： " + this.file.getPath(), ioe2);
				} else {
					throw new ConnectorException("An error has occurred when closing the specified file: " + this.file.getPath(), ioe2);
				}
			}
			if (this.isJapanese) {
				throw new ConnectorException("指定された文字コード「 " + this.encodingName + " 」はサポートされていません。");
			} else {
				throw new ConnectorException("The specified caracter encoding \"" + this.encodingName + "\" is unsupported.");
			}
		}

		this.bufferedWriter = new BufferedWriter(this.outputStreamWriter);
	}


	/**
	 * Closes the file.
	 * 
	 * @throws ConnectorException Thrown when it has failed to close the file.
	 */
	public synchronized void close() throws ConnectorException {
		if (FileIOMode.READ_MODE_SET.contains(this.mode)) {
			try {
				this.fileInputStream.close();
				this.inputStreamReader.close();
				this.bufferedReader.close();
			} catch (IOException ioe) {
				if (this.isJapanese) {
					throw new ConnectorException("指定されたファイルを閉じる際に、エラーが発生しました： " + this.file.getPath(), ioe);
				} else {
					throw new ConnectorException("An error has occurred when closing the specified file: " + this.file.getPath(), ioe);
				}
			}
		} else if (FileIOMode.APPEND_MODE_SET.contains(this.mode) || FileIOMode.WRITE_MODE_SET.contains(this.mode)) {
			try {
				this.bufferedWriter.flush();
				this.fileOutputStream.close();
				this.outputStreamWriter.close();
				this.bufferedWriter.close();
			} catch (IOException ioe) {
				if (this.isJapanese) {
					throw new ConnectorException("指定されたファイルを閉じる際に、エラーが発生しました： " + this.file.getPath(), ioe);
				} else {
					throw new ConnectorException("An error has occurred when closing the specified file: " + this.file.getPath(), ioe);
				}
			}
		} else {
			throw new ConnectorFatalException("Unexpected file I/O mode: " + this.mode);
		}
	}


	/**
	 * Writes the specified contents to the file.
	 * 
	 * If the mode of this instance is WRITE, all elements in "contents" array will be written in the file without being delimited.
	 * If the mode is WRITE_CSV, the elements will be written with delimited by "," (comma).
	 * If the mode is WRITE_TSV, the elements will be written with delimited by "\t" (tab space).
	 * 
	 * @param contents The contents to be written to the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized void write(String[] contents) throws ConnectorException {
		if (this.mode == null || this.mode == FileIOMode.UNOPEND_OR_CLOSED) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、まだ開かれていないか、既に閉じられた状態です。");
			} else {
				throw new ConnectorException("The specified file has not been not opened yet, or already closed. Please open the file by \"open\" function.");
			}
		}
		if (!FileIOMode.APPEND_MODE_SET.contains(this.mode) && !FileIOMode.WRITE_MODE_SET.contains(this.mode)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、書き込み可能なモード（WRITE, APPEND, WRITE_CSV, ...等）で開かれていません： " + this.file.getPath());
			} else {
				throw new ConnectorException("The specified file is not opened in \"writable\" modes (WRITE, APPEND, WRITE_CSV, ... etc.): " + this.file.getPath());
			}
		}

		try {
			switch (this.mode) {
				case WRITE: {
					for (String content: contents) {
						this.bufferedWriter.write(content);
					}
					return;
				}
				case WRITE_TSV: {
					for (int icontent=0; icontent<contents.length; icontent++) {
						this.bufferedWriter.write(contents[icontent]);
						if (icontent != contents.length - 1) {
							this.bufferedWriter.write('\t');
						}
					}
					return;
				}
				case WRITE_CSV: {
					for (int icontent=0; icontent<contents.length; icontent++) {
						this.bufferedWriter.write(contents[icontent]);
						if (icontent != contents.length - 1) {
							this.bufferedWriter.write(',');
						}
					}
					return;
				}
				default: {
					throw new ConnectorFatalException("Unexpected file I/O mode: " + mode);
				}
			}

		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルへの書き込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for writing contents to the specified file: " + this.file.getPath(), ioe);
			}
		}
	}


	/**
	 * Writes the specified contents to the file, and go to the next line.
	 * 
	 * If the mode of this instance is WRITE, all elements in "contents" array will be written in the file without being delimited.
	 * If the mode is WRITE_CSV, the elements will be written with delimited by "," (comma).
	 * If the mode is WRITE_TSV, the elements will be written with delimited by "\t" (tab).
	 * 
	 * @param contents The contents to be written to the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized void writeln(String[] contents) throws ConnectorException {
		this.write(contents);
		
		try {
			this.bufferedWriter.write(this.lineFeedCode);
		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルへの書き込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for writing contents to the specified file: " + this.file.getPath(), ioe);
			}
		}
	}

	
	/**
	 * Flushes the currently stored contents in the writing buffer.
	 * 
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized void flush() throws ConnectorException {
		if (this.mode == null || this.mode == FileIOMode.UNOPEND_OR_CLOSED) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、まだ開かれていないか、既に閉じられた状態です。");
			} else {
				throw new ConnectorException("The specified file has not been not opened yet, or already closed. Please open the file by \"open\" function.");
			}
		}
		if (!FileIOMode.APPEND_MODE_SET.contains(this.mode) && !FileIOMode.WRITE_MODE_SET.contains(this.mode)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、書き込み可能なモード（WRITE, APPEND, WRITE_CSV, ...等）で開かれていません： " + this.file.getPath());
			} else {
				throw new ConnectorException("The specified file is not opened in \"writable\" modes (WRITE, APPEND, WRITE_CSV, ... etc.): " + this.file.getPath());
			}
		}
		
		try {
			this.bufferedWriter.flush();
		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルへの書き込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for writing contents to the specified file: " + this.file.getPath(), ioe);
			}
		}
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
	 * @return The (delimited) contents of a line read from the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized String[] readln() throws ConnectorException {
		if (this.mode == null || this.mode == FileIOMode.UNOPEND_OR_CLOSED) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、まだ開かれていないか、既に閉じられた状態です。");
			} else {
				throw new ConnectorException("The specified file has not been not opened yet, or already closed. Please open the file by \"open\" function.");
			}
		}
		if (!FileIOMode.READ_MODE_SET.contains(this.mode)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、読み込み可能なモード（READ, READ_CSV, ...等）で開かれていません： " + this.file.getPath());
			} else {
				throw new ConnectorException("The specified file is not opened in \"readable\" modes (READ, READ_CSV, ... etc.): " + this.file.getPath());
			}
		}

		// Read one line from the file.
		String line = null;
		try {
			line = this.bufferedReader.readLine();
			if (line == null) {
				if (this.isJapanese) {
					throw new ConnectorException("指定されたファイルから次の一行を読み込もうとしましたが、残りの行がもうありません： " + this.file.getPath());
				} else {
					throw new ConnectorException("Tried to read the next line from the specified file, but no lines are remained: " + this.file.getPath());
				}
			}
		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルからの読み込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for reading contents from the specified file: " + this.file.getPath(), ioe);
			}
		}

		// Split the line by the delimiter, and return them.
		switch (this.mode) {
			case READ: {
				return new String[] { line };
			}
			case READ_CSV: {
				return line.split(",", -1);
			}
			case READ_TSV: {
				return line.split("\\t", -1);
			}
			case READ_STSV: {
				return line.split("\\s+", -1);
			}
			default: {
				throw new ConnectorFatalException("Unexpected file I/O mode: " + mode);
			}
		}
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
	 * @return The (delimited) contents of all contents read from the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized String[] read() throws ConnectorException {
		if (this.mode == null || this.mode == FileIOMode.UNOPEND_OR_CLOSED) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、まだ開かれていないか、既に閉じられた状態です。");
			} else {
				throw new ConnectorException("The specified file has not been not opened yet, or already closed. Please open the file by \"open\" function.");
			}
		}
		if (!FileIOMode.READ_MODE_SET.contains(this.mode)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、読み込み可能なモード（READ, READ_CSV, ...等）で開かれていません： " + this.file.getPath());
			} else {
				throw new ConnectorException("The specified file is not opened in \"readable\" modes (READ, READ_CSV, ... etc.): " + this.file.getPath());
			}
		}

		// If the mode is READ, simply read all contents, and return it as an array of which length is 1.
		try {
			if (this.mode == FileIOMode.READ) {
				StringBuilder resultBuilder = new StringBuilder();
				int readBufferLength = READ_BUFFER_LENGTH;
				char[] readBuffer = new char[readBufferLength];
				int readSucceededLength = 0;
				while ( (readSucceededLength = this.bufferedReader.read(readBuffer, 0, readBufferLength)) != -1 ) {
					for (int ichar=0; ichar<readSucceededLength; ichar++) {
						resultBuilder.append(readBuffer[ichar]);
					}
				}
				return new String[]{ resultBuilder.toString() };
			}
		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルからの読み込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for reading contents from the specified file: " + this.file.getPath(), ioe);
			}
		}

		// For other modes, split each lines by the delimiter, 
		// and store all splitted values in all lines in an array to be returned.
		try {
			List<String> resultList = new ArrayList<String>();
			String line = null;

			// Read each line:
			while ((line = this.bufferedReader.readLine()) != null) {

				String values[] = null;
				switch (this.mode) {
					case READ_CSV: {
						values = line.split(",", -1);
						break;
					}
					case READ_TSV: {
						values = line.split("\\t", -1);
						break;
					}
					case READ_STSV: {
						values = line.split("\\s+", -1);
						break;
					}
					default: {
						throw new ConnectorFatalException("Unexpected file I/O mode: " + mode);
					}
				}
				for (String value: values) {
					resultList.add(value);
				}
			}

			// Extract all values in all lines, and return them.
			String[] resultArray = new String[ resultList.size() ];
			resultArray = resultList.toArray(resultArray);
			return resultArray;

		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルからの読み込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for reading contents from the specified file: " + this.file.getPath(), ioe);
			}
		}
	}


	/**
	 * Counts up the total number of lines in the file, and reopens the file.
	 * 
	 * @return The total number of lines in the file.
	 * @throws ConnectorException Thrown when any I/O error occurred, or when the file is opened by unwritable modes.
	 */
	public synchronized int countln() throws ConnectorException {
		if (this.mode == null || this.mode == FileIOMode.UNOPEND_OR_CLOSED) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、まだ開かれていないか、既に閉じられた状態です。");
			} else {
				throw new ConnectorException("The specified file has not been not opened yet, or already closed. Please open the file by \"open\" function.");
			}
		}
		if (!FileIOMode.READ_MODE_SET.contains(this.mode)) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルは、読み込み可能なモード（READ, READ_CSV, ...等）で開かれていません： " + this.file.getPath());
			} else {
				throw new ConnectorException("The specified file is not opened in \"readable\" modes (READ, READ_CSV, ... etc.): " + this.file.getPath());
			}
		}

		// Read all lines from the file, and count up them.
		int totalNumberOfLines = 0;
		try {
			while (this.bufferedReader.readLine() != null) {
				totalNumberOfLines++;
			}
		} catch (IOException ioe) {
			if (this.isJapanese) {
				throw new ConnectorException("指定されたファイルからの読み込み処理で、I/Oエラーが発生しました: " + this.file.getPath(), ioe);
			} else {
				throw new ConnectorException("An I/O error occurred for reading contents from the specified file: " + this.file.getPath(), ioe);
			}
		}

		// Reopen the file.
		String filePathStock = this.file.getPath();
		String modeSpecStock = this.modeSpecifier;
		String encodingStock = this.encodingName;
		String lineFeedStock = this.lineFeedCode;
		this.close();
		this.open(filePathStock, modeSpecStock, encodingStock, lineFeedStock);

		return totalNumberOfLines;
	}
}
