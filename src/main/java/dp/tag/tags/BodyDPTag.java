package dp.tag.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * Taglibs that have body must extends it
 * 
 * @author jpm
 *
 */
abstract public class BodyDPTag extends DPTag{
	
	/**
	 *  get closer tag to close body of taglib with correct tag
	 */
	public abstract String getCloseTag();
	
	/**
	 * EVAL_BODY_INCLUDE will evaluate body of tag and show that
	 */
	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();
		return EVAL_BODY_INCLUDE;
	}
	
	
	/**
	 * print closer tag
	 */
	@Override
	public int doEndTag() throws JspException {
	
		JspWriter writer= pageContext.getOut();
				try {
			writer.println(getCloseTag());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.doEndTag();
	}
}
