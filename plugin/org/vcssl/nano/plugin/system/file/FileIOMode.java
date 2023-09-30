/*
 * Author:  RINEARN (Fumihiro Matsui), 2022
 * License: CC0
 */

package org.vcssl.nano.plugin.system.file;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * An enum representing modes of file I/O.
 */
public enum FileIOMode {

	/** Represents the mode for reading contents from a text file. */
	READ,

	/** Represents the mode for wrting contents to a text file. */
	WRITE,

	/** Represents the mode for appending contents to a text file. */
	APPEND,

	/** Represents the mode for reading contents from a numerical TSV file. */
	READ_TSV,

	/** Represents the mode for writing contents to a numerical TSV file. */
	WRITE_TSV,

	/** Represents the mode for appending contents to a numerical TSV file. */
	APPEND_TSV,

	/** Represents the mode for reading contents from a numerical CSV file. */
	READ_CSV,

	/** Represents the mode for writing contents to a numerical CSV file. */
	WRITE_CSV,

	/** Represents the mode for appending contents to a numerical CSV file. */
	APPEND_CSV,

	/** Represents the mode for reading contents from a numerical "Tabs or Spaces Separated Values" file. */
	READ_STSV,

	/** Represents the file has not been opened yet, or had already closed. */
	UNOPEND_OR_CLOSED;
	
	/** The Set containing all modes for reading contents from files. */
	public static final Set<FileIOMode> READ_MODE_SET= new HashSet<FileIOMode>() {{
		add(READ);
		add(READ_TSV);
		add(READ_CSV);
		add(READ_STSV);
	}};
	
	/** The Set containing all modes for writing contents to files. */
	public static final Set<FileIOMode> WRITE_MODE_SET= new HashSet<FileIOMode>() {{
		add(WRITE);
		add(WRITE_TSV);
		add(WRITE_CSV);
	}};
	
	/** The Set containing all modes for appending contents to files. */
	public static final Set<FileIOMode> APPEND_MODE_SET= new HashSet<FileIOMode>() {{
		add(APPEND);
		add(APPEND_TSV);
		add(APPEND_CSV);
	}};

	/** The Map for converting a mode-specifier string ("w" for WRITE, and so on) to the corresponding element of this enum. */
	public static final Map<String, FileIOMode> SPECIFIER_ENUM_MAP = new HashMap<String, FileIOMode>() {{
		put("r", READ);
		put("w", WRITE);
		put("a", APPEND);
		put("rcsv", READ_CSV);
		put("wcsv", WRITE_CSV);
		put("acsv", APPEND_CSV);
		put("rtsv", READ_TSV);
		put("wtsv", WRITE_TSV);
		put("atsv", APPEND_TSV);
		put("rstsv", READ_STSV);
	}};
}
