/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * rewrite java.io.RandomAccessFile, add a buffer for java.io.RandomAccessFile<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class BufferedRandomAccessFile extends java.io.RandomAccessFile {

	private static final int DEFAULT_SIZE = 64 * 1024; // 64k
	private int bufSize = 0;
	private int lineSize = 256;// the maximum of the line
	private byte[] buffer = null;// buffer
	private long pos = 0;// current position of the file
	private long begOffset = 0;// begin position of the buffer in file
	private long endOffset = 0;// end position of the buffer in file

	public BufferedRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
		this.bufSize = DEFAULT_SIZE;
		init();
	}

	public BufferedRandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
		this.bufSize = DEFAULT_SIZE;
		init();
	}

	public BufferedRandomAccessFile(String name, String mode, int size)
			throws FileNotFoundException {
		super(name, mode);
		this.bufSize = size;
		init();
	}

	public BufferedRandomAccessFile(String name, String mode, int size, int lineSize)
			throws FileNotFoundException {
		super(name, mode);
		this.bufSize = size;
		this.lineSize = lineSize;
		init();
	}

	public void init() {
		buffer = new byte[bufSize];
	}

	private int fill() throws IOException {
		int len = super.read(buffer, 0, bufSize);

		if (len > 0) {
			begOffset = endOffset;
			endOffset += len;
			// endOffset = super.getFilePointer();
		}
		return len;
	}

	public int read() throws IOException {
		if (pos >= endOffset && fill() == -1) {
			return -1;
		}
		int index = (int) (pos++ - begOffset);
		return buffer[index];
	}

	private StringBuffer input = new StringBuffer(lineSize);

	public String getLine() throws IOException {
		String line = null;
		if (input.length() != 0) {
			input.delete(0, lineSize);// clear buffer
		}

		int bufPos = (int) (pos - begOffset);
		int bufEnd = (int) (endOffset - begOffset);
		int c = -1;
		boolean eol = false;
		int i = -1;
		int j = bufPos;
		do {
			if (j >= bufEnd) {
				break;
			}

			if (buffer[j] == '\n') {// '\n'
				i = j;
				break;
			}
			j++;
		} while (true);

		if (i < 0) {
			while (!eol) {
				switch (c = read()) {
				case -1:
				case '\n':
					eol = true;
					break;
				case '\r':
					eol = true;
					if ((read()) != '\n') {// windows \r\n
						pos--;
					}
					break;
				default:
					input.append((char) c);
					break;
				}
			}

			if ((c == -1) && (input.length() == 0)) {
				return null;
			}
			return input.toString();
		}

		if (i > 0 && i > bufPos && buffer[i - 1] == '\r') {// '\r'
			line = new String(buffer, bufPos, i - bufPos - 1);
		} else {
			line = new String(buffer, bufPos, i - bufPos);
		}
		pos = pos + (i - bufPos) + 1;

		return line;

	}

	public long getPos() {
		return pos;
	}

	public void seek(long pos) throws IOException {
		this.pos = pos;
		begOffset = pos;
		endOffset = pos;
		super.seek(pos);
		this.fill();
	}
}
