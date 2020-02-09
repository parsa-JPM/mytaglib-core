package dp.tag.tags;

import javax.servlet.jsp.JspException;

public class Input extends DPTag {

	/**
	 * tag attribute
	 */
	private String name;
	/**
	 * tag attribute
	 */
	private String type;

	/**
	 * tag attribute
	 */
	private String value;

	/**
	 * tag attribute
	 */
	private String required;

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	@Override
	public int doStartTag() throws JspException {
		
		return super.doStartTag();
	}
	
	public String getView() {
		return "input/index.jsp";
	}

}
