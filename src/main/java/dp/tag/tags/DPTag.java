package dp.tag.tags;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


abstract public class DPTag extends BodyTagSupport{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * it will set in request attribute to add resources in taglibs jsp	
	 */
	public static final String CONTEXT_PATH = "/o/dptag";

	/**
	 * META-INF resources root
	 */
	public static final String META_INF = "/META-INF/resources/taglibs/";

	/**
	 * root directory of tag views
	 */
	public static final String ROOT_VIEW = "/taglibs/";

	/**
	 * tld file name
	 */
	public static final String TLD_FILE = "dp_tags.tld";
	/**
	 * it will be added to request attribute to make it's name unique
	 */
	public static final String Request_ATTR_PREFIX = "dpTag";
	/**
	 * index view of tag
	 */
	protected String view;

	/**
	 * it used to resolve tag view (init tag field)
	 * 
	 * @return String
	 */
	abstract public String getView();
	

	/**
	 * it do general things that all tags should do such as call its view
	 */
	@Override
	public int doStartTag() throws JspException {
		
		setAttributes();
		setContextPathAttribute();
		callView();
		return super.doStartTag();
	}
	
	/**
	 * set taglibs module context path
	 */
	private void setContextPathAttribute() {
		pageContext.getRequest().setAttribute("taglibContextPath", CONTEXT_PATH);
	}

	/**
	 * set tag attributes to the request attributes
	 */
	private void setAttributes() {
		List<String> names = getAttributesName();

		setRequestAttrWithReflection(names);
	}

	/**
	 * get value of attribute and set it to the request
	 * 
	 * 
	 * @param names
	 */
	private void setRequestAttrWithReflection(List<String> names) {
		names.forEach(name -> {
			try {
				// name with capital first letter
				String fieldName = name.substring(0, 1).toUpperCase() + name.substring(1);
				Field field = this.getClass().getDeclaredField(name);
				field.setAccessible(true);
				String prefix = Request_ATTR_PREFIX;
				pageContext.getRequest().setAttribute(prefix + fieldName, field.get(this));

			} catch (NoSuchFieldException | IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * find tag attributes name from tld file
	 * 
	 * @return List<String>
	 */
	private List<String> getAttributesName() {
		List<String> names = new ArrayList<>();

		try {
			InputStream tldFile = this.getClass().getClassLoader().getResourceAsStream("META-INF/" + TLD_FILE);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document document = db.parse(tldFile);
			document.getDocumentElement().normalize();

			NodeList tags = document.getElementsByTagName("tag");

			for (int tagCounter = 0; tagCounter < tags.getLength(); tagCounter++) {
				Node tag = tags.item(tagCounter);

				if (tag.getNodeType() == Node.ELEMENT_NODE) {
					Element tagEle = (Element) tag;
					String tagName = tagEle.getElementsByTagName("tag-class").item(0).getTextContent().trim();

					// check that this running tag is equal to tag
					if (this.getClass().getName().trim().equals(tagName))
						findAttrNames(names, tagEle);
				}

			}

		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}

		return names;
	}

	/**
	 * find attributes from tag element from tld xml
	 * 
	 * 
	 * @param names      List<String>
	 * @param tagElement Element
	 */
	private void findAttrNames(List<String> names, Element tagElement) {
		NodeList attributes = tagElement.getElementsByTagName("attribute");
		for (int attrCounter = 0; attrCounter < attributes.getLength(); attrCounter++) {
			Node attr = attributes.item(attrCounter);

			if (attr.getNodeType() == Node.ELEMENT_NODE) {
				Element attrEle = (Element) attr;
				names.add(attrEle.getElementsByTagName("name").item(0).getTextContent());
			}
		}
	}

	/**
	 * call view of taglibs
	 * 
	 */
	protected void callView() {

		/**
		 * if we are in taglibs module we will read view files from normal route in
		 * otherwise we will read from META-INF route
		 */
		try {
			
			dispatchView();

		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	private void dispatchView() throws ServletException, IOException {

		try {
			getRequestDispatcher(ROOT_VIEW + getView());
		} catch (FileNotFoundException e) {
			getRequestDispatcher(META_INF + getView());
		}

	}

	/**
	 * shortcut of getRequestDispacher command
	 * 
	 * @param path
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void getRequestDispatcher(String path) throws ServletException, IOException {

		pageContext.getRequest().getRequestDispatcher(path).include(pageContext.getRequest(),
				pageContext.getResponse());
	}
}

