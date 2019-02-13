package org.oscarehr.caisi_integrator.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XmlUtils
{
	public static void writeNode(Node node, OutputStream os) throws TransformerException
	{
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();

		DOMSource domSource = new DOMSource(node);
		StreamResult streamResult = new StreamResult(os);
		transformer.transform(domSource, streamResult);
	}

	public static String toString(Node node) throws TransformerException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeNode(node, baos);

		return(baos.toString());
	}

	public static Document toDocument(String s) throws IOException, org.xml.sax.SAXException, ParserConfigurationException
	{
		return(toDocument(s.getBytes()));
	}
	
	public static Document toDocument(byte[] x) throws IOException, org.xml.sax.SAXException, ParserConfigurationException
	{
		ByteArrayInputStream is = new ByteArrayInputStream(x, 0, x.length);
		return(toDocument(is));
	}

	public static Document toDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(is);
		return(document);
	}
	
	public static Document newDocument(String rootName) throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		doc.appendChild(doc.createElement(rootName));

		return(doc);
	}

	public static void appendChildToRoot(Document doc, String childName, byte[] childContents)
	{
		appendChild(doc, doc.getFirstChild(), childName, new String(Base64.encodeBase64(childContents)));
	}

	public static void appendChildToRoot(Document doc, String childName, String childContents)
	{
		appendChild(doc, doc.getFirstChild(), childName, childContents);
	}

	public static void appendChild(Document doc, Node parentNode, String childName, String childContents)
	{
		if (childContents==null) throw(new NullPointerException("ChildNode is null."));
		
		Element child = doc.createElement(childName);
		child.setTextContent(childContents);
		parentNode.appendChild(child);
	}

	public static String toFormattedString(Document doc) throws IOException
	{
		OutputFormat outputFormat=new OutputFormat(doc);
		outputFormat.setIndenting(true);
		
		StringWriter stringWriter=new StringWriter();
		
		XMLSerializer xmlSerializer = new XMLSerializer(stringWriter, outputFormat);
		xmlSerializer.serialize(doc);
		
		return(stringWriter.toString());
	}
	
	/**
	 * only returns the first instance of this child node
	 * @param node
	 * @param localName
	 * @return
	 */
	public static Node getChildNode(Node node, String name)
	{
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node temp = nodeList.item(i);
			if (temp.getNodeType()!=Node.ELEMENT_NODE) continue;
			if (name.equals(temp.getLocalName()) || name.equals(temp.getNodeName())) return(temp);
		}

		return(null);
	}

	public static String getChildNodeTextContents(Node node, String name)
	{
		Node tempNode = getChildNode(node, name);
		if (tempNode != null) return(tempNode.getTextContent());
		else return(null);
	}

	/**
	 * @return the attribute value or null
	 */
	public static String getAttributeValue(Node node, String attributeName)
	{
		NamedNodeMap attributes = node.getAttributes();
		if (attributes==null) return(null);
		
		Node tempNode=attributes.getNamedItem(attributeName);
		if (tempNode==null) return(null);
		
		return(tempNode.getNodeValue());
	}

	public static void main(String... argv) throws Exception
	{
		Document doc = newDocument("testRoot");
		appendChildToRoot(doc, "testChild1", "test child< bla< > contents");
		appendChildToRoot(doc, "testChild2", "test child contents 2");

		MiscUtils.getLogger().info(toString(doc));
	}
}
