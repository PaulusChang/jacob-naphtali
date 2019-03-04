package jacob.naphtali.base.bean.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtils {

	public static String getXmlString(Object obj, Class<?>... clsArray) {
		JAXBContext context = null;
		String xmlObj = null;
		try {
			List<Class<?>> clsList = new ArrayList<Class<?>>();
			clsList.add(obj.getClass());
			for (Class<?> cls : clsArray) {
				clsList.add(cls);
			}
			clsArray = new Class[clsList.size()];
			for (int i = 0; i < clsList.size(); i++) {
				clsArray[i] = clsList.get(i);
			}
			context = JAXBContext.newInstance(clsArray);
			Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象

			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // 设置编码字符集
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化XML输出，有分行和缩进
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			marshaller.marshal(obj, baos);
			xmlObj = new String(baos.toByteArray(), "utf8"); // 生成XML字符串
		} catch (Exception e) {
			e.printStackTrace();
		} // 获取上下文对象

		return xmlObj;
	}
	
	@SuppressWarnings("unchecked")
	public static String getMapXml(Map<String, Object> map, String root, String charset) {
		String xml = "";
		Set<String> set = map.keySet();
		for (String key : set) {
			if (map.get(key) instanceof Map) {
				xml += "<" + key + ">" + getMapXml((Map<String, Object>)map.get(key), null, charset) + "</" + key + ">";
				continue;
			}
			xml += "<" + key + ">" + map.get(key) + "</" + key + ">";
		}
		if (!StringUtils.isEmpty(root)) {
			xml = "<?xml version=\"1.0\" encoding=\"" + charset + "\" standalone=\"yes\"?>" + "<" + root + ">" + xml + "</" + root + ">";
		}
		return xml;
	}
	
	public static boolean saveXml(String xml, String path) {
		if (StringUtils.isEmpty(xml)) {
			return false;
		}
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		String fileName = path.split("[\\/]")[path.split("[\\/]").length - 1];
		File file = new File(path.substring(0, path.length() - fileName.length()));
		if (!file.exists()) {
			FileUtils.makeDir(file);
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(path);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			fileWriter.write(xml);
		} catch (IOException e) {
			try {
				fileWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			e.printStackTrace();
			return false;
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getTextTrim(Element ele, String node) {
		if (null == ele) {
			return null;
		}
		if (StringUtils.isEmpty(node)) {
			return null;
		}
		if (null == ele.element(node)) {
			return null;
		}
		return ele.element(node).getTextTrim();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(Class<T> cls, String xml) {
		try {
            JAXBContext context = JAXBContext
                    .newInstance(new Class[] { cls });
            InputStream buf = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            return (T) context.createUnmarshaller().unmarshal(buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * @author ChangJian
	 * @data 2018年8月2日
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> xmlToMap(String xml) {
		//创建一个Map返回用
		Map<String, Object> xmlMap = new HashMap<String, Object>();
		//把xml转成Document对象
		Document document;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
			return xmlMap;
		}
		//获取根结点
		Element rootElement = document.getRootElement();
		//调用了一个自定义方法，用来把Element对象转成Map对象
		xmlMap = elementToMap(rootElement);
		return xmlMap;
	}
	
	/**
	 * @author ChangJian
	 * @data 2018年8月2日
	 * @param element 是有子结点的
	 * @return
	 */
	private static Map<String, Object> elementToMap(Element element) {
		//定义一个Map返回用
		Map<String, Object> eleMap = new HashMap<String, Object>();
		//获取并遍历当前结点有子结点
		List<Element> elements = element.elements();
		for (Element subEle : elements) {
			//如果子结点没有孙子结点
			if (subEle.elements().size() == 0) {
				//调用了一个自定义方法，因为往eleMap里put值的时候，要考虑多种情况
				putMap(eleMap, subEle.getName(), subEle.getTextTrim());
				continue;
			}
			//有孙子结点的时候递归调用当前方法
			putMap(eleMap, subEle.getName(), elementToMap(subEle));
		}
		return eleMap;
	}
	
	/**
	 * @author ChangJian
	 * @data 2018年8月2日
	 * @param map
	 * @param key
	 * @param obj
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void putMap(Map<String, Object> map, String key, Object obj) {
		Object value = map.get(key);
		if (null == value) {
			map.put(key, obj);
			return;
		}
		if (value instanceof List) {
			((List) value).add(obj);
			map.put(key, value);
			return;
		}
		List<Object> valueList = new ArrayList<>();
		valueList.add(value);
		valueList.add(obj);
		map.put(key, valueList);
		return;
	}

	public static void testSaveXml() {
		String xml = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> <wsdl:definitions xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" xmlns:ns1=\"http://org.apache.axis2/xsd\" xmlns:ns=\"http://ws.uniedi.itown.com\" xmlns:wsaw=\"http://www.w3.org/2006/05/addressing/wsdl\" xmlns:http=\"http://schemas.xmlsoap.org/wsdl/http/\" xmlns:ax21=\"http://ws.uniedi.itown.com/xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:mime=\"http://schemas.xmlsoap.org/wsdl/mime/\" xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\" xmlns:soap12=\"http://schemas.xmlsoap.org/wsdl/soap12/\" targetNamespace=\"http://ws.uniedi.itown.com\"> <wsdl:documentation/> <wsdl:types> <xs:schema xmlns:ax22=\"http://ws.uniedi.itown.com/xsd\" attributeFormDefault=\"qualified\" elementFormDefault=\"qualified\" targetNamespace=\"http://ws.uniedi.itown.com\"> <xs:import namespace=\"http://ws.uniedi.itown.com/xsd\"/> <xs:complexType name=\"Exception\"> <xs:sequence> <xs:element minOccurs=\"0\" name=\"Exception\" nillable=\"true\" type=\"xs:anyType\"/> </xs:sequence> </xs:complexType> <xs:element name=\"putMessages\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"messages\" nillable=\"true\" type=\"ax22:MessageInfo\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"putMessagesResponse\"> <xs:complexType> <xs:sequence> <xs:element minOccurs=\"0\" name=\"return\" type=\"xs:boolean\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"putMessage\"> <xs:complexType> <xs:sequence> <xs:element minOccurs=\"0\" name=\"message\" nillable=\"true\" type=\"ax22:MessageInfo\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"putMessageResponse\"> <xs:complexType> <xs:sequence> <xs:element minOccurs=\"0\" name=\"return\" type=\"xs:boolean\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getPubMessages\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"params\" nillable=\"true\" type=\"ax22:EdiParameter\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getPubMessagesResponse\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"return\" nillable=\"true\" type=\"ax22:MessageInfo\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getMessages\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"params\" nillable=\"true\" type=\"ax22:EdiParameter\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getMessagesResponse\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"return\" nillable=\"点中文\" type=\"ax22:MessageInfo\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getMessageCount\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"params\" nillable=\"true\" type=\"ax22:EdiParameter\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getMessageCountResponse\"> <xs:complexType> <xs:sequence> <xs:element minOccurs=\"0\" name=\"return\" type=\"xs:int\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getMessage\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"params\" nillable=\"true\" type=\"ax22:EdiParameter\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"getMessageResponse\"> <xs:complexType> <xs:sequence> <xs:element minOccurs=\"0\" name=\"return\" nillable=\"true\" type=\"ax22:MessageInfo\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"deleteMessages\"> <xs:complexType> <xs:sequence> <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"params\" nillable=\"true\" type=\"ax22:EdiParameter\"/> </xs:sequence> </xs:complexType> </xs:element> <xs:element name=\"deleteMessagesResponse\"> <xs:complexType> <xs:sequence> <xs:element minOccurs=\"0\" name=\"return\" type=\"xs:boolean\"/> </xs:sequence> </xs:complexType> </xs:element> </xs:schema> <xs:schema attributeFormDefault=\"qualified\" elementFormDefault=\"qualified\" targetNamespace=\"http://ws.uniedi.itown.com/xsd\"> <xs:complexType name=\"MessageInfo\"> <xs:sequence> <xs:element minOccurs=\"0\" name=\"body\" nillable=\"true\" type=\"xs:base64Binary\"/> <xs:element minOccurs=\"0\" name=\"header\" nillable=\"true\" type=\"xs:string\"/> </xs:sequence> </xs:complexType> <xs:complexType name=\"EdiParameter\"> <xs:sequence> <xs:element minOccurs=\"0\" name=\"name\" nillable=\"true\" type=\"xs:string\"/> <xs:element minOccurs=\"0\" name=\"value\" nillable=\"true\" type=\"xs:string\"/> </xs:sequence> </xs:complexType> </xs:schema> </wsdl:types> <wsdl:message name=\"getMessageRequest\"> <wsdl:part name=\"parameters\" element=\"ns:getMessage\"/> </wsdl:message> <wsdl:message name=\"getMessageResponse\"> <wsdl:part name=\"parameters\" element=\"ns:getMessageResponse\"/> </wsdl:message> <wsdl:message name=\"putMessageRequest\"> <wsdl:part name=\"parameters\" element=\"ns:putMessage\"/> </wsdl:message> <wsdl:message name=\"putMessageResponse\"> <wsdl:part name=\"parameters\" element=\"ns:putMessageResponse\"/> </wsdl:message> <wsdl:message name=\"getMessageCountRequest\"> <wsdl:part name=\"parameters\" element=\"ns:getMessageCount\"/> </wsdl:message> <wsdl:message name=\"getMessageCountResponse\"> <wsdl:part name=\"parameters\" element=\"ns:getMessageCountResponse\"/> </wsdl:message> <wsdl:message name=\"getPubMessagesRequest\"> <wsdl:part name=\"parameters\" element=\"ns:getPubMessages\"/> </wsdl:message> <wsdl:message name=\"getPubMessagesResponse\"> <wsdl:part name=\"parameters\" element=\"ns:getPubMessagesResponse\"/> </wsdl:message> <wsdl:message name=\"putMessagesRequest\"> <wsdl:part name=\"parameters\" element=\"ns:putMessages\"/> </wsdl:message> <wsdl:message name=\"putMessagesResponse\"> <wsdl:part name=\"parameters\" element=\"ns:putMessagesResponse\"/> </wsdl:message> <wsdl:message name=\"deleteMessagesRequest\"> <wsdl:part name=\"parameters\" element=\"ns:deleteMessages\"/> </wsdl:message> <wsdl:message name=\"deleteMessagesResponse\"> <wsdl:part name=\"parameters\" element=\"ns:deleteMessagesResponse\"/> </wsdl:message> <wsdl:message name=\"getMessagesRequest\"> <wsdl:part name=\"parameters\" element=\"ns:getMessages\"/> </wsdl:message> <wsdl:message name=\"getMessagesResponse\"> <wsdl:part name=\"parameters\" element=\"ns:getMessagesResponse\"/> </wsdl:message> <wsdl:portType name=\"EdiServicePortType\"> <wsdl:operation name=\"getMessage\"> <wsdl:input message=\"ns:getMessageRequest\" wsaw:Action=\"urn:getMessage\"/> <wsdl:output message=\"ns:getMessageResponse\" wsaw:Action=\"urn:getMessageResponse\"/> </wsdl:operation> <wsdl:operation name=\"putMessage\"> <wsdl:input message=\"ns:putMessageRequest\" wsaw:Action=\"urn:putMessage\"/> <wsdl:output message=\"ns:putMessageResponse\" wsaw:Action=\"urn:putMessageResponse\"/> </wsdl:operation> <wsdl:operation name=\"getMessageCount\"> <wsdl:input message=\"ns:getMessageCountRequest\" wsaw:Action=\"urn:getMessageCount\"/> <wsdl:output message=\"ns:getMessageCountResponse\" wsaw:Action=\"urn:getMessageCountResponse\"/> </wsdl:operation> <wsdl:operation name=\"getPubMessages\"> <wsdl:input message=\"ns:getPubMessagesRequest\" wsaw:Action=\"urn:getPubMessages\"/> <wsdl:output message=\"ns:getPubMessagesResponse\" wsaw:Action=\"urn:getPubMessagesResponse\"/> </wsdl:operation> <wsdl:operation name=\"putMessages\"> <wsdl:input message=\"ns:putMessagesRequest\" wsaw:Action=\"urn:putMessages\"/> <wsdl:output message=\"ns:putMessagesResponse\" wsaw:Action=\"urn:putMessagesResponse\"/> </wsdl:operation> <wsdl:operation name=\"deleteMessages\"> <wsdl:input message=\"ns:deleteMessagesRequest\" wsaw:Action=\"urn:deleteMessages\"/> <wsdl:output message=\"ns:deleteMessagesResponse\" wsaw:Action=\"urn:deleteMessagesResponse\"/> </wsdl:operation> <wsdl:operation name=\"getMessages\"> <wsdl:input message=\"ns:getMessagesRequest\" wsaw:Action=\"urn:getMessages\"/> <wsdl:output message=\"ns:getMessagesResponse\" wsaw:Action=\"urn:getMessagesResponse\"/> </wsdl:operation> </wsdl:portType> <wsdl:binding name=\"EdiServiceSoap11Binding\" type=\"ns:EdiServicePortType\"> <soap:binding transport=\"http://schemas.xmlsoap.org/soap/http\" style=\"document\"/> <wsdl:operation name=\"getMessage\"> <soap:operation soapAction=\"urn:getMessage\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"putMessage\"> <soap:operation soapAction=\"urn:putMessage\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getPubMessages\"> <soap:operation soapAction=\"urn:getPubMessages\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getMessageCount\"> <soap:operation soapAction=\"urn:getMessageCount\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"deleteMessages\"> <soap:operation soapAction=\"urn:deleteMessages\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"putMessages\"> <soap:operation soapAction=\"urn:putMessages\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getMessages\"> <soap:operation soapAction=\"urn:getMessages\" style=\"document\"/> <wsdl:input> <soap:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap:body use=\"literal\"/> </wsdl:output> </wsdl:operation> </wsdl:binding> <wsdl:binding name=\"EdiServiceSoap12Binding\" type=\"ns:EdiServicePortType\"> <soap12:binding transport=\"http://schemas.xmlsoap.org/soap/http\" style=\"document\"/> <wsdl:operation name=\"getMessage\"> <soap12:operation soapAction=\"urn:getMessage\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"putMessage\"> <soap12:operation soapAction=\"urn:putMessage\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getPubMessages\"> <soap12:operation soapAction=\"urn:getPubMessages\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getMessageCount\"> <soap12:operation soapAction=\"urn:getMessageCount\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"deleteMessages\"> <soap12:operation soapAction=\"urn:deleteMessages\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"putMessages\"> <soap12:operation soapAction=\"urn:putMessages\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getMessages\"> <soap12:operation soapAction=\"urn:getMessages\" style=\"document\"/> <wsdl:input> <soap12:body use=\"literal\"/> </wsdl:input> <wsdl:output> <soap12:body use=\"literal\"/> </wsdl:output> </wsdl:operation> </wsdl:binding> <wsdl:binding name=\"EdiServiceHttpBinding\" type=\"ns:EdiServicePortType\"> <http:binding verb=\"POST\"/> <wsdl:operation name=\"getMessage\"> <http:operation location=\"EdiService/getMessage\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"getMessage\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"getMessage\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"putMessage\"> <http:operation location=\"EdiService/putMessage\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"putMessage\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"putMessage\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getPubMessages\"> <http:operation location=\"EdiService/getPubMessages\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"getPubMessages\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"getPubMessages\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getMessageCount\"> <http:operation location=\"EdiService/getMessageCount\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"getMessageCount\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"getMessageCount\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"deleteMessages\"> <http:operation location=\"EdiService/deleteMessages\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"deleteMessages\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"deleteMessages\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"putMessages\"> <http:operation location=\"EdiService/putMessages\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"putMessages\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"putMessages\"/> </wsdl:output> </wsdl:operation> <wsdl:operation name=\"getMessages\"> <http:operation location=\"EdiService/getMessages\"/> <wsdl:input> <mime:content type=\"text/xml\" part=\"getMessages\"/> </wsdl:input> <wsdl:output> <mime:content type=\"text/xml\" part=\"getMessages\"/> </wsdl:output> </wsdl:operation> </wsdl:binding> <wsdl:service name=\"EdiService\"> <wsdl:port name=\"EdiServiceHttpSoap11Endpoint\" binding=\"ns:EdiServiceSoap11Binding\"> <soap:address location=\"http://114.251.38.71:8080/ediws/services/EdiService.EdiServiceHttpSoap11Endpoint/\"/> </wsdl:port> <wsdl:port name=\"EdiServiceHttpSoap12Endpoint\" binding=\"ns:EdiServiceSoap12Binding\"> <soap12:address location=\"http://114.251.38.71:8080/ediws/services/EdiService.EdiServiceHttpSoap12Endpoint/\"/> </wsdl:port> <wsdl:port name=\"EdiServiceHttpEndpoint\" binding=\"ns:EdiServiceHttpBinding\"> <http:address location=\"http://114.251.38.71:8080/ediws/services/EdiService.EdiServiceHttpEndpoint/\"/> </wsdl:port> </wsdl:service> </wsdl:definitions> ";
		String path = "E:/sfsef/sfsef/queryInfo_2017-12-25.xml";
		System.out.println(saveXml(xml, path));
	}
	
	public static void main(String[] args) {
		testSaveXml();
	}
}
