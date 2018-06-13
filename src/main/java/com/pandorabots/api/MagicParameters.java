/**
 * MagicParameters.java
 */
package com.pandorabots.api;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class MagicParameters {
	/** Version. */
	private String version = "0.0.6";

	/** The User Key assigned by Pandorabots. */
	private String userKey = "unknown";

	/** The Application ID assigned by Pandorabots. */
	private String appId = "unknown";

	/** server name of pandorabots API */
	private String hostName = "unknown";

	/** flag to indicate verbosity of output. */
	private boolean debug = false;

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return the userKey
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Constructor.
	 * 
	 * <p>
	 * Read config.txt in user.dir directory.
	 * </p>
	 * 
	 */
	public MagicParameters() {
		String cwd = System.getProperty("user.dir");
		readParameters(cwd + "/config.txt");
	}

	/**
	 * Read parameter file.
	 * 
	 * @param configFileName
	 * @since 0.0.1
	 */
	private void readParameters(String configFileName) {
		try {
			List<String> lines = readLines(configFileName,
					Charset.defaultCharset());
			for (String line : lines) {
				String[] pair = line.split(":");
				if (pair.length >= 2) {
					if (pair[0].equals("user_key"))
						userKey = pair[1];
					else if (pair[0].equals("app_id"))
						appId = pair[1];
					else if (pair[0].equals("hostname"))
						hostName = pair[1];
					else if (pair[0].equals("debug")) {
						if (pair[1].equals("true"))
							debug = true;
						else
							debug = false;
					}
					// System.out.println("pair =" + pair[0] + "," + pair[1]);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Helper for reading file as list of lines.
	 * 
	 * @param path
	 * @param encoding
	 * @return content of file as string
	 * @throws IOException
	 * @since 0.0.1
	 */
	private List<String> readLines(String path, Charset encoding)
			throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), encoding);
		return lines;
	}
}