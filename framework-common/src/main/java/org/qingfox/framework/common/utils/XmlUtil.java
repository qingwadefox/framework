package org.qingfox.framework.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {

	/**
	 * 读取文档
	 * 
	 * @param file
	 * @return
	 * @throws DocumentException
	 */
	public static Document read(File file) throws DocumentException {
		return new SAXReader().read(file);
	}

	/**
	 * 读取文档
	 * 
	 * @param filePath
	 * @return
	 * @throws DocumentException
	 */
	public static Document read(String filePath) throws DocumentException {
		return new SAXReader().read(new File(filePath));
	}

	public static void write(File file, Document document) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(new FileWriter(file), format);
		writer.write(document);
		writer.close();
	}

	public static Element createElement(String arg0) {
		return DocumentHelper.createDocument().addElement(arg0);

	}

	/**
	 * 过滤转义符
	 * 
	 * @param e
	 * @return
	 */
	public static String parseText(Element e) {
		String content = e.asXML();
		return content.replace("&lt;", "<").replace("&gt;", ">")
				.replace("&amp;", "&").replace("&apos;", "'")
				.replace("&quot;", "\"");
	}

}
