/**
 * IK 中文分词  版本 5.0.1
 * IK Analyzer release 5.0.1
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 * 
 */
package org.wltea.analyzer.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IKAnalzyerDemo {
	Token e;
	static FileOutputStream o = null;

	public static void parse(String fileName) throws FileNotFoundException {
		Analyzer analyzer = new IKAnalyzer(true);
		File out_file = new File("C:\\Users\\sh04596\\Desktop\\inputout.txt");
		if (out_file.exists()) {
			out_file.delete();
		}
		o = new FileOutputStream("C:\\Users\\sh04596\\Desktop\\inputout.txt", true);
		File file = new File(fileName);
		if (!file.exists())
			return;

		BufferedReader reader = null;
		TokenStream ts = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;

			while ((tempString = reader.readLine()) != null) {
				ts = analyzer.tokenStream("myfield", new StringReader(tempString));
				// 获取词元位置属性
				OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
				// 获取词元文本属性
				CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
				// 获取词元文本属性
				TypeAttribute type = ts.addAttribute(TypeAttribute.class);

				// 重置TokenStream（重置StringReader）
				ts.reset();
				System.out.println(line + "-" + tempString);
				print("\n" + tempString);
				int current = -1;
				String result = "";
				while (ts.incrementToken()) {
					if (offset.endOffset() - offset.startOffset() > 1) {
						current = offset.endOffset() - 1;
						if (!result.equals("")) {
							print(result);
							result = "";
						}
						continue;
					}

					// System.out.println(offset.startOffset() + " - " +
					// offset.endOffset() + " : " + term.toString()
					// + " | " + type.type());

					System.out.println(offset.startOffset() + " : " + term.toString());
					if ((offset.startOffset() - current) == 1)
						result += term.toString();
					else {
						if (!result.equals("")) {
							print(result);
							result = term.toString();
						}
					}
					current = offset.startOffset();
				}
				if (!result.equals(""))
					print(result);
				line++;
				o.flush();
			}

			// o.write("\n\r".getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放TokenStream的所有资源
			if (ts != null) {
				try {
					ts.end();
					ts.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

	}

	public static void print(String s) throws IOException {
		if (s != null && !s.equals("") && s.length() > 1) {
			System.out.println(s);
			o.write(s.getBytes());
			o.write(" ".getBytes());
		}
		// System.out.println(s);
	}

	public static void main(String[] args) throws FileNotFoundException {
//		String filename = "C:\\Users\\sh04596\\Desktop\\input.txt";
//		parse(filename);
		test("个火车");
	}

	public static void test(String line) {
		Analyzer analyzer = new IKAnalyzer(true);

		BufferedReader reader = null;
		TokenStream ts = null;
		try {
			ts = analyzer.tokenStream("myfield", new StringReader(line));
			// 获取词元位置属性
			OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
			// 获取词元文本属性
			CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
			// 获取词元文本属性
			TypeAttribute type = ts.addAttribute(TypeAttribute.class);
			// 重置TokenStream（重置StringReader）
			PayloadAttribute payload = ts.addAttribute(PayloadAttribute.class);
			ts.reset();
			int current = -1;
			String result = "";
			while (ts.incrementToken()) {
				System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | "
						+ type.type());
//				System.out.println(payload.getPayload().utf8ToString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放TokenStream的所有资源
			if (ts != null) {
				try {
					ts.end();
					ts.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
