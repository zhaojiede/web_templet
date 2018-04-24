/*
 * XMLParser.java  2010-12-6 
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.joy.common.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * XMLParser.java 2011-10-11
 * 
 * Copyright 2011 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * (Description about the type)
 * 
 * @date 2011-10-11
 * @author CN14830
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class XMLParser {
	Logger log = Logger.getLogger(this.getClass());

	public static final String CHILDREN = "Children";

	public XMLParser() {
	}

	public Map<String, Object> parseXML(InputStream in, String nodeName) throws SAXException, ParserConfigurationException, IOException {
		return parseXMLByInputStream(in, nodeName, true);
	}

	public Map<String, Object> parseXML(InputStream in, String nodeName,
			boolean isAttr) throws SAXException, ParserConfigurationException,
			IOException {
		return parseXMLByInputStream(in, nodeName, isAttr);
	}

	public Map<String, Object> parseXML(String xmlStr, String nodeName) throws UnsupportedEncodingException, SAXException,
			ParserConfigurationException, IOException {
		return parseXMLByInputStream(
				new ByteArrayInputStream(xmlStr.getBytes("utf-8")), nodeName,
				true);
	}

	public Map<String, Object> parseXML(String xmlStr, String nodeName,
			boolean isAttr) throws UnsupportedEncodingException, SAXException,
			ParserConfigurationException, IOException {
		return parseXMLByInputStream(
				new ByteArrayInputStream(xmlStr.getBytes("utf-8")), nodeName,
				isAttr);
	}

	public Map<String, Object> parseXML(String xmlStr, String nodeName,
			String charset) throws UnsupportedEncodingException, SAXException,
			ParserConfigurationException, IOException {
		return parseXMLByInputStream(
				new ByteArrayInputStream(xmlStr.getBytes(charset)), nodeName,
				true);
	}

	public Map<String, Object> parseXML(String xmlStr, String nodeName,
			boolean isAttr, String charset) throws UnsupportedEncodingException, SAXException,
			ParserConfigurationException, IOException {
		return parseXMLByInputStream(
				new ByteArrayInputStream(xmlStr.getBytes(charset)), nodeName,
				isAttr);
	}

	private Map parseXMLByInputStream(InputStream is, String nodeName,
			boolean isAttr) throws SAXException, ParserConfigurationException,
			IOException {
		Map nodeMap = new HashMap<String, Object>();
		Document document = readXmlDocument(is, false, null);
		Element root = document.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		if (root.getNodeName().equalsIgnoreCase(nodeName)) {
			this.setNodeAttributes(root.getAttributes(), nodeMap);
			setNodeChildren(nodes, nodeMap, isAttr);
		} else {
			parseNodes(nodes, nodeName, nodeMap, isAttr);
		}
		return nodeMap;

	}

	private void setNodeAttributes(Node node, Map nodeAttrMap) {
		NamedNodeMap namedMap = node.getAttributes();
		this.setNodeAttributes(namedMap, nodeAttrMap);
	}

	private void setNodeAttributes(NamedNodeMap namedMap, Map nodeAttrMap) {
		if (namedMap == null) {
			return;
		}
		Node node = null;
		int attrNum = namedMap.getLength();
		for (int a = 0; a < attrNum; a++) {
			node = namedMap.item(a);
			if (node == null) {
				continue;
			}
			nodeAttrMap.put(node.getNodeName(), node.getNodeValue());
		}
	}

	private void parseNodes(NodeList childNodes, String nodeName,
			Map currentNodeMap, boolean isAttr) {
		Node node = null;
		String ndName = null;
		int num = childNodes.getLength();
		for (int i = 0; i < num; i++) {
			node = childNodes.item(i);
			ndName = node.getNodeName();
			// log.debug("current node name="+ndName+"\n");
			if (ndName != null && !ndName.equals("") && !ndName.startsWith("#")
					&& ndName.equalsIgnoreCase(nodeName)) {
				// 取得节点属性元素值
				setNodeAttributes(node, currentNodeMap);
				setNodeChildNodes(node, currentNodeMap, isAttr);
				return;
			} else {
				parseNodes(node.getChildNodes(), nodeName, currentNodeMap,
						isAttr);
			}
		}
	}

	private void setNodeChildNodes(Node node, Map currentNodeMap, boolean isAttr) {
		if (node == null || currentNodeMap == null) {
			return;
		}
		NodeList ndl = node.getChildNodes();
		this.setNodeChildren(ndl, currentNodeMap, isAttr);
	}

	private void setNodeChildren(NodeList ndl, Map currentNodeMap,
			boolean isAttr) {
		int num = ndl.getLength();
		Node nd = null;
		String ndName = null;
		List childList = new ArrayList<Map>();
		currentNodeMap.put(CHILDREN, childList);
		for (int i = 0; i < num; i++) {
			nd = ndl.item(i);
			ndName = nd.getNodeName();
			if (ndName.startsWith("#")) {
				continue;
			}
			Map childMap = new HashMap<String, Object>();
			setChildren(nd, childMap, isAttr);
			childList.add(childMap);
			log.debug("ndName=" + ndName);
		}
	}

	private void setChildren(Node currentNode, Map childMap, boolean isAttr) {
		Node nd = null;
		String ndName = null;
		NodeList nodeList = currentNode.getChildNodes();
		int num = nodeList.getLength();
		for (int i = 0; i < num; i++) {
			nd = nodeList.item(i);

			ndName = nd.getNodeName();
			if (ndName.startsWith("#")) {
				continue;
			}
			if (isAttr) {
				NamedNodeMap namedMap = nd.getAttributes();
				int attrNum = namedMap.getLength();
				for (int a = 0; a < attrNum; a++) {
					Node node = namedMap.item(a);
					if (node == null) {
						continue;
					}
					childMap.put(ndName, node.getNodeValue());
				}
			} else {
				log.debug(ndName + "=" + nd.getTextContent());
				childMap.put(ndName, nd.getTextContent());
			}
		}
	}

	private Document readXmlDocument(InputStream is, boolean validate,
			String docDescription) throws SAXException,
			ParserConfigurationException, IOException {
		if (is == null) {
			return null;
		}
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validate);
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(is);
		return document;
	}

	public static Map<String, String> parseSmsXml(String xmlStr) throws ParserConfigurationException, SAXException, IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(xmlStr.getBytes());
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		document = builder.parse(in);
		Element root = document.getDocumentElement();
		NodeList books = root.getChildNodes();
		Map smsMap = new HashMap<String, Object>();
		for (int i = 0; i < books.getLength(); i++) {
			String nodeNm = books.item(i).getNodeName();
			if (!nodeNm.contains("#")) {
				smsMap.put(books.item(i).getNodeName(), books.item(i).getTextContent());
			}
		}
		return smsMap;
	}

//	public static void main(String[] args) {
//		try {
//			/**
//			 * testTrade.xml
//			 * 
//			 * <?xml version="1.0" encoding="utf-8" ?> <itemcats_get_response>
//			 * <item_cats list="true"> <item_cat> <cid>11</cid>
//			 * <is_parent>true</is_parent> <name></name>
//			 * <parent_cid>0</parent_cid> <sort_order>48 </sort_order>
//			 * <status>normal</status> </item_cat> <item_cat> <cid>1101</cid>
//			 * <is_parent>false</is_parent> <name></name>
//			 * <parent_cid>0</parent_cid> <sort_order>50</sort_order>
//			 * <status>normal</status> </item_cat> </item_cats>
//			 * </itemcats_get_response>
//			 * 
//			 * 
//			 * 
//			 * 
//			 */
//			InputStream in = new FileInputStream("D:\\mai.xml");
//			XMLParser xp = new XMLParser();
//			Map mp = xp.parseSmsXml(in);
//			System.out.println(mp);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
}
