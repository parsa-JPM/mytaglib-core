package dp.tag.tags;

public class Form extends BodyDPTag{

	private String action;

	
	public void setAction(String action) {
		this.action = action;
	}
	

	@Override
	public String getView() {
		return "form/index.jsp";
	}


	@Override
	public String getCloseTag() {
		return "</form>";
	}

}
