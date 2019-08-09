/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.tap3.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.log4j.Logger;

/**
 * asn1 decode/encode<br/>
 * type:
 * sequence 要求是连续的
 * sequence of 序列，有多个
 * 
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class Asn1 implements Cloneable, AutoCloseable {
	private static final Logger Log = Logger.getLogger(Asn1.class);

	private static final int X00 = 0X00;// 00011111
	private static final int X1F = 0X1F;// 00011111
	private static final int X7F = 0X7F;// 01111111
	private static final int X01 = 0X01;// 00000001
	private static final int X80 = 0X80;// 10000000, indefinite form

	private static final int X0F = 0X0F;// 00001111
	private static final int XF0 = 0XF0;// 00001111

	private AsnTag asnTag = new AsnTag();
	private AsnLen asnLen = new AsnLen();
	private InputStream in;

	public void open(final Path source, final int bufferSize) throws FileNotFoundException {
		in = new BufferedInputStream(new FileInputStream(source.toString()), bufferSize);
	}

	public void open(final byte[] content, final int bufferSize) throws FileNotFoundException {
		in = new ByteArrayInputStream(content);
	}

	public AsnTag decodeTag() throws IOException {
		asnTag.clear();
		int tag = in.read();
		if (tag == -1) {
			asnTag.tag = -1;
			return asnTag;
		}
		asnTag.length++;
		int retTagId = tag & X1F;
		asnTag.tag = retTagId;

		if ((tag & X1F) == X1F) {// more than 2 bytes, else 1 byte
			retTagId = 0;// initial
			while (true) {
				tag = in.read();
				asnTag.length++;
				retTagId <<= 7;
				retTagId |= (tag & X7F);

				if (((tag >> 7) & X01) == X00) {
					break;
				}
			}
			asnTag.tag = retTagId;
			return asnTag;
		}

		return asnTag;
	}

	public AsnLen decodeLength(final int type) throws IOException {
		asnLen.clear();
		int tag = in.read();
		asnLen.length++;

		// if (tag == X80) {// indefinite form
		// return 0;
		// }
		int length = tag;

		if (tag > 127) {
			// 82 04 0A result:length=1034
			// 82 length of 2bytes
			// 04 100
			// 0A 11010010 01010010
			// result 10001010010 = 1034
			length = 0;
			if (type == 1) {
				int len = tag & X7F;
				for (int i = 0; i < len; i++) {
					tag = in.read();
					asnLen.length++;
					length <<= 8;
					// length |= (tag & X7F);
					length |= tag;
				}
			} else {

			}
		}

		asnLen.cntLength = length;
		return asnLen;
	}

	public AsnLen decodeLength() throws IOException {
		return decodeLength(1);
	}

	public String decodeValue(final Type type, final int vLength) throws IOException {

		switch (type) {
		case INTEGER: {
			byte[] b = new byte[vLength];
			int len = in.read(b);
			if (len != vLength) {
				return null;
			}
			long ret = 0;
			for (int i = 0; i < vLength; i++) {
				ret <<= 8;
				// 字节会先转成long，所以要舍掉除低8位的字节
				ret |= (b[i] & 0xff);
			}
			return String.valueOf(ret);
		}
		case NUMBERSTRING:
		case ASCIISTRING: {
			byte[] retValue = new byte[vLength];
			int len = in.read(retValue);
			if (len != vLength) {
				return null;
			}
			if (len < vLength) {
				System.out.println("Error");
			}

			return new String(retValue);
		}
		case BCDSTRING: {
			byte[] retValue = new byte[vLength];
			int len = in.read(retValue);
			if (len != vLength) {
				return null;
			}
			StringBuffer sb = new StringBuffer();
			int s = 0;
			for (byte b : retValue) {
				s = (b >> 4) & X0F;// 11110000 -> 1111
				sb.append(s);
				s = b & X0F;// 00001111 ->00001111
				if (s == X0F) {
					return sb.toString();
				}
				sb.append(s);
			}
			return sb.toString();
		}
		case HEXSTRING: {
			byte[] retValue = new byte[vLength];
			int len = in.read(retValue);
			if (len != vLength) {
				return null;
			}
			StringBuffer sb = new StringBuffer();
			String s;
			for (byte b : retValue) {
				s = Integer.toHexString(b);
				sb.append(s);

			}
			return sb.toString();
		}
		default: {
			System.err.println("error...");
			return null;
		}
		}

	}

	public byte[] encodeTag() {

		return null;
	}

	public byte[] encodeLength() {

		return null;
	}

	public byte[] encodeValue() {

		return null;
	}

	public void close() throws IOException {
		in.close();
	}

	public void skip(long n) throws IOException {
		in.skip(n);
	}

	public long eof() throws IOException {
		return in.available();
	}

	public int read(byte b[]) throws IOException {
		return in.read(b, 0, b.length);
	}

}
